package com.companerobot.misc;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.UserRole;
import com.companerobot.enums.UserStatus;
import com.companerobot.helpers.CipherHelper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import static com.companerobot.enums.UserStatus.WAITING_USER_LOCALIZATION;

public class UserCollection extends MongoBaseClass {

    protected static final MongoCollection<Document> customerCollection = database.getCollection("customers");

    public static void addUserToDB(Long userId) {
        Document document = new Document();
        document.put("userId", userId);
        document.put("userRole", null);
        document.put("userName", null);
        document.put("userPhoneNumber", null);
        document.put("isPhoneNumberHidden", true);
        document.put("userTag", null);
        document.put("userLocale", CountryCode.US);
        document.put("userStatus", WAITING_USER_LOCALIZATION);
        customerCollection.insertOne(document);
    }

    public static void updateUserRole(Long userId, UserRole userRole) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("userRole", userRole));
    }

    public static void updateUserLocalization(Long userId, CountryCode locale) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("userLocale", locale));
    }

    public static void updateIsUserPhoneHidden(Long userId, boolean isPhoneNumberHidden) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("isPhoneNumberHidden", isPhoneNumberHidden));
    }

    public static void updateUserName(Long userId, String userName) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("userName", userName));
    }

    public static void updateUserPhoneNumber(Long userId, String userPhoneNumber) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("userPhoneNumber", CipherHelper.encrypt(userPhoneNumber)));

    }

    public static void removeUserPhoneNumber(Long userId) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("userPhoneNumber", null));

    }

    public static void updateUserTag(Long userId, String userTag) {
        if (userTag == null) {
            customerCollection.updateOne(Filters.eq("userId", userId),
                    Updates.set("userTag", null));
        } else {
            customerCollection.updateOne(Filters.eq("userId", userId),
                    Updates.set("userTag", CipherHelper.encrypt(userTag)));
        }
    }

    public static void updateUserStatus(Long userId, UserStatus userStatus) {
        customerCollection.updateOne(Filters.eq("userId", userId),
                Updates.set("userStatus", userStatus));
    }

    public static boolean isUserExist(Long userId) {
        Document user = customerCollection.find(Filters.eq("userId", userId)).first();
        return user != null;
    }

    public static UserRole getUserRole(Long userId) {
        Object userRole = customerCollection
                .find(Filters.eq("userId", userId))
                .first()
                .get("userRole");

        if (userRole != null) {
            return UserRole.valueOf(userRole.toString());

        } else {
            return null;
        }
    }

    public static String getUserName(Long userId) {
        Object userNameObject = customerCollection
                .find(Filters.eq("userId", userId))
                .first()
                .get("userName");

        if (userNameObject != null) {
            return userNameObject.toString();

        } else {
            return null;
        }
    }

    public static String getUserPhoneNumber(Long userId) {
        Object userPhoneNumberObject = customerCollection
                .find(Filters.eq("userId", userId))
                .first()
                .get("userPhoneNumber");

        if (userPhoneNumberObject != null) {
            return CipherHelper.decrypt(userPhoneNumberObject.toString());

        } else {
            return null;
        }
    }

    public static String getUserTag(Long userId) {
        Object userTagObject = customerCollection
                .find(Filters.eq("userId", userId))
                .first()
                .get("userTag");

        if (userTagObject != null) {
            return CipherHelper.decrypt(userTagObject.toString());

        } else {
            return null;
        }
    }

    public static CountryCode getUserLocale(Long userId) {
        return CountryCode.valueOf(customerCollection
                .find(Filters.eq("userId", userId))
                .first()
                .get("userLocale")
                .toString());
    }

    public static String getIsPhoneNumberHidden(Long userId) {
        Object isPhoneNumberHidden = customerCollection.find(
                Filters.eq("userId", userId))
                .first()
                .get("isPhoneNumberHidden");

        if (isPhoneNumberHidden != null) {
            return isPhoneNumberHidden.toString();

        } else {
            return null;
        }
    }

    public static UserStatus getUserStatus(Long userId) {
        return UserStatus.valueOf(
                customerCollection.find(
                        Filters.eq("userId", userId))
                        .first()
                        .get("userStatus")
                        .toString());
    }
}
