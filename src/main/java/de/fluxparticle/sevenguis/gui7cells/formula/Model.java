package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;
import de.fluxparticle.sevenguis.gui7cells.cell.CellFactory;

public class Model {

    private final Cell[][] cells;

    public Model(int rows, int cols, CellFactory factory) {
        cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = factory.createCell(this);
            }
        }
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

}
