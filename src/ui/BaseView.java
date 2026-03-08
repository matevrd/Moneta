package ui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class BaseView extends VBox {

    protected static final int MAX_FIELD_WIDTH = 300;
    protected static final double FIELD_WIDTH_RATIO = 0.3;

    private Label errorLabel;

    protected BaseView(boolean isDark, double spacing, Insets padding) {
        getStyleClass().add(isDark ? "dark" : "light");
        setAlignment(Pos.CENTER);
        setSpacing(spacing);
        setPadding(padding);
    }

    protected Label createErrorLabel() {
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        return errorLabel;
    }

    protected void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    protected void clearError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    protected void bindFieldWidths(Scene scene, TextField... fields) {
        for (TextField field : fields) {
            field.maxWidthProperty().bind(
                Bindings.min(MAX_FIELD_WIDTH, scene.widthProperty().multiply(FIELD_WIDTH_RATIO))
            );
        }
    }

    protected void bindNodeWidths(Scene scene, Region... nodes) {
    for (Region node : nodes) {
        node.maxWidthProperty().bind(
                Bindings.min(MAX_FIELD_WIDTH, scene.widthProperty().multiply(FIELD_WIDTH_RATIO))
            );
        }
    }
}
