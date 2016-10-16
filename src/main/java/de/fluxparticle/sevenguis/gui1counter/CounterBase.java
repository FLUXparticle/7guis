package de.fluxparticle.sevenguis.gui1counter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by sreinck on 20.02.16.
 */
public abstract class CounterBase extends Application {

    protected TextField tfCount;

    protected Button btCountUp;

    @Override
    public final void start(Stage stage) throws Exception {
        tfCount = new TextField("0");
        tfCount.setEditable(false);
        tfCount.setPrefWidth(50);

        btCountUp = new Button("Count");

        bind();

        HBox root = new HBox(10, tfCount, btCountUp);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root));
        stage.setTitle("Counter");
        stage.show();
    }

    protected abstract void bind();

}
