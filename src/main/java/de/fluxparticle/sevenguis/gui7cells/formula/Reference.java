package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Created by sreinck on 24.02.16.
 */
public final class Reference extends Formula {

    private final int row;

    private final int column;

    public Reference(String name) {
        row = Integer.parseInt(name.substring(1))-1;
        column = name.charAt(0) - 'A';
    }

    public Reference(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return Character.toString((char) ('A' + column)) + (row + 1);
    }

    public Object eval(Model env) {
        return env.getCell(row, column).getValue();
    }

    public List<Cell> getReferences(Model env) {
        return singletonList(env.getCell(row, column));
    }

}
