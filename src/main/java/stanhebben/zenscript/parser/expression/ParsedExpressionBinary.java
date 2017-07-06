/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.parser.expression;

import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.ZenPosition;

import javax.management.OperationsException;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionBinary extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final OperatorType operator;

	public ParsedExpressionBinary(ZenPosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
		super(position);

		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
		// TODO: make better predictions
		Expression cLeft = left.compile(environment, predictedType).eval(environment);
		Expression cRight = right.compile(environment, predictedType).eval(environment);
		return cLeft.getType().binary(getPosition(), environment, cLeft, cRight, operator);
	}

	@Override
	public String toLua() {
		return left + " " + operator + " " + right;
	}
}
