package org.treil.comptes.finance;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nicolas
 * @since 04/12/2018.
 */
class RepartitionTest {

    @Test
    void init() {
        Repartition r = new Repartition();
        Collection<String> categories = r.getCategories();
        assertEquals(1, categories.size());
        assertTrue(categories.contains(Repartition.NO_CATEGORY));
    }

    @Test
    void addFraction() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        r.addFraction(cat1, 40);
        Collection<String> categories = r.getCategories();
        assertEquals(2, categories.size());
        assertEquals(40, r.getFractionPct(cat1));
        assertEquals(60, r.getFractionPct(Repartition.NO_CATEGORY));
    }

    @Test
    void addFraction100() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        r.addFraction(cat1, 100);
        Collection<String> categories = r.getCategories();
        assertEquals(1, categories.size());
        assertEquals(100, r.getFractionPct(cat1));
    }

    @Test
    void addFractionTwice() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        r.addFraction(cat1, 40);
        r.addFraction(cat1, 20);
        Collection<String> categories = r.getCategories();
        assertEquals(2, categories.size());
        assertEquals(60, r.getFractionPct(cat1));
        assertEquals(40, r.getFractionPct(Repartition.NO_CATEGORY));
    }

    @Test
    void addFractionZero() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        r.addFraction(cat1, 60);
        r.addFraction(cat1, 0);
        Collection<String> categories = r.getCategories();
        assertEquals(2, categories.size());
        assertEquals(60, r.getFractionPct(cat1));
        assertEquals(40, r.getFractionPct(Repartition.NO_CATEGORY));
    }

    @Test
    void addFractionOverflow() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        r.addFraction(cat1, 60);
        r.addFraction(cat1, 60);
        Collection<String> categories = r.getCategories();
        assertEquals(1, categories.size());
        assertEquals(100, r.getFractionPct(cat1));
    }

    @Test
    void addFractionIllegal() {
        assertThrows(IllegalArgumentException.class, () -> {
            Repartition r = new Repartition();
            String cat1 = "CAT1";
            r.addFraction(cat1, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Repartition r = new Repartition();
            String cat1 = "CAT1";
            r.addFraction(cat1, 101);
        });
    }

    @Test
    void addFractionOverflow2() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        String cat2 = "CAT2";
        String cat3 = "CAT3";
        r.setCategory(cat1);
        r.addFraction(cat2, 50);
        r.addFraction(cat3, 50);
        Collection<String> categories = r.getCategories();
        assertEquals(2, categories.size());
        assertEquals(50, r.getFractionPct(cat2));
        assertEquals(50, r.getFractionPct(cat3));
    }

    @Test
    void addFractionOverflow3() {
        Repartition r = new Repartition();
        String cat1 = "CAT1";
        String cat2 = "CAT2";
        String cat3 = "CAT3";
        r.setCategory(cat1);
        r.addFraction(cat2, 30);
        r.addFraction(cat3, 50);
        Collection<String> categories = r.getCategories();
        assertEquals(3, categories.size());
        assertEquals(20, r.getFractionPct(cat1));
        assertEquals(30, r.getFractionPct(cat2));
        assertEquals(50, r.getFractionPct(cat3));
    }

    @Test
    void setCategory() {
        Repartition r = new Repartition();
        String cat = "CAT";
        r.setCategory(cat);
        Collection<String> categories = r.getCategories();
        assertEquals(1, categories.size());
        assertTrue(categories.contains(cat));
        assertEquals(100, r.getFractionPct(cat));
    }
}