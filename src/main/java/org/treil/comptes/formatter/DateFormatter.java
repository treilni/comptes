package org.treil.comptes.formatter;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Nicolas
 * @since 27/11/2018.
 */
public class DateFormatter extends SimpleDateFormat {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n", Locale.getDefault());

    public static String formatDay(@NotNull Date date) {
        SimpleDateFormat f = new SimpleDateFormat(resourceBundle.getString("dayFormat"));
        return f.format(date);
    }
}
