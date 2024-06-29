package com.companerobot.helpers;

import com.companerobot.constants.Locales;
import com.companerobot.enums.CountryCode;

import java.util.ResourceBundle;

import static com.companerobot.enums.CountryCode.*;

public class LocalizationHelper {

    private static final ResourceBundle EN_BUNDLE = ResourceBundle.getBundle("localization", Locales.US);
    private static final ResourceBundle UA_BUNDLE = ResourceBundle.getBundle("localization", Locales.UA);
    private static final ResourceBundle ES_BUNDLE = ResourceBundle.getBundle("localization", Locales.ES);
    private static final ResourceBundle PT_BUNDLE = ResourceBundle.getBundle("localization", Locales.PT);
    private static final ResourceBundle DE_BUNDLE = ResourceBundle.getBundle("localization", Locales.DE);
    private static final ResourceBundle FR_BUNDLE = ResourceBundle.getBundle("localization", Locales.FR);


    public static String getValueByCode(String localizationCode, CountryCode locale) {
        return getLocalizationFile(locale).getString(localizationCode);
    }

    private static ResourceBundle getLocalizationFile(CountryCode locale) {

        if (locale == UA) {
            return UA_BUNDLE;

        } else if (locale == ES) {
            return ES_BUNDLE;

        } else if (locale == PT) {
            return PT_BUNDLE;

        } else if (locale == DE) {
            return DE_BUNDLE;

        } else if (locale == FR) {
            return FR_BUNDLE;

        } else {
            return EN_BUNDLE;
        }
    }
}
