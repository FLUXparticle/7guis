package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 18.11.16.
 */
public final class Equation extends Formula {

    private final Expression expression;

    public Equation(Expression expression) {
        this.expression = expression;
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
