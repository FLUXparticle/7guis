package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by sreinck on 16.10.16.
 */
public abstract class Cell {

    private final StringProperty text = new SimpleStringProperty("---");

    public abstract Content getContent();

    public abstract void setContent(Content content);

    protected void updateValue(Object value) {
        String newText = String.valueOf(value);
        text.setValue(newText);
    }

    public StringProperty textProperty() {
        return text;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

}
