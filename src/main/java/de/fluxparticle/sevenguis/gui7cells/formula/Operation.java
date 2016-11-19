package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

/**
 * Created by sreinck on 18.11.16.
 */
public class Operation extends Expression {

    private final Operator operator;

    private final Expression tail;

    public Operation(Expression info, Operator operator, Expression tail) {
        super(info);
        this.operator = operator;
        this.tail = tail;
    }

    @Override
    public Object evalExpression(Object leftObject, Model env) {
        double leftValue = (double) leftObject;
        double rightValue = (double) tail.getInfo().eval(env);

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

        return tail.evalExpression(value, env);
    }

    @Override
    public List<Cell> getReferences(Model env) {
        List<Cell> left = super.getReferences(env);
        List<Cell> right = tail.getReferences(env);
        return concat(left.stream(), right.stream()).collect(toList());
    }

    @Override
    public String toString() {
        return super.toString() + operator + tail;
    }

}
