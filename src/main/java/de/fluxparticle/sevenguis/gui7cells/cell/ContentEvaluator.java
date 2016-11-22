package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Expression;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Operator;

import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

/**
 * Created by sreinck on 20.11.16.
 */
public class ContentEvaluator implements ContentReducer<Object> {

    private final Model model;

    public ContentEvaluator(Model model) {
        this.model = model;
    }

    @Override
    public Object visitEquation(Expression expression, Object leftObject) {
        return expression.accept(this, null);
    }

    @Override
    public Object visitNumber(double value, Object leftObject) {
        return value;
    }

    @Override
    public Object visitText(String text, Object leftObject) {
        return text;
    }

    @Override
    public Object visitReference(int row, int column, Object leftObject) {
        return ((CellUtil) model.getCell(row, column)).getValue();
    }

    @Override
    public Object visitOperand(Formula left, Object leftObject) {
        return ofNullable(leftObject).orElseGet(() -> left.accept(this, null));
    }

    @Override
    public Object visitOperation(Formula left, Operator operator, Expression right, Object leftObject) {
        Object leftValue = left.accept(this, leftObject);
        Object rightValue = right.getLeft().accept(this, null);

        BiFunction<Double, Double, Double> function = getOperationFunction(operator);

        Object value = reduce(leftValue, rightValue, function);

        return right.accept(this, value);
    }

}
