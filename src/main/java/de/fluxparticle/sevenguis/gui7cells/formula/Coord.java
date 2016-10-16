package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.Collections;
import java.util.List;

/**
 * Created by sreinck on 24.02.16.
 */
public class Coord extends Formula {

    final int row;

    final int column;

    public Coord(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return Character.toString((char) ('A' + column)) + (row + 1);
    }

    public double eval(Model env) {
        return env.getCell(row, column).getValue();
    }

    public List<Cell> getReferences(Model env) {
        return Collections.singletonList(env.getCell(row, column));
    }

}
