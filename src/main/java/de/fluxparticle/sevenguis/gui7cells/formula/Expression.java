package de.fluxparticle.sevenguis.gui7cells.formula;

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

    public double evalExpression(double leftValue, Model env) {
        return leftValue;
    }

    @Override
    public final double eval(Model env) {
        double leftValue = info.eval(env);
        return evalExpression(leftValue, env);
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
