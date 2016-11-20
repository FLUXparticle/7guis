package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Text;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

/**
 * Created by sreinck on 19.11.16.
 */
public class CellFX extends Cell implements InvalidationListener {

    private final Model model;

    private final ObjectProperty<Object> value = new SimpleObjectProperty<>();

    private Content content = Text.EMPTY;

    public CellFX(Model model) {
        this.model = model;
        setValue("");
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        ReferenceCollector collector = new ReferenceCollector(model);
        for (Cell cell : this.content.accept(collector, new ArrayList<>())) {
            ((CellFX) cell).value.removeListener(this);
        }
        this.content = content;
        for (Cell cell : this.content.accept(collector, new ArrayList<>())) {
            ((CellFX) cell).value.addListener(this);
        }
        invalidated(null);
    }

    public Object getValue() {
        return value.get();
    }

    protected void setValue(Object value) {
        this.value.set(value);
        super.setValue(value);
    }

    @Override
    public void invalidated(Observable observable) {
        Object value;

        try {
            ContentEvaluatorFX evaluator = new ContentEvaluatorFX(model);
            value = content.accept(evaluator, null);
        } catch (Exception e) {
            value = "Error!";
        }

        setValue(value);
    }

}
