package com.companerobot.misc;

import com.companerobot.enums.DriverInfoFillingStatus;
import com.companerobot.helpers.CipherHelper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.geojson.Point;
import org.bson.Document;

import java.util.ArrayList;

import static com.companerobot.enums.DriverInfoFillingStatus.WAITING_CAR_MODEL;
import static com.companerobot.misc.MongoBaseClass.database;

public class DriverCollection {

    protected static final MongoCollection<Document> driverCollection = database.getCollection("driversInfo");

    public static void addDriverToDB(Long userId) {
        Document document = new Document();
        document.put("userId", userId);
        document.put("carModel", null);
        document.put("carColor", null);
        document.put("carNumber", null);
        document.put("location", null);
        document.put("isWaitingForNewTrip", false);
        document.put("driverInfoFillingStatus", WAITING_CAR_MODEL);
        driverCollection.insertOne(document);
//        driverCollection.createIndex(Indexes.geo2dsphere("location")); //TODO: Create index only once
    }

    public static Document getDriverInfoByDriverId(Long userId) {
        return driverCollection.find(Filters.eq("userId", userId)).first();
    }

    public static ArrayList<Long> getNearByDriversIdOnDutyList(Point point, double radiusInKms) {
        ArrayList<Long> driverUserIds = new ArrayList<>();
        double minDistance = 0;
        double maxDistance = radiusInKms * 1000;

        FindIterable<Document> nearbyDrivers = driverCollection.find(
                Filters.and(
                        Filters.nearSphere("location", point, maxDistance, minDistance),
                        Filters.eq("isWaitingForNewTrip", true)
                )
        );

        for (Document document : nearbyDrivers) {
            driverUserIds.add(Long.valueOf(document.get("userId").toString()));
        }

        return driverUserIds;
    }

    public static void setCarModel(Long userId, String carModel) {
        driverCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("carModel", carModel));
    }

    public static void setCarColor(Long userId, String carColor) {
        driverCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("carColor", carColor));
    }

    public static void setLocation(Long userId, Point point) {
        driverCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("location", point));
    }

    public static void setCarNumber(Long userId, String carNumber) {
        driverCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("carNumber", CipherHelper.encrypt(carNumber)));
    }

    public static void setIsWaitingForNewTrip(Long userId, boolean isWaitingForNewTrip) {
        driverCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("isWaitingForNewTrip", isWaitingForNewTrip));
    }

    public static void setDriverInfoFillingStatus(Long userId, DriverInfoFillingStatus driverInfoFillingStatus) {
        driverCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("driverInfoFillingStatus", driverInfoFillingStatus));
    }

    public static boolean getIsWaitingForNewTrip(Long userId) {
        return Boolean.parseBoolean(driverCollection.find(
                        Filters.eq("userId", userId))
                        .first()
                        .get("isWaitingForNewTrip")
                        .toString());
    }

}
