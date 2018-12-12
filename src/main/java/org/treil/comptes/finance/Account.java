package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;
import org.treil.comptes.time.Month;

import java.util.*;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class Account {
    private String number;
    private int originalBalanceCents;
    private final List<Category> categories = new ArrayList<>();

    @NotNull
    private final List<MonthList> history = new ArrayList<>();

    public Account(int originalBalanceCents, @NotNull List<Expense> expenses) {
        this.originalBalanceCents = originalBalanceCents;
        addExpenses(expenses);
    }

    private void addExpenses(@NotNull List<Expense> expenses) {
        final Map<Month, MonthList> monthListMap = new HashMap<>();
        history.forEach(monthList -> {
            monthListMap.put(monthList.getMonth(), monthList);
        });
        expenses.stream()
                .sorted(Comparator.comparing(Expense::getDate))
                .forEach(expense -> {
                    Month month = new Month(expense.getDate());
                    MonthList monthList = monthListMap.computeIfAbsent(month, month1 -> new MonthList(month1.toDate()));
                    monthList.add(expense);
                });
        history.clear();
        monthListMap.values().stream()
                .sorted(Comparator.comparing(MonthList::getMonth))
                .forEach(history::add);

        updateTotals();
    }

    private void updateTotals() {
        int balance = originalBalanceCents;
        for (MonthList monthList : history) {
            monthList.updateBalance(balance);
            balance = monthList.getEndBalanceCents();
        }
    }

    @NotNull
    public List<MonthList> getHistory() {
        return Collections.unmodifiableList(history);
    }
}
