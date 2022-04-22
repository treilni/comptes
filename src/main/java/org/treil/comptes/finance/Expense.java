package org.treil.comptes.finance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final Date date;

    private final int amountCents;

    @Nullable
    private String type;

    @NotNull
    private Action action = Action.OTHER; // virement reçu, prélèvement etc.

    @NotNull
    private String origin; // Bénéficiaire / Créditeur

    @NotNull
    private final Repartition repartition;

    @NotNull
    private final CreationType creationType;

    @JsonCreator
    public Expense(@NotNull @JsonProperty("date") Date date,
                   @JsonProperty("amountCents") int amountCents,
                   @NotNull @JsonProperty("origin") String origin,
                   @Nullable @JsonProperty("type") String type,
                   @NotNull @JsonProperty("creationType") CreationType creationType,
                   @NotNull @JsonProperty("repartition") Repartition repartition) {
        this.date = date;
        this.amountCents = amountCents;
        this.origin = origin;
        this.type = type;
        this.creationType = creationType;
        this.repartition = repartition;
    }

    public Expense(@NotNull Date date, int amountCents, @NotNull String origin, CreationType creationType) {
        this(date, amountCents, origin, null, creationType, new Repartition());
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

    @NotNull
    public Action getAction() {
        return action;
    }

    public void setAction(@NotNull Action action) {
        this.action = action;
    }

    public int getCategoryRepartitionPct(@NotNull String category) {
        return repartition.getFractionPct(category);
    }

    public void setOrigin(@NotNull String s) {
        this.origin = s;
    }
}
