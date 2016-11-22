package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.ContentVisitor;
import de.fluxparticle.sevenguis.gui7cells.formula.Operator;

import java.util.function.BiFunction;

/**
 * Created by sreinck on 21.11.16.
 */
public abstract class ContentReducer<T> implements ContentVisitor<T, T> {

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
