package org.prank;

import stanhebben.zenscript.ZenTokener;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.ZenPosition;

import java.util.ArrayList;
import java.util.List;

public class BracketExpression extends Expression {
    private List<Token> tokens;

    public BracketExpression(ZenPosition position, List<Token> tokens) {
        super(position);
        this.tokens = fixTokens(tokens);
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment1) {
    }

    @Override
    public ZenType getType() {
        return null;
    }

    @Override
    public String toLua() {
        int size = tokens.size();
        Token first = tokens.get(0);
        if (size == 1 && first.getType() == ZenTokener.T_INTVALUE)
            return toIdGetter();
        if (first.getValue().equals("ore"))
            return toOreGetter();
        if (first.getValue().equals("liquid"))
            return toLiquidGetter();

        StringBuilder sb = new StringBuilder("items.");
        boolean hasFirstColon = false;
        boolean hasSecondColon = false;
        //boolean is
        for (Token token : tokens) {
            int type = token.getType();
            if (type == ZenTokener.T_ID) {
                sb.append(token.getValue().toLowerCase());
            } else if (type == ZenTokener.T_COLON) {
                if (!hasFirstColon) {
                    hasFirstColon = true;
                    sb.append(".");
                } else if (!hasSecondColon)
                    hasSecondColon = true;
                else throw new RuntimeException("Too many semicolons in item ID!");
            } else if (type == ZenTokener.T_INTVALUE) {
                if (hasSecondColon) {
                    sb.append("[").append(token.getValue()).append("]");
                } else sb.append("_").append(token.getValue());
            } else if (type == ZenTokener.T_MUL) {
                sb.append(".any");
            } else {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    private List<Token> fixTokens(List<Token> tokens) {
        List<Token> result = new ArrayList<>(tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == ZenTokener.T_INTVALUE) {
                Token num;
                StringBuilder zeros = new StringBuilder();
                for (; i < tokens.size() && (num = tokens.get(i)).getType() == ZenTokener.T_INTVALUE; i++) {
                    zeros.append(num.getValue());
                }
                i--;
                token = new Token(zeros.toString(), ZenTokener.T_INTVALUE, token.getPosition());
            }

            result.add(token);
        }
        return result;
    }

    private String toLiquidGetter() {
        return "fluids." + tokens.get(2).getValue();
    }

    private String toIdGetter() {
        return "items:get(" + tokens.get(0).getValue() + ")";
    }

    private String toOreGetter() {
        boolean hasWildcard = false;
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.getValue());
            if (token.getType() == ZenTokener.T_MUL) hasWildcard = true;
        }
        if (hasWildcard) return "ores:get(\"" + sb.toString() + "\")";
        return "ores." + tokens.get(2).getValue();
    }
}
