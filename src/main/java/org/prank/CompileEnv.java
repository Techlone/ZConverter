package org.prank;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.IZenErrorLogger;
import stanhebben.zenscript.TypeExpansion;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.compiler.TypeRegistry;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.util.ZenPosition;

import java.util.List;

class CompileEnv implements IZenCompileEnvironment {
    @Override
    public IZenErrorLogger getErrorLogger() {
        return new IZenErrorLogger() {
            @Override
            public void error(ZenPosition position, String message) {
                System.err.println(position);
                System.err.println(message);
            }

            @Override
            public void warning(ZenPosition position, String message) {
                System.out.println(position);
                System.out.println(message);
            }
        };
    }

    @Override
    public IZenSymbol getGlobal(String name) {
        return new ZenSymbol(name);
    }

    @Override
    public IZenSymbol getDollar(String name) {
        return null;
    }

    @Override
    public IZenSymbol getBracketed(IEnvironmentGlobal environment, List<Token> tokens) {
        return position -> new BracketExpression(position, tokens);
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return null;
    }

    @Override
    public TypeExpansion getExpansion(String type) {
        return null;
    }
}
