package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;

/**
 * @author Nicolas
 * @since 11/12/2018.
 */
public class Category {
    private String code;
    private String name;
    private int budgetCents;
    private boolean hasBudget;

    public Category(@NotNull String name) {
        this(name, makeCode(name), 0, false);
    }

    @NotNull
    private static String makeCode(@NotNull String name) {
        name = name.toUpperCase().replaceAll("[^A-Z0-9_]", "_");
        return Normalizer.normalize(name, Normalizer.Form.NFD);
    }

    public Category(String code, String name, int budgetCents, boolean hasBudget) {
        this.code = code;
        this.name = name;
        this.budgetCents = budgetCents;
        this.hasBudget = hasBudget;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getBudgetCents() {
        return budgetCents;
    }

    public boolean isHasBudget() {
        return hasBudget;
    }
}
