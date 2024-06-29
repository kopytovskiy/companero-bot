package com.companerobot.helpers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.companerobot.constants.URLs.OPEN_STREET_MAP_BASE_URL;

public class AddressHelper {

    public static JSONObject getAddressByCoordinates(double latitude, double longitude, int zoom) {

        HttpRequest request;
        String response;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(OPEN_STREET_MAP_BASE_URL + "reverse?format=jsonv2&lat=" + latitude + "&lon=" + longitude + "&zoom=" + zoom))
                    .GET()
                    .build();

            response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        //https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=47.56860&lon=-53.55615&zoom=18

        return new JSONObject(response);
    }
}
