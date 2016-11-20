package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Text;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sreinck on 19.11.16.
 */
public class CellUtil extends Cell implements Observer {

    private static class ObservableObject extends Observable {

        private Object o;

        Object get() {
            return o;
        }

        void set(Object o) {
            this.o = o;
            setChanged();
            notifyObservers();
        }

    };

    private final ContentEvaluator evaluator;

    private final ReferenceCollector collector;

    private final ObservableObject value = new ObservableObject();

    private Content content;

    public CellUtil(Model model) {
        evaluator = new ContentEvaluator(model);
        collector = new ReferenceCollector(model);

        content = Text.EMPTY;
        update(null, null);
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        for (Cell cell : this.content.accept(collector, new ArrayList<>())) {
            ((CellUtil) cell).value.deleteObserver(this);
        }
        this.content = content;
        for (Cell cell : this.content.accept(collector, new ArrayList<>())) {
            ((CellUtil) cell).value.addObserver(this);
        }
        update(null, null);
    }

    public Object getValue() {
        return value.get();
    }

    @Override
    protected void updateValue(Object value) {
        this.value.set(value);
        super.updateValue(value);
    }

    @Override
    public void update(Observable o, Object arg) {
        Object value;

        try {
            value = content.accept(evaluator, null);
        } catch (Exception e) {
            value = "Error!";
        }

        updateValue(value);
    }

}
