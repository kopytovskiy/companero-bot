package com.companerobot.misc;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import static com.companerobot.constants.ExecutionConstants.MONGODB_CLIENT_URL;

public class MongoBaseClass {
    protected static final MongoClient mongoClient = MongoClients.create(MONGODB_CLIENT_URL);
    protected static MongoDatabase database = mongoClient.getDatabase("CompaneroBotDB");

}
