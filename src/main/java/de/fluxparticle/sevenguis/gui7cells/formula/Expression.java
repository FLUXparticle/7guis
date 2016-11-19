package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

/**
 * Created by sreinck on 18.11.16.
 */
public class Expression extends Formula {

    private final Formula info;

    public Expression(Formula info) {
        this.info = info;
    }

    public Formula getInfo() {
        return info;
    }

    public Object evalExpression(Object leftObject, Model env) {
        return leftObject;
    }

    @Override
    public final Object eval(Model env) {
        Object leftObject = info.eval(env);
        return evalExpression(leftObject, env);
    }

    @Override
    public List<Cell> getReferences(Model env) {
        return info.getReferences(env);
    }

    @Override
    public String toString() {
        return info.toString();
    }

}
