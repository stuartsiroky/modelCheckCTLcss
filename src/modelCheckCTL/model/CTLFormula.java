package modelCheckCTL.model;

// This is a binary search tree node
/**
 * This is the element of a binary search tree that will represent a CTL formula.
 * @author ssiroky
 *
 */

public class CTLFormula {
	String op_var;
	CTLFormula left;
	CTLFormula right;
	private boolean is_op;

	/**
	 * Constructor of Binary object to current formulas.
	 * @param op_var String
	 * @param left CTLFormula
	 * @param right CTLFormula
	 */
	public CTLFormula(String op_var, CTLFormula left, CTLFormula right) {
		super();
		setOp_var(op_var);
		this.left   = left;
		this.right  = right;
	}

	/**
	 * Constructor of unary formula
	 * @param op_var String
	 * @param right CTLFormula
	 */
	public CTLFormula(String op_var, CTLFormula right) {
		super();
		setOp_var(op_var);
		this.left   = null;
		this.right  = right;
	}

	/**
	 * Indicate if an operation or preposition
	 * @return boolean
	 */
	public boolean getIs_op() { return is_op; }

	/**
	 * Constructor of entire formula.
	 * @param op_var String
	 * @param left String
	 * @param right String
	 */
	public CTLFormula(String op_var, String left, String right) {
		super();
		setOp_var(op_var);
		this.left  = new CTLFormula(left);
		this.right = new CTLFormula(right);
	}

	/**
	 * Constructor of unary operator
	 * @param op_var String
	 * @param right String
	 */
	public CTLFormula(String op_var, String right) {
		super();
		setOp_var(op_var);
		this.left   = null;//new CTLFormula(left);
		this.right  = new CTLFormula(right);//null;
	}

	/**
	 * Constructor of leaf object
	 * @param op_var String
	 */
	public CTLFormula(String op_var) {
		super();
		setOp_var(op_var);
		this.left   = null;
		this.right  = null;
	}

	/**
	 * Constructor
	 */
	public CTLFormula() {
		super();
		setOp_var(op_var);
		this.left   = null;
		this.right  = null;
	}

	/**
	 * Returns the value of the object
	 * @return String
	 */
	public String getOp_var() { return op_var; }

	/**
	 * When setting the operator for the object we indicate if it is a operator or preposition.
	 * @param op_var 
	 */
	public void setOp_var(String op_var) {
		this.op_var = op_var;
		if(op_var == null) {
			is_op = false;
		}
		else if( op_var.equals("AU") || 
			     op_var.equals("EU") ||
			     op_var.equals("EX") ||
			     op_var.equals("EF") ||
			     op_var.equals("EG") ||
			     op_var.equals("AX") ||
			     op_var.equals("AF") ||
			     op_var.equals("AG") ||
			     op_var.equals("and") ||
			     op_var.equals("not") ||
			     op_var.equals("or") ||
			     op_var.equals("implies") 
				) {
			is_op = true;
		}
		else {
			is_op = false;
		}
	}

	/**
	 * indicate if the current node in the formula is a CTL operand
	 * @return boolean
	 */
	public boolean isCTLOp() {
		if( op_var.equals("AU") || 
			     op_var.equals("EU") ||
			     op_var.equals("EX") ||
			     op_var.equals("EF") ||
			     op_var.equals("EG") ||
			     op_var.equals("AX") ||
			     op_var.equals("AF") ||
			     op_var.equals("AG") ||
			     op_var.equals("not")
				) {
			return true;
		}
		else {
			return false;
		}	
	} //isCTLOp
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result = result.concat(toStringPre());
		if(left != null) {
			result = result.concat(left.toString());
		}
		result = result.concat(toStringIn());
		if(right != null) {
			result = result.concat(right.toString());
		}
		result = result.concat(toStringPost()).trim();
		return result.replaceAll(" +", " ");

	} // toString

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(toString().equals(obj.toString())) {
			return true;
		}
		else { return false; }
	}

	/**
	 * Retrun the current traversal string.
	 * @return String
	 */
	private String toStringIn() {
		if(op_var.equals("and") || 
				op_var.equals("or") || 
				op_var.equals("implies") || 
				op_var.equals("not") ||
				op_var.equals("EX") || 
				op_var.equals("EF") || 
				op_var.equals("EG") || 
				op_var.equals("AX") || 
				op_var.equals("AF") || 
				op_var.equals("AG")) {
			return " " + op_var + " ";
		}
		else if(op_var.equals("AU") || 
				op_var.equals("EU") ) {
			return " U ";
		}
		else { 
			return op_var;
		}
	} // toStringIn

	/**
	 * Return the pre traversal string.
	 * @return String
	 */
	private String toStringPre() {
		if(     //op_var.equals("not") || 
				op_var.equals("and") || 
				op_var.equals("or") || 
				op_var.equals("implies") ) {
			return "( ";
		}
		else if(op_var.equals("AU") ) {
			return " A[ ";
		}
		else if(op_var.equals("EU") ) {
			return " E[ ";
		}
		else {
			return " ";
		}
	} //toStringPre

	/**
	 * Return the post traversal object.
	 * @return String
	 */
	private String toStringPost() {
		if( //op_var.equals("not") || 
			op_var.equals("and") || 
			op_var.equals("or") || 
			op_var.equals("implies") ) {
			return " )";
		}
		else if(op_var.equals("AU") || 
				op_var.equals("EU") ) {
			return " ]";
		}
		else {
			return " ";
		}
	} // toStringPost

	
	/**
	 * @param R Right String to be added to the formula.
	 */
	public void addRight(String R) {
		CTLFormula rF = new CTLFormula (R);
		this.right = rF;
	}
	
	/**
	 * @param L Left String to be added to the formula.
	 */
	public void addLeft(String L) {
		CTLFormula lF = new CTLFormula (L);
		this.left = lF;
	}
	
	/**
	 * @param rF Right hand CTLFormula.
	 */
	public void addRightFormula(CTLFormula rF) { this.right = rF; }
	/**
	 * @param lF Left hand CTLFormula.
	 */
	public void addLeftFormula(CTLFormula lF) { this.left = lF; }
	//Setters and getters
	public CTLFormula getLeft() { return left; }
	public CTLFormula getRight() { return right; }
	public void setLeft(CTLFormula left) { this.left = left; }
	public void setRight(CTLFormula right) { this.right = right; }

} //CTLFormula
