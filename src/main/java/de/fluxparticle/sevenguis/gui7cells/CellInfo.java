package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.formula.Formula;

/**
 * Created by sreinck on 16.10.16.
 */
public class CellInfo {

    private final Formula formula;

    private final String text;

    public CellInfo(Formula formula, String text) {
        this.formula = formula;
        this.text = text;
    }

    public Formula getFormula() {
        return formula;
    }

    public String getText() {
        return text;
    }

}
