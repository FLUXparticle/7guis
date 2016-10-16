package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.Collections;
import java.util.List;

public abstract class Formula {

    public static final Formula EMPTY = new Textual("");

    public double eval(Model env) {
        return 0.0;
    }

    public List<Cell> getReferences(Model env) {
        return Collections.emptyList();
    }

}

