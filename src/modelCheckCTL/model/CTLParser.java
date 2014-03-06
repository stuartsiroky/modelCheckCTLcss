package modelCheckCTL.model;

/**
 * This is the main object for creating and handling a CTL formula.
 * @author ssiroky
 *
 */
public class CTLParser {
	
	CTLFormula ctlFormula;
	private boolean debug = false;
	private String msg = "";
	

	// This method will take a well formed formula CTL 
	// formula and translate it to only { false, not, and, AF, EU, EX }
	// this function calls a recursive method
	/**
	 * Take a String representing a CTL formula clean it up and return 
	 * a new formula that should be equivalent.
	 * @param f String
	 * @return String
	 * @throws ModelException
	 */
	public String translate(String f) throws ModelException {
		String formula;
		msg = "";
		formula = notSimp(removeSpace(f));
		print_debug("CTLParser::translate in formula = " + formula);

			if(syntaxCheck(formula)) {
				print_debug("CTLParser::translate formula clean = " + formula);
				ctlFormula = CTLFormulaStack.createCTLTree(formula);
				print_debug("CTLParser::translate pretranslated formula = " + ctlFormula);
				ctlFormula = CTLFormulaStack.createCTLTree(notSimp(convCTLFormula(ctlFormula).toString()));
				return ctlFormula.toString();
			}
			else {
				String tmp;
				tmp = "CTLParser::translate Could not translate formula: " + f;
				tmp = tmp.concat(msg);
				msg = tmp;
				//System.out.println("CTLParser::translate Could not translate formula: " + f);
				return "";
			}

	} //translate

	/**
	 * Remove any double not operators that you see.
	 * @param f String
	 * @return String
	 */
	private String notSimp(String f) {
		return f.replaceAll("not not ", "");
	}

	/**
	 * Convert the given CTL formula to a CTL formula with a limited number of 
	 * operators. Will only use true, not, and, AF, EX, EU
	 * @param f CTLFormula
	 * @return CTLFormula
	 */
	private CTLFormula convCTLFormula(CTLFormula f) {

		if(f.getIs_op() || f.getOp_var().equals("false")) {
			f = reduceFormula(f);		
		}

		if(f.getLeft() != null) {
			convCTLFormula(f.getLeft());
		}
		if(f.getRight() != null) {
			convCTLFormula(f.getRight());
		}
		
		print_debug("CTLParser::convCTLFormula recurse return f = " + f.toString());
		return f;
		
	} //convCTLFormula

	/**
	 * Take a CTLFormula and reduce the number of types of operators used.
	 * The result will only contain true, not, and, AF, EX, EU
	 * @param f CTLFormula
	 * @return CTLFormula
	 */
	private CTLFormula reduceFormula(CTLFormula f) { 
		
		String operand;
		operand = f.getOp_var();
		CTLFormula w; // working formula;
		CTLFormula r; // right predicate
		CTLFormula l; // left predicate
		
		l = f.getLeft();  // save predicates
		r = f.getRight(); // save predicates

		f.setLeft(null);
		f.setRight(null);
		
		if(operand.equals("or")){
			//"or" : not(not x and not b);
			l = new CTLFormula("not",l);
			r = new CTLFormula("not",r);
			w = new CTLFormula("and",l,r);
			f.setOp_var("not");
			f.setRight(w);
		}
		else if(operand.equals("implies")) {
			//"implies" : not(x and not b);
			r = new CTLFormula("not",r);
			w = new CTLFormula("and",l,r);
			f.setOp_var("not");
			f.setRight(w);
		}
		else if(operand.equals("AX")) {
			//"AX"      : not EX not x ;
			r = new CTLFormula("not",r);
			w = new CTLFormula("EX",r);
			f.setOp_var("not");
			f.setRight(w);
		}
		else if(operand.equals("AU")) {
			//"A[xUy]"  : not (E[not q U (not p or not q)] or EG not q);
			CTLFormula r1, w1;
			CTLFormula rb = new CTLFormula("not",r); // not q
			CTLFormula lb = new CTLFormula("not",l); // not p
			r1 = new CTLFormula("EG",rb); //EG not q
			CTLFormula rrr = new CTLFormula("or",lb,rb); //not p or not q
			w1 = new CTLFormula("EU",rb,rrr); //E[ not q U ( not p or not q ) ]
			w = new CTLFormula("or",w1,r1); //  E[ not q U ( not p or not q ) ] or EG not q )
			f.setOp_var("not");
			f.setRight(w);
			f.setLeft(null);
			System.out.println("f " + f.toString());
			}
		else if(operand.equals("EF")) {
			//"EF"      : E[true U x];
			l = new CTLFormula("true");
			f.setOp_var("EU");
			f.setRight(r);
			f.setLeft(l);
		}
		else if(operand.equals("EG")) {
			//"EG"      : not AF not x;
			r = new CTLFormula("not",r);
			w = new CTLFormula("AF",r);
			f.setOp_var("not");
			f.setRight(w);
		}
		else if(operand.equals("AG")) {
			//"AG"      : not EF not p;
			r = new CTLFormula("not",r);
			w = new CTLFormula("EF",r);
			f.setOp_var("not");
			f.setRight(w);
		}
		else if(operand.equals("false")) {
			// "0" false  : not true
			r = new CTLFormula("true");
			f.setOp_var("not");
			f.setRight(r);
		}
		else {
			f.setLeft(l);
			f.setRight(r);
		}
//	// root cases
//	"true"    : return true; 
//  "not"     : not x;     // return not x
//	"and"     : x and y;   // return x and y
//	"EX"      : EX x;      // return EX(x)
//	"E[xUy]"  : E[xUy];    // return E[xUy] 
//	"AF"      : AF x;      // return AF(x)

		return f;
		
	}

	/**
	 * Take a string and return a string that does not have extra spaces.
	 * @param f String
	 * @return String
	 */
	private String removeSpace(String f) {
		String tmp;
		tmp = f.replaceAll("([AE][XFG])"," $1 ");
		f = tmp.replaceAll("([\\(\\)\\[\\]\\{\\}U])", " $1 ");
		tmp = f.replaceAll("([AE]) +", "$1");
		f = tmp.trim();
		return f.replaceAll(" +", " ");
	}

	/**
	 * Do some static checking on the formula with regular expression checking.
	 * @param f String
	 * @return boolean
	 * @throws ModelException
	 */
	private boolean syntaxCheck(String f) throws ModelException {
		Boolean match = true;
	
		//System.out.println("formula " + f);
		// check parentheses balance
		if(!ParenthesesCheck.parentheses_valid(f)) {
			match = false;
			msg = msg.concat("Unbalanced parentheses.\n");
		}
		// must have a ctl quantifier
		if(!f.matches(".*[AE][XFG\\[].*")) { match = false; msg = msg.concat("No CTL quantifiers found ie AG().\n"); }
		// if any quantifier without A or E proceeding it 
		if(f.matches(".*[^AE][XFG\\[].*")) { match = false; msg = msg.concat("Operator without A or E qualifiers.\n"); }
		// double binary operators 
		if(f.matches(".*and and.*")) { match = false; msg = msg.concat("Cannot have double and and.\n"); }
		if(f.matches(".*or or.*")) { match = false; msg = msg.concat("Cannot have double or or.\n"); }
		if(f.matches(".*implies implies.*")) { match = false; msg = msg.concat("Cannot have double implies implies.\n"); }
		// binary operator as input to unary operators
		if(f.matches(".*[AE][XFG\\[] and.*")) { match = false; msg = msg.concat("CTL operator followed by and.\n"); }
		if(f.matches(".*[AE][XFG\\[] or.*")) { match = false; msg = msg.concat("CTL operator followed by or.\n"); }
		if(f.matches(".*[AE][XFG\\]] implies.*")) { match = false; msg = msg.concat("CTL operator followed by implies.\n"); }
		if(f.matches(".*not and.*")) { match = false; msg = msg.concat("not operator followed by and.\n"); }
		if(f.matches(".*not or.*")) { match = false; msg = msg.concat("not operator followed by or.\n"); }
		if(f.matches(".*not implies.*")) { match = false; msg = msg.concat("not operator followed by implies.\n"); }
		// until matches
		if(f.matches(".*[AE]\\[.*")) {
			if(!f.matches(".*[AE]\\[.* U .*\\].*")) { match = false; msg = msg.concat("CTL Until not formed correctly. ie A[ p U q ]\n"); }
		}
		if(match == false) {
			throw new ModelException(msg);
		}
		return match;

	} // syntaxCheck

	// Getters and Setters
	public CTLFormula reduceFormulaP(CTLFormula f) { return reduceFormula(f); }
	public String getMsg() { return msg = msg.concat(CTLFormulaStack.getMsg()); }
	public void setMsg(String msg) { this.msg = msg; }
	public CTLFormula getCtlFormula() { return ctlFormula; }
	public void setCtlFormula(CTLFormula ctlFormula) { this.ctlFormula = ctlFormula; }

	/**
	 * Print Debug info to console
	 * @param s
	 */
	private void print_debug(String s) {
		if(debug) {
			System.out.println(s);
		}
	}
	
} // class CTLParser
