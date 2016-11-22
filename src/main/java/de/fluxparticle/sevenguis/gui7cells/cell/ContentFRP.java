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
    public Value<Object> visitEquation(Expression expression, Value<Object> leftObject) {
        return expression.accept(this, null);
    }

    @Override
    public Value<Object> visitNumber(double value, Value<Object> leftObject) {
        return constValue(value);
    }

    @Override
    public Value<Object> visitText(String text, Value<Object> leftObject) {
        return constValue(text);
    }

    @Override
    public Value<Object> visitReference(int row, int column, Value<Object> leftObject) {
        return ((CellFRP) model.getCell(row, column)).valueProperty();
    }

    @Override
    public Value<Object> visitOperand(Formula left, Value<Object> leftObject) {
        if (leftObject != null) {
            return leftObject;
        } else {
            return left.accept(this, null);
        }
    }

    @Override
    public Value<Object> visitOperation(Formula left, Operator operator, Expression right, Value<Object> leftObject) {
        Value<Object> leftValue = left.accept(this, leftObject);
        Value<Object> rightValue = right.getLeft().accept(this, null);

        BiFunction<Double, Double, Double> function = getOperationFunction(operator);
        
        Value<Object> value = leftValue.lift(rightValue, lift(function));

        return right.accept(this, value);
    }

    private Lambda2<Object, Object, Object> lift(BiFunction<Double, Double, Double> f) {
        return (a, b) -> reduce(a, b, f);
    }

}
