package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.cell.CellFRP;

/**
 * Created by sreinck on 20.11.16.
 */
public class CellsFRP extends CellsBase {

    public CellsFRP() {
        super(CellFRP::new);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
