package de.fluxparticle.sevenguis.gui5crud;

import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

public class CrudFX extends CrudBase {

    private FilteredList<Name> dbView;

    private ObservableList<Name> db;

    @Override
    protected void bind() {
        db = FXCollections.observableArrayList(NAMES);
        dbView = db.filtered(item -> true);
        lvEntries.setItems(dbView);

        dbView.predicateProperty().bind( EasyBind.map(tfPrefix.textProperty(), prefix -> item -> item.startsWith(prefix)) );

        MonadicBinding<Name> fullname = EasyBind.combine(tfName.textProperty(), tfSurname.textProperty(), Name::new);
        MonadicBinding<Integer> selectedSourceIndex = EasyBind.map(lvEntries.getSelectionModel().selectedIndexProperty().asObject(), idx -> dbView.getSourceIndex(idx));
        btCreate.setOnAction( e -> db.add(fullname.get()) );
        btDelete.setOnAction( e -> db.remove(selectedSourceIndex.get().intValue()) );
        btUpdate.setOnAction( e -> db.set(selectedSourceIndex.get(), fullname.get()) );

        ObjectExpression<Name> selectedItem = lvEntries.getSelectionModel().selectedItemProperty();
        btDelete.disableProperty().bind( selectedItem.isNull() );
        btUpdate.disableProperty().bind( selectedItem.isNull() );
    }

    public static void main(String[] args) {
        launch(args);
    }

}