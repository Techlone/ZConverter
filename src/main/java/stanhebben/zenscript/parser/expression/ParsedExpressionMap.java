/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.parser.expression;

import java.util.List;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionAs;
import stanhebben.zenscript.expression.ExpressionMap;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;
import stanhebben.zenscript.type.casting.ICastingRule;
import stanhebben.zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionMap extends ParsedExpression {
	private final List<ParsedExpression> keys;
	private final List<ParsedExpression> values;

	public ParsedExpressionMap(
			ZenPosition position,
			List<ParsedExpression> keys,
			List<ParsedExpression> values) {
		super(position);

		this.keys = keys;
		this.values = values;
	}

	@Override
	public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
		ZenType predictedKeyType = null;
		ZenType predictedValueType = null;
		ICastingRule castingRule = null;
		ZenTypeAssociative mapType = ZenTypeAssociative.INSTANCE;

		if (predictedType instanceof ZenTypeAssociative) {
			ZenTypeAssociative inputType = (ZenTypeAssociative) predictedType;
			predictedKeyType = inputType.getKeyType();
			predictedValueType = inputType.getValueType();
			mapType = inputType;
		} else {
			castingRule = ZenTypeAssociative.INSTANCE.getCastingRule(predictedType, environment);
			if (castingRule != null) {
				if (castingRule.getInputType() instanceof ZenTypeAssociative) {
					ZenTypeAssociative inputType = (ZenTypeAssociative) castingRule.getInputType();
					predictedKeyType = inputType.getKeyType();
					predictedValueType = inputType.getValueType();
					mapType = inputType;
				} else {
					environment.error(getPosition(), "Caster found for any[any] but its input is not an associative array");
				}
			}
		}

		Expression[] cKeys = new Expression[keys.size()];
		Expression[] cValues = new Expression[values.size()];

		for (int i = 0; i < keys.size(); i++) {
			cKeys[i] = keys.get(i).compileKey(environment, predictedKeyType);
			cValues[i] = values.get(i).compile(environment, predictedValueType).eval(environment);
		}

		Expression result = new ExpressionMap(getPosition(), cKeys, cValues, mapType);
		if (castingRule != null) {
			return new ExpressionAs(getPosition(), result, castingRule);
		} else {
			return result;
		}
	}

	@Override
	public String toLua() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		int size = keys.size();
		if (size != 0)
			sb.append(getPair(0));
		for (int i = 1; i < size; i++)
			sb.append(", ").append(getPair(i));
		return sb.toString();
	}

	private String getPair(int i) {
		ParsedExpression key = keys.get(i);
		ParsedExpression value = values.get(i);
		return "[" + key + "] = " + value;
	}
}
