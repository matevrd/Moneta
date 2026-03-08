package ui;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public final class ToastService {

    private ToastService() {}

    public static void showSuccess(StackPane root, String message) {
        Label check = new Label("\u2713");
        check.getStyleClass().add("toast-check");

        Region divider = new Region();
        divider.getStyleClass().add("toast-divider");
        divider.setPrefSize(1, 13);
        divider.setMinSize(1, 13);
        divider.setMaxSize(1, 13);

        Label msg = new Label(message);
        msg.getStyleClass().add("toast-msg");

        HBox toast = new HBox(10, check, divider, msg);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        toast.getStyleClass().add("toast");
        toast.setMouseTransparent(true);
        toast.setTranslateY(-80);
        StackPane.setAlignment(toast, Pos.TOP_CENTER);
        root.getChildren().add(toast);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(480), toast);
        slideIn.setFromY(-80);
        slideIn.setToY(20);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(380), toast);
        slideOut.setToY(-80);
        slideOut.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(380), toast);
        fadeOut.setToValue(0);

        ParallelTransition exit = new ParallelTransition(slideOut, fadeOut);
        exit.setOnFinished(e -> root.getChildren().remove(toast));

        new SequentialTransition(
                slideIn,
                new PauseTransition(Duration.millis(2400)),
                exit
        ).play();
    }
}
