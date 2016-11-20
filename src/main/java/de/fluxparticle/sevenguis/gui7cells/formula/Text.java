package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Created by sreinck on 19.11.16.
 */
public final class Text extends Content {

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

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitText(text, data);
    }

}
