package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class Expense implements Serializable {
    public enum CreationType {
        BANK, // Created from bank uploaded file
        MANUAL // created manually by user
    }

    public enum Action {
        CARD_DEBIT("card.debit"),
        CARD_CREDIT("card.credit"),
        WITHDRAWAL("withdrawal"),
        DEBITED_TRANSFER("debited.transfer"),
        CREDITED_TRANSFER("credited.transfer"),
        DEBITED_CHECK("debited.check"),
        CREDITED_CHECK("credited.check"),
        AUTHORIZED_DEBIT("authorized.debit"),
        BANK_EXPENSE("bank.expense"),
        OTHER("other.action");

        private final String i18nCode;

        Action(String i18nCode) {
            this.i18nCode = i18nCode;
        }

        public String getI18nCode() {
            return i18nCode;
        }
    }

    @NotNull
    private Date date;

    private int amountCents;

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

    @Deprecated
    // FOr serialization only
    public Expense() {
        date = new Date();
        amountCents = 0;
    }

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

    @Deprecated
    public void setRepartition(@NotNull Repartition repartition) {
        this.repartition = repartition;
    }

    @Deprecated
    public void setCreationType(@NotNull CreationType creationType) {
        this.creationType = creationType;
    }
}
