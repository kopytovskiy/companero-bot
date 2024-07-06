package com.companerobot.misc;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

import static com.companerobot.misc.MongoBaseClass.database;

public class ReviewCollection {

    protected static final MongoCollection<Document> reviewCollection = database.getCollection("reviews");

    public static void addNewUserToReviewDB(Long userId) {
        Document document = new Document();
        document.put("userId", userId);
        document.put("reviews", new ArrayList<>());
        reviewCollection.insertOne(document);
    }

    public static void addUserReview(Long userId, Long reviewerId, boolean isLiked) {

        if(!isUserHasReviews(userId)) {
            addNewUserToReviewDB(userId);
        }

        if(!isReviewerIdReviewed(userId, reviewerId)) {
            addNewReview(userId, reviewerId, isLiked);

        } else {
            updateOldReview(userId, reviewerId, isLiked);
        }
    }

    private static void addNewReview(Long userId, Long reviewerId, boolean isLiked) {
        Document reviewDocument = new Document();
        reviewDocument.put("reviewerId", reviewerId);
        reviewDocument.put("isLiked", isLiked);

        reviewCollection.updateOne(
                Filters.eq("userId", userId),
                Updates.push("reviews", reviewDocument)
        );
    }

    private static void updateOldReview(Long userId, Long reviewerId, boolean isLiked) {
        reviewCollection.updateOne(
                Filters.eq("userId", userId),
                Updates.set("reviews.$[elem].isLiked", isLiked),
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

    public static boolean isReviewerIdReviewed(Long userId, Long reviewerId) {
        Document isReviewerIdReviewed = reviewCollection.find(
                Filters.and(
                        Filters.eq("userId", userId),
                        Filters.eq("reviews.reviewerId", reviewerId))).first();
        return isReviewerIdReviewed != null;
    }

}
