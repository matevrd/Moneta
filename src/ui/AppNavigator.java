package ui;

import bank.model.User;
import bank.service.BankService;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AppNavigator {

    private final AnimationController anim;
    private final StackPane root;
    private final VBox content;
    private final ImageView logo;
    private final double offset;
    private final Scene scene;
    private final boolean isDark;
    private final BankService bankService;
    private LoginView loginView;
    private RegisterView registerView;

    public AppNavigator(AnimationController anim, StackPane root, VBox content,
                        ImageView logo, double offset, Scene scene,
                        boolean isDark, BankService bankService) {
        this.anim = anim;
        this.root = root;
        this.content = content;
        this.logo = logo;
        this.offset = offset;
        this.scene = scene;
        this.isDark = isDark;
        this.bankService = bankService;
    }

    public void bind(LoginView login, RegisterView register) {
        this.loginView = login;
        this.registerView = register;
        login.setOnRegisterClick(() -> {
            if (!anim.isAnimating()) anim.switchPanel(login, register, logo, offset);
        });
        login.setOnLoginSuccess(user -> {
            ToastService.showSuccess(root, "Willkommen, " + user.getUsername() + "!");
            if (!anim.isAnimating()) showAccountSelector(user);
        });

        register.setOnLoginClick(() -> {
            if (!anim.isAnimating()) anim.switchPanel(register, login, logo, offset);
        });
        register.setOnRegisterSuccess(() -> {
            ToastService.showSuccess(root, "Erfolgreiche Registrierung");
            if (!anim.isAnimating()) anim.switchPanel(register, login, logo, offset);
        });
    }

    private void showAccountSelector(User user) {
        AccountSelectorView selector = new AccountSelectorView(isDark, user, bankService);
        selector.setOpacity(0);
        selector.setMouseTransparent(true);
        selector.bindToScene(scene);
        anim.switchToSelector(root, content, logo, selector, offset);

        selector.setOnNewAccount(() -> {
            if (isProfileComplete(user)) {
                showNewAccount(user, selector, selector);
            } else {
                showProfileSetup(user, selector);
            }
        });
        selector.setOnAccountSelected(account -> {
            // TODO: open dashboard for selected account
        });

        selector.setLogOut(() -> {
            if (!anim.isAnimating())
                anim.switchFromSelector(root, content, loginView, registerView, logo, offset);
        });
    }

    private void showProfileSetup(User user, AccountSelectorView selector) {
        ProfileSetupView profile = new ProfileSetupView(isDark, user);
        profile.setOpacity(0);
        profile.bindToScene(scene);
        anim.switchInWrapper(anim.getSelectorWrapper(), selector, profile);
        profile.setOnComplete(() -> showNewAccount(user, selector, profile));
    }

    private void showNewAccount(User user, AccountSelectorView selector, Node from) {
        NewAccountView nav = new NewAccountView(isDark, user, bankService);
        nav.setOpacity(0);
        nav.bindToScene(scene);
        anim.switchInWrapper(anim.getSelectorWrapper(), from, nav);
        nav.setOnBack(() -> anim.switchInWrapper(anim.getSelectorWrapper(), nav, selector));
        nav.setOnCreated(acc -> {
            selector.refreshAccounts(user);
            anim.switchInWrapper(anim.getSelectorWrapper(), nav, selector);
            ToastService.showSuccess(root, "Konto erfolgreich erstellt!");
        });
    }

    private boolean isProfileComplete(User user) {
        return !user.getFirstName().isEmpty()
            && !user.getLastName().isEmpty()
            && !user.getBirthday().isEmpty()
            && !user.getAddress().isEmpty();
    }
}
