package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class Account {
    private String number;
    private int originalBalanceCents;

    @NotNull
    private final List<MonthList> history = new ArrayList<MonthList>();

    public Account(int originalBalanceCents, @NotNull List<Expense> expenses) {
        this.originalBalanceCents = originalBalanceCents;

    }
}
