package com.companerobot.parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.UserStatus;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.helpers.RestrictionHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.keyboards.UserManagementReplyKeyboards;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.constants.TextValues.*;
import static com.companerobot.enums.UserStatus.*;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageToUser;

public class ContactParser {

    public static void parseContact(Message message) {
        Long currentUserId = message.getFrom().getId();
        CountryCode userLocale = UserCollection.getUserLocale(currentUserId);
        UserStatus userStatus = UserCollection.getUserStatus(currentUserId);
        String userPhoneNumber = message.getContact().getPhoneNumber();

        if (RestrictionHelper.isRestrictedPhoneNumber(userPhoneNumber)) {
            sendMessageToUser(currentUserId, LocalizationHelper.getValueByCode(RESTRICTED_PHONE_NUMBER_MESSAGE, userLocale));
            return;
        }

        if (userStatus == WAITING_USER_CONTACT) {
            addUserNumberAtRegistration(message, currentUserId, userLocale);

        } else if (userStatus == EDIT_PHONE_NUMBER) {
            addUserNumberAtEditing(message, currentUserId, userLocale);
        }
    }


    private static void addUserNumberAtRegistration(Message message, Long userId, CountryCode userLocale) {
        if (UserCollection.getUserName(userId) == null &&
                UserCollection.getUserPhoneNumber(userId) == null) {

            if (message.getContact().getUserId() == null || !message.getContact().getUserId().equals(message.getFrom().getId())) {
                sendMessageToUser(userId, LocalizationHelper.getValueByCode(INCORRECT_CONTACT_MESSAGE, userLocale));
            } else {
                String userName = message.getContact().getFirstName();
                String userPhoneNumber = message.getContact().getPhoneNumber();
                String userTag = message.getFrom().getUserName(); //.getUserName() - IT IS CORRECT ;)

                UserCollection.updateUserName(userId, userName);
                UserCollection.updateUserPhoneNumber(userId, userPhoneNumber);
                UserCollection.updateUserStatus(userId, WAITING_ROLE);

                sendMessageToUser(userId, LocalizationHelper.getValueByCode(MESSAGE_AFTER_REGISTRATION_WITH_PHONE_NUMBER, userLocale).formatted(userName));
                if (userTag == null) {
                    sendMessageToUser(userId, LocalizationHelper.getValueByCode(MESSAGE_MISSING_USER_TAG_GENERAL, userLocale));
                } else {
                    UserCollection.updateUserTag(userId, userTag);
                }
                sendMessageExecutor(
                        ReplyKeyboardHelper.chooseRoleKeyboard(userId)
                );
            }
        }
    }


    private static void addUserNumberAtEditing(Message message, Long userId, CountryCode userLocale) {
        if (message.getContact().getUserId() == null || !message.getContact().getUserId().equals(message.getFrom().getId())) {
            sendMessageToUser(userId, LocalizationHelper.getValueByCode(INCORRECT_CONTACT_MESSAGE, userLocale));
        } else {
            String userPhoneNumber = message.getContact().getPhoneNumber();
            UserCollection.updateUserPhoneNumber(userId, userPhoneNumber);
            sendMessageToUser(userId, LocalizationHelper.getValueByCode(SUCCESSFUL_DATA_SAVED_MESSAGE, userLocale));

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
}
