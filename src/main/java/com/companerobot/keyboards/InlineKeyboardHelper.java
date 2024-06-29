package com.companerobot.keyboards;

import com.companerobot.enums.CountryCode;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static com.companerobot.constants.Callbacks.ACCEPT_ORDER_CALLBACK;
import static com.companerobot.constants.Callbacks.ADD_POSTPONED_DEPARTURE_DETAILS_CALLBACK;
import static com.companerobot.constants.InlineButtonNames.*;
import static com.companerobot.constants.URLs.GOOGLE_MAPS_BASE_URL;

public class InlineKeyboardHelper {

    public static InlineKeyboardMarkup acceptOrderMarkupKeyboard(Long driverId, double pickUpAddressLatitude, double pickUpPointLongitude) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);
        String googleMapURL = String.format("%s%s,%s", GOOGLE_MAPS_BASE_URL, pickUpAddressLatitude, pickUpPointLongitude);

        InlineKeyboardButton acceptButton = InlineKeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(ACCEPT_INLINE_TEXT, driverLocale))
                .callbackData(ACCEPT_ORDER_CALLBACK)
                .build();

        InlineKeyboardButton openPickupPoint = InlineKeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(OPEN_PICKUP_POINT_INLINE_TEXT, driverLocale))
                .url(googleMapURL)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(acceptButton))
                .keyboardRow(new InlineKeyboardRow(openPickupPoint)).build();
    }


    public static InlineKeyboardMarkup destinationPointMarkupKeyboard(Long driverId, String destinationAddressLatitude, String destinationAddressLongitude) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);
        String googleMapURL = String.format("%s%s,%s", GOOGLE_MAPS_BASE_URL, destinationAddressLatitude, destinationAddressLongitude);

        InlineKeyboardButton openDestinationPoint = InlineKeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(OPEN_DESTINATION_POINT_INLINE_TEXT, driverLocale))
                .url(googleMapURL)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(openDestinationPoint)).build();
    }


    public static SendMessage passengerContactsMarkupKeyboard(Long driverId, Long passengerId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        InlineKeyboardButton writeToUserButton = InlineKeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(WRITE_TO_PASSENGER_INLINE_TEXT, driverLocale))
                .url("https://t.me/" + UserCollection.getUserTag(passengerId))
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(writeToUserButton))
                .build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(driverId.toString())
                .replyMarkup(inlineKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage changeDepartureTimeKeyboard(Long passengerId, String message) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        InlineKeyboardButton addDepartureDateDetailsButton = InlineKeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(ADD_POSTPONED_DEPARTURE_DETAILS_INLINE_TEXT, passengerLocale))
                .callbackData(ADD_POSTPONED_DEPARTURE_DETAILS_CALLBACK)
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(addDepartureDateDetailsButton))
                .build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(passengerId.toString())
                .replyMarkup(inlineKeyboardMarkup)
                .text(message)
                .build();
    }




    public static SendMessage driverContactsMarkupKeyboard(Long driverId, Long passengerId, String message) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        InlineKeyboardButton writeToUserButton = InlineKeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(WRITE_TO_DRIVER_INLINE_TEXT, passengerLocale))
                .url("https://t.me/" + UserCollection.getUserTag(driverId))
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(writeToUserButton))
                .build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(passengerId.toString())
                .replyMarkup(inlineKeyboardMarkup)
                .text(message)
                .build();
    }
}
