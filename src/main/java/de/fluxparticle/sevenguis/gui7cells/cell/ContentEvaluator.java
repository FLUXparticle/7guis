package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Expression;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Operator;

import java.util.function.BiFunction;

/**
 * Created by sreinck on 20.11.16.
 */
public class ContentEvaluator extends ContentReducer<Object> {

    private final Model model;

    public ContentEvaluator(Model model) {
        this.model = model;
    }

    @Override
    public Object visitNumber(double value, Void data) {
        return value;
    }

    @Override
    public Object visitText(String text, Void data) {
        return text;
    }

    @Override
    public Object visitReference(int row, int column, Void data) {
        return ((CellUtil) model.getCell(row, column)).getValue();
    }

    @Override
    public Object visitOperation(Expression left, Operator operator, Formula right, Void data) {
        Object leftValue = left.accept(this, null);
        Object rightValue = right.accept(this, null);

        BiFunction<Double, Double, Double> function = getOperationFunction(operator);

        return reduce(leftValue, rightValue, function);
    }

}
