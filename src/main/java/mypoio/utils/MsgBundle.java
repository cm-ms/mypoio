package mypoio.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class MsgBundle {
    private static ResourceBundle BUNDLE =
            ResourceBundle.getBundle("messages");

    private MsgBundle() {}

    public static void init(Locale locale) {
        BUNDLE = ResourceBundle.getBundle("messages", locale);
    }


    public static String get(String key, Object... args) {
        return String.format(BUNDLE.getString(key), args);
    }
}
