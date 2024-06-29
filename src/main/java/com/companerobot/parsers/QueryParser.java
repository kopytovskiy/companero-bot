package com.companerobot.parsers;

import com.companerobot.enums.UserRole;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.companerobot.enums.UserRole.DRIVER;
import static com.companerobot.enums.UserRole.PASSENGER;
import static com.companerobot.parsers.query_parsers.DriverQueryParser.parseDriverQuery;
import static com.companerobot.parsers.query_parsers.PassengerQueryParser.parsePassengerQuery;

public class QueryParser {

    public static void parseQuery(Update update) {
        UserRole userRole = UserCollection.getUserRole(update.getCallbackQuery().getFrom().getId());

        if (userRole == PASSENGER) {
            parsePassengerQuery(update);

        } else if (userRole == DRIVER) {
            parseDriverQuery(update);
        }
    }
}
