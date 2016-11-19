package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;
import de.fluxparticle.sevenguis.gui7cells.cell.CellFactory;

public class Model {

    private final CellFactory factory;

    private final Cell[][] cells;

    public Model(int height, int width, CellFactory factory) {
        this.factory = factory;
        cells = new Cell[height][width];
    }

    public Cell getCell(int row, int column) {
        Cell cell = cells[row][column];
        if (cell == null) {
            cell = factory.createCell();
            cells[row][column] = cell;
        }
        return cell;
    }

}
