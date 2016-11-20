package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.*;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import static javafx.beans.binding.DoubleExpression.doubleExpression;
import static org.fxmisc.easybind.EasyBind.combine;
import static org.fxmisc.easybind.EasyBind.map;


/**
 * Created by sreinck on 20.11.16.
 */
public class ContentObservable implements ContentVisitor<ObservableValue<?>, ObservableValue<?>> {

    private final Model model;

    public ContentObservable(Model model) {
        this.model = model;
    }

    @Override
    public ObservableValue<?> visitEquation(Expression expression, ObservableValue<?> leftObservable) {
        return expression.accept(this, null);
    }

    @Override
    public ObservableValue<?> visitNumber(double value, ObservableValue<?> leftObservable) {
        return new SimpleDoubleProperty(value);
    }

    @Override
    public ObservableValue<?> visitText(String text, ObservableValue<?> leftObservable) {
        return new SimpleStringProperty(text);
    }

    @Override
    public ObservableValue<?> visitReference(int row, int column, ObservableValue<?> leftObservable) {
        return ((CellFX) model.getCell(row, column)).valueProperty();
    }

    @Override
    public ObservableValue<?> visitOperand(Formula left, ObservableValue<?> leftObservable) {
        if (leftObservable != null) {
            return leftObservable;
        } else {
            return left.accept(this, null);
        }
    }

    @Override
    public ObservableValue<?> visitOperation(Formula left, Operator operator, Expression right, ObservableValue<?> leftObservable) {
        DoubleExpression leftValue = doubleExpression(map(left.accept(this, leftObservable), o -> (Double) o));
        DoubleExpression rightValue = doubleExpression(map(right.getLeft().accept(this, null), o -> (Double) o));

        ObservableValue<?> value;
        switch (operator) {
            case ADD:
                value = leftValue.add(rightValue);
                break;
            case SUB:
                value = leftValue.subtract(rightValue);
                break;
            case MUL:
                value = leftValue.multiply(rightValue);
                break;
            case DIV:
                value = leftValue.divide(rightValue);
                break;
            case MOD:
                value = combine(leftValue.asObject(), rightValue.asObject(), (l, r) -> l % r);
                break;
            default:
                throw new RuntimeException();
        }

        return right.accept(this, value);
    }
    
}
