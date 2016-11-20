package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.sevenguis.gui7cells.cell.ContentEvaluator;
import de.fluxparticle.syntax.config.EnumSyntaxConfig;
import de.fluxparticle.syntax.lexer.ParserException;
import de.fluxparticle.syntax.parser.Lexer;
import de.fluxparticle.syntax.parser.Parser;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * Created by sreinck on 18.11.16.
 */
public class FormulaSyntaxTest {

    private static final EnumSyntaxConfig<FormulaSyntax> CONFIG = new EnumSyntaxConfig<>(FormulaSyntax.class);

    private static final Parser PARSER = CONFIG.getParser(FormulaSyntax.FORMULA);

    @Test
    public void number() throws ParserException {
        testParse("4.2");
    }

    @Test
    public void equation() throws ParserException {
        testParse("=4.2");
    }

    @Test
    public void reference() throws ParserException {
        testParse("=C4");
    }

    @Test
    public void addition() throws ParserException {
        testParse("=C4+1.0");
    }

    @Test
    public void multiplication() throws ParserException {
        testCalc("=2*3", 6);
    }

    @Test
    public void multiplicationLast() throws ParserException {
        testCalc("=1+2*3", 7);
    }

    @Test
    public void multiplicationFirst() throws ParserException {
        testCalc("=2*3+4", 10);
    }

    @Test
    public void subtraction() throws ParserException {
        testCalc("=1-2-3", -4);
    }

    @Test
    public void divisionLast() throws ParserException {
        testCalc("=1-6/2", -2);
    }

    private static void testParse(String input) throws ParserException {
        Content check = parseFormula(input);
        assertEquals(input, check.toString());
    }

    private static void testCalc(String input, double expected) throws ParserException {
        Formula check = parseFormula(input);
        ContentEvaluator evaluator = new ContentEvaluator(null);
        double actual = (Double) check.accept(evaluator, null);
        assertEquals(expected, actual, 0.0);
    }

    private static Formula parseFormula(String input) throws ParserException {
        StringReader reader = new StringReader(input);
        Lexer l = CONFIG.newLexer(reader);
        return (Formula) PARSER.check(l);
    }

}
