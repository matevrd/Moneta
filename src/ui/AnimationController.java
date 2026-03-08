package ui;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationController {

    private boolean animating;
    private VBox selectorWrapper;
    private final Scene scene;

    public AnimationController(Scene scene) {
        this.scene = scene;
    }

    public boolean isAnimating() { return animating; }
    public VBox getSelectorWrapper() { return selectorWrapper; }

    public void playIntro(ImageView logo, LoginView login, double offset) {
        animating = true;
        login.setMouseTransparent(true);
        logo.setTranslateY(offset);

        FadeTransition logoFade = fade(logo, 900, 0, 1);

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(900), logo);
        slideUp.setToY(0);
        slideUp.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition loginFade = fade(login, 2000, 0, 1);

        ParallelTransition reveal = new ParallelTransition(slideUp, loginFade);
        reveal.setOnFinished(e -> {
            animating = false;
            login.setMouseTransparent(false);
        });

        new SequentialTransition(logoFade, new PauseTransition(Duration.millis(600)), reveal).play();
    }

    public void switchPanel(Node out, Node in, ImageView logo, double offset) {
        animating = true;
        out.setMouseTransparent(true);
        in.setMouseTransparent(true);

        TranslateTransition drop = new TranslateTransition(Duration.millis(700), logo);
        drop.setToY(offset);
        drop.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition rise = new TranslateTransition(Duration.millis(900), logo);
        rise.setToY(0);
        rise.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition hidePhase = new ParallelTransition(fade(out, 500, -1, 0), drop);
        ParallelTransition showPhase = new ParallelTransition(
                rise,
                new SequentialTransition(new PauseTransition(Duration.millis(300)), fade(in, 1700, 0, 1))
        );
        showPhase.setOnFinished(e -> {
            animating = false;
            in.setMouseTransparent(false);
        });

        new SequentialTransition(hidePhase, new PauseTransition(Duration.millis(500)), showPhase).play();
    }

    public void switchToSelector(StackPane root, VBox content, ImageView logo,
                                  AccountSelectorView selector, double offset) {
        animating = true;
        content.setMouseTransparent(true);

        TranslateTransition drop = new TranslateTransition(Duration.millis(700), logo);
        drop.setToY(offset);
        drop.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition hidePhase = new ParallelTransition(fade(content, 500, -1, 0), drop);
        hidePhase.setOnFinished(e -> {
            root.getChildren().remove(content);

            selectorWrapper = new VBox(0, logo, selector);
            selectorWrapper.setAlignment(Pos.CENTER);
            VBox.setMargin(selector, new Insets(-30, 0, 0, 0));
            selectorWrapper.setOpacity(0);
            root.getChildren().add(0, selectorWrapper);

            logo.fitWidthProperty().unbind();
            logo.fitWidthProperty().bind(Bindings.min(450, scene.widthProperty().multiply(0.42)));

            TranslateTransition rise = new TranslateTransition(Duration.millis(900), logo);
            rise.setToY(0);
            rise.setInterpolator(Interpolator.EASE_BOTH);

            ParallelTransition showPhase = new ParallelTransition(
                    fade(selectorWrapper, 400, 0, 1),
                    rise,
                    new SequentialTransition(new PauseTransition(Duration.millis(300)), fade(selector, 1500, 0, 1))
            );
            showPhase.setOnFinished(ev -> {
                animating = false;
                selector.setMouseTransparent(false);
            });
            showPhase.play();
        });

        new SequentialTransition(hidePhase, new PauseTransition(Duration.millis(300))).play();
    }

    public void switchFromSelector(StackPane root, VBox content, LoginView login,
                                   RegisterView register, ImageView logo, double offset) {
        animating = true;
        if (selectorWrapper != null) selectorWrapper.setMouseTransparent(true);

        login.setOpacity(0);
        login.setMouseTransparent(true);
        if (register != null) {
            register.setOpacity(0);
            register.setMouseTransparent(true);
        }
        content.setOpacity(1);
        if (!root.getChildren().contains(content)) root.getChildren().add(content);

        TranslateTransition drop = new TranslateTransition(Duration.millis(700), logo);
        drop.setToY(offset);
        drop.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition hidePhase = new ParallelTransition(fade(selectorWrapper, 500, -1, 0), drop);
        hidePhase.setOnFinished(e -> {
            selectorWrapper.getChildren().remove(logo);
            content.getChildren().add(0, logo);
            content.setMouseTransparent(false);
            root.getChildren().remove(selectorWrapper);
            selectorWrapper = null;

            logo.fitWidthProperty().unbind();
            logo.fitWidthProperty().bind(Bindings.min(500, scene.widthProperty().multiply(0.45)));

            TranslateTransition rise = new TranslateTransition(Duration.millis(900), logo);
            rise.setToY(0);
            rise.setInterpolator(Interpolator.EASE_BOTH);

            ParallelTransition showPhase = new ParallelTransition(
                    rise,
                    new SequentialTransition(new PauseTransition(Duration.millis(300)), fade(login, 1500, 0, 1))
            );
            showPhase.setOnFinished(ev -> {
                animating = false;
                login.setMouseTransparent(false);
            });
            showPhase.play();
        });

        new SequentialTransition(hidePhase, new PauseTransition(Duration.millis(300))).play();
    }

    public void switchInWrapper(VBox wrapper, Node out, Node in) {
        in.setOpacity(0);
        VBox.setMargin(in, new Insets(-60, 0, 0, 0));
        FadeTransition fadeOut = fade(out, 300, -1, 0);
        fadeOut.setOnFinished(e -> {
            wrapper.getChildren().remove(out);
            if (!wrapper.getChildren().contains(in)) wrapper.getChildren().add(in);
            fade(in, 400, 0, 1).play();
        });
        fadeOut.play();
    }

    public FadeTransition fade(Node node, double ms, double from, double to) {
        FadeTransition ft = new FadeTransition(Duration.millis(ms), node);
        if (from >= 0) ft.setFromValue(from);
        ft.setToValue(to);
        return ft;
    } 
}
