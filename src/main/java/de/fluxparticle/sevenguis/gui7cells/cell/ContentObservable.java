package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Expression;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Operator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import java.util.function.BiFunction;

import static org.fxmisc.easybind.EasyBind.combine;


/**
 * Created by sreinck on 20.11.16.
 */
public class ContentObservable extends ContentReducer<ObservableValue<Object>> {

    private final Model model;

    public ContentObservable(Model model) {
        this.model = model;
    }

    @Override
    public ObservableValue<Object> visitNumber(double value, Void data) {
        return new SimpleObjectProperty<>(value);
    }

    @Override
    public ObservableValue<Object> visitText(String text, Void data) {
        return new SimpleObjectProperty<>(text);
    }

    @Override
    public ObservableValue<Object> visitReference(int row, int column, Void data) {
        return ((CellFX) model.getCell(row, column)).valueProperty();
    }

    @Override
    public ObservableValue<Object> visitOperation(Expression left, Operator operator, Formula right, Void data) {
        ObservableValue<Object> leftValue = left.accept(this, null);
        ObservableValue<Object> rightValue = right.accept(this, null);

        BiFunction<Double, Double, Double> function = getOperationFunction(operator);

        return combine(leftValue, rightValue, lift(function));
    }

    private BiFunction<Object, Object, Object> lift(BiFunction<Double, Double, Double> f) {
        return (a, b) -> reduce(a, b, f);
    }

}
