package stanhebben.zenscript.type;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArrayGet;
import stanhebben.zenscript.expression.ExpressionArrayLength;
import stanhebben.zenscript.expression.ExpressionArraySet;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.casting.CastingRuleArrayArray;
import stanhebben.zenscript.type.casting.CastingRuleArrayList;
import stanhebben.zenscript.type.casting.CastingRuleDelegateArray;
import stanhebben.zenscript.type.casting.ICastingRule;
import stanhebben.zenscript.type.casting.ICastingRuleDelegate;
import stanhebben.zenscript.util.MethodOutput;
import stanhebben.zenscript.util.ZenPosition;

public class ZenTypeArrayBasic extends ZenTypeArray {
	public static final ZenTypeArrayBasic INSTANCE = new ZenTypeArrayBasic(ZenTypeAny.INSTANCE);
	private Type asmType;

	public ZenTypeArrayBasic(ZenType base) {
		super(base);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ZenTypeArrayBasic) {
			ZenTypeArrayBasic o = (ZenTypeArrayBasic) other;
			return o.getBaseType().equals(getBaseType());
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + (this.getBaseType() != null ? this.getBaseType().hashCode() : 0);
		return hash;
	}

	@Override
	public ICastingRule getCastingRule(ZenType type, IEnvironmentGlobal environment) {
		ICastingRule base = super.getCastingRule(type, environment);
		if (base == null && getBaseType() == ZenTypeAny.INSTANCE && type instanceof ZenTypeArray) {
			ZenType toBaseType = ((ZenTypeArray) type).getBaseType();
			if (type instanceof ZenTypeArrayBasic) {
				return new CastingRuleArrayArray(ZenTypeAny.INSTANCE.getCastingRule(toBaseType, environment), this, (ZenTypeArrayBasic) type);
			} else if (type instanceof ZenTypeArrayList) {
				return new CastingRuleArrayList(ZenTypeAny.INSTANCE.getCastingRule(toBaseType, environment), this, (ZenTypeArrayList) type);
			} else {
				throw new RuntimeException("Invalid array type: " + type);
			}
		} else {
			return base;
		}
	}

	@Override
	public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {
		ICastingRuleDelegate arrayRules = new CastingRuleDelegateArray(rules, this);
		getBaseType().constructCastingRules(environment, arrayRules, followCasters);

		if (followCasters) {
			constructExpansionCastingRules(environment, rules);
		}
	}

	@Override
	public IZenIterator makeIterator(int numValues, IEnvironmentMethod methodOutput) {
		if (numValues == 1) {
			return new ValueIterator(methodOutput.getOutput());
		} else if (numValues == 2) {
			return new IndexValueIterator(methodOutput.getOutput());
		} else {
			return null;
		}
	}

	@Override
	public String getAnyClassName(IEnvironmentGlobal global) {
		return null;
	}

	@Override
	public Type toASMType() {
		if (asmType != null) return asmType;
		return asmType = Type.getType("[" + base.toASMType().getDescriptor());
	}

	@Override
	public Class toJavaClass() {
		try {
			return Class.forName("[L" + getBaseType().toJavaClass().getName() + ";");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getSignature() {
		return "[" + getBaseType().getSignature();
	}

	@Override
	public IPartialExpression getMemberLength(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value) {
		return new ExpressionArrayLength(position, value.eval(environment));
	}

	@Override
	public Expression indexGet(ZenPosition position, IEnvironmentGlobal environment, Expression array, Expression index) {
		return new ExpressionArrayGet(
				position,
				array,
				index.cast(position, environment, ZenTypeInt.INSTANCE));
	}

	@Override
	public Expression indexSet(ZenPosition position, IEnvironmentGlobal environment, Expression array, Expression index, Expression value) {
		return new ExpressionArraySet(
				position,
				array,
				index.cast(position, environment, ZenTypeInt.INSTANCE),
				value.cast(position, environment, getBaseType()));
	}

	private class ValueIterator implements IZenIterator {
		private final MethodOutput methodOutput;
		private int index;

		public ValueIterator(MethodOutput methodOutput) {
			this.methodOutput = methodOutput;
		}

		@Override
		public void compileStart(int[] locals) {
			index = methodOutput.local(Type.INT_TYPE);
			methodOutput.iConst0();
			methodOutput.storeInt(index);
		}

		@Override
		public void compilePreIterate(int[] locals, Label exit) {
			methodOutput.dup();
			methodOutput.arrayLength();
			methodOutput.loadInt(index);
			methodOutput.ifICmpLE(exit);

			methodOutput.dup();
			methodOutput.loadInt(index);
			methodOutput.arrayLoad(getBaseType().toASMType());
			methodOutput.store(getBaseType().toASMType(), locals[0]);
		}

		@Override
		public void compilePostIterate(int[] locals, Label exit, Label repeat) {
			methodOutput.iinc(index, 1);
			methodOutput.goTo(repeat);
		}

		@Override
		public void compileEnd() {
			// pop the array
			methodOutput.pop();
		}

		@Override
		public ZenType getType(int i) {
			return getBaseType();
		}
	}

	private class IndexValueIterator implements IZenIterator {
		private final MethodOutput methodOutput;

		public IndexValueIterator(MethodOutput methodOutput) {
			this.methodOutput = methodOutput;
		}

		@Override
		public void compileStart(int[] locals) {
			methodOutput.iConst0();
			methodOutput.storeInt(locals[0]);
		}

		@Override
		public void compilePreIterate(int[] locals, Label exit) {
			methodOutput.dup();
			methodOutput.arrayLength();
			methodOutput.loadInt(locals[0]);
			methodOutput.ifICmpLE(exit);

			methodOutput.dup();
			methodOutput.loadInt(locals[0]);
			methodOutput.arrayLoad(getBaseType().toASMType());
			methodOutput.store(getBaseType().toASMType(), locals[1]);
		}

		@Override
		public void compilePostIterate(int[] locals, Label exit, Label repeat) {
			methodOutput.iinc(locals[0]);
			methodOutput.goTo(repeat);
		}

		@Override
		public void compileEnd() {
			// pop the array
			methodOutput.pop();
		}

		@Override
		public ZenType getType(int i) {
			return i == 0 ? ZenTypeInt.INSTANCE : getBaseType();
		}
	}
}
