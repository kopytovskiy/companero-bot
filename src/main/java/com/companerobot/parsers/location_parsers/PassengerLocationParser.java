package com.companerobot.parsers.location_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.OrderStatus;
import com.companerobot.helpers.AddressHelper;
import com.companerobot.helpers.CipherHelper;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.RoutingHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.OrderCollection;
import com.companerobot.misc.UserCollection;
import org.bson.Document;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.constants.TextMessages.REQUEST_DESTINATION_POINT_PASSENGER_MESSAGE;
import static com.companerobot.enums.OrderStatus.*;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageToUser;

public class PassengerLocationParser {

    public static void parsePassengerLocation(Message message) {
        Long passengerId = message.getFrom().getId();
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);
        Document order = OrderCollection.getUnfinishedOrderByPassengerId(passengerId);

        if (order == null) {
            return;
        }

        OrderStatus orderStatus = OrderStatus.valueOf(order.get("orderStatus").toString());

        if (orderStatus == WAITING_PICKUP_ADDRESS) {
            setPickupPoint(message, passengerId, passengerLocale);

        } else if (orderStatus == WAITING_DESTINATION_ADDRESS) {
            setDestinationPoint(message, passengerId);
        }

    }


    private static void setPickupPoint(Message message, Long passengerId, CountryCode passengerLocale) {
        JSONObject pickupAddressResponse = AddressHelper.getAddressByCoordinates(message.getLocation().getLatitude(), message.getLocation().getLongitude(), 18);
        String orderId = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_PICKUP_ADDRESS).get("orderId").toString();

        OrderCollection.setPickUpAddressLatitude(orderId, message.getLocation().getLatitude());
        OrderCollection.setPickUpAddressLongitude(orderId, message.getLocation().getLongitude());
        OrderCollection.setPickUpAddress(orderId, pickupAddressResponse.getString("display_name"));
        OrderCollection.setCountryCode(orderId, CountryCode.valueOf(pickupAddressResponse.getJSONObject("address").getString("country_code").toUpperCase()));

        OrderCollection.updateOrderStatus(orderId, WAITING_DESTINATION_ADDRESS);
        sendMessageToUser(passengerId, LocalizationHelper.getValueByCode(REQUEST_DESTINATION_POINT_PASSENGER_MESSAGE, passengerLocale));
    }


    private static void setDestinationPoint(Message message, Long passengerId) {
        JSONObject destinationAddressResponse = AddressHelper.getAddressByCoordinates(message.getLocation().getLatitude(), message.getLocation().getLongitude(), 18);
        Document order = OrderCollection.getOrderByPassengerIdAndStatus(passengerId, WAITING_DESTINATION_ADDRESS);
        String orderId = order.get("orderId").toString();

        String destinationLocationCoordinates = String.format("%s,%s", message.getLocation().getLongitude(), message.getLocation().getLatitude());
        OrderCollection.setDestinationAddressLongitude(orderId, message.getLocation().getLongitude());
        OrderCollection.setDestinationAddressLatitude(orderId, message.getLocation().getLatitude());
        OrderCollection.setDestinationAddress(orderId, destinationAddressResponse.getString("display_name"));

        OrderCollection.setApproximateDestinationAddress(orderId,
                AddressHelper.getAddressByCoordinates(
                        message.getLocation().getLatitude(),
                        message.getLocation().getLongitude(), 12).getString("display_name"));

        String pickUpAddressLatitude = CipherHelper.decrypt(order.get("pickUpAddressLatitude").toString());
        String pickUpAddressLongitude = CipherHelper.decrypt(order.get("pickUpAddressLongitude").toString());
        String pickupLocationCoordinates = String.format("%s,%s", pickUpAddressLongitude, pickUpAddressLatitude);

        JSONObject tripData = RoutingHelper.getTripData(pickupLocationCoordinates, destinationLocationCoordinates);
        double tripLength = RoutingHelper.getTripLengthInKilometers(tripData.getDouble("distance"));
        OrderCollection.setTripLength(orderId, tripLength);
        OrderCollection.updateOrderStatus(orderId, WAITING_IF_NOTES_NEEDED);

        sendMessageExecutor(
                ReplyKeyboardHelper.passengerNotesRequestKeyboard(passengerId)
        );
    }
}
