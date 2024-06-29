package com.companerobot.parsers;

import com.companerobot.keyboards.ReplyKeyboardHelper;
import com.companerobot.misc.UserCollection;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.companerobot.helpers.MessageExecutionHelper.sendMessageExecutor;

public class CommandParser {

    public static void parseCommand(Message message) {
        User currentUser = message.getFrom();
        Long currentUserId = currentUser.getId();

        if (message.getText().equals("/start") && !UserCollection.isUserExist(currentUserId)) {
            UserCollection.addUserToDB(currentUserId);
            sendMessageExecutor(
                    ReplyKeyboardHelper.chooseLanguageKeyboard(currentUserId)
            );
        }
    }
}
