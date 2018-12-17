package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.Expense;

/**
 * @author Nicolas
 * @since 17/12/2018.
 */
public interface PostProcessor {
    void process(@NotNull Expense expense, String[] values, CsvOptions options);
}
