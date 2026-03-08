package ui;

import java.util.function.Consumer;

import bank.model.Account;
import bank.model.AccountType;
import bank.model.User;
import bank.service.BankService;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AccountSelectorView extends BaseView {

    private static final int MAX_CARD_WIDTH = 320;
    private static final double CARD_WIDTH_RATIO = 0.35;

    private final VBox accountList;
    private Consumer<Account> onAccountSelected;
    private Runnable onNewAccount;
    private Runnable logOut;

    public AccountSelectorView(boolean isDark, User user, BankService bankService) {
        super(isDark, 12, new Insets(0, 40, 20, 40));

        accountList = new VBox(10);
        accountList.setAlignment(Pos.CENTER);
        accountList.setMaxWidth(MAX_CARD_WIDTH);
        for (Account account : user.getAccounts()) {
            accountList.getChildren().add(buildCard(account));
        }

        Region spacer = new Region();
        spacer.setPrefHeight(4);

        Label newAccountLink = new Label("+ Neues Konto");
        newAccountLink.getStyleClass().add("link-label");
        newAccountLink.setOnMouseClicked(e -> { if (onNewAccount != null) onNewAccount.run(); });

        Label logOutLabel = new Label("-> Abmelden");
        logOutLabel.getStyleClass().add("link-label");
        logOutLabel.setOnMouseClicked(e -> {if (this.logOut != null) this.logOut.run(); });

        getChildren().addAll(accountList, spacer, newAccountLink, logOutLabel);
    }

    public void refreshAccounts(User user) {
        accountList.getChildren().clear();
        for (Account account : user.getAccounts()) {
            accountList.getChildren().add(buildCard(account));
        }
    }

    private HBox buildCard(Account account) {
        String type = account.getAccountType() == AccountType.CURRENT ? "Girokonto" : "Sparkonto";
        String number = account.getAccountNumber() != null ? account.getAccountNumber() : "";
        Label typeLabel = new Label(type);
        typeLabel.getStyleClass().add("card-type");

        Label numberLabel = new Label(number);
        numberLabel.getStyleClass().add("card-number");

        VBox left = new VBox(4, typeLabel, numberLabel);
        left.setAlignment(Pos.CENTER_LEFT);

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Label balanceLabel = new Label(String.format("%.2f €", account.getBalance()));
        balanceLabel.getStyleClass().add("card-balance");

        HBox card = new HBox(0, left, gap, balanceLabel);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("account-card");
        card.setMaxWidth(MAX_CARD_WIDTH);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setOnMouseClicked(e -> { if (onAccountSelected != null) onAccountSelected.accept(account); });

        return card;
    }

    public void setOnAccountSelected(Consumer<Account> c) { this.onAccountSelected = c; }
    public void setOnNewAccount(Runnable r) { this.onNewAccount = r; }
    public void setLogOut(Runnable r) { this.logOut = r;};

    public void bindToScene(Scene scene) {
        accountList.maxWidthProperty().bind(
            Bindings.min(MAX_CARD_WIDTH, scene.widthProperty().multiply(CARD_WIDTH_RATIO))
        );
    }
}
