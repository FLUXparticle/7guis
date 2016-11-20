package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

/**
 * Created by sreinck on 18.11.16.
 */
public final class Operand extends Expression {

    private final Formula left;

    public Operand(Formula left) {
        this.left = left;
    }

    @Override
    public Formula getLeft() {
        return left;
    }

    public Object evalExpression(Object leftObject, Model env) {
        return leftObject;
    }

    @Override
    public final Object eval(Model env) {
        return left.eval(env);
    }

    @Override
    public List<Cell> getReferences(Model env) {
        return left.getReferences(env);
    }

    @Override
    public String toString() {
        return left.toString();
    }

}
