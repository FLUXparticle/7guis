package de.fluxparticle.sevenguis.gui4timer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class TimerBase extends Application {

    protected static final Duration DURATION = Duration.millis(100);

    protected ProgressBar pbProgress;

    protected Label lNumericProgress;

    protected Slider sSlider;

    protected Button btReset;

    public final void start(Stage stage) {
        pbProgress = new ProgressBar();
        lNumericProgress = new Label();
        sSlider = new Slider(1, 40, 20);
        btReset = new Button("Reset");

        bind();

        VBox root = new VBox(10, new HBox(10, new Label("Elapsed Time: "), pbProgress),
                lNumericProgress,
                                 new HBox(10, new Label("Duration: "), sSlider),
                btReset);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root));
        stage.setTitle("Timer");
        stage.show();
    }

    protected abstract void bind();

}
