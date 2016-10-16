package de.fluxparticle.sevenguis.gui6circledrawer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static java.lang.String.format;

/**
 * Created by sreinck on 22.06.16.
 */
class Dialog {

    private final Stage stage;

    private final Slider slider;

    public Dialog(Circle selected) {
        stage = new Stage();

        {
            VBox content = new VBox(10);
            content.setPadding(new Insets(10));

            {
                Label info = new Label(format("Adjust diameter of circle at (%s, %s)", selected.getX(), selected.getY()));

                slider = new Slider(10, 50, selected.getDiameter());

                content.getChildren().addAll(info, slider);
            }

            stage.setScene(new Scene(content));
        }
    }

    public Stage getStage() {
        return stage;
    }

    public Slider getSlider() {
        return slider;
    }

}
