package de.fluxparticle.sevenguis.gui7cells.parser;

/**
 * Created by sreinck on 24.02.16.
 */
class Token {

    public static final int EPSILON = 0;
    public static final int EQUALS = 1;
    public static final int IDENT = 2;
    public static final int DECIMAL = 3;
    public static final int OPEN_BRACKET = 4;
    public static final int CLOSE_BRACKET = 5;
    public static final int COMMA = 6;
    public static final int COLON = 7;
    public static final int CELL = 8;

    public final int token;
    public final String sequence;

    public Token(int token, String sequence) {
        this.token = token;
        this.sequence = sequence;
    }

}
