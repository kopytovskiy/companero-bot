package com.companerobot.parsers;

import com.companerobot.enums.UserRole;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.enums.UserRole.DRIVER;
import static com.companerobot.enums.UserRole.PASSENGER;
import static com.companerobot.parsers.message_parsers.DriverMessageParser.parseDriverMessage;
import static com.companerobot.parsers.message_parsers.MiscMessageParser.parseMiscMessage;
import static com.companerobot.parsers.message_parsers.PassengerMessageParser.parsePassengerMessage;


public class MessageParser {

    public static void parseMessage(Message message) {
        UserRole userRole = UserCollection.getUserRole(message.getFrom().getId());

        if (userRole == PASSENGER) {
            parsePassengerMessage(message);

        } else if (userRole == DRIVER) {
            parseDriverMessage(message);

//        } else if (userRole == SUPER_ADMIN) {
//            parseSuperAdminMessage(message);
        } else {
            parseMiscMessage(message);
        }
    }
}
