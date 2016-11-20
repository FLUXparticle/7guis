package de.fluxparticle.sevenguis.gui7cells.cell

import de.fluxparticle.sevenguis.gui7cells.formula.ContentVisitor
import de.fluxparticle.sevenguis.gui7cells.formula.Expression
import de.fluxparticle.sevenguis.gui7cells.formula.Formula
import de.fluxparticle.sevenguis.gui7cells.formula.Operator

import static de.fluxparticle.fenja.Value.constValue

/**
 * Created by sreinck on 20.11.16.
 */
class ContentFenja implements ContentVisitor {

    def model;

    @Override
    visitEquation(Expression expression, leftObject) {
        expression.accept(this, null)
    }

    @Override
    visitNumber(double value, leftObject) {
        constValue(value)
    }

    @Override
    visitText(String text, leftObject) {
        constValue(text)
    }

    @Override
    visitReference(int row, int column, leftObject) {
        model.getCell(row, column).value
    }

    @Override
    visitOperand(Formula left, leftObject) {
        leftObject ?: left.accept(this, null)
    }

    @Override
    visitOperation(Formula left, Operator operator, Expression right, leftObject) {
        def leftValue = left.accept(this, leftObject).map { it as Double }
        def rightValue = right.getLeft().accept(this, null).map { it as Double }

        def value
        switch (operator) {
            case Operator.ADD:
                value = (leftValue ** rightValue) { a, b -> a + b }
                break;
            case Operator.SUB:
                value = (leftValue ** rightValue) { a, b -> a - b }
                break;
            case Operator.MUL:
                value = (leftValue ** rightValue) { a, b -> a * b }
                break;
            case Operator.DIV:
                value = (leftValue ** rightValue) { a, b -> a / b }
                break;
            case Operator.MOD:
                value = (leftValue ** rightValue) { a, b -> a % b }
                break;
            default:
                throw new RuntimeException();
        }

        right.accept(this, value);
    }

}
