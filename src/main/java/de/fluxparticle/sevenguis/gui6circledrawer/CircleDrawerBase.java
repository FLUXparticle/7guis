package de.fluxparticle.sevenguis.gui6circledrawer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by sreinck on 17.06.16.
 */
abstract class CircleDrawerBase extends Application {

    protected Button btUndo;

    protected Button btRedo;

    @Override
    public final void start(Stage stage) throws Exception {
        btUndo = new Button("Undo");
        btRedo = new Button("Redo");

        CircleDrawerCanvasBase canvas = createCanvas();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        HBox top = new HBox(10, btUndo, btRedo);
        top.setPadding(new Insets(0, 0, 10, 0));
        root.setTop(top);
        root.setCenter(canvas);

        stage.setOnCloseRequest(e -> Platform.exit());

        stage.setScene(new Scene(root));
        stage.setTitle("Circle Drawer");
        stage.show();
    }

    abstract CircleDrawerCanvasBase createCanvas();

}
