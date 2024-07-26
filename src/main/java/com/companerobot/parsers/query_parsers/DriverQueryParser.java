package com.companerobot.parsers.query_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.OrderStatus;
import com.companerobot.enums.OrderType;
import com.companerobot.helpers.CipherHelper;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.MessageExecutionHelper;
import com.companerobot.keyboards.InlineKeyboardHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.ReviewCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;

import java.util.Objects;

import static com.companerobot.constants.Callbacks.*;
import static com.companerobot.constants.HookMessages.*;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.enums.OrderStatus.*;
import static com.companerobot.enums.OrderStatus.DRIVER_WAITING;
import static com.companerobot.enums.OrderType.IMMEDIATE;
import static com.companerobot.enums.OrderType.POSTPONED;
import static com.companerobot.enums.UserStatus.ACTIVE;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageToUser;

public class DriverQueryParser {

    public static void parseDriverQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String messageText = update.getCallbackQuery().getMessage().toString();
        Long driverId = update.getCallbackQuery().getFrom().getId();
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        switch (callbackData) {
            case ACCEPT_ORDER_CALLBACK -> acceptOrder(update, messageText, driverId, driverLocale);
            case LIKE_RIDE_CALLBACK -> addReviewOnPassenger(update, messageText, driverId, driverLocale, true);
            case DISLIKE_RIDE_CALLBACK -> addReviewOnPassenger(update, messageText, driverId, driverLocale, false);
        }
    }

    private static void acceptOrder(Update update, String messageText, Long driverId, CountryCode driverLocale) {
        String orderId = messageText.substring(messageText.indexOf(LocalizationHelper.getValueByCode(ORDER_ID_PART_MESSAGE, driverLocale)) + LocalizationHelper.getValueByCode(ORDER_ID_PART_MESSAGE, driverLocale).length(), messageText.indexOf("\n"));
        if (OrderCollection.getOrderByOrderId(orderId) != null
                && !OrderCollection.isDriverHasUnfinishedTrips(driverId)
                && OrderStatus.valueOf(OrderCollection.getOrderByOrderId(orderId).get("orderStatus").toString()) == DRIVER_WAITING
                && UserCollection.getUserStatus(driverId) == ACTIVE) {

            OrderType orderType = OrderCollection.getOrderType(orderId);

            if (orderType == IMMEDIATE) {
                acceptImmediateOrder(update, orderId, driverId, driverLocale);

            } else if (orderType == POSTPONED) {
                acceptPostponedOrder(update, orderId, driverId, driverLocale);
            }

        } else if (OrderCollection.isDriverHasUnfinishedTrips(driverId)) {
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(HAS_ALREADY_ORDER_ERROR_MESSAGE, driverLocale));

        } else if (OrderStatus.valueOf(OrderCollection.getOrderByOrderId(orderId).get("orderStatus").toString()) != DRIVER_WAITING) {
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(UNAVAILABLE_ORDER_MESSAGE, driverLocale));

        } else if (UserCollection.getUserStatus(driverId) != ACTIVE) {
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(ERROR_DRIVER_NEED_EXIT_SETTINGS_MENU_MESSAGE, driverLocale));
        }
    }


    private static void acceptImmediateOrder(Update update, String orderId, Long driverId, CountryCode driverLocale) {
        OrderCollection.setDriverId(orderId, driverId);
        OrderCollection.updateOrderStatus(orderId, WAITING_DRIVER_LOCATION);

        Document order = OrderCollection.getOrderByOrderId(orderId);
        Long passengerId = Long.valueOf(order.get("userId").toString());
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        String driverName = UserCollection.getUserName(driverId);
        String driverPhoneNumber = UserCollection.getUserPhoneNumber(driverId);

        Document driverInfo = DriverCollection.getDriverInfoByDriverId(driverId);
        String driverCarModel = driverInfo.get("carModel").toString();
        String driverCarColor = driverInfo.get("carColor").toString();
        String driverCarNumber = CipherHelper.decrypt(driverInfo.get("carNumber").toString());

        int driverReviewsAmount = ReviewCollection.getUserReviewsAmount(driverId);
        double driverRating = ReviewCollection.getUserRating(driverId);

        String driverUserTag = update.getCallbackQuery().getFrom().getUserName();
        if (!Objects.equals(UserCollection.getUserTag(driverId), driverUserTag)) {
            UserCollection.updateUserTag(driverId, driverUserTag);
        }

        boolean isDriverPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(driverId));
        StringBuilder driverInfoMessage = new StringBuilder(LocalizationHelper.getValueByCode(DRIVER_INFO_BASE_MESSAGE, passengerLocale).formatted(driverName))
                .append(LocalizationHelper.getValueByCode(DRIVER_INFO_CAR_PLATE_MESSAGE, passengerLocale).formatted(driverCarModel, driverCarColor, driverCarNumber));

        if (!isDriverPhoneNumberHidden) {
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_PHONE_NUMBER_MESSAGE, passengerLocale).formatted(driverPhoneNumber));
        }

        if (driverReviewsAmount >= 10) {
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_RATING_MESSAGE, passengerLocale).formatted(driverRating));
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_REVIEWS_AMOUNT_MESSAGE, passengerLocale).formatted(driverReviewsAmount));
        }

        if (isDriverPhoneNumberHidden && driverUserTag == null) {
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE, driverLocale));
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_MISSING_CONTACTS_MESSAGE, passengerLocale));
        }

        driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_LOCATION_MESSAGE, passengerLocale));

        if (driverUserTag == null) {
            sendMessageToUser(passengerId, driverInfoMessage.toString());
        } else {
            sendMessageExecutor(
                    InlineKeyboardHelper.driverContactsMarkupKeyboard(driverId, passengerId, driverInfoMessage.toString())
            );
        }

//        if (userTag == null) { //TODO: Refactor to StringBuilder
//            if (!Objects.equals(UserCollection.getUserTag(driverId), userTag)) {
//                UserCollection.updateUserTag(driverId, userTag);
//            }
//
//            if (isPhoneNumberHidden) {
//                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE, driverLocale));
//                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_TAG_AND_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber));
//            } else {
//                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber, driverPhoneNumber));
//            }
//        } else {
//            if (!Objects.equals(UserCollection.getUserTag(driverId), userTag)) {
//                UserCollection.updateUserTag(driverId, userTag);
//            }
//
//            String driverFoundMessage;
//            if (isPhoneNumberHidden) {
//                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber);
//            } else {
//                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber, driverPhoneNumber);
//            }
//
//            sendMessageExecutor(InlineKeyboardHelper.driverContactsMarkupKeyboard(driverId, passengerId, driverFoundMessage));
//        }

        DriverCollection.setIsWaitingForNewTrip(driverId, false);
        sendMessageExecutor(
                ReplyKeyboardHelper.requestLocationWithButton(driverId, LocalizationHelper.getValueByCode(REQUEST_DRIVER_LOCATION_MESSAGE, driverLocale)
                        .formatted(LocalizationHelper.getValueByCode(SHARE_LOCATION_HOOK_MESSAGE, driverLocale)))
        );
    }


    private static void acceptPostponedOrder(Update update, String orderId, Long driverId, CountryCode driverLocale) {
        OrderCollection.setDriverId(orderId, driverId);
        OrderCollection.updateOrderStatus(orderId, POSTPONED_CONVERSATION_RESULT_WAITING);

        Document order = OrderCollection.getOrderByOrderId(orderId);
        Long passengerId = Long.valueOf(order.get("userId").toString());
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        String driverName = UserCollection.getUserName(driverId);
        String driverPhoneNumber = UserCollection.getUserPhoneNumber(driverId);

        Document driverInfo = DriverCollection.getDriverInfoByDriverId(driverId);
        String driverCarModel = driverInfo.get("carModel").toString();

        int driverReviewsAmount = ReviewCollection.getUserReviewsAmount(driverId);
        double driverRating = ReviewCollection.getUserRating(driverId);

        String driverUserTag = update.getCallbackQuery().getFrom().getUserName();
        if (!Objects.equals(UserCollection.getUserTag(driverId), driverUserTag)) {
            UserCollection.updateUserTag(driverId, driverUserTag);
        }

        boolean isDriverPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(driverId));
        StringBuilder driverInfoMessage = new StringBuilder(LocalizationHelper.getValueByCode(DRIVER_INFO_BASE_MESSAGE, passengerLocale).formatted(driverName))
                .append(LocalizationHelper.getValueByCode(DRIVER_INFO_CAR_PLATE_POSTPONED_MESSAGE, passengerLocale).formatted(driverCarModel));

        if (!isDriverPhoneNumberHidden) {
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_PHONE_NUMBER_MESSAGE, passengerLocale).formatted(driverPhoneNumber));
        }

        if (driverReviewsAmount >= 10) {
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_RATING_MESSAGE, passengerLocale).formatted(driverRating));
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_REVIEWS_AMOUNT_MESSAGE, passengerLocale).formatted(driverReviewsAmount));
        }

        if (isDriverPhoneNumberHidden && driverUserTag == null) {
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE, driverLocale));
            driverInfoMessage.append(LocalizationHelper.getValueByCode(DRIVER_INFO_MISSING_CONTACTS_MESSAGE, passengerLocale));
        } else {
            driverInfoMessage.append(LocalizationHelper.getValueByCode(FURTHER_CONTACTS_POSTPONED_MESSAGE, passengerLocale));
        }

        if (driverUserTag == null) {
            sendMessageToUser(passengerId, driverInfoMessage.toString());
        } else {
            sendMessageExecutor(
                    InlineKeyboardHelper.driverContactsMarkupKeyboard(driverId, passengerId, driverInfoMessage.toString())
            );
        }


//        if (driverUserTag == null) { //TODO: Refactor to StringBuilder
//
//            if (isDriverPhoneNumberHidden) {
//                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE, driverLocale));
//                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_TAG_AND_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel));
//
//            } else {
//                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverPhoneNumber));
//            }
//        } else {
//            String driverFoundMessage;
//            if (isDriverPhoneNumberHidden) {
//                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel);
//            } else {
//                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverPhoneNumber);
//            }
//
//            sendMessageExecutor(
//                    InlineKeyboardHelper.driverContactsMarkupKeyboard(driverId, passengerId, driverFoundMessage)
//            );
//
//        }

        sendMessageExecutor(
                ReplyKeyboardHelper.postponedOrderPassengerKeyboard(
                        passengerId, LocalizationHelper.getValueByCode(FURTHER_STEPS_FOR_PASSENGER_POSTPONED_MESSAGE, passengerLocale)
                                .formatted(
                                        LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, passengerLocale),
                                        LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, passengerLocale)))
        );


        //FOR DRIVER
        String passengerName = UserCollection.getUserName(passengerId);
        String passengerPhoneNumber = UserCollection.getUserPhoneNumber(passengerId);

        String passengerUserTag = UserCollection.getUserTag(passengerId);
        boolean isPassengerPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(passengerId));

        int passengerReviewsAmount = ReviewCollection.getUserReviewsAmount(passengerId);
        double passengerRating = ReviewCollection.getUserRating(passengerId);

        StringBuilder passengerInfoMessage = new StringBuilder(LocalizationHelper.getValueByCode(PASSENGER_INFO_BASE_MESSAGE, driverLocale).formatted(passengerName));

        if (!isPassengerPhoneNumberHidden) {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PASSENGER_INFO_PHONE_NUMBER_MESSAGE, driverLocale).formatted(passengerPhoneNumber));
        }

        if (passengerReviewsAmount >= 10) {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PASSENGER_INFO_RATING_MESSAGE, driverLocale).formatted(passengerRating));
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PASSENGER_INFO_REVIEWS_AMOUNT_MESSAGE, driverLocale).formatted(passengerReviewsAmount));
        }

        if (isPassengerPhoneNumberHidden && passengerUserTag == null) {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PASSENGER_INFO_MISSING_CONTACTS_MESSAGE, driverLocale));
        } else {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(FURTHER_CONTACTS_POSTPONED_MESSAGE, passengerLocale));
        }

        passengerInfoMessage.append(LocalizationHelper.getValueByCode(FINANCIAL_OPERATIONS_WARN_MESSAGE, driverLocale));

        if (passengerUserTag == null) {
            sendMessageToUser(driverId, passengerInfoMessage.toString());
        } else {
            sendMessageExecutor(
                    InlineKeyboardHelper.passengerContactsMarkupKeyboard(driverId, passengerId, passengerInfoMessage.toString())
            );
        }

//        if (passengerUserTag == null) { //TODO: Refactor to StringBuilder
//            if (isPassengerPhoneNumberHidden) {
//                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITHOUT_TAG_AND_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName));
//            } else {
//                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITH_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName, passengerPhoneNumber));
//            }
//        } else {
//            String passengerDataMessage;
//            if (isPassengerPhoneNumberHidden) {
//                passengerDataMessage = LocalizationHelper.getValueByCode(PASSENGER_INFO_WITHOUT_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName);
//            } else {
//                passengerDataMessage = LocalizationHelper.getValueByCode(PASSENGER_INFO_WITH_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName, passengerPhoneNumber);
//            }
//
//            sendMessageExecutor(
//                    InlineKeyboardHelper.passengerContactsMarkupKeyboard(driverId, passengerId, passengerDataMessage)
//            );
//        }

        sendMessageExecutor(
                ReplyKeyboardHelper.postponedOrderDriverKeyboard(
                        driverId, LocalizationHelper.getValueByCode(FURTHER_STEPS_FOR_DRIVER_POSTPONED_MESSAGE, driverLocale)
                                .formatted(
                                        LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale),
                                        LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale)))
        );

        DriverCollection.setIsWaitingForNewTrip(driverId, false);
    }


    private static void addReviewOnPassenger(Update update, String messageText, Long driverId, CountryCode driverLocale, boolean isLiked) {
        MaybeInaccessibleMessage message = update.getCallbackQuery().getMessage();

        String orderId = messageText.substring(
                messageText.indexOf(LocalizationHelper.getValueByCode(ORDER_ID_PART_MESSAGE, driverLocale))
                        + LocalizationHelper.getValueByCode(ORDER_ID_PART_MESSAGE, driverLocale).length(),
                messageText.indexOf("\n"));


        Document order = OrderCollection.getOrderByOrderId(orderId);
        Long passengerId = Long.valueOf(order.get("userId").toString());

        ReviewCollection.addUserReview(passengerId, driverId, isLiked);
        MessageExecutionHelper.editMessage(message, LocalizationHelper.getValueByCode(AFTER_RATE_MESSAGE, driverLocale));
    }
}
