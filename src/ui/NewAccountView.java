package ui;

import java.util.function.Consumer;

import bank.model.Account;
import bank.model.User;
import bank.service.BankService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NewAccountView extends BaseView {

    private boolean isGiro = true;
    private final TextField balanceField;
    private final TextField interestField;
    private final VBox interestRow;
    private Consumer<Account> onCreated;
    private Runnable onBack;

    public NewAccountView(boolean isDark, User user, BankService bankService) {
        super(isDark, 14, new Insets(0, 40, 24, 40));

        Label title = new Label("Neues Konto");
        title.getStyleClass().add("selector-title");

        Button giroBtn = new Button("Girokonto");
        giroBtn.getStyleClass().addAll("type-btn", "type-btn-selected");

        Button sparBtn = new Button("Sparkonto");
        sparBtn.getStyleClass().add("type-btn");

        HBox typeRow = new HBox(8, giroBtn, sparBtn);
        typeRow.setAlignment(Pos.CENTER);
        typeRow.setMaxWidth(MAX_FIELD_WIDTH);

        balanceField = new TextField();
        balanceField.setPromptText("Anfangsguthaben (€)");
        balanceField.setMaxWidth(MAX_FIELD_WIDTH);
        balanceField.getStyleClass().add("auth-field");

        interestField = new TextField();
        interestField.setPromptText("Zinssatz (%)");
        interestField.setMaxWidth(MAX_FIELD_WIDTH);
        interestField.getStyleClass().add("auth-field");

        interestRow = new VBox(interestField);
        interestRow.setAlignment(Pos.CENTER);
        interestRow.setVisible(false);
        interestRow.setManaged(false);

        giroBtn.setOnAction(e -> {
            isGiro = true;
            if (!giroBtn.getStyleClass().contains("type-btn-selected"))
                giroBtn.getStyleClass().add("type-btn-selected");
            sparBtn.getStyleClass().remove("type-btn-selected");
            interestRow.setVisible(false);
            interestRow.setManaged(false);
        });

        sparBtn.setOnAction(e -> {
            isGiro = false;
            if (!sparBtn.getStyleClass().contains("type-btn-selected"))
                sparBtn.getStyleClass().add("type-btn-selected");
            giroBtn.getStyleClass().remove("type-btn-selected");
            interestRow.setVisible(true);
            interestRow.setManaged(true);
        });

        Button createBtn = new Button("Erstellen");
        createBtn.getStyleClass().add("auth-btn");
        createBtn.setOnAction(e -> {
            clearError();
            String balStr = balanceField.getText().trim().replace(",", ".");
            double balance;
            try {
                balance = Double.parseDouble(balStr);
                if (balance < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError("Bitte geben Sie ein gültiges Anfangsguthaben ein.");
                return;
            }
            Account created;
            if (isGiro) {
                created = bankService.createCurrentAccount(user, balance);
            } else {
                String rateStr = interestField.getText().trim().replace(",", ".");
                double rate;
                try {
                    rate = Double.parseDouble(rateStr);
                    if (rate < 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    showError("Bitte geben Sie einen gültigen Zinssatz ein.");
                    return;
                }
                created = bankService.createSavingsAccount(user, balance, rate);
            }
            if (onCreated != null) onCreated.accept(created);
        });

        Label backLink = new Label("← Zurück");
        backLink.getStyleClass().add("link-label");
        backLink.setOnMouseClicked(ev -> { if (onBack != null) onBack.run(); });

        getChildren().addAll(title, typeRow, balanceField, interestRow, createErrorLabel(), createBtn, backLink);
    }

    public void setOnCreated(Consumer<Account> c) { this.onCreated = c; }
    public void setOnBack(Runnable r) { this.onBack = r; }

    public void bindToScene(Scene scene) {
        bindFieldWidths(scene, balanceField, interestField);
    }
}
