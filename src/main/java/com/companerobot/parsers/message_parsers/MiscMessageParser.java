package com.companerobot.parsers.message_parsers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.UserStatus;
import com.companerobot.helpers.LocalizationHelper;
import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.ReviewCollection;
import com.companerobot.misc.UserCollection;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.constants.HookMessages.*;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.constants.URLs.*;
import static com.companerobot.enums.CountryCode.*;
import static com.companerobot.enums.UserRole.DRIVER;
import static com.companerobot.enums.UserRole.PASSENGER;
import static com.companerobot.enums.UserStatus.*;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;
import static com.companerobot.helpers.MessageExecutionHelper.sendMessageToUser;

public class MiscMessageParser {

    public static void parseMiscMessage(Message message) {
        Long userId = message.getFrom().getId();
        CountryCode userLocale = UserCollection.getUserLocale(userId);
        UserStatus userStatus = UserCollection.getUserStatus(userId);

        String messageText = message.getText();
        String localizedSkipShareMyContact = LocalizationHelper.getValueByCode(SKIP_SHARE_MY_CONTACT_HOOK_MESSAGE, userLocale);
        String localizedAuthorizeAsPassenger = LocalizationHelper.getValueByCode(AUTHORIZE_AS_PASSENGER_HOOK_MESSAGE, userLocale);
        String localizedAuthorizeAsDriver = LocalizationHelper.getValueByCode(AUTHORIZE_AS_DRIVER_HOOK_MESSAGE, userLocale);

        if (messageText.equals(CHOOSE_ENGLISH_LANGUAGE_HOOK_MESSAGE)) {
            chooseEnglishLanguage(userId, userStatus);

        } else if (messageText.equals(CHOOSE_SPANISH_LANGUAGE_HOOK_MESSAGE)) {
            chooseSpanishLanguage(userId, userStatus);

        } else if (messageText.equals(CHOOSE_UKRAINIAN_LANGUAGE_HOOK_MESSAGE)) {
            chooseUkrainianLanguage(userId, userStatus);

        } else if (messageText.equals(CHOOSE_PORTUGUESE_LANGUAGE_HOOK_MESSAGE)) {
            choosePortugueseLanguage(userId, userStatus);

        } else if (messageText.equals(CHOOSE_GERMAN_LANGUAGE_HOOK_MESSAGE)) {
            chooseGermanLanguage(userId, userStatus);

        } else if (messageText.equals(CHOOSE_FRENCH_LANGUAGE_HOOK_MESSAGE)) {
            chooseFrenchLanguage(userId, userStatus);

        } else if (messageText.equals(localizedSkipShareMyContact)) {
            skipShareMyContact(message, userId, userStatus, userLocale);

        } else if (messageText.equals(localizedAuthorizeAsPassenger)) {
            authorizeAsPassenger(userId, userStatus, userLocale);

        } else if (messageText.equals(localizedAuthorizeAsDriver)) {
            authorizeAsDriver(userId, userStatus, userLocale);
        }
    }


    private static void chooseEnglishLanguage(Long userId, UserStatus userStatus) {
        if (userStatus == WAITING_USER_LOCALIZATION) {
            UserCollection.updateUserLocalization(userId, US);
            UserCollection.updateUserStatus(userId, WAITING_USER_CONTACT);
            sendMessageToUser(userId, String.format(
                    LocalizationHelper.getValueByCode(USER_POLICY_AGREEMENT_MESSAGE, US),
                    USER_POLICY_AGREEMENT_ENG_URL,
                    PRIVACY_POLICY_ENG_URL));
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestContactWithButton(userId, LocalizationHelper.getValueByCode(REQUEST_CONTACTS_MESSAGE, US))
            );
        }
    }


    private static void chooseUkrainianLanguage(Long userId, UserStatus userStatus) {
        if (userStatus == WAITING_USER_LOCALIZATION) {
            UserCollection.updateUserLocalization(userId, UA);
            UserCollection.updateUserStatus(userId, WAITING_USER_CONTACT);
            sendMessageToUser(userId, String.format(
                    LocalizationHelper.getValueByCode(USER_POLICY_AGREEMENT_MESSAGE, UA),
                    USER_POLICY_AGREEMENT_UA_URL,
                    PRIVACY_POLICY_UA_URL));
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestContactWithButton(userId, LocalizationHelper.getValueByCode(REQUEST_CONTACTS_MESSAGE, UA))
            );
        }
    }


    private static void chooseSpanishLanguage(Long userId, UserStatus userStatus) {
        if (userStatus == WAITING_USER_LOCALIZATION) {
            UserCollection.updateUserLocalization(userId, ES);
            UserCollection.updateUserStatus(userId, WAITING_USER_CONTACT);
            sendMessageToUser(userId, String.format(
                    LocalizationHelper.getValueByCode(USER_POLICY_AGREEMENT_MESSAGE, ES),
                    USER_POLICY_AGREEMENT_ES_URL,
                    PRIVACY_POLICY_ES_URL));
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestContactWithButton(userId, LocalizationHelper.getValueByCode(REQUEST_CONTACTS_MESSAGE, ES))
            );
        }
    }


    private static void choosePortugueseLanguage(Long userId, UserStatus userStatus) {
        if (userStatus == WAITING_USER_LOCALIZATION) {
            UserCollection.updateUserLocalization(userId, PT);
            UserCollection.updateUserStatus(userId, WAITING_USER_CONTACT);
            sendMessageToUser(userId, String.format(
                    LocalizationHelper.getValueByCode(USER_POLICY_AGREEMENT_MESSAGE, PT),
                    USER_POLICY_AGREEMENT_PT_URL,
                    PRIVACY_POLICY_PT_URL));
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestContactWithButton(userId, LocalizationHelper.getValueByCode(REQUEST_CONTACTS_MESSAGE, PT))
            );
        }
    }


    private static void chooseGermanLanguage(Long userId, UserStatus userStatus) {
        if (userStatus == WAITING_USER_LOCALIZATION) {
            UserCollection.updateUserLocalization(userId, DE);
            UserCollection.updateUserStatus(userId, WAITING_USER_CONTACT);
            sendMessageToUser(userId, String.format(
                    LocalizationHelper.getValueByCode(USER_POLICY_AGREEMENT_MESSAGE, DE),
                    USER_POLICY_AGREEMENT_DE_URL,
                    PRIVACY_POLICY_DE_URL));
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestContactWithButton(userId, LocalizationHelper.getValueByCode(REQUEST_CONTACTS_MESSAGE, DE))
            );
        }
    }


    private static void chooseFrenchLanguage(Long userId, UserStatus userStatus) {
        if (userStatus == WAITING_USER_LOCALIZATION) {
            UserCollection.updateUserLocalization(userId, FR);
            UserCollection.updateUserStatus(userId, WAITING_USER_CONTACT);
            sendMessageToUser(userId, String.format(
                    LocalizationHelper.getValueByCode(USER_POLICY_AGREEMENT_MESSAGE, FR),
                    USER_POLICY_AGREEMENT_FR_URL,
                    PRIVACY_POLICY_FR_URL));
            sendMessageExecutor(
                    ReplyKeyboardHelper.requestContactWithButton(userId, LocalizationHelper.getValueByCode(REQUEST_CONTACTS_MESSAGE, FR))
            );
        }
    }


    private static void skipShareMyContact(Message message, Long userId, UserStatus userStatus, CountryCode driverLocale) {
        if (userStatus == WAITING_USER_CONTACT) {
            String userName = message.getFrom().getFirstName();
            String userTag = message.getFrom().getUserName(); //.getUserName() - IT IS CORRECT ;)
            UserCollection.updateUserName(userId, userName);
            UserCollection.updateUserStatus(userId, WAITING_ROLE);

            if (userTag == null) {
                sendMessageToUser(userId, LocalizationHelper.getValueByCode(MESSAGE_AFTER_REGISTRATION_WITHOUT_PHONE_NUMBER, driverLocale));
                sendMessageToUser(userId, LocalizationHelper.getValueByCode(MESSAGE_MISSING_USER_TAG_GENERAL, driverLocale));
                sendMessageExecutor(
                        ReplyKeyboardHelper.chooseRoleKeyboard(userId)
                );
            } else {
                UserCollection.updateUserTag(userId, userTag);
                sendMessageToUser(userId, LocalizationHelper.getValueByCode(MESSAGE_AFTER_REGISTRATION_WITHOUT_PHONE_NUMBER, driverLocale));
                sendMessageExecutor(
                        ReplyKeyboardHelper.chooseRoleKeyboard(userId)
                );
            }
        }
    }


    private static void authorizeAsPassenger(Long userId, UserStatus userStatus, CountryCode driverLocale) {
        if (userStatus == WAITING_ROLE) {
            UserCollection.updateUserRole(userId, PASSENGER);
            UserCollection.updateUserStatus(userId, ACTIVE);
            ReviewCollection.addNewUserToReviewDB(userId);

            sendMessageExecutor(ReplyKeyboardHelper.mainMenuPassengerKeyboard(userId,
                    LocalizationHelper.getValueByCode(PASSENGER_ROLE_WELCOME_MESSAGE, driverLocale)
                            .formatted(LocalizationHelper.getValueByCode(CREATE_NEW_ORDER_HOOK_MESSAGE, driverLocale))));
        }
    }


    private static void authorizeAsDriver(Long userId, UserStatus userStatus, CountryCode driverLocale) {
        if (userStatus == WAITING_ROLE) {
            UserCollection.updateUserRole(userId, DRIVER);
            UserCollection.updateUserStatus(userId, ACTIVE);
            DriverCollection.addDriverToDB(userId);
            ReviewCollection.addNewUserToReviewDB(userId);

            sendMessageExecutor(
                    ReplyKeyboardHelper.removeKeyboardForUser(userId, LocalizationHelper.getValueByCode(CAR_MODEL_REQUEST_MESSAGE, driverLocale))
            );
        }
    }

}
