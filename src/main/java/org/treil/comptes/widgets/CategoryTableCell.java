package org.treil.comptes.widgets;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * @author Nicolas
 * @since 17/01/2019.
 */
public class CategoryTableCell extends javafx.scene.control.TableCell<org.treil.comptes.finance.Expense, Integer> {
    public CategoryTableCell() {
        super();
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        VBox b = new VBox();
        b.setFillWidth(true);
        b.setAlignment(Pos.CENTER);
        if (item != null) {
            if (item == 0 || item == 100) {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(item == 100);
                b.getChildren().add(checkBox);
            } else {
                Label label = new Label(item.toString());
                b.getChildren().add(label);
            }
        }
        setGraphic(b);
    }
}
