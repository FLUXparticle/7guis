package de.fluxparticle.sevenguis.gui7cells.formula;

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
    public String toString() {
        return text;
    }

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitText(text, data);
    }

}
