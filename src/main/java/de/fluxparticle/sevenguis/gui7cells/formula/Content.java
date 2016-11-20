package de.fluxparticle.sevenguis.gui7cells.formula;

public abstract class Content {

    public abstract String toString();

    public abstract <R, D> R accept(ContentVisitor<R, D> visitor, D data);

}
