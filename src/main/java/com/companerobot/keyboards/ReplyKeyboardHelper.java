package com.companerobot.keyboards;

import com.companerobot.enums.CountryCode;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.companerobot.constants.HookMessages.*;
import static com.companerobot.constants.TextMessages.*;

public class ReplyKeyboardHelper {

    public static SendMessage requestLocationWithButton(Long driverId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        KeyboardButton sendLocationButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(SHARE_LOCATION_HOOK_MESSAGE, driverLocale))
                .requestLocation(true)
                .build();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(sendLocationButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .keyboardRow(keyboardRow)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(driverId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(message)
                .build();
    }

    public static SendMessage requestContactWithButton(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton sendContactButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(SHARE_MY_CONTACT_HOOK_MESSAGE, userLocale))
                .requestContact(true)
                .build();

        KeyboardButton skipStepButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(SKIP_SHARE_MY_CONTACT_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(sendContactButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(skipStepButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(message)
                .build();
    }

    public static SendMessage removeKeyboardForUser(Long userId, String message) {

        ReplyKeyboardRemove replyKeyboardRemove = ReplyKeyboardRemove.builder().removeKeyboard(true).build();
        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardRemove)
                .text(message)
                .build();
    }


    public static SendMessage driverAcceptedOrderKeyboard(Long driverId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        KeyboardButton arrivedButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CONFIRM_ARRIVAL_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton cancelOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(arrivedButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(cancelOrderButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .chatId(driverId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage driverOnPickUpPointKeyboard(Long driverId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        KeyboardButton finishOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(FINISH_ORDER_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton cancelOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(finishOrderButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(cancelOrderButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .chatId(driverId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage mainMenuDriverGetOrdersKeyboard(Long driverId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        KeyboardButton getNewOrdersButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(START_WORK_SHIFT_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton updateLocationButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(UPDATE_LOCATION_HOOK_MESSAGE, driverLocale))
                .requestLocation(true)
                .build();

        KeyboardButton openSettings = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(OPEN_SETTINGS_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton getSupportButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(GET_SUPPORT_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(getNewOrdersButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(updateLocationButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(openSettings);
        keyboardRow3.add(getSupportButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(driverId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }


    public static SendMessage mainMenuDriverHideOrdersKeyboard(Long driverId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        KeyboardButton hideOrdersButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(STOP_WORK_SHIFT_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton updateLocationButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(UPDATE_LOCATION_HOOK_MESSAGE, driverLocale))
                .requestLocation(true)
                .build();

        KeyboardButton openSettings = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(OPEN_SETTINGS_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton getSupportButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(GET_SUPPORT_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(hideOrdersButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(updateLocationButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(openSettings);
        keyboardRow3.add(getSupportButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(driverId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage mainMenuPassengerKeyboard(Long passengerId, String message) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        KeyboardButton createNewOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton openSettings = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(OPEN_SETTINGS_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton getSupportButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(GET_SUPPORT_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(createNewOrderButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(openSettings);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(getSupportButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(passengerId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage cancelOrderButtonKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton arrivedButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(arrivedButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(message)
                .build();
    }

    public static SendMessage passengerNotesRequestKeyboard(Long passengerId) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        KeyboardButton addNotesButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(ADD_NOTES_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton skipNotesButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(SKIP_NOTES_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton cancelOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(addNotesButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(skipNotesButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(cancelOrderButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .chatId(passengerId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(LocalizationHelper.getValueByCode(ASK_IF_NOTES_NEEDED_MESSAGE, passengerLocale))
                .build();
    }

    public static SendMessage passengerOrderAddPriceKeyboard(Long passengerId, String message, double farePrice) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        KeyboardButton finishOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(RECOMMENDED_PRICE_HOOK_MESSAGE, passengerLocale).formatted(farePrice))
                .build();

        KeyboardButton finishOrderFreeButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(FREE_RIDE_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton cancelOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(finishOrderButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(finishOrderFreeButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(cancelOrderButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(passengerId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage backToPreviousPageKeyboard(Long passengerId, String message) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        KeyboardButton backToPreviousPageButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(backToPreviousPageButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(passengerId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(message)
                .build();
    }

    public static SendMessage passengerOrderConfirmationKeyboard(Long passengerId, String message) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        KeyboardButton finishOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CONFIRM_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton cancelOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(finishOrderButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(cancelOrderButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(passengerId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage chooseLanguageKeyboard(Long userId) {
        KeyboardButton chooseEnglishLanguageButton = KeyboardButton.builder()
                .text(CHOOSE_ENGLISH_LANGUAGE_HOOK_MESSAGE)
                .build();

        KeyboardButton chooseSpanishLanguageButton = KeyboardButton.builder()
                .text(CHOOSE_SPANISH_LANGUAGE_HOOK_MESSAGE)
                .build();

        KeyboardButton chooseUkrainianLanguageButton = KeyboardButton.builder()
                .text(CHOOSE_UKRAINIAN_LANGUAGE_HOOK_MESSAGE)
                .build();

        KeyboardButton choosePortugeseLanguageButton = KeyboardButton.builder()
                .text(CHOOSE_PORTUGUESE_LANGUAGE_HOOK_MESSAGE)
                .build();

        KeyboardButton chooseGermanLanguageButton = KeyboardButton.builder()
                .text(CHOOSE_GERMAN_LANGUAGE_HOOK_MESSAGE)
                .build();

        KeyboardButton chooseFrenchLanguageButton = KeyboardButton.builder()
                .text(CHOOSE_FRENCH_LANGUAGE_HOOK_MESSAGE)
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(chooseEnglishLanguageButton);
        keyboardRow1.add(chooseSpanishLanguageButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(chooseUkrainianLanguageButton);
        keyboardRow2.add(choosePortugeseLanguageButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(chooseGermanLanguageButton);
        keyboardRow3.add(chooseFrenchLanguageButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(WELCOME_MESSAGE)
                .build();
    }


    public static SendMessage chooseRoleKeyboard(Long userId) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton chooseEnglishLanguageButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(AUTHORIZE_AS_PASSENGER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton chooseUkrainianLanguageButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(AUTHORIZE_AS_DRIVER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(chooseEnglishLanguageButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(chooseUkrainianLanguageButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(LocalizationHelper.getValueByCode(CHOOSE_ROLE_MESSAGE, userLocale))
                .build();
    }

    public static SendMessage postponedOrderPassengerKeyboard(Long passengerId, String message) {
        CountryCode passengerLocale = UserCollection.getUserLocale(passengerId);

        KeyboardButton confirmButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton declineButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardButton cancelOrderButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CANCEL_ORDER_HOOK_MESSAGE, passengerLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(confirmButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(declineButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(cancelOrderButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(passengerId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }

    public static SendMessage postponedOrderDriverKeyboard(Long driverId, String message) {
        CountryCode driverLocale = UserCollection.getUserLocale(driverId);

        KeyboardButton confirmButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(CONFIRM_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardButton declineButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(DECLINE_POSTPONED_ORDER_HOOK_MESSAGE, driverLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(confirmButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(declineButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .resizeKeyboard(true).build();

        return SendMessage.builder()
                .parseMode("html")
                .chatId(driverId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .text(message)
                .build();
    }
}
