package ui;

import java.util.function.Consumer;

import bank.exception.AuthenticationException;
import bank.model.User;
import bank.service.BankService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LoginView extends BaseView {
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final BankService bankService;
    private final Button loginBtn;
    private Runnable onRegisterClick;
    private Consumer<User> onLoginSuccess;

    public LoginView(boolean isDark, BankService bankService) {
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

        loginBtn = new Button("Anmelden");
        loginBtn.getStyleClass().add("auth-btn");
        loginBtn.setOnAction(e -> performLogin());

        Label staticLabel = new Label("Noch kein Konto? ");
        staticLabel.getStyleClass().add("muted-label");

        Label registerLink = new Label("Registrieren");
        registerLink.getStyleClass().add("link-label");
        registerLink.setOnMouseClicked(e -> { if (onRegisterClick != null) onRegisterClick.run(); });

        HBox registerRow = new HBox(0, staticLabel, registerLink);
        registerRow.setAlignment(Pos.CENTER);

        getChildren().addAll(usernameField, passwordField, loginBtn, createErrorLabel(), registerRow);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showError("Benutzername und Passwort sind erforderlich.");
            return;
        }
        loginBtn.setDisable(true);
        try {
            User user = bankService.login(username, password);
            clearError();
            if (onLoginSuccess != null) onLoginSuccess.accept(user);
        } catch (AuthenticationException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Ein unerwarteter Fehler ist aufgetreten.");
        } finally {
            loginBtn.setDisable(false);
        }
    }

    public void setOnRegisterClick(Runnable r) {
        this.onRegisterClick = r;
    }

    public void setOnLoginSuccess(Consumer<User> c) {
        this.onLoginSuccess = c;
    }

    public void bindToScene(Scene scene) {
        bindFieldWidths(scene, usernameField, passwordField);
    }
}
