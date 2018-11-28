package org.treil.comptes.time;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Nicolas
 * @since 27/11/2018.
 */
public class Month implements Comparable<Month> {
    private static final String format = "yyyy-MM";
    @NotNull
    private String id;

    public Month(@NotNull Date date) {
        id = (new SimpleDateFormat(format)).format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Month month = (Month) o;
        return Objects.equals(id, month.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int compareTo(@NotNull Month o) {
        return id.compareTo(o.id);
    }

    @NotNull
    public Date toDate() {
        try {
            return (new SimpleDateFormat(format)).parse(id);
        } catch (ParseException ignored) {
            // never happens
        }
        return new Date();
    }
}
