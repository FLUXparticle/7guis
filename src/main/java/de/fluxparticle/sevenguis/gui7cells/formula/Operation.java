package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

/**
 * Created by sreinck on 18.11.16.
 */
public final class Operation extends Expression {

    private final Formula left;

    private final Operator operator;

    private final Expression right;

    public Operation(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Formula getLeft() {
        return left;
    }

    @Override
    public Object evalExpression(Object leftObject, Model env) {
        double leftValue = (double) leftObject;
        double rightValue = (double) right.getLeft().eval(env);

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

        return right.evalExpression(value, env);
    }

    @Override
    public Object eval(Model env) {
        Object leftObject = left.eval(env);
        return evalExpression(leftObject, env);
    }

    @Override
    public List<Cell> getReferences(Model env) {
        List<Cell> left = this.left.getReferences(env);
        List<Cell> right = this.right.getReferences(env);
        return concat(left.stream(), right.stream()).collect(toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append(operator);
        sb.append(right);
        return sb.toString();
    }

}
