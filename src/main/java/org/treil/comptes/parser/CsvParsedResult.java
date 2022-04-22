package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.finance.Expense;

import java.util.Collections;
import java.util.List;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class CsvParsedResult {
    private static final Logger logger = LoggerFactory.getLogger(CsvParsedResult.class);

    private final int initialBalanceCents;
    @NotNull
    private final List<Expense> expenseList;

    public CsvParsedResult(int initialBalanceCents, @NotNull List<Expense> expenses) {
        this.initialBalanceCents = initialBalanceCents;
        this.expenseList = Collections.unmodifiableList(expenses);
    }

    public int getInitialBalanceCents() {
        return initialBalanceCents;
    }

    @NotNull
    public List<Expense> getExpenseList() {
        return expenseList;
    }
}
