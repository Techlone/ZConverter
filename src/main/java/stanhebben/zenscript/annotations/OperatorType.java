package stanhebben.zenscript.annotations;

/**
 * Enum used to indicate operator type.
 * 
 * @author Stan Hebben
 */
public enum OperatorType {
	ADD("+"),
	SUB("-"),
	MUL("*"),
	DIV("/"),
	MOD,
	CAT,
	OR("or"),
	AND("and"),
	XOR,
	NEG,
	NOT("not"),
	INDEXSET,
	INDEXGET,
	RANGE,
	CONTAINS,
	COMPARE,
	MEMBERGETTER,
	MEMBERSETTER,
	EQUALS;

	private String luaOp = null;

	OperatorType() { }

	OperatorType(String luaOp) {
		this.luaOp = luaOp;
	}

	public String toLua() {
		if (luaOp != null) return luaOp;
		throw new RuntimeException("Unknown lua operation");
	}


	@Override
	public String toString() {
		return toLua();
	}
}
