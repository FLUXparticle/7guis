package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 18.11.16.
 */
public final class Operand extends Expression {

    private final Formula left;

    public Operand(Formula left) {
        this.left = left;
    }

    @Override
    public String toString() {
        return left.toString();
    }

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitOperand(left, data);
    }

}
