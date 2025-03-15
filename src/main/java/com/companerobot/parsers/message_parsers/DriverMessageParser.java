package com.companerobot.parsers.message_parsers;

import com.companerobot.enums.*;
import com.companerobot.helpers.CipherHelper;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.keyboards.InlineKeyboardHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.keyboards.UserManagementReplyKeyboards;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.companerobot.constants.HookMessages.*;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.constants.TextValues.*;
import static com.companerobot.enums.DriverInfoFillingStatus.*;
import static com.companerobot.enums.OrderStatus.*;
import static com.companerobot.enums.UserStatus.*;
import static com.companerobot.helpers.MessageExecutionHelper.*;

public class DriverMessageParser {

    public static void parseDriverMessage(Message message) {
        Long driverId = message.getFrom().getId();
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);
        Document driverInfo = DriverCollection.getDriverInfoByDriverId(driverId);

        String messageText = message.getText();
        String localizedStartWorkShift = LocalizationHelper.getValueByCode(START_WORK_SHIFT_HOOK_MESSAGE, driverLocale);
        String localizedStopWorkShift = LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale);
        String localizedConfirmArrival = LocalizationHelper.getValueByCode(CONFIRM_ARRIVAL_HOOK_MESSAGE, driverLocale);
        String localizedFinishOrder = LocalizationHelper.getValueByCode(FINISH_ORDER_HOOK_MESSAGE, driverLocale);
        String localizedCancelOrder = LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, driverLocale);
        String localizedDeclinePostponedOrder = LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale);
        String localizedConfirmPostponedOrder = LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale);
        String localizedGetSupport = LocalizationHelper.getValueByCode(GET_SUPPORT_HOOK_MESSAGE, driverLocale);

        //USER SETTINGS
        String localizedOpenSettings = LocalizationHelper.getValueByCode(OPEN_SETTINGS_HOOK_MESSAGE, driverLocale);
        String localizedExitSettings = LocalizationHelper.getValueByCode(EXIT_SETTINGS_HOOK_MESSAGE, driverLocale);
        String localizedBackToPreviousPage = LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, driverLocale);
        String localizedEditUserName = LocalizationHelper.getValueByCode(EDIT_USER_NAME_HOOK_MESSAGE, driverLocale);
        String localizedRemovePhoneNumber = LocalizationHelper.getValueByCode(REMOVE_PHONE_NUMBER_HOOK_MESSAGE, driverLocale);
        String localizedEditPhoneNumber = LocalizationHelper.getValueByCode(EDIT_PHONE_NUMBER_HOOK_MESSAGE, driverLocale);
        String localizedEditSharePhoneNumber = LocalizationHelper.getValueByCode(EDIT_SHARE_PHONE_NUMBER_HOOK_MESSAGE, driverLocale);
        String localizedRevealPhoneNumber = LocalizationHelper.getValueByCode(REVEAL_PHONE_NUMBER_HOOK_MESSAGE, driverLocale);
        String localizedHidePhoneNumber = LocalizationHelper.getValueByCode(HIDE_PHONE_NUMBER_HOOK_MESSAGE, driverLocale);

        if (messageText.equals(localizedStartWorkShift)) {
            startWorkShift(driverInfo, driverId, driverLocale);

        } else if (messageText.equals(localizedStopWorkShift)) {
            stopWorkShift(driverInfo, driverId, driverLocale);

        } else if (messageText.equals(localizedConfirmArrival)) {
            confirmArrival(driverId, driverLocale);

        } else if (messageText.equals(localizedFinishOrder)) {
            finishOrder(driverId, driverLocale);

        } else if (messageText.equals(localizedCancelOrder)) {
            cancelOrder(driverId, driverLocale);

        } else if (messageText.equals(localizedDeclinePostponedOrder)) {
            declinePostponedOrder(driverId, driverLocale);

        } else if (messageText.equals(localizedConfirmPostponedOrder)) {
            confirmPostponedOrder(driverId, driverLocale);

        } else if (messageText.equals(localizedGetSupport)) {
            getSupport(driverId, driverLocale);

            //USER SETTINGS
        } else if (messageText.equals(localizedOpenSettings)) {
            openSettings(driverId, driverLocale);

        } else if (messageText.equals(localizedExitSettings)) {
            exitSettings(driverId, driverLocale);

        } else if (messageText.equals(localizedBackToPreviousPage)) {
            backToPreviousPage(driverId, driverLocale);

        } else if (messageText.equals(localizedEditUserName)) {
            editUserName(driverId, driverLocale);

        } else if (messageText.equals(localizedRemovePhoneNumber)) {
            removePhoneNumber(driverId, driverLocale);

        } else if (messageText.equals(localizedEditPhoneNumber)) {
            editPhoneNumber(driverId, driverLocale);

        } else if (messageText.equals(localizedEditSharePhoneNumber)) {
            editSharePhoneNumber(driverId, driverLocale);

        } else if (messageText.equals(localizedRevealPhoneNumber)) {
            revealPhoneNumber(driverId, driverLocale);

        } else if (messageText.equals(localizedHidePhoneNumber)) {
            hidePhoneNumber(driverId, driverLocale);

        } else {
            parseCustomMessage(message, driverInfo, driverId, driverLocale);
        }
    }


    private static void startWorkShift(Document driverInfo, Long driverId, CountryCode driverLocale) {
        if (!Boolean.parseBoolean(driverInfo.get("isWaitingForNewTrip").toString())) {
            DriverCollection.setIsWaitingForNewTrip(driverId, true);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void confirmArrival(Long driverId, CountryCode driverLocale) {
        Document order = OrderCollection.getOrderByDriverIdAndStatus(driverId, DRIVER_ACCEPTED);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            String destinationAddress = CipherHelper.decrypt(order.get("destinationAddress").toString());
            String destinationAddressLatitude = CipherHelper.decrypt(order.get("destinationAddressLatitude").toString());
            String destinationAddressLongitude = CipherHelper.decrypt(order.get("destinationAddressLongitude").toString());
            OrderCollection.updateOrderStatus(orderId, DRIVER_ON_PICKUP);

            Long passengerId = Long.valueOf(order.get("userId").toString());
            CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);
            sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_ARRIVED_PASSENGER_MESSAGE, passengerLocale));

            double price = Double.parseDouble(order.get("price").toString());
            if (price > 0.00) {
                String currency = order.get("currency").toString();
                InlineKeyboardMarkup openWalletKeyboard = InlineKeyboardHelper.openWallet(passengerId);
                SendMessage openWalletSendMessage = SendMessage.builder()
                        .chatId(passengerId.toString())
                        .replyMarkup(openWalletKeyboard)
                        .parseMode("html")
                        .text(LocalizationHelper.getValueByCode(PAYMENT_REMINDER_MESSAGE, passengerLocale).formatted(price, currency))
                        .build();
                sendMessageExecutor(openWalletSendMessage);
            }

            InlineKeyboardMarkup openDestinationPointKeyboard = InlineKeyboardHelper.destinationPointMarkupKeyboard(driverId, destinationAddressLatitude, destinationAddressLongitude);
            SendMessage destinationPointSendMessage = SendMessage.builder()
                    .chatId(driverId.toString())
                    .replyMarkup(openDestinationPointKeyboard)
                    .parseMode("html")
                    .text(LocalizationHelper.getValueByCode(DESTINATION_POINT_DRIVER_MESSAGE, driverLocale).formatted(destinationAddress))
                    .build();
            sendMessageExecutor(destinationPointSendMessage);

            sendMessageExecutor(ReplyKeyboardHelper.driverOnPickUpPointKeyboard(driverId, LocalizationHelper.getValueByCode(FUTURE_FINISH_ORDER_REMINDER_MESSAGE, driverLocale)
                    .formatted(LocalizationHelper.getValueByCode(FINISH_ORDER_HOOK_MESSAGE, driverLocale))));

        }
    }


    private static void stopWorkShift(Document driverInfo, Long driverId, CountryCode driverLocale) {
        if (Boolean.parseBoolean(driverInfo.get("isWaitingForNewTrip").toString())) {
            DriverCollection.setIsWaitingForNewTrip(driverId, false);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverGetOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(START_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(START_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void finishOrder(Long driverId, CountryCode driverLocale) {
        Document order = OrderCollection.getOrderByDriverIdAndStatus(driverId, DRIVER_ON_PICKUP);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, ORDER_FINISHED);

            Long passengerId = Long.valueOf(order.get("userId").toString());
            CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

            sendMessageExecutor(
                    InlineKeyboardHelper.requestRideReviewMarkupKeyboard(driverId, LocalizationHelper.getValueByCode(ADD_REVIEW_ON_PASSENGER_FOR_DRIVERS_MESSAGE, driverLocale).formatted(orderId))
            );

            sendMessageExecutor(
                    InlineKeyboardHelper.requestRideReviewMarkupKeyboard(passengerId, LocalizationHelper.getValueByCode(ADD_REVIEW_ON_DRIVER_FOR_PASSENGERS_MESSAGE, passengerLocale).formatted(orderId))
            );

            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuPassengerKeyboard(passengerId, LocalizationHelper.getValueByCode(FINISHED_ORDER_PASSENGER_MESSAGE, passengerLocale)
                            .formatted(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale)))
            );
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(FINISHED_ORDER_DRIVER_MESSAGE, driverLocale));
            DriverCollection.setIsWaitingForNewTrip(driverId, true);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void cancelOrder(Long driverId, CountryCode driverLocale) {
        Document order = OrderCollection.getUnfinishedOrderByDriverId(driverId);
        OrderStatus orderStatus = OrderStatus.valueOf(order.get("orderStatus").toString());

        if (orderStatus == DRIVER_ACCEPTED || orderStatus == DRIVER_ON_PICKUP) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, DRIVER_WAITING);
            OrderCollection.setDriverId(orderId, null);
            sendOrderToDrivers(OrderCollection.getOrderByOrderId(orderId));

            Long passengerId = Long.valueOf(order.get("userId").toString());
            CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);
            sendMessageExecutor(
                    ReplyKeyboardHelper.cancelOrderButtonKeyboard(passengerId, LocalizationHelper.getValueByCode(CANCEL_TRIP_BY_DRIVER_MESSAGE, passengerLocale))
            );

            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(CANCEL_TRIP_BY_DRIVER_SELF_MESSAGE, driverLocale));
            DriverCollection.setIsWaitingForNewTrip(driverId, true);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void declinePostponedOrder(Long driverId, CountryCode driverLocale) {
        Document order = OrderCollection.getOrderByDriverIdAndStatus(driverId, POSTPONED_CONVERSATION_RESULT_WAITING);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, DRIVER_WAITING);
            OrderCollection.setDriverId(orderId, null);
            sendOrderToDrivers(OrderCollection.getOrderByOrderId(orderId));

            Long passengerId = Long.valueOf(order.get("userId").toString());
            CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);
            sendMessageExecutor(
                    ReplyKeyboardHelper.cancelOrderButtonKeyboard(passengerId, LocalizationHelper.getValueByCode(DECLINED_POSTPONED_TRIP_FOR_PASSENGER_MESSAGE, passengerLocale))
            );

            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(DECLINED_POSTPONED_TRIP_FOR_DRIVER_MESSAGE, driverLocale));
            DriverCollection.setIsWaitingForNewTrip(driverId, true);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void confirmPostponedOrder(Long driverId, CountryCode driverLocale) {
        Document order = OrderCollection.getOrderByDriverIdAndStatus(driverId, POSTPONED_CONVERSATION_RESULT_WAITING);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, ORDER_FINISHED);
            OrderCollection.setDriverId(orderId, driverId);

            Long passengerId = Long.valueOf(order.get("userId").toString());
            CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuPassengerKeyboard(passengerId, LocalizationHelper.getValueByCode(CONFIRMED_POSTPONED_TRIP_FOR_PASSENGER_MESSAGE, passengerLocale)
                            .formatted(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale)))
            );

            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(CONFIRMED_POSTPONED_TRIP_FOR_DRIVER_MESSAGE, driverLocale));
            DriverCollection.setIsWaitingForNewTrip(driverId, true);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void getSupport(Long driverId, CountryCode driverLocale) {
        sendMessageToUser(driverId, LocalizationHelper.getValueByCode(GET_SUPPORT_MESSAGE, driverLocale));
    }


    //USER SETTINGS
    private static void openSettings(Long driverId, CountryCode driverLocale) {
        if (!OrderCollection.isDriverHasUnfinishedTrips(driverId)
                && UserCollection.getUserStatus(driverId) == ACTIVE) {
            loadSettingsMenu(driverId, driverLocale);
        }
    }


    private static void exitSettings(Long driverId, CountryCode driverLocale) {
        if (!OrderCollection.isDriverHasUnfinishedTrips(driverId)
                && UserCollection.getUserStatus(driverId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(driverId, ACTIVE);

            boolean isWaitingForNewTrip = DriverCollection.getIsWaitingForNewTrip(driverId);
            if (isWaitingForNewTrip) {
                sendMessageExecutor(
                        ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                                .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
                );
            } else {
                sendMessageExecutor(
                        ReplyKeyboardHelper.mainMenuDriverGetOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(START_SHIFT_MESSAGE, driverLocale)
                                .formatted(LocalizationHelper.getValueByCode(START_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
                );
            }
        }
    }


    private static void backToPreviousPage(Long driverId, CountryCode driverLocale) {
        UserStatus userStatus = UserCollection.getUserStatus(driverId);
        if (userStatus == EDIT_PHONE_NUMBER || userStatus == EDIT_SHARE_PHONE_NUMBER) {
            loadSettingsMenu(driverId, driverLocale);
        }
    }


    private static void editUserName(Long driverId, CountryCode driverLocale) {
        if (UserCollection.getUserStatus(driverId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(driverId, EDIT_USER_NAME);
            sendMessageExecutor(
                    UserManagementReplyKeyboards.changeUsernameKeyboard(driverId, LocalizationHelper.getValueByCode(EDIT_USER_NAME_MESSAGE, driverLocale))
            );
        }
    }


    private static void removePhoneNumber(Long driverId, CountryCode driverLocale) {
        if (UserCollection.getUserStatus(driverId) == EDIT_PHONE_NUMBER) {
            if (UserCollection.getUserPhoneNumber(driverId) != null) {
                UserCollection.removeUserPhoneNumber(driverId);
                UserCollection.updateIsUserPhoneHidden(driverId, true);
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, driverLocale));
                loadSettingsMenu(driverId, driverLocale);
            } else {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(ERROR_NO_PHONE_NUMBER_MESSAGE, driverLocale));
            }
        }
    }


    private static void editPhoneNumber(Long driverId, CountryCode driverLocale) {
        if (UserCollection.getUserStatus(driverId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(driverId, EDIT_PHONE_NUMBER);

            if (UserCollection.getUserPhoneNumber(driverId) != null) {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.updateOrRemovePhoneNumberKeyboard(driverId, LocalizationHelper.getValueByCode(REMOVE_PHONE_NUMBER_MESSAGE, driverLocale))
                );
            } else {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.changePhoneNumberKeyboard(driverId, LocalizationHelper.getValueByCode(EDIT_PHONE_NUMBER_MESSAGE, driverLocale))
                );
            }
        }
    }


    private static void editSharePhoneNumber(Long driverId, CountryCode driverLocale) {
        if (UserCollection.getUserStatus(driverId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(driverId, EDIT_SHARE_PHONE_NUMBER);
            boolean isPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(driverId));
            if (isPhoneNumberHidden) {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.hiddenNumberKeyboard(driverId, LocalizationHelper.getValueByCode(HIDDEN_PHONE_NUMBER_TRUE_MESSAGE, driverLocale)
                                .formatted(LocalizationHelper.getValueByCode(REVEAL_PHONE_NUMBER_HOOK_MESSAGE, driverLocale)))
                );
            } else {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.notHiddenNumberKeyboard(driverId, LocalizationHelper.getValueByCode(HIDDEN_PHONE_NUMBER_FALSE_MESSAGE, driverLocale)
                                .formatted(LocalizationHelper.getValueByCode(HIDE_PHONE_NUMBER_HOOK_MESSAGE, driverLocale)))
                );
            }
        }
    }


    private static void revealPhoneNumber(Long driverId, CountryCode driverLocale) {
        if (UserCollection.getUserStatus(driverId) == EDIT_SHARE_PHONE_NUMBER) {
            String driverPhoneNumber = UserCollection.getUserPhoneNumber(driverId);

            if (driverPhoneNumber != null) {
                UserCollection.updateIsUserPhoneHidden(driverId, false);
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, driverLocale));
                loadSettingsMenu(driverId, driverLocale);
            } else {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(ERROR_NO_PHONE_NUMBER_MESSAGE, driverLocale));
            }
        }
    }


    private static void hidePhoneNumber(Long driverId, CountryCode driverLocale) {
        if (UserCollection.getUserStatus(driverId) == EDIT_SHARE_PHONE_NUMBER) {
            String driverPhoneNumber = UserCollection.getUserPhoneNumber(driverId);

            if (driverPhoneNumber != null) {
                UserCollection.updateIsUserPhoneHidden(driverId, true);
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, driverLocale));
                loadSettingsMenu(driverId, driverLocale);
            } else {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(ERROR_NO_PHONE_NUMBER_MESSAGE, driverLocale));
            }
        }
    }


    private static void parseCustomMessage(Message message, Document driverInfo, Long driverId, CountryCode driverLocale) {
        DriverInfoFillingStatus driverStatus = DriverInfoFillingStatus.valueOf(driverInfo.get("driverInfoFillingStatus").toString());

        if (driverStatus == INFO_IS_FILLED) {
            parseCustomMessageForUser(message, driverId, driverLocale);
        } else {
            parseCustomMessageForRegistration(message, driverStatus, driverId, driverLocale);
        }
    }


    private static void parseCustomMessageForRegistration(Message message, DriverInfoFillingStatus driverStatus, Long driverId, CountryCode driverLocale) {

        if (driverStatus == WAITING_CAR_MODEL) {
            DriverCollection.setCarModel(driverId, message.getText());
            DriverCollection.setDriverInfoFillingStatus(driverId, WAITING_CAR_COLOR);
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(CAR_COLOR_REQUEST_MESSAGE, driverLocale));

        } else if (driverStatus == WAITING_CAR_COLOR) {
            DriverCollection.setCarColor(driverId, message.getText());
            DriverCollection.setDriverInfoFillingStatus(driverId, WAITING_CAR_NUMBER);
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(CAR_PLATE_NUMBER_REQUEST_MESSAGE, driverLocale));

        } else if (driverStatus == WAITING_CAR_NUMBER) {
            DriverCollection.setCarNumber(driverId, message.getText());
            DriverCollection.setDriverInfoFillingStatus(driverId, WAITING_LOCATION);
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestLocationWithButton(driverId, LocalizationHelper.getValueByCode(DRIVER_LOCATION_REQUEST_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(SHARE_LOCATION_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    private static void parseCustomMessageForUser(Message message, Long driverId, CountryCode driverLocale) {

        UserStatus userStatus = UserCollection.getUserStatus(driverId);

        if (userStatus == EDIT_USER_NAME) {
            if (!message.getText().equals(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, driverLocale))) {
                UserCollection.updateUserName(driverId, message.getText());
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, driverLocale));
            }
            loadSettingsMenu(driverId, driverLocale);
        }
    }


    private static void loadSettingsMenu(Long userId, CountryCode userLocale) {
        String userName = UserCollection.getUserName(userId);
        String phoneNumber = UserCollection.getUserPhoneNumber(userId);
        if (phoneNumber == null) {
            phoneNumber = LocalizationHelper.getValueByCode(EMPTY_VALUE, userLocale);
        } else {
            phoneNumber = "+" + phoneNumber;
        }

        String isPhoneNumberHidden;
        if (Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(userId))) {
            isPhoneNumberHidden = LocalizationHelper.getValueByCode(YES_VALUE, userLocale);
        } else {
            isPhoneNumberHidden = LocalizationHelper.getValueByCode(NO_VALUE, userLocale);
        }

        UserCollection.updateUserStatus(userId, IN_SETTINGS_MENU);
        sendMessageExecutor(
                UserManagementReplyKeyboards.openedSettingsKeyboard(userId,
                        LocalizationHelper.getValueByCode(SHOW_ALL_USER_DATA_MESSAGE, userLocale).formatted(userName, phoneNumber, isPhoneNumberHidden))
        );
    }

}
