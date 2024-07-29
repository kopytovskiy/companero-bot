package com.companerobot.parsers.query_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.MessageExecutionHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.ReviewCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;

import static com.companerobot.constants.Callbacks.*;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.enums.OrderStatus.PASSENGER_CONFIRMATION_WAITING;
import static com.companerobot.enums.OrderStatus.WAITING_DEPARTURE_DETAILS;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;

public class PassengerQueryParser {

    public static void parsePassengerQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String messageText = update.getCallbackQuery().getMessage().toString();
        Long passengerId = update.getCallbackQuery().getFrom().getId();
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        if (callbackData.equals(ADD_POSTPONED_DEPARTURE_DETAILS_CALLBACK)) {
            addDepartureDetails(update);

        } else if (callbackData.equals(LIKE_RIDE_CALLBACK)) {
            addReviewOnDriver(update, messageText, passengerId, passengerLocale, true);

        } else if (callbackData.equals(DISLIKE_RIDE_CALLBACK)) {
            addReviewOnDriver(update, messageText, passengerId, passengerLocale, false);
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

    private static void addReviewOnDriver(Update update, String messageText, Long passengerId, CountryCode passengerLocale, boolean isLiked) {
        MaybeInaccessibleMessage message = update.getCallbackQuery().getMessage();

        String orderId = messageText.substring(
                messageText.indexOf(LocalizationHelper.getValueByCode(ORDER_ID_PART_MESSAGE, passengerLocale))
                        + LocalizationHelper.getValueByCode(ORDER_ID_PART_MESSAGE, passengerLocale).length(),
                messageText.indexOf("\n"));


        Document order = OrderCollection.getOrderByOrderId(orderId);
        Long driverId = Long.valueOf(order.get("driverId").toString());

        ReviewCollection.addUserReview(driverId, passengerId, isLiked);
        MessageExecutionHelper.editMessage(message, LocalizationHelper.getValueByCode(AFTER_RATE_MESSAGE, passengerLocale));
    }

}
