package de.fluxparticle.sevenguis.gui7cells.formula;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sreinck on 16.10.16.
 */
public class Cell extends Observable implements Observer {

    private final Reference reference;

    private final StringProperty text = new SimpleStringProperty();

    private final Model model;

    private Formula formula = null;

    private double value = 0;

    public Cell(Reference reference, Model model) {
        this.reference = reference;
        this.model = model;
    }

    public String toString() {
/*
        if (formula instanceof Textual) {
            Textual textual = (Textual) formula;
            return textual.value;
        }
*/
        return String.valueOf(value);
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        for (Cell cell : this.formula.getReferences(model)) {
            cell.deleteObserver(this);
        }
        this.formula = formula;
        for (Cell cell : this.formula.getReferences(model)) {
            cell.addObserver(this);
        }
        update(null, null);
        updateText();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        System.out.printf("add %s -> %s%n", this.reference, ((Cell) o).reference);
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        System.out.printf("del %s -> %s%n", this.reference, ((Cell) o).reference);
        super.deleteObserver(o);
    }

    public double getValue() {
        return value;
    }

    private void setValue(double value) {
        if (!(this.value == value || Double.isNaN(this.value) && Double.isNaN(value))) {
            this.value = value;
            updateText();
            setChanged();
            notifyObservers();
        }
    }

    private void updateText() {
        String display = this.toString();
        text.setValue(display);
    }

    @Override
    public void update(Observable o, Object arg) {
        double value = formula.eval(model);
        setValue(value);
    }

    public StringProperty textProperty() {
        return text;
    }

}
