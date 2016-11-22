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
    public ObservableValue<Object> visitEquation(Expression expression, ObservableValue<Object> leftObservable) {
        return expression.accept(this, null);
    }

    @Override
    public ObservableValue<Object> visitNumber(double value, ObservableValue<Object> leftObservable) {
        return new SimpleObjectProperty<>(value);
    }

    @Override
    public ObservableValue<Object> visitText(String text, ObservableValue<Object> leftObservable) {
        return new SimpleObjectProperty<>(text);
    }

    @Override
    public ObservableValue<Object> visitReference(int row, int column, ObservableValue<Object> leftObservable) {
        return ((CellFX) model.getCell(row, column)).valueProperty();
    }

    @Override
    public ObservableValue<Object> visitOperand(Formula left, ObservableValue<Object> leftObservable) {
        if (leftObservable != null) {
            return leftObservable;
        } else {
            return left.accept(this, null);
        }
    }

    @Override
    public ObservableValue<Object> visitOperation(Formula left, Operator operator, Expression right, ObservableValue<Object> leftObservable) {
        ObservableValue<Object> leftValue = left.accept(this, leftObservable);
        ObservableValue<Object> rightValue = right.getLeft().accept(this, null);

        BiFunction<Double, Double, Double> function = getOperationFunction(operator);

        ObservableValue<Object> value = combine(leftValue, rightValue, lift(function));

        return right.accept(this, value);
    }

    private BiFunction<Object, Object, Object> lift(BiFunction<Double, Double, Double> f) {
        return (a, b) -> reduce(a, b, f);
    }

}
