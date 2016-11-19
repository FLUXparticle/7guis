package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Text;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by sreinck on 16.10.16.
 */
public class Cell implements InvalidationListener {

    private final Model model;

    private Content content = Text.EMPTY;

    private final ObjectProperty<Object> value = new SimpleObjectProperty<>();

    private final StringProperty text = new SimpleStringProperty();

    public Cell(Model model) {
        this.model = model;
        setValue("");
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        for (Cell cell : this.content.getReferences(model)) {
            cell.valueProperty().removeListener(this);
        }
        this.content = content;
        for (Cell cell : this.content.getReferences(model)) {
            cell.valueProperty().addListener(this);
        }
        invalidated(null);
    }

    public Object getValue() {
        return value.get();
    }

    public ObjectProperty<Object> valueProperty() {
        return value;
    }

    public void setValue(Object value) {
        this.value.set(value);
        String newText = String.valueOf(value);
        text.setValue(newText);
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    @Override
    public void invalidated(Observable observable) {
        Object value;

        try {
            value = content.eval(model);
        } catch (Exception e) {
            value = "Error!";
        }

        setValue(value);
    }

}
