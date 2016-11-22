package de.fluxparticle.sevenguis.gui7cells.cell

import de.fluxparticle.sevenguis.gui7cells.formula.Expression
import de.fluxparticle.sevenguis.gui7cells.formula.Formula
import de.fluxparticle.sevenguis.gui7cells.formula.Operator

import java.util.function.BiFunction

import static de.fluxparticle.fenja.Value.constValue

/**
 * Created by sreinck on 20.11.16.
 */
class ContentFenja extends ContentReducer {

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
        def leftValue = left.accept(this, leftObject)
        def rightValue = right.getLeft().accept(this, null)

        def function = getOperationFunction(operator)

        def value = (leftValue ** rightValue) lift(function)

        right.accept(this, value);
    }

    def lift(BiFunction c) {
        { a, b -> reduce(a, b, c) }
    }

}
