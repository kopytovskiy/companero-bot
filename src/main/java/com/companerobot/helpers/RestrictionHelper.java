package com.companerobot.helpers;

import com.companerobot.misc.RestrictedAreasCollection;

import java.util.ArrayList;

public class RestrictionHelper {

    private static final ArrayList<String> restrictedCountries = RestrictedAreasCollection.getRestrictedCountriesList();
    private static final ArrayList<String> restrictedPhoneNumbersList = RestrictedAreasCollection.getRestrictedPhoneNumbersList();


    public static boolean isRestrictedLocation(double latitude, double longitude) {
        return restrictedCountries.contains(AddressHelper.getAddressByCoordinates(latitude, longitude, 1)
                .getJSONObject("address").getString("country_code").toUpperCase());
    }

    public static boolean isRestrictedPhoneNumber(String phoneNumber) {
        return restrictedPhoneNumbersList.stream()
                .anyMatch(phoneNumber::startsWith);
    }


}
