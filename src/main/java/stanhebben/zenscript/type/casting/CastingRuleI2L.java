/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.casting;

import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeInt;
import stanhebben.zenscript.type.ZenTypeLong;

/**
 *
 * @author Stan
 */
public class CastingRuleI2L extends BaseCastingRule {
	public CastingRuleI2L(ICastingRule baseRule) {
		super(baseRule);
	}

	@Override
	public void compileInner(IEnvironmentMethod method) {
		method.getOutput().i2l();
	}

	@Override
	public ZenType getInnerInputType() {
		return ZenTypeInt.INSTANCE;
	}

	@Override
	public ZenType getResultingType() {
		return ZenTypeLong.INSTANCE;
	}
}
