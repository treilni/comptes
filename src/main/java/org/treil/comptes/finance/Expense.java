package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class Expense {
    @NotNull
    private final Date date;

    private final int amountCents;

    @Nullable
    private String category;

    @NotNull
    private String origin;

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin, @Nullable String category) {
        this.date = date;
        this.amountCents = amountCents;
        this.origin = origin;
        this.category = category;
    }

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin) {
        this(date, amountCents, origin, null);
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    @NotNull
    public Date getDate() {
        return date;
    }

    public int getAmountCents() {
        return amountCents;
    }

    @NotNull
    public String getOrigin() {
        return origin;
    }
}
