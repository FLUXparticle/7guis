package de.fluxparticle.sevenguis.gui0diamond;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by sreinck on 20.02.16.
 */
public abstract class DiamondBase extends Application {

    protected TextField tfCount;

    protected Button btCountUp;

    protected TextField tfLeft;

    protected TextField tfRight;

    protected ListView<Integer> lvResults;

    @Override
    public final void start(Stage stage) throws Exception {
        tfCount = new TextField("0");
        tfCount.setEditable(false);
        tfCount.setPrefWidth(50);

        btCountUp = new Button("Count");

        tfLeft = new TextField("---");
        tfLeft.setEditable(false);
        tfLeft.setPrefWidth(50);

        tfRight = new TextField("---");
        tfRight.setEditable(false);
        tfRight.setPrefWidth(50);

        lvResults = new ListView<>();
        lvResults.setPrefWidth(100);

        bind();

        HBox top = new HBox(10, tfCount, btCountUp);
        top.setPadding(new Insets(10));

        HBox mid = new HBox(10, tfLeft, tfRight);
        mid.setPadding(new Insets(10));

        VBox root = new VBox(10, top, mid, lvResults);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root));
        stage.setTitle("Diamond");
        stage.show();
    }

    protected abstract void bind();

}
