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
import com.companerobot.misc.ReviewCollection;
import com.companerobot.misc.UserCollection;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.constants.HookMessages.*;
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
        DriverCollection.setIsWaitingForNewTrip(driverId, true);
        sendMessageExecutor(
                ReplyKeyboardHelper.mainMenuDriverHideOrdersKeyboard(driverId, LocalizationHelper.getValueByCode(STOP_SHIFT_MESSAGE, driverLocale)
                        .formatted(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale)))
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
        int passengerReviewsAmount = ReviewCollection.getUserReviewsAmount(passengerId);

        StringBuilder passengerInfoMessage = new StringBuilder(LocalizationHelper.getValueByCode(LOCATION_SENT_TO_PASSENGER_MESSAGE, driverLocale))
                .append(LocalizationHelper.getValueByCode(PASSENGER_INFO_BASE_MESSAGE, driverLocale).formatted(passengerName));

        if (!isPhoneNumberHidden) {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PHONE_NUMBER_MESSAGE, driverLocale).formatted(passengerPhoneNumber));
        }

        if (passengerReviewsAmount >= 10) {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(RATING_MESSAGE, driverLocale)
                    .formatted(ReviewCollection.getUserRating(passengerId)));
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PASSENGER_INFO_REVIEWS_AMOUNT_MESSAGE, driverLocale).formatted(passengerReviewsAmount));
        }

        if (isPhoneNumberHidden && passengerUserTag == null) {
            passengerInfoMessage.append(LocalizationHelper.getValueByCode(PASSENGER_INFO_MISSING_CONTACTS_MESSAGE, driverLocale));
        }

        passengerInfoMessage.append(LocalizationHelper.getValueByCode(FINANCIAL_OPERATIONS_WARN_MESSAGE, driverLocale));

        if (passengerUserTag == null) {
            sendMessageToUser(driverId, passengerInfoMessage.toString());
        } else {
            sendMessageExecutor(
                    InlineKeyboardHelper.passengerContactsMarkupKeyboard(driverId, passengerId, passengerInfoMessage.toString())
            );
        }

        sendMessageExecutor(ReplyKeyboardHelper.driverAcceptedOrderKeyboard(driverId, LocalizationHelper.getValueByCode(FUTURE_ARRIVAL_REMINDER_MESSAGE, driverLocale)
                .formatted(LocalizationHelper.getValueByCode(CONFIRM_ARRIVAL_HOOK_MESSAGE, driverLocale))));
    }

}
