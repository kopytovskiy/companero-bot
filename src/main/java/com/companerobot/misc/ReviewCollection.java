package com.companerobot.misc;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.companerobot.misc.MongoBaseClass.database;
import static com.mongodb.client.model.Aggregates.*;


public class ReviewCollection {

    protected static final MongoCollection<Document> reviewCollection = database.getCollection("reviews");

    public static void addNewUserToReviewDB(Long userId) {
        Document document = new Document();
        document.put("userId", userId);
        document.put("rating", 0.0);
        document.put("reviews", new ArrayList<>());
        reviewCollection.insertOne(document);
    }

    public static void addUserReview(Long userId, Long reviewerId, boolean isLiked) {

        if (!isReviewerAlreadyAddedReview(userId, reviewerId)) {
            addNewReview(userId, reviewerId, isLiked);

        } else {
            updateOldReview(userId, reviewerId, isLiked);
        }

        updateUserRating(userId);
    }

    private static void addNewReview(Long userId, Long reviewerId, boolean isLiked) {
        Date currentTime = new Date();

        Document reviewDocument = new Document();
        reviewDocument.put("reviewerId", reviewerId);
        reviewDocument.put("isLiked", isLiked);
        reviewDocument.put("createdAt", currentTime);
        reviewDocument.put("updatedAt", currentTime);

        reviewCollection.updateOne(
                Filters.eq("userId", userId),
                Updates.push("reviews", reviewDocument)
        );
    }

    private static void updateOldReview(Long userId, Long reviewerId, boolean isLiked) {
        reviewCollection.updateOne(
                Filters.eq("userId", userId),
                Updates.combine(
                        Updates.set("reviews.$[elem].isLiked", isLiked),
                        Updates.set("reviews.$[elem].updatedAt", new Date())),
                new UpdateOptions()
                        .arrayFilters(Arrays.asList(
                                Filters.eq("elem.reviewerId", reviewerId))
                        )
        );
    }

    public static boolean isUserHasReviews(Long userId) {
        Document user = reviewCollection.find(Filters.eq("userId", userId)).first();
        return user != null;
    }

    private static int getUserLikesAmount(Long userId) {
        Document result = reviewCollection.aggregate(Arrays.asList(
                match(Filters.eq("userId", userId)),
                unwind("$reviews"),
                match(Filters.eq("reviews.isLiked", true)),
                count())).first();

        if (result != null) {
            return result.getInteger("count");
        } else {
            return 0;
        }
    }

    public static int getUserReviewsAmount(Long userId) {
        Document result = reviewCollection.find(Filters.eq("userId", userId))
                .projection(Projections.computed("reviewsSize",
                        new Document("$size", "$reviews"))).first();

        if (result != null) {
            return result.getInteger("reviewsSize");
        } else {
            return 0;
        }
    }

    public static void updateUserRating(Long userId) {

        double userReviewsAmount = getUserReviewsAmount(userId);
        double userLikesAmount = getUserLikesAmount(userId);

        double rating = new BigDecimal(userLikesAmount / userReviewsAmount * 10).
                setScale(2, RoundingMode.HALF_UP).doubleValue();

        reviewCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("rating", rating));
    }

    public static double getUserRating(Long userId) {
            return reviewCollection.find(Filters.eq("userId", userId))
                    .first()
                    .getDouble("rating");
    }

    public static boolean isReviewerAlreadyAddedReview(Long userId, Long reviewerId) {
        Document isReviewerIdReviewed = reviewCollection.find(
                Filters.and(
                        Filters.eq("userId", userId),
                        Filters.eq("reviews.reviewerId", reviewerId))).first();
        return isReviewerIdReviewed != null;
    }

}
