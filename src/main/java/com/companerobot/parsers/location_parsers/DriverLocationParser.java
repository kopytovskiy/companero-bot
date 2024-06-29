package com.companerobot.parsers.location_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.DriverInfoFillingStatus;
import com.companerobot.enums.OrderStatus;
import com.companerobot.helpers.CipherHelper;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.RoutingHelper;
import com.companerobot.keyboards.InlineKeyboardHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.UserCollection;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.constants.HookMessages.CONFIRM_ARRIVAL_HOOK_MESSAGE;
import static com.companerobot.constants.HookMessages.START_WORK_SHIFT_HOOK_MESSAGE;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.enums.DriverInfoFillingStatus.*;
import static com.companerobot.enums.OrderStatus.DRIVER_ACCEPTED;
import static com.companerobot.enums.OrderStatus.WAITING_DRIVER_LOCATION;
import static com.companerobot.helpers.MessageExecutionHelper.*;

public class DriverLocationParser {

    public static void parseDriverLocation(Message message) {
        Long driverId = message.getFrom().getId();
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);
        Document order = OrderCollection.getUnfinishedOrderByDriverId(driverId);

        if (order != null) {
            parseDriverLocationInOrder(message, order, driverId, driverLocale);
        } else {
            parseDriverLocation(message, driverId, driverLocale);
        }
    }


    private static void parseDriverLocationInOrder(Message message, Document order, Long driverId, CountryCode driverLocale) {
        OrderStatus orderStatus = OrderStatus.valueOf(order.get("orderStatus").toString());

        if (orderStatus == WAITING_DRIVER_LOCATION) {
            sendLocationToPassenger(message, order, driverId, driverLocale);
        }
    }


    private static void parseDriverLocation(Message message, Long driverId, CountryCode driverLocale) {
        Document driverInfo = DriverCollection.getDriverInfoByDriverId(driverId);
        DriverInfoFillingStatus driverStatus = DriverInfoFillingStatus.valueOf(driverInfo.get("driverInfoFillingStatus").toString());

        if (driverStatus == WAITING_LOCATION) {
            setDriverLocationAtRegistration(message, driverId, driverLocale);

        } else if (driverStatus == INFO_IS_FILLED) {
            updateDriverLocation(message, driverId, driverLocale);

        }
    }

    private static void setDriverLocationAtRegistration(Message message, Long driverId, CountryCode driverLocale) {
        Point point = new Point(new Position(message.getLocation().getLongitude(), message.getLocation().getLatitude()));
        DriverCollection.setLocation(driverId, point);
        DriverCollection.setDriverInfoFillingStatus(driverId, INFO_IS_FILLED);

        sendMessageToUser(driverId, LocalizationHelper.getValueByCode(CAR_INFO_REGISTRATION_FINISHED_MESSAGE, driverLocale));
        sendMessageExecutor(
                ReplyKeyboardHelper.mainMenuDriverGetOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(START_SHIFT_MESSAGE, driverLocale)
                        .formatted(LocalizationHelper.getValueByCode(START_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
        );
    }


    private static void updateDriverLocation(Message message, Long driverId, CountryCode driverLocale) {
        Point point = new Point(new Position(message.getLocation().getLongitude(), message.getLocation().getLatitude()));
        DriverCollection.setLocation(driverId, point);

        sendMessageToUser(driverId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, driverLocale));
    }


    private static void sendLocationToPassenger(Message message, Document order, Long driverId, CountryCode driverLocale) {
        String currentDriverLocation = String.format("%s,%s", message.getLocation().getLongitude(), message.getLocation().getLatitude());


        String destinationAddressLatitude = CipherHelper.decrypt(order.get("destinationAddressLatitude").toString());
        String destinationAddressLongitude = CipherHelper.decrypt(order.get("destinationAddressLongitude").toString());
        String passengerPickupPoint = String.format("%s,%s", destinationAddressLongitude, destinationAddressLatitude);

        JSONObject tripData = RoutingHelper.getTripData(currentDriverLocation, passengerPickupPoint);
        double tripLengthToPassenger = RoutingHelper.getTripLengthInKilometers(tripData.getDouble("distance"));
        int estimationTime = RoutingHelper.getTripDurationInMinutes(tripData.getDouble("duration"));

        Long passengerId = Long.valueOf(order.get("userId").toString());
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);
        sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(DRIVER_LOCATION_MESSAGE, passengerLocale).formatted(tripLengthToPassenger, estimationTime));
        sendLocationToUser(passengerId, currentDriverLocation);

        OrderCollection.updateOrderStatus(order.get("orderId").toString(), DRIVER_ACCEPTED);

        String passengerName = UserCollection.getUserName(passengerId);
        String passengerPhoneNumber = UserCollection.getUserPhoneNumber(passengerId);

        String passengerUserTag = UserCollection.getUserTag(passengerId);
        boolean isPhoneNumberHidden = Boolean.parseBoolean(UserCollection.getIsPhoneNumberHidden(passengerId));

        if (passengerUserTag == null) { //TODO: Refactor to StringBuilder
            if (isPhoneNumberHidden) {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITHOUT_TAG_AND_NUMBER_MESSAGE, driverLocale).formatted(passengerName));
            } else {
                sendMessageToUser(driverId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITH_NUMBER_MESSAGE, driverLocale).formatted(passengerName, passengerPhoneNumber));
            }
        } else {
            if (isPhoneNumberHidden) {
                sendMessageExecutor(
                        InlineKeyboardHelper.passengerContactsMarkupKeyboard(driverId, passengerId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITHOUT_NUMBER_MESSAGE, driverLocale).formatted(passengerName))
                );

            } else {
                sendMessageExecutor(
                        InlineKeyboardHelper.passengerContactsMarkupKeyboard(driverId, passengerId, LocalizationHelper.getValueByCode(PASSENGER_INFO_WITH_NUMBER_MESSAGE, driverLocale).formatted(passengerName, passengerPhoneNumber))
                );
            }
        }

        sendMessageExecutor(ReplyKeyboardHelper.driverAcceptedOrderKeyboard(driverId, LocalizationHelper.getValueByCode(FUTURE_ARRIVAL_REMINDER_MESSAGE, driverLocale)
                .formatted(LocalizationHelper.getValueByCode(CONFIRM_ARRIVAL_HOOK_MESSAGE, driverLocale))));
    }

}