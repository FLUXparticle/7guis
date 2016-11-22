package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.fenja.Value;
import de.fluxparticle.sevenguis.gui7cells.formula.Expression;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Operator;
import nz.sodium.Lambda2;

import java.util.function.BiFunction;

import static de.fluxparticle.fenja.Value.constValue;

/**
 * Created by sreinck on 20.11.16.
 */
public class ContentFRP extends ContentReducer<Value<Object>> {

    private final Model model;

    public ContentFRP(Model model) {
        this.model = model;
    }

    @Override
    public Value<Object> visitNumber(double value, Void data) {
        return constValue(value);
    }

    @Override
    public Value<Object> visitText(String text, Void data) {
        return constValue(text);
    }

    @Override
    public Value<Object> visitReference(int row, int column, Void data) {
        return ((CellFRP) model.getCell(row, column)).valueProperty();
    }

    @Override
    public Value<Object> visitOperation(Expression left, Operator operator, Formula right, Void data) {
        Value<Object> leftValue = left.accept(this, null);
        Value<Object> rightValue = right.accept(this, null);

        BiFunction<Double, Double, Double> function = getOperationFunction(operator);
        
        return leftValue.lift(rightValue, lift(function));
    }

    private Lambda2<Object, Object, Object> lift(BiFunction<Double, Double, Double> f) {
        return (a, b) -> reduce(a, b, f);
    }

}
