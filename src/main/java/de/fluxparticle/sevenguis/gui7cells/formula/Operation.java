package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 18.11.16.
 */
public final class Operation extends Expression {

    private final Expression left;

    private final Operator operator;

    private final Formula right;

    public Operation(Expression left, Operator operator, Formula right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append(operator);
        sb.append(right);
        return sb.toString();
    }

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitOperation(left, operator, right, data);
    }

}
