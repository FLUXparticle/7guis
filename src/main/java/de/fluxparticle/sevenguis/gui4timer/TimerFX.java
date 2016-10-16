package de.fluxparticle.sevenguis.gui4timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class TimerFX extends TimerBase {

    protected void bind() {
        SimpleDoubleProperty elapsed = new SimpleDoubleProperty(0);

        pbProgress.progressProperty().bind(elapsed.divide(sSlider.valueProperty()));

        lNumericProgress.textProperty().bind(elapsed.asString("%.1fs"));

        btReset.setOnAction(event -> elapsed.set(0));

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if (elapsed.get() < sSlider.valueProperty().get()) elapsed.set(elapsed.get() + 0.1);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
