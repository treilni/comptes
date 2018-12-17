package org.treil.comptes.finance;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nicolas
 * @since 04/12/2018.
 */
public class Repartition implements Serializable {
    public static final String NO_CATEGORY = "#";

    @NotNull
    private final Map<String, Integer> percentByCategory = new ConcurrentHashMap<>(); // prevents null keys

    public Repartition() {
        percentByCategory.put(NO_CATEGORY, 100);
    }

    public void addFraction(@NotNull String category, int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent should be between 0 and 100");
        }
        Integer removed = percentByCategory.remove(category);
        if (percentByCategory.isEmpty()) {
            // category was the only one, so it should be 100 whatever we want to do
            percentByCategory.put(category, 100);
            return;
        }
        int target = (removed != null ? removed : 0) + percent;
        if (target > 100) {
            target = 100;
        }

        int sumOfRest = percentByCategory.values().stream().mapToInt(Integer::intValue).sum();
        int overflow = sumOfRest + target - 100;
        ArrayList<String> categories = new ArrayList<>(percentByCategory.keySet());
        categories.sort(Comparator.naturalOrder());
        for (String cat : categories) {
            Integer pct = percentByCategory.get(cat);
            if (pct > overflow) {
                percentByCategory.put(cat, pct - overflow);
                break;
            } else {
                percentByCategory.remove(cat);
                overflow -= pct;
                if (overflow == 0) {
                    break;
                }
            }
        }
        percentByCategory.put(category, target);
    }

    public void setCategory(@NotNull String category) {
        percentByCategory.clear();
        percentByCategory.put(category, 100);
    }

    @NotNull
    public Collection<String> getCategories() {
        return percentByCategory.keySet();
    }

    public int getFractionPct(@NotNull String cat) {
        Integer result = percentByCategory.get(cat);
        return result == null ? 0 : result;
    }

    @Deprecated
    public void setPercentByCategory(@NotNull Map<String, Integer> percentByCategory) {
        this.percentByCategory.clear();
        this.percentByCategory.putAll(percentByCategory);
    }
}
