package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Created by sreinck on 19.11.16.
 */
public class Text extends Content {

    public static final Text EMPTY = new Text("");

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public Object eval(Model env) {
        return text;
    }

    @Override
    public List<Cell> getReferences(Model env) {
        return emptyList();
    }

    @Override
    public String toString() {
        return text;
    }

}
