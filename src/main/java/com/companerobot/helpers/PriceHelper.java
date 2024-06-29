package com.companerobot.helpers;

public class PriceHelper {

    public static double convertStringToDouble(String string) {
        string = string.replaceAll(",", ".").trim();
        string = string.replaceAll(" ", "");
        return Double.parseDouble(string);
    }

}
