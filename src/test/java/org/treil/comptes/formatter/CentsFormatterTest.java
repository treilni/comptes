package org.treil.comptes.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nicolas
 * @since 27/11/2018.
 */
class CentsFormatterTest {

    @org.junit.jupiter.api.Test
    void format() {
        CentsFormatter centsFormatter = new CentsFormatter();
        assertEquals("0,00", centsFormatter.format(0, null));
        assertEquals("0,01", centsFormatter.format(1, null));
        assertEquals("0,10", centsFormatter.format(10, null));
        assertEquals("1,00", centsFormatter.format(100, null));
        assertEquals("999,99", centsFormatter.format(99999, null));
        assertEquals("1 000,00", centsFormatter.format(100000, null));
        assertEquals("12 345 678,93", centsFormatter.format(1234567893, null));
    }

    @org.junit.jupiter.api.Test
    void formatNegative() {
        CentsFormatter centsFormatter = new CentsFormatter();
        assertEquals("0,00", centsFormatter.format(-0, null));
        assertEquals("-0,01", centsFormatter.format(-1, null));
        assertEquals("-0,10", centsFormatter.format(-10, null));
        assertEquals("-1,00", centsFormatter.format(-100, null));
        assertEquals("-999,99", centsFormatter.format(-99999, null));
        assertEquals("-1 000,00", centsFormatter.format(-100000, null));
        assertEquals("-12 345 678,93", centsFormatter.format(-1234567893, null));
    }
}