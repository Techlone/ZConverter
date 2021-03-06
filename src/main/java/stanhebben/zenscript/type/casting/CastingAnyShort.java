/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.casting;

import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAny;
import stanhebben.zenscript.type.ZenTypeShort;
import stanhebben.zenscript.value.IAny;

/**
 *
 * @author Stan
 */
public class CastingAnyShort implements ICastingRule {
	public static final CastingAnyShort INSTANCE = new CastingAnyShort();

	private CastingAnyShort() {
	}

	@Override
	public void compile(IEnvironmentMethod method) {
		method.getOutput().invokeInterface(IAny.class, "asShort", short.class);
	}

	@Override
	public ZenType getInputType() {
		return ZenTypeAny.INSTANCE;
	}

	@Override
	public ZenType getResultingType() {
		return ZenTypeShort.INSTANCE;
	}
}
