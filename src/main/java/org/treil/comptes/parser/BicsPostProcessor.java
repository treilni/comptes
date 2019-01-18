package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.finance.Expense;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nicolas
 * @since 17/12/2018.
 */
public class BicsPostProcessor implements PostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BicsPostProcessor.class);
    private static final String CreditCardRegex = "^[0-9]+ CB[*@0-9]+ ";
    private static final String PrlvSepa = "PRLV SEPA ";
    private static final String Evi = "EVI ";
    private static final String Vir = "VIR ";
    private static final String Virement = "VIREMENT ";
    private static final String Cheque = "CHEQUE ";
    private static final String RemChq = "REM CHQ ";
    private static final String Cotis = "COTIS ";
    private static final String Retrait = "RETRAIT DU";

    @NotNull
    private final Matcher ccMatcher;

    @NotNull
    private final Map<String, Expense.Action> startToAction = new HashMap<>();

    public BicsPostProcessor() {
        Pattern pattern = Pattern.compile(CreditCardRegex);
        this.ccMatcher = pattern.matcher("");
        startToAction.put(PrlvSepa, Expense.Action.AUTHORIZED_DEBIT);
        startToAction.put(Evi, Expense.Action.CREDITED_TRANSFER);
        startToAction.put(Vir, Expense.Action.DEBITED_TRANSFER);
        startToAction.put(Virement, Expense.Action.DEBITED_TRANSFER);
        startToAction.put(Cheque, Expense.Action.DEBITED_CHECK);
        startToAction.put(Cotis, Expense.Action.BANK_EXPENSE);
        startToAction.put(RemChq, Expense.Action.CREDITED_CHECK);
    }

    @Override
    public void process(@NotNull Expense expense, String[] values, CsvOptions options) {
        String origin = expense.getOrigin();
        ccMatcher.reset(origin);
        if (ccMatcher.find()) {
            int end = ccMatcher.end();
            expense.setOrigin(origin.substring(end));
            expense.setAction(Expense.Action.CARD_DEBIT);
        } else {
            boolean found = false;
            for (Map.Entry<String, Expense.Action> entry : startToAction.entrySet()) {
                String pattern = entry.getKey();
                if (origin.startsWith(pattern)) {
                    expense.setOrigin(origin.substring(pattern.length()));
                    expense.setAction(entry.getValue());
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (origin.contains(Retrait)) {
                    expense.setAction(Expense.Action.WITHDRAWAL);
                }
            }
        }
    }
}
