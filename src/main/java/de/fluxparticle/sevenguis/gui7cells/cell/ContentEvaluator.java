package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.*;

import static java.util.Optional.ofNullable;

/**
 * Created by sreinck on 20.11.16.
 */
public class ContentEvaluator implements ContentVisitor<Object, Object> {

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
