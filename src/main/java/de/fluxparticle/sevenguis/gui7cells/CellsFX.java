package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.cell.CellFX;

/**
 * Created by sreinck on 20.11.16.
 */
public class CellsFX extends CellsBase {

    public CellsFX() {
        super(CellFX::new);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
