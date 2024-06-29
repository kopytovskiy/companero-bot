package com.companerobot.helpers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CyrillicStringHelper {

    public static String getCyrillicString(String text) {
        if (System.getProperty("os.name").toLowerCase().contains("windows") && Locale.getDefault().toString().contains("EN")) {
            try {
                return new String(text.getBytes("windows-1251"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return text;
        }
    }
    
}
