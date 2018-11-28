package org.treil.comptes.widgets;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.Expense;
import org.treil.comptes.finance.MonthList;
import org.treil.comptes.formatter.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Nicolas
 * @since 26/11/2018.
 */
public class MonthTab extends Tab {
    private TableView<Expense> table = new TableView<>();

    public MonthTab(ResourceBundle resourceBundle, @NotNull MonthList monthList) {
        SimpleDateFormat f = new SimpleDateFormat(resourceBundle.getString("monthYearFormat"));
        setText(f.format(monthList.getMonth().toDate()));

        VBox vPanel = new VBox();
        TableColumn<Expense, Date> dateColumn = new TableColumn<>();
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate()));
        dateColumn.setCellFactory(param -> new TableCell<Expense, Date>() {
            @Override
            protected void updateItem(Date d, boolean empty) {
                setText(empty ? "" : DateFormatter.formatDay(d));
            }
        });
        dateColumn.setText("Date");

        table.getColumns().add(dateColumn);
//        grid.addColumn(c = 0, new StyledCell("Date", Style.columnHeader));
//        grid.addColumn(++c, new StyledCell("Type", Style.columnHeader));
//        grid.addColumn(++c, new StyledCell("Origine", Style.columnHeader));
//        grid.addColumn(++c, new StyledCell("Débit", Style.columnHeader));
//        grid.addColumn(++c, new StyledCell("Crédit", Style.columnHeader));
        vPanel.getChildren().add(table);
        setContent(vPanel);

        display(monthList);
    }

    private void display(@NotNull MonthList monthList) {
        int i = 1;
        table.setItems(FXCollections.observableList(monthList.getExpenses()));
    }

    private class StyledCell extends Label {
        public StyledCell(String value, Style style) {
            super(value);
            if (style != null) {
                getStyleClass().add(style.name());
            }
        }
    }
}
