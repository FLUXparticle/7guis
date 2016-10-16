package de.fluxparticle.sevenguis.gui7cells.parser;

import de.fluxparticle.sevenguis.gui7cells.formula.*;
import de.fluxparticle.sevenguis.gui7cells.formula.Number;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private static Parser instance = new Parser();
    private static Tokenizer tokenizer;
    static {
        tokenizer = new Tokenizer();
        tokenizer.add("[a-zA-Z_]\\d+", Token.CELL);
        tokenizer.add("[a-zA-Z_]\\w*", Token.IDENT);
        tokenizer.add("-?\\d+(\\.\\d*)?", Token.DECIMAL);
        tokenizer.add("=", Token.EQUALS);
        tokenizer.add(",", Token.COMMA);
        tokenizer.add(":", Token.COLON);
        tokenizer.add("\\(", Token.OPEN_BRACKET);
        tokenizer.add("\\)", Token.CLOSE_BRACKET);
    }

    public static Formula parse(String formulaString) {
        return instance.parseFormula(formulaString);
    }


    String formulaString;
    LinkedList<Token> tokens;
    Token lookahead;

    private Parser() {}

    private Formula parseFormula(String formulaString) {
        this.formulaString = formulaString;

        try {
            tokenizer.tokenize(formulaString.replaceAll("\\s+",""));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        this.tokens = tokenizer.getTokens();
        if (tokens.isEmpty()) return Formula.EMPTY;
        lookahead = this.tokens.getFirst();

        return formula();
    }

    private Formula formula() {
        switch(lookahead.token) {
            case Token.DECIMAL:
                String n = lookahead.sequence;
                nextToken();
                return new Number(Double.parseDouble(n));
            case Token.EQUALS:
                nextToken();
                return expression();
            case Token.EPSILON:
                return Formula.EMPTY;
            default:
                return new Textual(formulaString);
        }
    }

    private Formula expression() {
        switch(lookahead.token) {
            case Token.CELL:
                int c = lookahead.sequence.charAt(0) - 'A';
                int r = Integer.parseInt(lookahead.sequence.substring(1))-1;
                nextToken();
                if (lookahead.token == Token.COLON) { // Range
                    nextToken();
                    if (lookahead.token == Token.CELL) {
                        int c2 = lookahead.sequence.charAt(0) - 'A';
                        int r2 = Integer.parseInt(lookahead.sequence.substring(1))-1;
                        nextToken();
                        return new Range(new Coord(r, c), new Coord(r2, c2));
                    } else {
                        throw new ParseException("Incorrect Range: " + lookahead.sequence);
                    }
                } else {
                    return new Coord(r, c);
                }
            case Token.DECIMAL:
                Double d = Double.parseDouble(lookahead.sequence);
                nextToken();
                return new Number(d);
            case Token.IDENT:
                return application();
            default:
                throw new ParseException("Incorrect Expression: " + lookahead.sequence);
        }
    }

    private Formula application() {
        String opName = lookahead.sequence;
        nextToken();
        if (lookahead.token != Token.OPEN_BRACKET)
            throw new ParseException("No opening bracket: " + opName);
        nextToken();
        List<Formula> args = new ArrayList<Formula>();
        while (true) {
            if (lookahead.token == Token.EPSILON)
                throw new ParseException("No closing bracket");
            args.add(expression());
            if (lookahead.token == Token.COMMA) nextToken();
            if (lookahead.token == Token.CLOSE_BRACKET)
                return new Application(opName, args);
        }
    }

    private void nextToken() {
        tokens.pop();
        if (tokens.isEmpty()) lookahead = new Token(Token.EPSILON, "");
        else lookahead = tokens.getFirst();
    }

}

