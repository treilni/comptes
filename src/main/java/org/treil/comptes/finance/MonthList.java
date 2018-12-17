package org.treil.comptes.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotNull
    private Month month;
    private boolean complete = false;
    @JsonIgnore
    private int totalCents = 0;
    private int endBalanceCents = 0;
    private final List<Expense> expenses = new ArrayList<>();

    @Deprecated
    public MonthList() {
        this(new Date());
    }

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

    public int getStartBalanceCents() {
        return getEndBalanceCents() - totalCents;
    }

    @Deprecated
    public void setMonth(@NotNull Month month) {
        this.month = month;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Deprecated
    public void setEndBalanceCents(int endBalanceCents) {
        this.endBalanceCents = endBalanceCents;
    }

    public void setExpenses(@NotNull List<Expense> expenses) {
        this.expenses.clear();
        totalCents = 0;
        expenses.forEach(this::add);
    }
}
