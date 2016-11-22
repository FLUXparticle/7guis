package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.ContentVisitor;
import de.fluxparticle.sevenguis.gui7cells.formula.Expression;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Operator;

import java.util.function.BiFunction;

/**
 * Created by sreinck on 21.11.16.
 */
public abstract class ContentReducer<R> implements ContentVisitor<R, Void> {

    @Override
    public final R visitEquation(Expression expression, Void data) {
        return expression.accept(this, null);
    }

    @Override
    public final R visitOperand(Formula left, Void data) {
        return left.accept(this, null);
    }

    static BiFunction<Double, Double, Double> getOperationFunction(Operator operator) {
        switch (operator) {
            case ADD:
                return (a, b) -> a + b;
            case SUB:
                return (a, b) -> a - b;
            case MUL:
                return (a, b) -> a * b;
            case DIV:
                return (a, b) -> a / b;
            case MOD:
                return (a, b) -> a % b;
        }

        throw new IllegalArgumentException();
    }

    static Object reduce(Object a, Object b, BiFunction<Double, Double, Double> f) {
        try {
            return f.apply((Double) a, (Double) b);
        } catch (Exception e) {
            return "Error!";
        }
    }

}
