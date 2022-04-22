package org.treil.comptes.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Nicolas
 * @since 27/11/2018.
 */
public class CentsFormatter {
    private static final char decimalSeparator = ',';
    private static final char thousandSeparator = ' ';

    public static String format(int cents) {
        return format(cents, null);
    }

    public static String format(int cents, @Nullable String currency) {
        boolean isNegative = cents < 0;
        if (isNegative) {
            cents = -cents;
        }
        int intPart = cents / 100;
        int decimalPart = cents % 100;

        StringBuilder b = formatIntPart(intPart, isNegative);
        b.append(decimalSeparator);
        if (decimalPart < 10) {
            b.append('0');
        }
        b.append(decimalPart);
        if (currency != null) {
            b.append(' ').append(currency);
        }
        return b.toString();
    }

    @NotNull
    private static StringBuilder formatIntPart(int intPart, boolean isNegative) {
        StringBuilder builder = new StringBuilder();
        if (isNegative) {
            builder.append('-');
        }
        String s = Integer.toString(intPart);
        int length = s.length();
        int j = length;
        for (int i = 0; i < length; i++, j--) {
            if (i > 0 && j % 3 == 0) {
                builder.append(thousandSeparator);
            }
            builder.append(s.charAt(i));
        }
        return builder;
    }
}
