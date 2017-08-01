package stanhebben.zenscript.statements;

import java.util.List;
import stanhebben.zenscript.compiler.EnvironmentScope;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.util.ZenPosition;

public class StatementBlock extends Statement {
	private final List<Statement> statements;

	public StatementBlock(ZenPosition position, List<Statement> statements) {
		super(position);

		this.statements = statements;
	}

	@Override
	public void compile(IEnvironmentMethod environment) {
		IEnvironmentMethod local = new EnvironmentScope(environment);
		for (Statement statement : statements) {
			statement.compile(local);
			if (statement.isReturn()) {
				return;
			}
		}
	}

	@Override
	StringBuilder toLua(StringBuilder sb) {
		if (statements.size() != 0)
			sb.append("    ").append(statements.get(0));
		for (int i = 1; i < statements.size(); i++)
			sb.append(nl).append("    ").append(statements.get(i));
		return sb;
	}
}
