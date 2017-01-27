package de.fluxparticle.sevenguis.gui7cells.formula;

import de.fluxparticle.syntax.parser.ParserHelper;
import de.fluxparticle.syntax.structure.Rule;
import de.fluxparticle.syntax.structure.RuleType;
import de.fluxparticle.syntax.structure.SimpleRule;
import de.fluxparticle.syntax.structure.SingleElement;

import java.util.List;
import java.util.function.Function;

import static de.fluxparticle.sevenguis.gui7cells.formula.Operator.fromString;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

/**
 * Created by sreinck on 18.11.16.
 */
public enum FormulaSyntax implements Rule {

    DIGIT("[0-9]", objects -> objects[0].toString()),
    NUMBER("+DIGIT ?{'.' +DIGIT}", FormulaSyntax::reduceNumber),

    BIG_LETTER("[A-Z]", objects -> objects[0].toString()),
    REFERENCE("BIG_LETTER +DIGIT", FormulaSyntax::reduceReference),

    BRACKETS("'(' EXPRESSION ')'", objects -> objects[1]),

    FACTOR("(REFERENCE|NUMBER|BRACKETS)", objects -> new Operand((Formula) objects[0])),

    TERM("FACTOR *{('*'|'/'|'%') FACTOR}", FormulaSyntax::reduceExpression),

    EXPRESSION("TERM *{('+'|'-') TERM}", FormulaSyntax::reduceExpression),

    EQUATION("'=' EXPRESSION", objects -> new Equation((Expression) objects[1])),

    FORMULA("(NUMBER|EQUATION)", objects -> objects[0]);

    private final SingleElement[] elements;

    private final Function<Object[], Object> reduceFunction;

    FormulaSyntax(String s, Function<Object[], Object> reduceFunction) {
        SimpleRule rule = ParserHelper.parse(name() + " := " + s + ";");
        this.elements = rule.getElements();
        this.reduceFunction = reduceFunction;
    }

    @Override
    public RuleType getRuleType() {
        return RuleType.SIMPLE;
    }

    @Override
    public Object reduce(Object[] objects) {
        return reduceFunction.apply(objects);
    }

    @Override
    public SingleElement[] getElements() {
        return elements;
    }

    private static Object reduceNumber(Object[] objects) {
        String s = join(objects[0]) + ofNullable((List) objects[1]).map(l -> "." + join(l.get(1))).orElse("");
        double value = Double.parseDouble(s);
        return new Number(value);
    }

    private static Object reduceReference(Object[] objects) {
        String s = objects[0].toString() + join(objects[1]);
        return new Reference(s);
    }

    @SuppressWarnings("unchecked")
    private static Object reduceExpression(Object[] objects) {
        Expression left = (Expression) objects[0];

        for (List l : (List<List>) objects[1]) {
            Operator op = fromString(l.get(0).toString());
            Formula right = (Formula) l.get(1);
            left = new Operation(left, op, right);
        }

        return left;
    }

    @SuppressWarnings("unchecked")
    private static String join(Object o) {
        return ((List<String>) o).stream().collect(joining());
    }

}
