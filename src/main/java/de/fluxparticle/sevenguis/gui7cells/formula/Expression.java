package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 18.11.16.
 */
public abstract class Expression extends Formula {

    public abstract Formula getLeft();

    public abstract Object evalExpression(Object leftObject, Model env);

}
