package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.*;

import java.util.List;

/**
 * Created by sreinck on 20.11.16.
 */
public class ReferenceCollector implements ContentVisitor<List<Cell>, List<Cell>> {

    private final Model model;

    public ReferenceCollector(Model model) {
        this.model = model;
    }

    @Override
    public List<Cell> visitEquation(Expression expression, List<Cell> list) {
        return expression.accept(this, list);
    }

    @Override
    public List<Cell> visitNumber(double value, List<Cell> list) {
        return list;
    }

    @Override
    public List<Cell> visitText(String text, List<Cell> list) {
        return list;
    }

    @Override
    public List<Cell> visitReference(int row, int column, List<Cell> list) {
        Cell cell = model.getCell(row, column);
        list.add(cell);
        return list;
    }

    @Override
    public List<Cell> visitOperand(Formula left, List<Cell> list) {
        return left.accept(this, list);
    }

    @Override
    public List<Cell> visitOperation(Formula left, Operator operator, Expression right, List<Cell> list) {
        list = left.accept(this, list);
        list = right.accept(this, list);
        return list;
    }

}
