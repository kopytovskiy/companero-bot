package com.companerobot.parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.UserRole;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.RestrictionHelper;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.constants.TextMessages.RESTRICTED_LOCATION_MESSAGE;
import static com.companerobot.parsers.location_parsers.DriverLocationParser.parseDriverLocation;
import static com.companerobot.parsers.location_parsers.PassengerLocationParser.parsePassengerLocation;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageToUser;

public class LocationParser {

    public static void parseLocation(Message message) {

        double latitude = message.getLocation().getLatitude();
        double longitude = message.getLocation().getLongitude();
        Long userId = message.getFrom().getId();
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        if (RestrictionHelper.isRestrictedLocation(latitude, longitude)) {
            sendMessageToUser(userId, LocalizationHelper.getValueByCode(RESTRICTED_LOCATION_MESSAGE, userLocale));
            return;
        }

        UserRole userRole = UserCollection.getUserRole(userId);

        switch (userRole) {
            case PASSENGER:
                parsePassengerLocation(message);
                break;
            case DRIVER:
                parseDriverLocation(message);
                break;
            case null, default:
                break;
        }
    }
}
