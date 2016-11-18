package de.fluxparticle.sevenguis.gui7cells.formula;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {

    private final Cell[][] cells;

    public Model(int height, int width) {
        cells = new Cell[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Reference reference = new Reference(r, c);
                cells[r][c] = new Cell(reference, this);
            }
        }
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    public ObservableList<ObservableList<Cell>> getCellsAsObservableList() {
        ObservableList<ObservableList<Cell>> cs = FXCollections.observableArrayList();
        for (int i = 0; i < cells.length; i++) {
            cs.add(FXCollections.observableArrayList());
            for (int j = 0; j < cells[i].length; j++) {
                cs.get(i).add(cells[i][j]);
            }
        }
        return cs;
    }

}
