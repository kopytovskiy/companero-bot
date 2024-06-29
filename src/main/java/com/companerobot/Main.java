package com.companerobot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import static com.companerobot.constants.ExecutionConstants.BOT_TOKEN;

public class Main {

    public static CompaneroBot companeroBot = new CompaneroBot();
    public static void main(String[] args) {
        System.out.println("Bot is starting...");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(BOT_TOKEN, companeroBot);
            System.out.println("CompaneroBot successfully started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}