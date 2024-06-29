package com.companerobot;

import com.companerobot.parsers.*;

import com.companerobot.parsers.MessageParser;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class CompaneroBot implements LongPollingSingleThreadUpdateConsumer {

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message currentMessage = update.getMessage();

            if (currentMessage.isCommand()) {
                CommandParser.parseCommand(currentMessage);

            } else if (currentMessage.hasLocation()) {
                LocationParser.parseLocation(currentMessage);

            } else if (currentMessage.hasContact()) {
                ContactParser.parseContact(currentMessage);

            } else {
                MessageParser.parseMessage(currentMessage);

            }
        } else if (update.hasCallbackQuery()) {
            QueryParser.parseQuery(update);
        }
    }
}