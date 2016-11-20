package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.*;

import static java.util.Optional.ofNullable;

/**
 * Created by sreinck on 20.11.16.
 */
public class ContentEvaluatorFX implements ContentVisitor<Object, Double> {

    private final Model model;

    public ContentEvaluatorFX(Model model) {
        this.model = model;
    }

    @Override
    public Object visitEquation(Expression expression, Double leftObject) {
        return expression.accept(this, null);
    }

    @Override
    public Object visitNumber(double value, Double leftObject) {
        return value;
    }

    @Override
    public Object visitText(String text, Double leftObject) {
        return text;
    }

    @Override
    public Object visitReference(int row, int column, Double leftObject) {
        return model.getCell(row, column).getValue();
    }

    @Override
    public Object visitOperand(Formula left, Double leftObject) {
        return ofNullable((Object) leftObject).orElseGet(() -> left.accept(this, null));
    }

    @Override
    public Object visitOperation(Formula left, Operator operator, Expression right, Double leftObject) {
        double leftValue = (double) left.accept(this, leftObject);
        double rightValue = (double) right.getLeft().accept(this, null);

        double value;
        switch (operator) {
            case ADD:
                value = leftValue + rightValue;
                break;
            case SUB:
                value = leftValue - rightValue;
                break;
            case MUL:
                value = leftValue * rightValue;
                break;
            case DIV:
                value = leftValue / rightValue;
                break;
            case MOD:
                value = leftValue % rightValue;
                break;
            default:
                throw new RuntimeException();
        }

        return right.accept(this, value);
    }

}
