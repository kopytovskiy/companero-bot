package com.companerobot.helpers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.companerobot.constants.URLs.REVOLUT_CURRENCY_CONVERTER_URL;

public class PriceCalculatingHelper {

    private static final double PRICE_PER_KILOMETER = 0.5;
    private static final double BASE_FARE = 2;
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final double DEFAULT_EXCHANGE_RATE = 1.00;

    public static double getFarePrice(double tripLength, double countryPriceIndex) {
        if (tripLength < 2.00) {
            return BASE_FARE * countryPriceIndex;
        } else {
            double extraLength = tripLength - 2;
            return (BASE_FARE + (extraLength * PRICE_PER_KILOMETER)) * countryPriceIndex;
        }
    }

    public static JSONObject getExchangeRateByCurrency(String currency) {
        HttpRequest request;
        String response;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(REVOLUT_CURRENCY_CONVERTER_URL + currency))
                    .GET()
                    .build();

            response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new JSONObject(response);
    }

    public static double convertPriceToLocalCurrency(double indexedPrice, String countryCurrency) {
        double exchangeRate = determineExchangeRate(countryCurrency);
        return indexedPrice * exchangeRate;
    }

    private static double determineExchangeRate(String countryCurrency) {
        if (countryCurrency.equals(DEFAULT_CURRENCY)) {
            return DEFAULT_EXCHANGE_RATE;
        }

        JSONObject currencyResponse = PriceCalculatingHelper.getExchangeRateByCurrency(countryCurrency);

        if (currencyResponse.has("rate")) {
            return currencyResponse.getDouble("rate");
        } else {
            return DEFAULT_EXCHANGE_RATE;
        }
    }

}
