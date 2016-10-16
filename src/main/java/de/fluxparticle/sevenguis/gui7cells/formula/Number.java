package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 24.02.16.
 */
public class Number extends Formula {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public double eval(Model env) {
        return value;
    }

}
