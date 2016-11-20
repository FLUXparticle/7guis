package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;

import java.util.List;

public abstract class Content {

    public abstract Object eval(Model env);

    public abstract List<Cell> getReferences(Model env);

    public abstract String toString();

    public abstract <R, D> R accept(ContentVisitor<R, D> visitor, D data);

}
