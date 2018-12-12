package org.treil.comptes.widgets;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

/**
 * @author Nicolas
 * @since 28/11/2018.
 */
public enum Style {
    columnHeader, amount, balanceInfo;

    public void applyTo(@NotNull Node node) {
        node.getStyleClass().add(this.name());
    }
}
