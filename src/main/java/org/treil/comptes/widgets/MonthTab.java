package org.treil.comptes.widgets;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.Expense;
import org.treil.comptes.finance.MonthList;
import org.treil.comptes.formatter.CentsFormatter;
import org.treil.comptes.formatter.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.function.Function;

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
        vPanel.getChildren().add(makeBalanceRow(monthList, resourceBundle));

        appendColumn(resourceBundle.getString("date"), Expense::getDate, DateFormatter::formatDay);
        appendColumn(resourceBundle.getString("type"), Expense::getType, String::toString);
        appendColumn(resourceBundle.getString("action"), Expense::getAction, String::toString);
        appendColumn(resourceBundle.getString("origin"), Expense::getOrigin, String::toString);
        appendColumn(resourceBundle.getString("debit"), expense -> {
            int amountCents = expense.getAmountCents();
            return amountCents < 0 ? amountCents : null;
        }, CentsFormatter::format, Style.amount);
        appendColumn(resourceBundle.getString("credit"), expense -> {
            int amountCents = expense.getAmountCents();
            return amountCents > 0 ? amountCents : null;
        }, CentsFormatter::format, Style.amount);

        vPanel.getChildren().add(table);
        setContent(vPanel);

        display(monthList);
    }

    private Node makeBalanceRow(@NotNull MonthList monthList, @NotNull ResourceBundle resourceBundle) {
        HBox result = new HBox();
        result.getChildren().add(new Label(resourceBundle.getString("solde.initial")));
        result.getChildren().add(new Label(CentsFormatter.format(monthList.getEndBalanceCents() - monthList.getTotalCents(), "€")));
        Style.balanceInfo.applyTo(result);
        return result;
    }

    private <T> void appendColumn(String title, Function<Expense, T> mapper, Function<T, String> renderer) {
        appendColumn(title, mapper, renderer, null);
    }

    private <T> void appendColumn(String title, Function<Expense, T> mapper, Function<T, String> renderer, Style style) {
        TableColumn<Expense, T> column = new TableColumn<>();
        column.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(mapper.apply(cellData.getValue())));
        column.setCellFactory(param -> {
            TableCell<Expense, T> result = new TableCell<Expense, T>() {
                @Override
                protected void updateItem(T value, boolean empty) {
                    setText(value == null || empty ? "" : renderer.apply(value));
                }
            };
            if (style != null) {
                style.applyTo(result);
            }
            return result;
        });
        column.setText(title);

        table.getColumns().add(column);
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
