package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreinck on 24.02.16.
 */
public class Range extends Formula {

    private final Coord coord1;

    private final Coord coord2;

    public Range(Coord coord1, Coord coord2) {
        this.coord1 = coord1;
        this.coord2 = coord2;
    }

    public String toString() {
        return String.valueOf(coord1) + ":" + String.valueOf(coord2);
    }

    public double eval(Model env) {
        throw new RuntimeException("Range cannot be evaluated!");
    }

    public List<Cell> getReferences(Model env) {
        List<Cell> result = new ArrayList<>();
        for (int r = coord1.row; r <= coord2.row; r++) {
            for (int c = coord1.column; c <= coord2.column; c++) {
                result.add(env.getCell(r, c));
            }
        }
        return result;
    }

}
