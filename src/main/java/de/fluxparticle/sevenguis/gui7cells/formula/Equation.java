package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.List;

/**
 * Created by sreinck on 18.11.16.
 */
public class Equation extends Formula {

    private final Expression expression;

    public Equation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object eval(Model env) {
        return expression.eval(env);
    }

    @Override
    public List<Cell> getReferences(Model env) {
        return expression.getReferences(env);
    }

    @Override
    public String toString() {
        return "=" + expression.toString();
    }

}
