package de.fluxparticle.sevenguis.gui5crud;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class CrudBase extends Application {

    protected static Name[] NAMES = {
            new Name("Emil", "Hans"),
            new Name("Musterman", "Max"),
            new Name("Tisch", "Roman")
    };

    protected TextField tfPrefix;

    protected TextField tfName;

    protected TextField tfSurname;

    protected Button btCreate;

    protected Button btUpdate;

    protected Button btDelete;

    protected ListView<Name> lvEntries;

    public final void start(Stage stage) {
        tfPrefix = new TextField();
        tfPrefix.setPrefWidth(250);
        tfName = new TextField();
        tfName.setPrefWidth(100);
        tfSurname = new TextField();
        tfSurname.setPrefWidth(100);
        btCreate = new Button("Create");
        btUpdate = new Button("Update");
        btDelete = new Button("Delete");
        lvEntries = new ListView<>();
        lvEntries.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        bind();

        BorderPane root = new BorderPane();
        root.setPrefSize(400, 400);
        root.setPadding(new Insets(10));
        HBox top = new HBox(10, new Label("Filter prefix: "), tfPrefix);
        top.setPadding(new Insets(0, 0, 10, 0));
        top.setAlignment(Pos.BASELINE_LEFT);
        root.setTop(top);
        root.setCenter(lvEntries);
        GridPane right = new GridPane();
        right.setHgap(10);
        right.setVgap(10);
        right.setPadding(new Insets(0, 0, 0, 10));
        right.addRow(0, new Label("Name: "), tfName);
        right.addRow(1, new Label("Surname: "), tfSurname);
        root.setRight(right);
        HBox bottom = new HBox(10, btCreate, btUpdate, btDelete);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(bottom);

        stage.setScene(new Scene(root));
        stage.setTitle("CRUD");
        stage.show();
    }

    protected abstract void bind();

}

