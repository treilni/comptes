package org.treil.comptes.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

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
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        return name.toUpperCase().replaceAll("[^A-Z0-9_]", "_");
    }

    public Category(@JsonProperty("code") String code,
                    @JsonProperty("name") String name,
                    @JsonProperty("budgetCents") int budgetCents,
                    @JsonProperty("hasBudget") boolean hasBudget) {
        this.code = code;
        this.name = name;
        this.budgetCents = budgetCents;
        this.hasBudget = hasBudget;
    }

    public Category(String name, int budgetCents, boolean hasBudget) {
        this(makeCode(name), name, budgetCents, hasBudget);
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

    @NotNull
    public static List<Category> makeSampleCategories() {
        List<Category> result = new ArrayList<>();
        result.add(new Category("Charges", 0, false));
        result.add(new Category("Santé", 0, false));
        result.add(new Category("Alimentation", 0, false));
        result.add(new Category("Vêtements", 0, false));
        result.add(new Category("Maison", 0, false));
        result.add(new Category("Vacances", 0, false));
        result.add(new Category("Loisirs", 0, false));
        return result;
    }
}
