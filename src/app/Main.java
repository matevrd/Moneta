package app;

import bank.repository.DatabaseManager;
import bank.service.BankService;
import javafx.application.Application;
import javafx.application.ColorScheme;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.AnimationController;
import ui.AppNavigator;
import ui.LoginView;
import ui.RegisterView;
import utils.ImageUtils;

public class Main extends Application {

    @Override
    public void init() throws Exception{
        DatabaseManager.initialize();
    }

    @Override
    public void start(Stage stage) {
        boolean isDark = Platform.getPreferences().getColorScheme() == ColorScheme.DARK;

        BankService bankService = new BankService();
        ImageView logo = createLogo(isDark);
        LoginView login = new LoginView(isDark, bankService);
        RegisterView register = new RegisterView(isDark, bankService);

        login.setOpacity(0);
        register.setOpacity(0);
        register.setMouseTransparent(true);

        StackPane panels = new StackPane(register, login);
        panels.setAlignment(Pos.CENTER);

        VBox content = new VBox(0, logo, panels);
        content.setAlignment(Pos.CENTER);
        VBox.setMargin(panels, new Insets(-80, 0, 0, 0));

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: " + (isDark ? "#1a1a1a" : "#e6e2de") + ";");

        Scene scene = new Scene(root, 1000, 700);
        var cssUrl = Main.class.getResource("/ui/auth.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        logo.fitWidthProperty().bind(Bindings.min(500, scene.widthProperty().multiply(0.45)));
        login.bindToScene(scene);
        register.bindToScene(scene);

        stage.setTitle("Moneta Finance");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(700);
        stage.show();

        double offset = (panels.getHeight() - 120) / 2.0;
        AnimationController anim = new AnimationController(scene);
        anim.playIntro(logo, login, offset);
        new AppNavigator(anim, root, content, logo, offset, scene, isDark, bankService)
                .bind(login, register);
    }

    private static ImageView createLogo(boolean isDark) {
        Image img = isDark
                ? ImageUtils.removeBackground(new Image("file:lib/logo/dark.png"), 0x000000, 20)
                : ImageUtils.removeBackground(new Image("file:lib/logo/light.png"), 0xe6e2de, 20);
        ImageView view = new ImageView(img);
        view.setPreserveRatio(true);
        view.setOpacity(0);
        return view;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
