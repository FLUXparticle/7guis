package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.cell.CellUtil;

/**
 * Created by sreinck on 20.11.16.
 */
public class CellsUtil extends CellsBase {

    public CellsUtil() {
        super(CellUtil::new);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
