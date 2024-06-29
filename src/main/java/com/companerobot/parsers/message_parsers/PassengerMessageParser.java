package com.companerobot.parsers.message_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.OrderStatus;
import com.companerobot.enums.UserStatus;
import com.companerobot.helpers.CipherHelper;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.PriceCalculatingHelper;
import com.companerobot.helpers.PriceHelper;
import com.companerobot.keyboards.InlineKeyboardHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.keyboards.UserManagementReplyKeyboards;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Objects;

import static com.companerobot.constants.HookMessages.*;
import static com.companerobot.constants.HookMessages.BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.constants.TextValues.*;
import static com.companerobot.constants.TextValues.NO_VALUE;
import static com.companerobot.enums.OrderStatus.*;
import static com.companerobot.enums.OrderType.POSTPONED;
import static com.companerobot.enums.UserStatus.*;
import static com.companerobot.enums.UserStatus.EDIT_SHARE_PHONE_NUMBER;
import static com.companerobot.helpers.MessageExecutionHelper.*;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageToUser;

public class PassengerMessageParser {

    public static void parsePassengerMessage(Message message) {
        Long passengerId = message.getFrom().getId();
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        String messageText = message.getText();
        String localizedCancelOrder = LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, passengerLocale);
        String localizedCreateNewOrder = LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale);
        String localizedAddNotes = LocalizationHelper.getValueByCode(ADD_NOTES_HOOK_MESSAGE, passengerLocale);
        String localizedSkipNotes = LocalizationHelper.getValueByCode(SKIP_NOTES_HOOK_MESSAGE, passengerLocale);
        String localizedFreeRide = LocalizationHelper.getValueByCode(FREE_RIDE_HOOK_MESSAGE, passengerLocale);
        String localizedBackToPreviousPage = LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, passengerLocale);
        String localizedConfirmOrder = LocalizationHelper.getValueByCode(CONFIRM_ORDER_HOOK_MESSAGE, passengerLocale);
        String localizedDeclinePostponedOrder = LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, passengerLocale);
        String localizedConfirmPostponedOrder = LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, passengerLocale);
        String localizedGetSupport = LocalizationHelper.getValueByCode(GET_SUPPORT_HOOK_MESSAGE, passengerLocale);

        //USER SETTINGS
        String localizedOpenSettings = LocalizationHelper.getValueByCode(OPEN_SETTINGS_HOOK_MESSAGE, passengerLocale);
        String localizedExitSettings = LocalizationHelper.getValueByCode(EXIT_SETTINGS_HOOK_MESSAGE, passengerLocale);
        String localizedEditUserName = LocalizationHelper.getValueByCode(EDIT_USER_NAME_HOOK_MESSAGE, passengerLocale);
        String localizedRemovePhoneNumber = LocalizationHelper.getValueByCode(REMOVE_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale);
        String localizedEditPhoneNumber = LocalizationHelper.getValueByCode(EDIT_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale);
        String localizedEditSharePhoneNumber = LocalizationHelper.getValueByCode(EDIT_SHARE_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale);
        String localizedRevealPhoneNumber = LocalizationHelper.getValueByCode(REVEAL_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale);
        String localizedHidePhoneNumber = LocalizationHelper.getValueByCode(HIDE_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale);


        if (messageText.equals(localizedCancelOrder)) {
            cancelOrderByPassenger(passengerId, passengerLocale);

        } else if (messageText.equals(localizedCreateNewOrder)) {
            createNewOrder(passengerId, passengerLocale);

        } else if (messageText.equals(localizedAddNotes)) {
            addNotesToOrder(passengerId, passengerLocale);

        } else if (messageText.equals(localizedSkipNotes)) {
            skipNotesForOrder(passengerId, passengerLocale);

        } else if (messageText.equals(localizedFreeRide)) {
            requestFreeRide(message, passengerId, passengerLocale);

        } else if (messageText.equals(localizedBackToPreviousPage)) {
            backToPreviousPage(passengerId, passengerLocale);

        } else if (messageText.equals(localizedConfirmOrder)) {
            confirmOrder(passengerId, passengerLocale);

        } else if (messageText.equals(localizedDeclinePostponedOrder)) {
            declinePostponedOrder(passengerId, passengerLocale);

        } else if (messageText.equals(localizedConfirmPostponedOrder)) {
            confirmPostponedOrder(passengerId, passengerLocale);

        } else if (messageText.equals(localizedGetSupport)) {
            getSupport(passengerId, passengerLocale);

            //USER SETTINGS
        } else if (messageText.equals(localizedOpenSettings)) {
            openSettings(passengerId, passengerLocale);

        } else if (messageText.equals(localizedExitSettings)) {
            exitSettings(passengerId, passengerLocale);

        } else if (messageText.equals(localizedEditUserName)) {
            editUserName(passengerId, passengerLocale);

        } else if (messageText.equals(localizedRemovePhoneNumber)) {
            removePhoneNumber(passengerId, passengerLocale);

        } else if (messageText.equals(localizedEditPhoneNumber)) {
            editPhoneNumber(passengerId, passengerLocale);

        } else if (messageText.equals(localizedEditSharePhoneNumber)) {
            editSharePhoneNumber(passengerId, passengerLocale);

        } else if (messageText.equals(localizedRevealPhoneNumber)) {
            revealPhoneNumber(passengerId, passengerLocale);

        } else if (messageText.equals(localizedHidePhoneNumber)) {
            hidePhoneNumber(passengerId, passengerLocale);

        } else {
            parseCustomMessage(message, passengerId, passengerLocale);
        }
    }


    private static void parseCustomMessage(Message message, Long passengerId, CountryCode passengerLocale) {
        boolean isPassengerHasUnfinishedTrips = OrderCollection.isPassengerHasUnfinishedTrips(passengerId);

        if (isPassengerHasUnfinishedTrips) {
            parseCustomMessageForOrder(message, passengerId, passengerLocale);
        } else {
            parseCustomMessageForUser(message, passengerId, passengerLocale);
        }
    }


    private static void createNewOrder(Long passengerId, CountryCode passengerLocale) {
        if (!OrderCollection.isPassengerHasUnfinishedTrips(passengerId)
                && UserCollection.getUserStatus(passengerId) == ACTIVE) {
            OrderCollection.createNewOrder(passengerId);
            sendMessageExecutor(
                    ReplyKeyboardHelper.cancelOrderButtonKeyboard(passengerId, LocalizationHelper.getValueByCode(REQUEST_PICKUP_POINT_PASSENGER_MESSAGE, passengerLocale))
            );
        }
    }


    private static void addNotesToOrder(Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_IF_NOTES_NEEDED);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, WAITING_NOTES);
            sendMessageExecutor(
                    ReplyKeyboardHelper.cancelOrderButtonKeyboard(passengerId,
                            LocalizationHelper.getValueByCode(ADD_NOTES_MESSAGE, passengerLocale))
            );
        }
    }


    private static void skipNotesForOrder(Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_IF_NOTES_NEEDED);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.setNotes(orderId, "-");

            OrderCollection.updateOrderStatus(orderId, WAITING_PRICE);
            CountryCode orderCountry = OrderCollection.getOrderCountryCode(orderId);
            String countryCurrency = orderCountry.getCurrency();

            double indexedPrice = PriceCalculatingHelper.getFarePrice(OrderCollection.getTripLength(orderId), orderCountry.getPriceIndex());
            double convertedPrice = PriceCalculatingHelper.convertPriceToLocalCurrency(indexedPrice, countryCurrency);

            OrderCollection.setPriceForOrder(orderId, convertedPrice);
            OrderCollection.setCurrencyForOrder(orderId, countryCurrency);

            sendMessageExecutor(
                    ReplyKeyboardHelper.passengerOrderAddPriceKeyboard(passengerId,
                            LocalizationHelper.getValueByCode(ADD_PRICE_ORDER_MESSAGE, passengerLocale).formatted(convertedPrice, countryCurrency),
                            convertedPrice)
            );
        }
    }


    private static void requestFreeRide(Message message, Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_PRICE);
        if (order != null) {
            OrderCollection.updateOrderStatus(order.get("orderId").toString(), PASSENGER_CONFIRMATION_WAITING);
            OrderCollection.setPriceForOrder(order.get("orderId").toString(), 0.00);

            sendMessageExecutor(
                    InlineKeyboardHelper.changeDepartureTimeKeyboard(passengerId,
                            LocalizationHelper.getValueByCode(CONFIRMATION_PASSENGER_ORDER_FREE_RIDE_MESSAGE, passengerLocale)
                                    .formatted(
                                            CipherHelper.decrypt(order.get("pickUpAddress").toString()),
                                            CipherHelper.decrypt(order.get("destinationAddress").toString()),
                                            CipherHelper.decrypt(order.get("notes").toString()),
                                            LocalizationHelper.getValueByCode(NOW_VALUE, passengerLocale)
                                    ))
            );

            sendMessageExecutor(
                    ReplyKeyboardHelper.passengerOrderConfirmationKeyboard(
                            passengerId, LocalizationHelper.getValueByCode(CONFIRMATION_REMINDER_MESSAGE, passengerLocale)
                    ));

            String userTag = message.getFrom().getUserName(); //.getUserName() - IT IS CORRECT ;)
            boolean isPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(passengerId));
            String userTagInDatabase = UserCollection.getUserTag(passengerId);

            if (userTag == null && isPhoneNumberHidden) {
                if (!Objects.equals(userTagInDatabase, userTag)) {
                    UserCollection.updateUserTag(passengerId, userTag); //Remove tag if it was in DB
                }
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_PASSENGER_WARN_MESSAGE, passengerLocale));
            } else {
                if (!Objects.equals(userTagInDatabase, userTag)) {
                    UserCollection.updateUserTag(passengerId, userTag);
                }
            }
        }
    }


    private static void backToPreviousPage(Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_DEPARTURE_DETAILS);
        if (order != null) {
            OrderCollection.updateOrderStatus(order.get("orderId").toString(), PASSENGER_CONFIRMATION_WAITING);
            double price = Double.parseDouble(order.get("price").toString());

            String allOrderDataMessage;
            if (price == 0.00) {
                allOrderDataMessage = LocalizationHelper.getValueByCode(CONFIRMATION_PASSENGER_ORDER_FREE_RIDE_MESSAGE, passengerLocale);
            } else {
                allOrderDataMessage = LocalizationHelper.getValueByCode(CONFIRMATION_PASSENGER_ORDER_MESSAGE, passengerLocale);
            }

            sendMessageExecutor(
                    InlineKeyboardHelper.changeDepartureTimeKeyboard(passengerId, allOrderDataMessage
                            .formatted(
                                    CipherHelper.decrypt(order.get("pickUpAddress").toString()),
                                    CipherHelper.decrypt(order.get("destinationAddress").toString()),
                                    CipherHelper.decrypt(order.get("notes").toString()),
                                    order.get("departureTime").toString(),
                                    price,
                                    order.get("currency")
                            ))
            );

            sendMessageExecutor(
                    ReplyKeyboardHelper.passengerOrderConfirmationKeyboard(
                            passengerId, LocalizationHelper.getValueByCode(CONFIRMATION_REMINDER_MESSAGE, passengerLocale)
                    ));
        } else {
            UserStatus userStatus = UserCollection.getUserStatus(passengerId);
            if (userStatus == EDIT_PHONE_NUMBER || userStatus == EDIT_SHARE_PHONE_NUMBER) {
                loadSettingsMenu(passengerId, passengerLocale);
            }
        }
    }


    private static void confirmOrder(Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, PASSENGER_CONFIRMATION_WAITING);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, DRIVER_WAITING);

            sendOrderToDrivers(OrderCollection.getOrderByOrderId(orderId));
            sendMessageExecutor(
                    ReplyKeyboardHelper.cancelOrderButtonKeyboard(passengerId, LocalizationHelper.getValueByCode(CONFIRM_ORDER_BY_PASSENGER_MESSAGE, passengerLocale))
            );
        }
    }


    private static void declinePostponedOrder(Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, POSTPONED_CONVERSATION_RESULT_WAITING);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, DRIVER_WAITING);
            OrderCollection.setDriverId(orderId, null);
            sendOrderToDrivers(OrderCollection.getOrderByOrderId(orderId));

            Long driverId = Long.valueOf(order.get("driverId").toString());
            CountryCode driverLocale = UserCollection.getUserLocale(driverId);
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


    private static void confirmPostponedOrder(Long passengerId, CountryCode passengerLocale) {
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, POSTPONED_CONVERSATION_RESULT_WAITING);
        if (order != null) {
            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, ORDER_FINISHED);
            Long driverId = Long.valueOf(order.get("driverId").toString());
            CountryCode driverLocale = UserCollection.getUserLocale(driverId);
            OrderCollection.setDriverId(orderId, driverId);

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


    private static void getSupport(Long passengerId, CountryCode passengerLocale) {
        sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(GET_SUPPORT_MESSAGE, passengerLocale));
    }


    private static void cancelOrderByPassenger(Long passengerId, CountryCode passengerLocale) {

        Document order = OrderCollection.getUnfinishedOrderByPassengerId(passengerId);
        OrderStatus orderStatus = OrderStatus.valueOf(order.get("orderStatus").toString());

        if (orderStatus == DRIVER_WAITING
                || orderStatus == WAITING_PICKUP_ADDRESS
                || orderStatus == WAITING_DESTINATION_ADDRESS
                || orderStatus == WAITING_IF_NOTES_NEEDED
                || orderStatus == WAITING_NOTES
                || orderStatus == WAITING_PRICE
                || orderStatus == PASSENGER_CONFIRMATION_WAITING) {

            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, ORDER_CANCELED);

            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuPassengerKeyboard(passengerId, LocalizationHelper.getValueByCode(CANCEL_TRIP_BY_PASSENGER_SELF_MESSAGE, passengerLocale)
                            .formatted(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale)))
            );

        } else if (orderStatus == WAITING_DRIVER_LOCATION
                || orderStatus == DRIVER_ACCEPTED
                || orderStatus == DRIVER_ON_PICKUP
                || orderStatus == POSTPONED_CONVERSATION_RESULT_WAITING) {

            String orderId = order.get("orderId").toString();
            OrderCollection.updateOrderStatus(orderId, ORDER_CANCELED);
            OrderCollection.setDriverId(orderId, null);

            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuPassengerKeyboard(passengerId, LocalizationHelper.getValueByCode(CANCEL_TRIP_BY_PASSENGER_SELF_MESSAGE, passengerLocale)
                            .formatted(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale)))
            );

            Long driverId = Long.valueOf(order.get("driverId").toString());
            CountryCode driverLocale = UserCollection.getUserLocale(driverId);
            sendMessageToUser(driverId, LocalizationHelper.getValueByCode(CANCEL_TRIP_BY_PASSENGER_MESSAGE, driverLocale));
            DriverCollection.setIsWaitingForNewTrip(driverId, true);

            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
            );
        }
    }


    //USER SETTINGS
    private static void openSettings(Long passengerId, CountryCode passengerLocale) {
        if (!OrderCollection.isPassengerHasUnfinishedTrips(passengerId)
                && UserCollection.getUserStatus(passengerId) == ACTIVE) {
            loadSettingsMenu(passengerId, passengerLocale);
        }
    }


    private static void exitSettings(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(passengerId, ACTIVE);
            sendMessageExecutor(
                    ReplyKeyboardHelper.mainMenuPassengerKeyboard(passengerId, LocalizationHelper.getValueByCode(PASSENGER_ROLE_WELCOME_MESSAGE, passengerLocale)
                            .formatted(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale)))
            );
        }
    }


    private static void editUserName(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(passengerId, EDIT_USER_NAME);
            sendMessageExecutor(
                    UserManagementReplyKeyboards.changeUsernameKeyboard(passengerId, LocalizationHelper.getValueByCode(EDIT_USER_NAME_MESSAGE, passengerLocale))
            );
        }
    }


    private static void removePhoneNumber(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == EDIT_PHONE_NUMBER) {
            if (UserCollection.getUserPhoneNumber(passengerId) != null) {
                UserCollection.removeUserPhoneNumber(passengerId);
                UserCollection.updateIsUserPhoneHidden(passengerId, true);
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, passengerLocale));
                loadSettingsMenu(passengerId, passengerLocale);
            } else {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(ERROR_NO_PHONE_NUMBER_MESSAGE, passengerLocale));
            }
        }
    }


    private static void editPhoneNumber(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(passengerId, EDIT_PHONE_NUMBER);

            if (UserCollection.getUserPhoneNumber(passengerId) != null) {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.updateOrRemovePhoneNumberKeyboard(passengerId, LocalizationHelper.getValueByCode(REMOVE_PHONE_NUMBER_MESSAGE, passengerLocale))
                );
            } else {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.changePhoneNumberKeyboard(passengerId, LocalizationHelper.getValueByCode(EDIT_PHONE_NUMBER_MESSAGE, passengerLocale))
                );
            }
        }
    }


    private static void editSharePhoneNumber(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == IN_SETTINGS_MENU) {
            UserCollection.updateUserStatus(passengerId, EDIT_SHARE_PHONE_NUMBER);
            boolean isPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(passengerId));
            if (isPhoneNumberHidden) {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.hiddenNumberKeyboard(passengerId, LocalizationHelper.getValueByCode(HIDDEN_PHONE_NUMBER_TRUE_MESSAGE, passengerLocale)
                                .formatted(LocalizationHelper.getValueByCode(REVEAL_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale)))
                );
            } else {
                sendMessageExecutor(
                        UserManagementReplyKeyboards.notHiddenNumberKeyboard(passengerId, LocalizationHelper.getValueByCode(HIDDEN_PHONE_NUMBER_FALSE_MESSAGE, passengerLocale)
                                .formatted(LocalizationHelper.getValueByCode(HIDE_PHONE_NUMBER_HOOK_MESSAGE, passengerLocale)))
                );
            }
        }
    }


    private static void revealPhoneNumber(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == EDIT_SHARE_PHONE_NUMBER) {
            String passengerPhoneNumber = UserCollection.getUserPhoneNumber(passengerId);

            if (passengerPhoneNumber != null) {
                UserCollection.updateIsUserPhoneHidden(passengerId, false);
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, passengerLocale));
                loadSettingsMenu(passengerId, passengerLocale);
            } else {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(ERROR_NO_PHONE_NUMBER_MESSAGE, passengerLocale));
            }
        }
    }


    private static void hidePhoneNumber(Long passengerId, CountryCode passengerLocale) {
        if (UserCollection.getUserStatus(passengerId) == EDIT_SHARE_PHONE_NUMBER) {
            String passengerPhoneNumber = UserCollection.getUserPhoneNumber(passengerId);

            if (passengerPhoneNumber != null) {
                UserCollection.updateIsUserPhoneHidden(passengerId, true);
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, passengerLocale));
                loadSettingsMenu(passengerId, passengerLocale);
            } else {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(ERROR_NO_PHONE_NUMBER_MESSAGE, passengerLocale));
            }
        }
    }


    private static void parseCustomMessageForOrder(Message message, Long passengerId, CountryCode passengerLocale) {

        Document waitingNotesOrder = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_NOTES);
        Document waitingPriceOrder = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_PRICE);
        Document waitingDepartureDetails = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_DEPARTURE_DETAILS);


        if (waitingNotesOrder != null) {
            addNotes(waitingNotesOrder, message, passengerId, passengerLocale);

        } else if (waitingPriceOrder != null) {
            addPrice(waitingPriceOrder, message, passengerId, passengerLocale);

        } else if (waitingDepartureDetails != null) {
            addDepartureDetails(waitingDepartureDetails, message, passengerId, passengerLocale);
        }

    }


    private static void addNotes(Document order, Message message, Long passengerId, CountryCode passengerLocale) {
        String orderId = order.get("orderId").toString();
        OrderCollection.setNotes(orderId, message.getText());

        OrderCollection.updateOrderStatus(orderId, WAITING_PRICE);
        CountryCode orderCountry = OrderCollection.getOrderCountryCode(orderId);
        String countryCurrency = orderCountry.getCurrency();

        double indexedPrice = PriceCalculatingHelper.getFarePrice(OrderCollection.getTripLength(orderId), orderCountry.getPriceIndex());
        double exchangeRate = 1.00;

        if (!countryCurrency.equals("EUR")) {
            JSONObject currencyResponse = PriceCalculatingHelper.getExchangeRateByCurrency(countryCurrency);
            if (currencyResponse.has("rate")) {
                exchangeRate = currencyResponse.getDouble("rate");
            } else {
                countryCurrency = "EUR"; //IF CURRENCY NOT FOUND, SET EUR AS DEFAULT ONE
            }
        }

        double convertedPrice = indexedPrice * exchangeRate;

        OrderCollection.setPriceForOrder(orderId, convertedPrice);
        OrderCollection.setCurrencyForOrder(orderId, countryCurrency);

        sendMessageExecutor(
                ReplyKeyboardHelper.passengerOrderAddPriceKeyboard(passengerId,
                        LocalizationHelper.getValueByCode(ADD_PRICE_ORDER_MESSAGE, passengerLocale).formatted(convertedPrice, countryCurrency),
                        convertedPrice)
        );
    }


    private static void addPrice(Document order, Message message, Long passengerId, CountryCode passengerLocale) {
        String priceString = message.getText();
        double priceDouble;
        if (priceString.matches("^-?[0-9]+([.,][0-9]+)?$")) {
            priceDouble = PriceHelper.convertStringToDouble(priceString);
            if (priceDouble > 2147483647) {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(PRICE_ERROR_MORE_THAN_INT_MAX_MESSAGE, passengerLocale));
            } else if (priceDouble < 0) {
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(PRICE_ERROR_LESS_THAN_0_MESSAGE, passengerLocale));
            } else {
                OrderCollection.setPriceForOrder(order.get("orderId").toString(), priceDouble);
                OrderCollection.updateOrderStatus(order.get("orderId").toString(), PASSENGER_CONFIRMATION_WAITING);
                OrderCollection.setDepartureTime(order.get("orderId").toString(), LocalizationHelper.getValueByCode(NOW_VALUE, passengerLocale));

                sendMessageExecutor(
                        InlineKeyboardHelper.changeDepartureTimeKeyboard(passengerId,
                                LocalizationHelper.getValueByCode(CONFIRMATION_PASSENGER_ORDER_MESSAGE, passengerLocale)
                                        .formatted(
                                                CipherHelper.decrypt(order.get("pickUpAddress").toString()),
                                                CipherHelper.decrypt(order.get("destinationAddress").toString()),
                                                CipherHelper.decrypt(order.get("notes").toString()),
                                                LocalizationHelper.getValueByCode(NOW_VALUE, passengerLocale),
                                                priceDouble,
                                                order.get("currency")
                                        ))
                );

                sendMessageExecutor(
                        ReplyKeyboardHelper.passengerOrderConfirmationKeyboard(
                                passengerId,
                                LocalizationHelper.getValueByCode(CONFIRMATION_REMINDER_MESSAGE, passengerLocale)
                        ));

                String userTag = message.getFrom().getUserName(); //.getUserName() - IT IS CORRECT ;)
                boolean isPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(passengerId));

                if (userTag == null && isPhoneNumberHidden) {
                    if (!Objects.equals(UserCollection.getUserTag(passengerId), userTag)) {
                        UserCollection.updateUserTag(passengerId, userTag);
                    }
                    sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(MISSING_USER_TAG_WITHOUT_NUMBER_PASSENGER_WARN_MESSAGE, passengerLocale));
                } else {
                    if (!Objects.equals(UserCollection.getUserTag(passengerId), userTag)) {
                        UserCollection.updateUserTag(passengerId, userTag);
                    }
                }
            }
        } else {
            sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(PRICE_ERROR_OTHER_MESSAGE, passengerLocale));
        }
    }


    private static void addDepartureDetails(Document order, Message message, Long passengerId, CountryCode passengerLocale) {
        String orderId = order.get("orderId").toString();
        OrderCollection.updateOrderStatus(orderId, PASSENGER_CONFIRMATION_WAITING);
        OrderCollection.updateOrderType(orderId, POSTPONED);
        String departureTime = message.getText();
        OrderCollection.setDepartureTime(orderId, departureTime);
        double price = Double.parseDouble(order.get("price").toString());

        String allOrderDataMessage;
        if (price == 0.00) {
            allOrderDataMessage = LocalizationHelper.getValueByCode(CONFIRMATION_PASSENGER_ORDER_FREE_RIDE_MESSAGE, passengerLocale);
        } else {
            allOrderDataMessage = LocalizationHelper.getValueByCode(CONFIRMATION_PASSENGER_ORDER_MESSAGE, passengerLocale);
        }

        sendMessageExecutor(
                InlineKeyboardHelper.changeDepartureTimeKeyboard(passengerId, allOrderDataMessage
                        .formatted(
                                CipherHelper.decrypt(order.get("pickUpAddress").toString()),
                                CipherHelper.decrypt(order.get("destinationAddress").toString()),
                                CipherHelper.decrypt(order.get("notes").toString()),
                                departureTime,
                                price,
                                order.get("currency")
                        ))
        );

        sendMessageExecutor(
                ReplyKeyboardHelper.passengerOrderConfirmationKeyboard(
                        passengerId, LocalizationHelper.getValueByCode(CONFIRMATION_REMINDER_MESSAGE, passengerLocale)
                ));
    }


    private static void parseCustomMessageForUser(Message message, Long passengerId, CountryCode passengerLocale) {

        UserStatus userStatus = UserCollection.getUserStatus(passengerId);

        if (userStatus == EDIT_USER_NAME) {
            if (!message.getText().equals(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, passengerLocale))) {
                UserCollection.updateUserName(passengerId, message.getText());
                sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, passengerLocale));
            }
            loadSettingsMenu(passengerId, passengerLocale);
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
