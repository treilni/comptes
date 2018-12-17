package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.Expense;

/**
 * @author Nicolas
 * @since 17/12/2018.
 */
public class BicsPostProcessor implements PostProcessor {
    private static final String CreditCardRegex = "^[0-9]+ CB[*@-9]+ ";

    @Override
    public void process(@NotNull Expense expense, String[] values, CsvOptions options) {

    }
}
