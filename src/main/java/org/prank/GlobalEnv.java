package org.prank;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.TypeExpansion;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.ZenPosition;

import java.lang.reflect.Type;
import java.util.Set;

class GlobalEnv implements IEnvironmentGlobal {
    @Override
    public ZenType getType(Type type) {
        return null;
    }

    @Override
    public void error(ZenPosition position, String message) {

    }

    @Override
    public void warning(ZenPosition position, String message) {

    }

    @Override
    public IZenCompileEnvironment getEnvironment() {
        return null;
    }

    @Override
    public TypeExpansion getExpansion(String name) {
        return null;
    }

    @Override
    public String makeClassName() {
        return null;
    }

    @Override
    public boolean containsClass(String name) {
        return false;
    }

    @Override
    public Set<String> getClassNames() {
        return null;
    }

    @Override
    public byte[] getClass(String name) {
        return new byte[0];
    }

    @Override
    public void putClass(String name, byte[] data) {

    }

    @Override
    public IPartialExpression getValue(String name, ZenPosition position) {
        return null;
    }

    @Override
    public void putValue(String name, IZenSymbol value, ZenPosition position) {

    }
}
