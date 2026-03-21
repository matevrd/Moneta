package ui;

import bank.model.User;
import bank.service.BankService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileSetupView extends BaseView {

    private final Label subtitle;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField birthdayField;
    private final TextField addressField;
    private Runnable onComplete;
    private final BankService bankService;

    public ProfileSetupView(boolean isDark, User user, BankService bankService) {
        super(isDark, 9, new Insets(0, 40, 12, 40));
        this.bankService = bankService;

        subtitle = new Label("Profil vervollständigen");
        subtitle.setMaxWidth(MAX_FIELD_WIDTH);
        subtitle.getStyleClass().add("muted-label");

        firstNameField = new TextField(user.getFirstName());
        firstNameField.setPromptText("Vorname");
        firstNameField.setMaxWidth(MAX_FIELD_WIDTH);
        firstNameField.getStyleClass().add("auth-field");

        lastNameField = new TextField(user.getLastName());
        lastNameField.setPromptText("Nachname");
        lastNameField.setMaxWidth(MAX_FIELD_WIDTH);
        lastNameField.getStyleClass().add("auth-field");

        birthdayField = new TextField(user.getBirthday());
        birthdayField.setPromptText("Geburtsdatum (TT.MM.JJJJ)");
        birthdayField.setMaxWidth(MAX_FIELD_WIDTH);
        birthdayField.getStyleClass().add("auth-field");

        addressField = new TextField(user.getAddress());
        addressField.setPromptText("Adresse");
        addressField.setMaxWidth(MAX_FIELD_WIDTH);
        addressField.getStyleClass().add("auth-field");

        Button saveBtn = new Button("Speichern");
        saveBtn.getStyleClass().add("auth-btn");
        saveBtn.setOnAction(e -> {
            String fn   = firstNameField.getText().trim();
            String ln   = lastNameField.getText().trim();
            String bd   = birthdayField.getText().trim();
            String addr = addressField.getText().trim();
            if (fn.isEmpty() || ln.isEmpty() || bd.isEmpty() || addr.isEmpty()) {
                showError("Bitte füllen Sie alle Felder aus.");
                return;
            }
            user.setFirstName(fn);
            user.setLastName(ln);
            user.setBirthday(bd);
            user.setAddress(addr);
            bankService.updateProfile(user);
            if (onComplete != null) onComplete.run();
        });

        getChildren().addAll(subtitle, firstNameField, lastNameField,
                birthdayField, addressField, createErrorLabel(), saveBtn);
    }

    public void setOnComplete(Runnable r) { this.onComplete = r; }

    public void bindToScene(Scene scene) {
        bindNodeWidths(scene, subtitle, firstNameField, lastNameField, birthdayField, addressField);
    }
}
