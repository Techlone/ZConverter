package stanhebben.zenscript.statements;

import org.objectweb.asm.Label;
import org.prank.ZConverter;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.parser.expression.ParsedExpression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeBool;
import stanhebben.zenscript.util.MethodOutput;
import stanhebben.zenscript.util.ZenPosition;

public class StatementIf extends Statement {
	private final ParsedExpression condition;
	private final Statement onThen;
	private final Statement onElse;

	public StatementIf(ZenPosition position, ParsedExpression condition, Statement onThen, Statement onElse) {
		super(position);

		this.condition = condition;
		this.onThen = onThen;
		this.onElse = onElse;
	}

	@Override
	public void compile(IEnvironmentMethod environment) {
		environment.getOutput().position(getPosition());

		Expression cCondition = condition.compile(environment, ZenTypeBool.INSTANCE)
			.eval(environment)
			.cast(getPosition(), environment, ZenTypeBool.INSTANCE);

		ZenType expressionType = cCondition.getType();
		if (expressionType.canCastImplicit(ZenTypeBool.INSTANCE, environment)) {
			Label labelEnd = new Label();
			Label labelElse = onElse == null ? labelEnd : new Label();

			cCondition.compileIf(labelElse, environment);
			onThen.compile(environment);

			MethodOutput methodOutput = environment.getOutput();
			if (onElse != null) {
				methodOutput.goTo(labelEnd);
				methodOutput.label(labelElse);
				onElse.compile(environment);
			}

			methodOutput.label(labelEnd);
		} else {
			environment.error(getPosition(), "condition is not a boolean");
		}
	}

	@Override
	public StringBuilder toLua(StringBuilder sb) {
		sb.append("if ").append(condition).append(" then").append(nl)
				.append(onThen).append(nl);
		if (onElse != null)
			sb.append(ZConverter.getIndent()).append("else").append(nl)
					.append(onElse).append(nl);
		return sb.append(ZConverter.getIndent()).append("end");
	}
}
