package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;

/**
 * Created by sreinck on 16.10.16.
 */
public class CellInfo {

    private final Content content;

    private final String text;

    public CellInfo(Content content, String text) {
        this.content = content;
        this.text = text;
    }

    public Content getContent() {
        return content;
    }

    public String getText() {
        return text;
    }

}
