package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.List;

import static java.util.Collections.emptyList;

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

    public Object eval(Model env) {
        return value;
    }

    @Override
    public List<Cell> getReferences(Model env) {
        return emptyList();
    }

}
