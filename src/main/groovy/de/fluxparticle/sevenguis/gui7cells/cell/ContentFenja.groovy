package de.fluxparticle.sevenguis.gui7cells.cell

import de.fluxparticle.sevenguis.gui7cells.formula.Expression
import de.fluxparticle.sevenguis.gui7cells.formula.Formula
import de.fluxparticle.sevenguis.gui7cells.formula.Operator

import java.util.function.BiFunction

import static de.fluxparticle.fenja.Value.constValue

/**
 * Created by sreinck on 20.11.16.
 */
class ContentFenja extends ContentReducer<Object> {

    def model;

    @Override
    visitNumber(double value, Void data) {
        constValue(value)
    }

    @Override
    visitText(String text, Void data) {
        constValue(text)
    }

    @Override
    visitReference(int row, int column, Void data) {
        model.getCell(row, column).value
    }

    @Override
    visitOperation(Expression left, Operator operator, Formula right, Void data) {
        def leftValue = left.accept(this, null)
        def rightValue = right.accept(this, null)

        def function = getOperationFunction(operator)

        (leftValue ** rightValue) lift(function)
    }

    static def lift(BiFunction c) {
        { a, b -> reduce(a, b, c) }
    }

}
