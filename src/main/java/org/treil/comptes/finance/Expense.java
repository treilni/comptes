package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class Expense {
    public enum CreationType {
        BANK, // Created from bank uploaded file
        MANUAL // created manually by user
    }

    @NotNull
    private final Date date;

    private final int amountCents;

    @Nullable
    private String type;

    @Nullable
    private String action; // virement reçu, prélèvement etc.

    @NotNull
    private String origin; // Bénéficiaire / Créditeur

    @NotNull
    private Repartition repartition = new Repartition();

    @NotNull
    private CreationType creationType;

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin, @Nullable String type, @NotNull CreationType creationType) {
        this.date = date;
        this.amountCents = amountCents;
        this.origin = origin;
        this.type = type;
        this.creationType = creationType;
    }

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin, CreationType creationType) {
        this(date, amountCents, origin, null, creationType);
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

    public int getCategoryRepartitionPct(@NotNull String category) {
        return repartition.getFractionPct(category);
    }
}
