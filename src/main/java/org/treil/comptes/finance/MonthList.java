package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;
import org.treil.comptes.formatter.CentsFormatter;
import org.treil.comptes.time.Month;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class MonthList {
    private final Month month;
    private boolean complete = false;
    private int totalCents = 0;
    private int endBalanceCents = 0;
    private final List<Expense> expenses = new ArrayList<>();

    public MonthList(@NotNull Date date) {
        month = new Month(date);
    }

    public Month getMonth() {
        return month;
    }

    public void add(@NotNull Expense expense) {
        expenses.add(expense);
        totalCents += expense.getAmountCents();
    }

    @Override
    public String toString() {
        return "MonthList{" +
                "month=" + month +
                ", complete=" + complete +
                ", totalCents=" + CentsFormatter.format(totalCents, null) +
                ", expenses=" + expenses.size() +
                '}';
    }

    public void updateBalance(int startBalanceCents) {
        endBalanceCents = startBalanceCents + totalCents;
    }

    public int getEndBalanceCents() {
        return endBalanceCents;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }
}
