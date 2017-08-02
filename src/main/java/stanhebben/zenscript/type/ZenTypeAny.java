package stanhebben.zenscript.type;

import org.objectweb.asm.Type;
import stanhebben.zenscript.annotations.CompareType;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.casting.CastingAnyBool;
import stanhebben.zenscript.type.casting.CastingAnyByte;
import stanhebben.zenscript.type.casting.CastingAnyDouble;
import stanhebben.zenscript.type.casting.CastingAnyFloat;
import stanhebben.zenscript.type.casting.CastingAnyInt;
import stanhebben.zenscript.type.casting.CastingAnyLong;
import stanhebben.zenscript.type.casting.CastingAnyShort;
import stanhebben.zenscript.type.casting.CastingAnyString;
import stanhebben.zenscript.type.casting.CastingRuleAnyAs;
import stanhebben.zenscript.type.casting.CastingRuleNullableStaticMethod;
import stanhebben.zenscript.type.casting.ICastingRule;
import stanhebben.zenscript.type.casting.ICastingRuleDelegate;
import stanhebben.zenscript.type.natives.JavaMethod;
import static stanhebben.zenscript.util.AnyClassWriter.*;
import stanhebben.zenscript.util.ZenPosition;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import stanhebben.zenscript.value.IAny;

/**
 *
 * @author Stanneke
 */
public class ZenTypeAny extends ZenType {
	public static final ZenTypeAny INSTANCE = new ZenTypeAny();

	private ZenTypeAny() {
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name) {
		environment.error(position, "any values not yet supported");
		return new ExpressionInvalid(position);
	}

	@Override
	public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
		environment.error(position, "any values not yet supported");
		return new ExpressionInvalid(position);
	}

	@Override
	public IZenIterator makeIterator(int numValues, IEnvironmentMethod methodOutput) {
		return null; // TODO: handle iteration on any-values
	}

	@Override
	public ICastingRule getCastingRule(ZenType type, IEnvironmentGlobal environment) {
		ICastingRule base = super.getCastingRule(type, environment);
		if (base == null) {
			return new CastingRuleAnyAs(type);
		} else {
			return base;
		}
	}

	@Override
	public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {
		rules.registerCastingRule(ZenTypeBool.INSTANCE, CastingAnyBool.INSTANCE);
		rules.registerCastingRule(ZenTypeBoolObject.INSTANCE, new CastingRuleNullableStaticMethod(BOOL_VALUEOF, CastingAnyBool.INSTANCE));
		rules.registerCastingRule(ZenTypeByte.INSTANCE, CastingAnyByte.INSTANCE);
		rules.registerCastingRule(ZenTypeByteObject.INSTANCE, new CastingRuleNullableStaticMethod(BYTE_VALUEOF, CastingAnyByte.INSTANCE));
		rules.registerCastingRule(ZenTypeShort.INSTANCE, CastingAnyShort.INSTANCE);
		rules.registerCastingRule(ZenTypeShortObject.INSTANCE, new CastingRuleNullableStaticMethod(SHORT_VALUEOF, CastingAnyShort.INSTANCE));
		rules.registerCastingRule(ZenTypeInt.INSTANCE, CastingAnyInt.INSTANCE);
		rules.registerCastingRule(ZenTypeIntObject.INSTANCE, new CastingRuleNullableStaticMethod(INT_VALUEOF, CastingAnyInt.INSTANCE));
		rules.registerCastingRule(ZenTypeLong.INSTANCE, CastingAnyLong.INSTANCE);
		rules.registerCastingRule(ZenTypeLongObject.INSTANCE, new CastingRuleNullableStaticMethod(LONG_VALUEOF, CastingAnyLong.INSTANCE));
		rules.registerCastingRule(ZenTypeFloat.INSTANCE, CastingAnyFloat.INSTANCE);
		rules.registerCastingRule(ZenTypeFloatObject.INSTANCE, new CastingRuleNullableStaticMethod(FLOAT_VALUEOF, CastingAnyFloat.INSTANCE));
		rules.registerCastingRule(ZenTypeDouble.INSTANCE, CastingAnyDouble.INSTANCE);
		rules.registerCastingRule(ZenTypeDoubleObject.INSTANCE, new CastingRuleNullableStaticMethod(DOUBLE_VALUEOF, CastingAnyDouble.INSTANCE));
		rules.registerCastingRule(ZenTypeString.INSTANCE, CastingAnyString.INSTANCE);

		if (followCasters) {
			constructExpansionCastingRules(environment, rules);
		}
	}

	@Override
	public boolean canCastExplicit(ZenType type, IEnvironmentGlobal environment) {
		return true;
	}

	@Override
	public Class toJavaClass() {
		return IAny.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(IAny.class);
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return signature(IAny.class);
	}

	@Override
	public boolean isPointer() {
		return true;
	}

	@Override
	public Expression unary(ZenPosition position, IEnvironmentGlobal environment, Expression value, OperatorType operator) {
		switch (operator) {
			case NEG:
				return new ExpressionCallVirtual(position, environment, METHOD_NEG, value);
			case NOT:
				return new ExpressionCallVirtual(position, environment, METHOD_NOT, value);
			default:
				return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
		}
	}

	@Override
	public Expression binary(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, OperatorType operator) {
		switch (operator) {
			case ADD:
				return new ExpressionCallVirtual(position, environment, METHOD_ADD, left, right.cast(position, environment, INSTANCE));
			case CAT:
				return new ExpressionCallVirtual(position, environment, METHOD_CAT, left, right.cast(position, environment, INSTANCE));
			case SUB:
				return new ExpressionCallVirtual(position, environment, METHOD_SUB, left, right.cast(position, environment, INSTANCE));
			case MUL:
				return new ExpressionCallVirtual(position, environment, METHOD_MUL, left, right.cast(position, environment, INSTANCE));
			case DIV:
				return new ExpressionCallVirtual(position, environment, METHOD_DIV, left, right.cast(position, environment, INSTANCE));
			case MOD:
				return new ExpressionCallVirtual(position, environment, METHOD_MOD, left, right.cast(position, environment, INSTANCE));
			case AND:
				return new ExpressionCallVirtual(position, environment, METHOD_AND, left, right.cast(position, environment, INSTANCE));
			case OR:
				return new ExpressionCallVirtual(position, environment, METHOD_OR, left, right.cast(position, environment, INSTANCE));
			case XOR:
				return new ExpressionCallVirtual(position, environment, METHOD_XOR, left, right.cast(position, environment, INSTANCE));
			case CONTAINS:
				return new ExpressionCallVirtual(position, environment, METHOD_CONTAINS, left, right.cast(position, environment, INSTANCE));
			case INDEXGET:
				return new ExpressionCallVirtual(position, environment, METHOD_INDEXGET, left, right.cast(position, environment, INSTANCE));
			case RANGE:
				return new ExpressionCallVirtual(position, environment, METHOD_RANGE, left, right.cast(position, environment, INSTANCE));
			case COMPARE:
				return new ExpressionCallVirtual(position, environment, METHOD_COMPARETO, left, right.cast(position, environment, INSTANCE));
			case MEMBERGETTER:
				return new ExpressionCallVirtual(position, environment, METHOD_MEMBERGET, left, right.cast(position, environment, ZenTypeString.INSTANCE));
			default:
				return new ExpressionInvalid(position, INSTANCE);
		}
	}

	@Override
	public Expression trinary(ZenPosition position, IEnvironmentGlobal environment, Expression first, Expression second, Expression third, OperatorType operator) {
		switch (operator) {
			case INDEXSET:
				return new ExpressionCallVirtual(position, environment, METHOD_INDEXSET, first, second.cast(position, environment, INSTANCE), third.cast(position, environment, INSTANCE));
			case MEMBERSETTER:
				return new ExpressionCallVirtual(position, environment, METHOD_MEMBERSET, first, second.cast(position, environment, ZenTypeString.INSTANCE), third.cast(position, environment, INSTANCE));
			default:
				return new ExpressionInvalid(position, INSTANCE);
		}
	}

	@Override
	public Expression compare(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, CompareType type) {
		Expression comparator = new ExpressionCallVirtual(
				position,
				environment,
				JavaMethod.get(environment, IAny.class, "compareTo", IAny.class),
				left,
				right);

		return new ExpressionCompareGeneric(
				position,
				comparator,
				type);
	}

	@Override
	public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
		return new ExpressionCallVirtual(
				position,
				environment,
				JavaMethod.get(environment, IAny.class, "call", IAny[].class),
				receiver,
				arguments);
	}

	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		ZenType[] result = new ZenType[numArguments];
		for (int i = 0; i < result.length; i++) {
			result[i] = INSTANCE;
		}
		return result;
	}

	@Override
	public String getName() {
		return "any";
	}

	@Override
	public String getAnyClassName(IEnvironmentGlobal environment) {
		throw new UnsupportedOperationException("Cannot get any class name from the any type. That's like trying to stuff a freezer into a freezer.");
	}

	@Override
	public Expression defaultValue(ZenPosition position) {
		return new ExpressionNull(position);
	}
}
