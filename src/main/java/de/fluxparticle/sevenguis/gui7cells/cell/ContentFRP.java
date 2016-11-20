package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.fenja.Value;
import de.fluxparticle.sevenguis.gui7cells.formula.*;
import nz.sodium.Lambda2;

import static de.fluxparticle.fenja.Value.constValue;

/**
 * Created by sreinck on 20.11.16.
 */
public class ContentFRP implements ContentVisitor<Value<Object>,Value<Object>> {

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
        Value<Double> leftValue = left.accept(this, leftObject).map(o -> (Double) o);
        Value<Double> rightValue = right.getLeft().accept(this, null).map(o -> (Double) o);

        Lambda2<Double, Double, Object> lambda;
        switch (operator) {
            case ADD:
                lambda = (a, b) -> a + b;
                break;
            case SUB:
                lambda = (a, b) -> a - b;
                break;
            case MUL:
                lambda = (a, b) -> a * b;
                break;
            case DIV:
                lambda = (a, b) -> a / b;
                break;
            case MOD:
                lambda = (a, b) -> a % b;
                break;
            default:
                throw new RuntimeException();
        }

        Value<Object> value = leftValue.lift(rightValue, lambda);

        return right.accept(this, value);
    }

}
