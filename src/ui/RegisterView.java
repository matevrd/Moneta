package ui;

import bank.service.BankService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;

public class RegisterView extends BaseView {
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final BankService bankService;
    private final Button registerBtn;
    private Runnable onLoginClick;
    private Runnable onRegisterSuccess;

    public RegisterView(boolean isDark, BankService bankService) {
        super(isDark, 12, new Insets(0, 40, 24, 40));
        this.bankService = bankService;

        usernameField = new TextField();
        usernameField.setPromptText("Benutzername");
        usernameField.setMaxWidth(MAX_FIELD_WIDTH);
        usernameField.getStyleClass().add("auth-field");

        passwordField = new PasswordField();
        passwordField.setPromptText("Passwort");
        passwordField.setMaxWidth(MAX_FIELD_WIDTH);
        passwordField.getStyleClass().add("auth-field");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Passwort wiederholen");
        confirmPasswordField.setMaxWidth(MAX_FIELD_WIDTH);
        confirmPasswordField.getStyleClass().add("auth-field");

        usernameField.setTextFormatter(maxLength(32));
        passwordField.setTextFormatter(maxLength(128));
        confirmPasswordField.setTextFormatter(maxLength(128));

        registerBtn = new Button("Registrieren");
        registerBtn.getStyleClass().add("auth-btn");
        registerBtn.setOnAction(e -> performRegister());

        Label staticLabel = new Label("Bereits ein Konto? ");
        staticLabel.getStyleClass().add("muted-label");

        Label loginLink = new Label("Anmelden");
        loginLink.getStyleClass().add("link-label");
        loginLink.setOnMouseClicked(e -> { if (onLoginClick != null) onLoginClick.run(); });

        HBox loginRow = new HBox(0, staticLabel, loginLink);
        loginRow.setAlignment(Pos.CENTER);

        getChildren().addAll(usernameField, passwordField, confirmPasswordField, registerBtn, createErrorLabel(), loginRow);

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) bindToScene(newScene);
        });
    }

    private void performRegister() {
        String username  = usernameField.getText().trim();
        String password  = passwordField.getText();
        String confirm   = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Bitte alle Felder ausfüllen.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Passwörter stimmen nicht überein.");
            return;
        }
        if (password.length() < 8) {
            showError("Passwort muss mindestens 8 Zeichen lang sein.");
            return;
        }
        registerBtn.setDisable(true);
        try {
            bankService.register(username, password);
            clearError();
            clearFields();
            if (onRegisterSuccess != null) onRegisterSuccess.run();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Ein unerwarteter Fehler ist aufgetreten.");
        } finally {
            registerBtn.setDisable(false);
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    public void setOnLoginClick(Runnable r) {
        this.onLoginClick = r;
    }

    public void setOnRegisterSuccess(Runnable r) {
        this.onRegisterSuccess = r;
    }

    private static TextFormatter<?> maxLength(int max) {
        return new TextFormatter<>(c -> c.getControlNewText().length() <= max ? c : null);
    }

    public void bindToScene(Scene scene) {
        bindFieldWidths(scene, usernameField, passwordField, confirmPasswordField);
    }
}
