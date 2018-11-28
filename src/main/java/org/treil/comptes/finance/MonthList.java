package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;
import org.treil.comptes.formatter.CentsFormatter;
import org.treil.comptes.time.Month;

import java.util.*;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class MonthList {
    private final Month month;
    private final Date nextMonth;
    private boolean complete = false;
    private int totalCents = 0;
    private final List<Expense> expenses = new ArrayList<Expense>();

    public MonthList(@NotNull Date date) {
        month = new Month(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(month.toDate());
        calendar.add(Calendar.MONTH, 1);
        nextMonth = calendar.getTime();
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
}
