package com.companerobot.parsers;

import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.enums.UserRole.DRIVER;
import static com.companerobot.enums.UserRole.PASSENGER;
import static com.companerobot.parsers.location_parsers.DriverLocationParser.parseDriverLocation;
import static com.companerobot.parsers.location_parsers.PassengerLocationParser.parsePassengerLocation;

public class LocationParser {

    public static void parseLocation(Message message) {
        if (UserCollection.getUserRole(message.getFrom().getId()) == PASSENGER) {
            parsePassengerLocation(message);

        } else if (UserCollection.getUserRole(message.getFrom().getId()) == DRIVER) {
            parseDriverLocation(message);
        }
    }
}
