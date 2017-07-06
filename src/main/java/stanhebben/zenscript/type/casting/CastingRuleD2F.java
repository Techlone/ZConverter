/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.casting;

import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class CastingRuleD2F extends BaseCastingRule {
	public CastingRuleD2F(ICastingRule baseRule) {
		super(baseRule);
	}

	@Override
	public void compileInner(IEnvironmentMethod method) {
		method.getOutput().d2f();
	}

	@Override
	public ZenType getInnerInputType() {
		return ZenType.DOUBLE;
	}

	@Override
	public ZenType getResultingType() {
		return ZenType.FLOAT;
	}
}
