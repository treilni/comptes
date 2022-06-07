package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.Expense;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

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
    int firstLineIndex = 0;

    PostProcessor postProcessing = null;

    private static final CsvOptions BNP_OPTIONS = new CsvOptions();

    private static final CsvOptions BICS_OPTIONS = new CsvOptions(csvOptions -> {
        csvOptions.balanceLineIndex = -1;
        csvOptions.balanceFieldIndex = -1;
        csvOptions.dateFormat = "yyyy-MM-dd";

        csvOptions.dateColumnIndex = 0;
        csvOptions.typeColumnIndex = 2;
        csvOptions.actionColumnIndex = -1;
        csvOptions.originColumnIndex = 4;
        csvOptions.amountColumnIndex = 5;
        csvOptions.firstLineIndex = 1;

        csvOptions.postProcessing = new BicsPostProcessor();
    });

    private CsvOptions() {
    }

    private CsvOptions(Consumer<CsvOptions> init) {
        init.accept(this);
    }

    @NotNull
    Date parseDate(@NotNull String date) throws ParseException {
        return (new SimpleDateFormat(dateFormat)).parse(date.trim());
    }

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

    public static CsvOptions guess(@NotNull File file) {
        return file.getName().matches(".*BICS.*") ? BICS_OPTIONS : BNP_OPTIONS;
    }

}
