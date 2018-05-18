package de.fluxparticle.sevenguis.gui7cells

import de.fluxparticle.sevenguis.gui7cells.cell.CellFenja

/**
 * Created by sreinck on 20.11.16.
 */
class CellsGroovy extends CellsBase {

    CellsGroovy() {
        super(CellFenja.metaClass.&invokeConstructor)
    }

    public static void main(String... args) {
        launch(CellsGroovy, args)
    }

}
