package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.finance.Expense;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.treil.comptes.finance.Expense.CreationType.BANK;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class CsvParser {
    public static final String CSV_SUFFIX = "csv";
    public static final String TXT_SUFFIX = "txt";
    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);

    public CsvParsedResult parse(@NotNull File file) throws IOException, ParseException {
        CsvOptions options = CsvOptions.guess(file);

        logger.info(String.format("Parsing file %s", file.getAbsolutePath()));
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader freader = new InputStreamReader(inputStream, Charset.forName(options.charset));
        BufferedReader reader = new BufferedReader(freader);
        int l = 0;
        String line = reader.readLine();
        int initialBalanceCents = 0;
        List<Expense> expenseList = new ArrayList<>();
        while (line != null) {
            String[] values = line.split(options.fieldsSeparator);
            if (l == options.balanceLineIndex) {
                if (options.balanceFieldIndex >= 0) {
                    initialBalanceCents = options.parseCents(values[options.balanceFieldIndex]);
                }
            } else if (l >= options.firstLineIndex) {
                Date date = options.parseDate(values[options.dateColumnIndex]);
                int amountCents = options.parseCents(values[options.amountColumnIndex]);
                String type = getValue(values, options.typeColumnIndex);
                String origin = values[options.originColumnIndex];
                Expense e = new Expense(date, amountCents, origin, BANK);
                e.setType(type);
                if (options.actionColumnIndex >= 0) {
                    String action = getValue(values, options.actionColumnIndex);
                    //e.setAction(action);
                } else {
                    e.setAction(Expense.Action.OTHER);
                }
                if (options.postProcessing != null) {
                    options.postProcessing.process(e, values, options);
                }
                expenseList.add(e);
                logger.debug(String.format("Parsed line %s", line));
            }
            line = reader.readLine();
            l++;
        }
        reader.close();
        return new CsvParsedResult(initialBalanceCents, expenseList);
    }

    @Nullable
    private String getValue(@NotNull String[] values, int index) {
        return index < 0 ? null : values[index];
    }
}
