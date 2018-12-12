package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class CsvOptions {
    String charset = "ISO-8859-1";
    String fieldsSeparator = ";";
    char centsSeparator = ',';
    int balanceLineIndex = 0;
    int balanceFieldIndex = 5;
    String dateFormat = "dd/MM/yy";

    int dateColumnIndex = 0;
    int typeColumnIndex = 1;
    int actionColumnIndex = 2;
    int originColumnIndex = 3;
    int amountColumnIndex = 4;
    int expectedColumns = 5;

    @NotNull
    Date parseDate(@NotNull String date) throws ParseException {
        return (new SimpleDateFormat(dateFormat)).parse(date.trim());
    }

    @NotNull
    int parseCents(@NotNull String s) {
        s = s.trim();
        int centsSepIndex = s.indexOf(centsSeparator);
        if (centsSepIndex >= 0 && centsSepIndex != s.length() - 3) {
            throw new IllegalArgumentException(String.format("Expected 2 digits after '%s' separator", centsSeparator));
        }
        s = s.replaceAll("[^\\-0-9]", "");
        int parsed = Integer.parseInt(s);
        return centsSepIndex >= 0 ? parsed : parsed * 100;
    }
}
