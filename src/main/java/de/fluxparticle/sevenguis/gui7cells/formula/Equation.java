package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

/**
 * Created by sreinck on 18.11.16.
 */
public final class Equation extends Formula {

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

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitEquation(expression, data);
    }

}
