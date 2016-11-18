package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.List;

public abstract class Formula {

    public abstract double eval(Model env);

    public abstract List<Cell> getReferences(Model env);

    public abstract String toString();

}
