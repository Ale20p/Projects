package org.example.motionsim.Controller;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {
    private static Locale currentLocale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle("motionsim.messages", currentLocale);

    public static void setLanguage(String language) {
        currentLocale = language.equalsIgnoreCase("Français") ? Locale.FRENCH : Locale.ENGLISH;
        bundle = ResourceBundle.getBundle("motionsim.messages", currentLocale);
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}