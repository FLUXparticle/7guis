package de.fluxparticle.sevenguis.gui7cells.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sreinck on 24.02.16.
 */
public class Application extends Formula {
    private static Map<String, Op> opTable = new HashMap<>();

    static {
        opTable.put("add", vals -> vals.get(0) + vals.get(1));
        opTable.put("sub", vals -> vals.get(0) - vals.get(1));
        opTable.put("div", vals -> vals.get(0) / vals.get(1));
        opTable.put("mul", vals -> vals.get(0) * vals.get(1));
        opTable.put("mod", vals -> vals.get(0) % vals.get(1));
        opTable.put("sum", vals -> {
            double accum = 0;
            for (Double i : vals) {
                accum += i;
            }
            return accum;
        });
        opTable.put("prod", vals -> {
            double accum = 1;
            for (Double i : vals) {
                accum *= i;
            }
            return accum;
        });
    }

    private final String function;

    private final List<Formula> arguments;

    public Application(String function, List<Formula> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    private static List<Double> evalList(List<Formula> args, Model env) {
        List<Double> result = new ArrayList<>();
        for (Formula f : args) {
            if (f instanceof Range) {
                for (Cell c : f.getReferences(env)) {
                    result.add(c.getValue());
                }
            } else {
                result.add(f.eval(env));
            }
        }
        return result;
    }

    public String toString() {
        StringBuilder t = new StringBuilder();
        t.append(function);
        t.append("(");
        for (int i = 0; i < arguments.size() - 1; i++) {
            t.append(arguments.get(i).toString());
            t.append(", ");
        }
        if (!arguments.isEmpty()) t.append(arguments.get(arguments.size() - 1).toString());
        t.append(")");
        return t.toString();
    }

    public double eval(Model env) {
        try {
            List<Double> argvals = evalList(arguments, env);
            return opTable.get(function).eval(argvals);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public List<Cell> getReferences(Model env) {
        List<Cell> result = new ArrayList<>();
        for (Formula argument : arguments) {
            result.addAll(argument.getReferences(env));
        }
        return result;
    }

    private static interface Op {
        public double eval(List<Double> vals);
    }

}
