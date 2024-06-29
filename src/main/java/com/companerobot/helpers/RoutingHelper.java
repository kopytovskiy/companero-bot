package com.companerobot.helpers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static com.companerobot.constants.URLs.OSRM_BASE_URL;

public class RoutingHelper {

    public static JSONObject getTripData(String startPoint, String finishPoint) {
        HttpRequest request;
        String response;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(OSRM_BASE_URL + "/v1/driving/" + startPoint + ";" + finishPoint))
                    .GET()
                    .build();

            response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new JSONObject(response).getJSONArray("routes").getJSONObject(0);
    }

    public static int getTripDurationInMinutes(double tripDurationInSec) {
        return (int) Math.ceil(tripDurationInSec / 60);
    }

    public static double getTripLengthInKilometers(double tripLength) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);
        return Double.parseDouble(decimalFormat.format(tripLength * 0.001));
    }
}


