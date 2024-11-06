package com.companerobot.misc;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static com.companerobot.misc.MongoBaseClass.database;

public class RestrictedAreasCollection {

    private static final MongoCollection<Document> restrictedAreasCollection = database.getCollection("restrictedAreas");

    public static ArrayList<String> getRestrictedCountriesList() {
        ArrayList<String> restrictedAreas = new ArrayList<>();
        FindIterable<Document> restictedAreasDocumentsCollection = restrictedAreasCollection.find();

        for (Document document : restictedAreasDocumentsCollection) {
            restrictedAreas.add(document.get("countryCode").toString());
        }

        return restrictedAreas;
    }

    public static ArrayList<String> getRestrictedPhoneNumbersList() {
        ArrayList<String> restrictedPhoneCodes = new ArrayList<>();
        FindIterable<Document> restictedAreasDocumentsCollection = restrictedAreasCollection.find();

        for (Document document : restictedAreasDocumentsCollection) {
            restrictedPhoneCodes.add(document.get("phoneCode").toString());
        }

        return restrictedPhoneCodes;
    }

}
