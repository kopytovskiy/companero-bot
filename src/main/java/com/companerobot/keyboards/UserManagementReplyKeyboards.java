package com.companerobot.keyboards;

import com.companerobot.enums.CountryCode;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.companerobot.constants.HookMessages.*;

public class UserManagementReplyKeyboards {

    public static SendMessage openedSettingsKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton editNameButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(EDIT_USER_NAME_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton editPhoneNumberButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(EDIT_PHONE_NUMBER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton editSharePhoneNumberButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(EDIT_SHARE_PHONE_NUMBER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton exitSettingsButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(EXIT_SETTINGS_HOOK_MESSAGE, userLocale))
                .build();


        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(editNameButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(editPhoneNumberButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(editSharePhoneNumberButton);

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(exitSettingsButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .keyboardRow(keyboardRow4)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(message)
                .build();
    }

    public static SendMessage changeUsernameKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton backToSettingsMenuButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(backToSettingsMenuButton);

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

    public static SendMessage changePhoneNumberKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton sendContactButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(SHARE_MY_CONTACT_HOOK_MESSAGE, userLocale))
                .requestContact(true)
                .build();

        KeyboardButton backToSettingsMenuButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(sendContactButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(backToSettingsMenuButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
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

    public static SendMessage updateOrRemovePhoneNumberKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton sendContactButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(SHARE_MY_CONTACT_HOOK_MESSAGE, userLocale))
                .requestContact(true)
                .build();

        KeyboardButton removeContactButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(REMOVE_PHONE_NUMBER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton backToSettingsMenuButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(sendContactButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(removeContactButton);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(backToSettingsMenuButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow1)
                .keyboardRow(keyboardRow2)
                .keyboardRow(keyboardRow3)
                .resizeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(userId.toString())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("html")
                .text(message)
                .build();
    }

    public static SendMessage hiddenNumberKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton revealPhoneNumberButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(REVEAL_PHONE_NUMBER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton backToSettingsMenuButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(revealPhoneNumberButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(backToSettingsMenuButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
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

    public static SendMessage notHiddenNumberKeyboard(Long userId, String message) {
        CountryCode userLocale = UserCollection.getUserLocale(userId);

        KeyboardButton hidePhoneNumberButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(HIDE_PHONE_NUMBER_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardButton backToSettingsMenuButton = KeyboardButton.builder()
                .text(LocalizationHelper.getValueByCode(BACK_TO_PREVIOUS_PAGE_HOOK_MESSAGE, userLocale))
                .build();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(hidePhoneNumberButton);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(backToSettingsMenuButton);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
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
}
