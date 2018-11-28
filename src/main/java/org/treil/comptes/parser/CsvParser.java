package org.treil.comptes.parser;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.finance.Expense;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class CsvParser {
    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);
    @NotNull
    private CsvOptions options = new CsvOptions();

    public CsvParsedResult parse(@NotNull File file) throws IOException, ParseException {
        logger.info(String.format("Parsing file %s", file.getAbsolutePath()));
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader freader = new InputStreamReader(inputStream, Charset.forName(options.charset));
        BufferedReader reader = new BufferedReader(freader);
        int l = 0;
        String line = reader.readLine();
        int initialBalanceCents = 0;
        List<Expense> expenseList = new ArrayList<Expense>();
        while (line != null) {
            String[] values = line.split(options.fieldsSeparator);
            if (l == options.balanceLineIndex) {
                initialBalanceCents = options.parseCents(values[options.balanceFieldIndex]);
            } else {
                Date date = options.parseDate(values[options.dateColumnIndex]);
                int amountCents = options.parseCents(values[options.amountColumnIndex]);
                String type = values[options.typeColumnIndex];
                String action = values[options.actionColumnIndex];
                String origin = values[options.originColumnIndex];
                expenseList.add(new Expense(date, amountCents, origin));
            }
            line = reader.readLine();
            l++;
        }
        reader.close();
        return new CsvParsedResult(initialBalanceCents, expenseList);
    }
}
