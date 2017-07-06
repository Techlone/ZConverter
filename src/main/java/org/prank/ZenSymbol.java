package org.prank;

import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.ZenPosition;

public class ZenSymbol implements IZenSymbol {
    private String name;

    public ZenSymbol(String name) {
        this.name = name;
    }

    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new Expression(position) {
            @Override
            public void compile(boolean result, IEnvironmentMethod environment) {

            }

            @Override
            public ZenType getType() {
                return null;
            }
        };
    }
}
