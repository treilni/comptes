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
    private String type;

    @Nullable
    private String action; // virement reçu, prélèvement etc.

    @NotNull
    private String origin;

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin, @Nullable String type) {
        this.date = date;
        this.amountCents = amountCents;
        this.origin = origin;
        this.type = type;
    }

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin) {
        this(date, amountCents, origin, null);
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
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

    @Nullable
    public String getAction() {
        return action;
    }

    public void setAction(@Nullable String action) {
        this.action = action;
    }
}
