package de.fluxparticle.sevenguis.gui7cells.parser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sreinck on 24.02.16.
 */
class Tokenizer {

    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;

    public Tokenizer() {
        tokenInfos = new LinkedList<TokenInfo>();
        tokens = new LinkedList<Token>();
    }

    public void add(String regex, int token) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^("+regex+")"), token));
    }

    public void tokenize(String s) {
        tokens.clear();
        while (!s.equals("")) {
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    String tok = m.group().trim();
                    tokens.add(new Token(info.token, tok));
                    s = m.replaceFirst("");
                    break;
                }
            }
            if (!match) throw new ParseException("Unexpected char in input: " + s);
        }
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    private static class TokenInfo {

        public final Pattern regex;
        public final int token;

        public TokenInfo(Pattern regex, int token) {
            super();
            this.regex = regex;
            this.token = token;
        }

    }

}
