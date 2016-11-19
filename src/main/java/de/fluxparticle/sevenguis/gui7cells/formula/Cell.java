package de.fluxparticle.sevenguis.gui7cells.formula;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sreinck on 16.10.16.
 */
public class Cell extends Observable implements Observer {

    private final Model model;

    private final StringProperty text = new SimpleStringProperty();

    private Content content = Text.EMPTY;

    private Object value = 0.0;

    public Cell(Model model) {
        this.model = model;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        for (Cell cell : this.content.getReferences(model)) {
            cell.deleteObserver(this);
        }
        this.content = content;
        for (Cell cell : this.content.getReferences(model)) {
            cell.addObserver(this);
        }
        update(null, null);
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }

    public Object getValue() {
        return value;
    }

    private void setValue(Object value) {
        if (this.value != value) {
            this.value = value;
            setChanged();
            notifyObservers();
        }
        updateText();
    }

    private void updateText() {
        String display = this.toString();
        text.setValue(display);
    }

    @Override
    public void update(Observable o, Object arg) {
        Object value;

        try {
            value = content.eval(model);
        } catch (Exception e) {
            value = "Error!";
        }

        setValue(value);
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

}
