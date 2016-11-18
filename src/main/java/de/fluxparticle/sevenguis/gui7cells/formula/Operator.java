package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 18.11.16.
 */
public enum Operator {

    ADD('+'), SUB('-'), MUL('*'), DIV('/'), MOD('%');

    private final char symbol;

    Operator(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }

    public static Operator fromString(String s) {
        for (Operator operator : values()) {
            if (operator.toString().equals(s)) {
                return operator;
            }
        }

        throw new IllegalArgumentException();
    }

}
