package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 20.11.16.
 */
public interface ContentVisitor<R, D> {

    R visitEquation(Expression expression, D data);

    R visitNumber(double value, D data);

    R visitText(String text, D data);

    R visitReference(int row, int column, D data);

    R visitOperand(Formula left, D data);

    R visitOperation(Expression left, Operator operator, Formula right, D data);

}
