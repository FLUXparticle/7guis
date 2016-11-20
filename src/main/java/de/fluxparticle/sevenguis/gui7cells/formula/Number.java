package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 24.02.16.
 */
public final class Number extends Formula {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitNumber(value, data);
    }

}
