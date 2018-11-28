package org.treil.comptes.widgets;

import javafx.scene.control.Tab;
import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.MonthList;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class MonthTab extends Tab {

    public MonthTab(ResourceBundle resourceBundle, @NotNull MonthList monthList) {
        SimpleDateFormat f = new SimpleDateFormat(resourceBundle.getString("monthYearFormat"));
        setText(f.format(monthList.getMonth().toDate()));
    }
}
