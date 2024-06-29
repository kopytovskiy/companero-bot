package com.companerobot.parsers.query_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.companerobot.constants.Callbacks.ADD_POSTPONED_DEPARTURE_DETAILS_CALLBACK;
import static com.companerobot.constants.TextMessages.DEPARTURE_DETAILS_MESSAGE;
import static com.companerobot.enums.OrderStatus.PASSENGER_CONFIRMATION_WAITING;
import static com.companerobot.enums.OrderStatus.WAITING_DEPARTURE_DETAILS;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;

public class PassengerQueryParser {

    public static void parsePassengerQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();

        if (callbackData.equals(ADD_POSTPONED_DEPARTURE_DETAILS_CALLBACK)) {
            addDepartureDetails(update);
        }
    }


    private static void addDepartureDetails(Update update) {
        Long passengerId = update.getCallbackQuery().getFrom().getId();
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, PASSENGER_CONFIRMATION_WAITING);
        OrderCollection.updateOrderStatus(order.get("orderId").toString(), WAITING_DEPARTURE_DETAILS);

        sendMessageExecutor(
                ReplyKeyboardHelper.backToPreviousPageKeyboard(passengerId, LocalizationHelper.getValueByCode(DEPARTURE_DETAILS_MESSAGE, passengerLocale))
        );
    }

}
