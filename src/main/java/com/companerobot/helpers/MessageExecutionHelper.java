package com.companerobot.helpers;

import com.companerobot.enums.CountryCode;
import com.companerobot.enums.OrderType;
import com.companerobot.keyboards.InlineKeyboardHelper;
import com.companerobot.misc.DriverCollection;
import com.companerobot.misc.UserCollection;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;

import static com.companerobot.constants.ExecutionConstants.BOT_TOKEN;
import static com.companerobot.constants.TextMessages.*;
import static com.companerobot.constants.TextValues.NOW_VALUE;
import static com.companerobot.enums.OrderType.IMMEDIATE;
import static java.lang.Math.toIntExact;

public class MessageExecutionHelper {

    private static final TelegramClient telegramClient = new OkHttpTelegramClient(BOT_TOKEN);

    public static void sendMessageToUser(Long userId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(userId.toString())
                .parseMode("html")
                .text(message).build();

        sendMessageExecutor(sendMessage);
    }

    public static void sendLocationToUser(Long userId, String coordinates) {
        String[] parts = coordinates.split(",");
        double longitude = Double.parseDouble(parts[0]);
        double latitude = Double.parseDouble(parts[1]);

        SendLocation sendLocation = SendLocation.builder().chatId(userId)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        try {
            telegramClient.executeAsync(sendLocation);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendOrderToDrivers(Document order) {
        String orderId = order.get("orderId").toString();
        String pickUpAddress = CipherHelper.decrypt(order.get("pickUpAddress").toString());
        double pickUpAddressLatitude = Double.parseDouble(CipherHelper.decrypt(order.get("pickUpAddressLatitude").toString()));
        double pickUpPointLongitude = Double.parseDouble(CipherHelper.decrypt(order.get("pickUpAddressLongitude").toString()));
        String approximateDestinationAddress = CipherHelper.decrypt(order.get("approximateDestinationAddress").toString());
        String tripLength = order.get("tripLengthInKilometers").toString();
        String notes = CipherHelper.decrypt(order.get("notes").toString());
        OrderType orderType = OrderType.valueOf(order.get("orderType").toString());
        double price = Double.parseDouble(order.get("price").toString());
        String currency = order.get("currency").toString();

        double radiusInKms;
        if (orderType == IMMEDIATE) {
            radiusInKms = 25;
        } else {
            radiusInKms = 125;
        }

        Point pickUpPoint = new Point(new Position(pickUpPointLongitude, pickUpAddressLatitude));
        ArrayList<Long> driverUserIds = DriverCollection.getNearByDriversIdOnDutyList(pickUpPoint, radiusInKms);
        for (Long driverId : driverUserIds) {
            CountryCode driverLocale = UserCollection.getUserLocale(driverId);

            StringBuilder newOrderMessage = new StringBuilder(LocalizationHelper.getValueByCode(NEW_ORDER_BASE_MESSAGE, driverLocale)
                    .formatted(orderId, pickUpAddress, approximateDestinationAddress, tripLength, notes));

            if (orderType == IMMEDIATE) {
                newOrderMessage.append(LocalizationHelper.getValueByCode(DEPARTURE_TIME_ORDER_MESSAGE, driverLocale)
                        .formatted(LocalizationHelper.getValueByCode(NOW_VALUE, driverLocale)));
            } else {
                newOrderMessage.append(LocalizationHelper.getValueByCode(DEPARTURE_TIME_ORDER_MESSAGE, driverLocale)
                        .formatted(order.get("postponedDepartureTime").toString()));
            }

            if (price == 0.00) {
                newOrderMessage.append(LocalizationHelper.getValueByCode(ORDER_FREE_RIDE_MESSAGE, driverLocale));
            } else {
                newOrderMessage.append(LocalizationHelper.getValueByCode(ORDER_PRICE_MESSAGE, driverLocale).formatted(price, currency));
            }


            SendMessage sendMessage = SendMessage.builder()
                    .chatId(driverId.toString())
                    .replyMarkup(InlineKeyboardHelper.acceptOrderMarkupKeyboard(driverId, pickUpAddressLatitude, pickUpPointLongitude))
                    .parseMode("html")
                    .text(newOrderMessage.toString())
                    .build();
            sendMessageExecutor(sendMessage);
        }
    }


    //TODO: ADD BROADCAST MESSAGING FOR SUPER_ADMIN ROLE


    public static void sendMessageExecutor(SendMessage sendMessage) {
        try {
            telegramClient.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editMessage(MaybeInaccessibleMessage message, String text) {
        long messageId = message.getMessageId();
        long chatId = message.getChatId();

        EditMessageText updatedMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(toIntExact(messageId))
                .text(text)
                .build();

        try {
            telegramClient.executeAsync(updatedMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
