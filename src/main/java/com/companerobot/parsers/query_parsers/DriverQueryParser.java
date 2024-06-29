package com.companerobot.parsers.query_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.OrderStatus;
import com.companerobot.enums.OrderType;
import com.companerobot.helpers.CipherHelper;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.keyboards.InlineKeyboardHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

import static com.companerobot.constants.Callbacks.ACCEPT_ORDER_CALLBACK;
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

        if (callbackData.equals(ACCEPT_ORDER_CALLBACK)) {
            acceptOrder(update, messageText, driverId, driverLocale);
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

        String userTag = update.getCallbackQuery().getFrom().getUserName();
        boolean isPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(driverId));

        if (userTag == null) { //TODO: Refactor to StringBuilder
            if (!Objects.equals(UserCollection.getUserTag(driverId), userTag)) {
                UserCollection.updateUserTag(driverId, userTag);
            }

            if (isPhoneNumberHidden) {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE, driverLocale));
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_TAG_AND_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber));
            } else {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber, driverPhoneNumber));
            }
        } else {
            if (!Objects.equals(UserCollection.getUserTag(driverId), userTag)) {
                UserCollection.updateUserTag(driverId, userTag);
            }

            String driverFoundMessage;
            if (isPhoneNumberHidden) {
                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber);
            } else {
                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverCarColor, driverCarNumber, driverPhoneNumber);
            }

            sendMessageExecutor(InlineKeyboardHelper.driverContactsMarkupKeyboard(driverId, passengerId, driverFoundMessage));
        }

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

        String driverUserTag = update.getCallbackQuery().getFrom().getUserName();
        boolean isDriverPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(driverId));

        if (driverUserTag == null) { //TODO: Refactor to StringBuilder
            if (!Objects.equals(UserCollection.getUserTag(driverId), driverUserTag)) {
                UserCollection.updateUserTag(driverId, driverUserTag);
            }

            if (isDriverPhoneNumberHidden) {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE, driverLocale));
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_TAG_AND_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel));

            } else {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverPhoneNumber));
            }
        } else {
            if (!Objects.equals(UserCollection.getUserTag(driverId), driverUserTag)) {
                UserCollection.updateUserTag(driverId, driverUserTag);
            }
            String driverFoundMessage;
            if (isDriverPhoneNumberHidden) {
                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITHOUT_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel);
            } else {
                driverFoundMessage = LocalizationHelper.getValueByCode(DRIVER_FOUND_WITH_NUMBER_POSTPONED_MESSAGE, passengerLocale).formatted(driverName, driverCarModel, driverPhoneNumber);
            }

            sendMessageExecutor(
                    InlineKeyboardHelper.driverContactsMarkupKeyboard(driverId, passengerId, driverFoundMessage)
            );

        }

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

        if (passengerUserTag == null) { //TODO: Refactor to StringBuilder
            if (isPassengerPhoneNumberHidden) {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITHOUT_TAG_AND_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName));
            } else {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITH_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName, passengerPhoneNumber));
            }
        } else {
            String passengerDataMessage;
            if (isPassengerPhoneNumberHidden) {
                passengerDataMessage = LocalizationHelper.getValueByCode(PASSENGER_INFO_WITHOUT_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName);
            } else {
                passengerDataMessage = LocalizationHelper.getValueByCode(PASSENGER_INFO_WITH_NUMBER_POSTPONED_MESSAGE, driverLocale).formatted(passengerName, passengerPhoneNumber);
            }

            sendMessageExecutor(
                    InlineKeyboardHelper.passengerContactsMarkupKeyboard(driverId, passengerId, passengerDataMessage)
            );
        }

        sendMessageExecutor(
                ReplyKeyboardHelper.postponedOrderDriverKeyboard(
                        driverId, LocalizationHelper.getValueByCode(FURTHER_STEPS_FOR_DRIVER_POSTPONED_MESSAGE, driverLocale)
                                .formatted(
                                        LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale),
                                        LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale)))
        );

        DriverCollection.setIsWaitingForNewTrip(driverId, false);
    }

}
