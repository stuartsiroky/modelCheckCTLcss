package modelCheckCTL.model;

import java.util.Stack;

/**
 * This class is responsible for creating a data structure to represent a well
 * formed CTL formula.
 * 
 * @author ssiroky
 */

public class CTLFormulaStack {

	static private Stack<CTLFormula> fStack = new Stack<CTLFormula>();
	static private Stack<String> uStack = new Stack<String>();
	static private int openParenCnt;
	static private boolean prevWasSymbol;
	static private boolean prevWasUSymbol;
	static private boolean debug = false;
	static private String msg = "";

	/**
	 * This is the main function and will be used to create the data structure
	 * representing the CTL formula
	 * 
	 * @param formula
	 *            The string must be a well CTL formula
	 * @return CTLFormula
	 * @throws ModelException
	 */
	public static CTLFormula createCTLTree(String formula)
			throws ModelException {
		msg = "";
		openParenCnt = 0;
		prevWasSymbol = false;
		prevWasUSymbol = false;
		CTLFormula f = new CTLFormula();
		String msg = "";

		String[] working;
		// System.out.println("original formula = " + formula);
		working = formula.split(" ");
		for (String myString : working) {
			f = new CTLFormula(myString);
			boolean closeParen;
			boolean openParen;
			boolean predicate;
			boolean symbol;
			boolean binarySymbol;
			boolean unarySymbol;
			boolean AESymbol;

			openParen = isOpenParen(myString);
			closeParen = isCloseParen(myString);
			predicate = isPredicate(myString);
			binarySymbol = isBinarySymbol(myString);
			unarySymbol = isUnarySymbol(myString);
			AESymbol = isAESymbol(myString);
			symbol = binarySymbol | unarySymbol;

			print_debug("--" + myString + "-- " + openParen + " " + closeParen
					+ " " + predicate + " " + binarySymbol + " " + unarySymbol
					+ " " + AESymbol + " " + symbol + " " + prevWasSymbol + " "
					+ prevWasUSymbol + " " + fStack.size() + " " + openParenCnt);
			if (fStack.size() > 0) {
				String tmp = "STACK CONTENTS ";
				for (CTLFormula x : fStack) {
					tmp = tmp.concat(x.toString() + ", ");
				}
				print_debug(tmp);
			}
			if (predicate & prevWasUSymbol) {
				print_debug("CTLFormulaStack::createCTLTree 5");
				f = fStack.pop();
				f.addRight(myString);

				if (!fStack.empty()) {
					String token = fStack.peek().getOp_var();
					binarySymbol = isBinarySymbol(token);
					unarySymbol = isUnarySymbol(token);
					symbol = binarySymbol | unarySymbol;

				} // stack not empty

				print_debug("CTLFormulaStack::createCTLTree 5 push "
						+ f.toString());
				fStack.push(f);
			} // unary operator with predicate i.e. not q
			else if (unarySymbol || AESymbol || openParen
					|| (predicate && openParenCnt != 0)) {
				print_debug("CTLFormulaStack::createCTLTree 1 push " + myString);
				fStack.push(f);
				if (openParen) {
					if (AESymbol) {
						print_debug("CTLFormulaStack::createCTLTree 1 push Ustack "
								+ myString);
						uStack.push(myString);
					}
					openParenCnt++;
				}
			} // if symbol, openParen, predicate w/o paren
			else if (openParenCnt != 0 && closeParen) {
				print_debug("CTLFormulaStack::createCTLTree 2");
				prevWasSymbol = false;
				unwindStack();
				openParenCnt--;
			} // else if closeParen
			else if (predicate && prevWasSymbol) {
				print_debug("CTLFormulaStack::createCTLTree 3");
				f = fStack.pop();
				f.addRight(myString);
				print_debug("CTLFormulaStack::createCTLTree 3 push " + myString);
				print_debug("CTLFormulaStack::createCTLTree 3 after "
						+ f.toString());
				fStack.push(f);
				prevWasSymbol = false;
			} // predicate RH side
			else if (binarySymbol) {
				print_debug("CTLFormulaStack::createCTLTree 4");
				String tmp = "";
				if (myString.equals("U")) {
					tmp = uStack.pop();
					if (tmp.equals("A[")) {
						f.setOp_var("AU");
					} else {
						f.setOp_var("EU");
					}
				}
				f.addLeftFormula(fStack.pop());
				print_debug("CTLFormulaStack::createCTLTree 4 push formula "
						+ f.toString());
				fStack.push(f);
			} else {
				fStack.push(f);
				print_debug("CTLFormulaStack::createCTLTree 6" + f.toString());
			}

			if (symbol) {
				prevWasSymbol = true;
				prevWasUSymbol = unarySymbol;
			} else {
				prevWasSymbol = false;
				prevWasUSymbol = false;
			}

		} // for loop

		// unwind the rest of the stack
		while (fStack.size() > 1) {
			CTLFormula t;
			t = fStack.pop();
			f = fStack.pop();
			print_debug("CTLFormulaStack::createCTLTree 6 " + t.toString());
			f.addRightFormula(t);
			fStack.push(f);
		}

		if (fStack.size() == 0) {
			msg = msg.concat("Not a complete formula. Stack ended up empty.\n");
			throw new ModelException(msg);
		} else {
			f = fStack.pop();
		}

		if (!f.isCTLOp()) {
			msg = msg
					.concat("Malformed CTL Formula, not all element begin with CTL. "
							+ f.getOp_var() + "\n");
			throw new ModelException(msg);
		}
		return f;

	} // createCTLTree

	/**
	 * This function will take objects off the stack and rebuild the formula
	 * until an open paren is encountered or the stack is empty.
	 */
	private static void unwindStack() {
		String token;
		CTLFormula f;

		do {
			f = fStack.pop();
			token = fStack.peek().getOp_var();
			print_debug("unwind 1 " + f.toString() + " token = " + token);

			if (isOpenParen(token)) {
				print_debug("unwind 55A " + fStack.size());
				fStack.pop(); // remove parens
				print_debug("unwind 55B " + fStack.size());
			} else {
				CTLFormula f_;
				f_ = f;
				print_debug("unwind 3A " + f_.toString());
				f = fStack.pop();
				print_debug("unwind 3B " + f.toString());
				token = fStack.peek().getOp_var();
				f.addRightFormula(f_);
				if (isOpenParen(token)) {
					fStack.pop();
				} else {
					fStack.push(f);
				}
				print_debug("unwind 3 " + f.toString() + " token = " + token);
			}
			print_debug("unwind 2 " + f.toString());

		} while (!isOpenParen(token));

		fStack.push(f); // put formula back on
		print_debug("unwind 55 " + fStack.size());
	} // unwindStack

	/**
	 * Returns true if the string passed is a operator AX, AF, AG, A[.., EX, EF,
	 * EG, E[, and, or, implies, not else it returns false
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isSymbol(String s) {
		if (isUnarySymbol(s) | isBinarySymbol(s) | isAESymbol(s)) {
			return true;
		} else {
			return false;
		}
	} // isSymbol

	/**
	 * Returns true if the operator is a unary operator like not.
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isUnarySymbol(String s) {
		if (s.equals("not") || s.equals("AF") || s.equals("AX")
				|| s.equals("AG") || s.equals("EF") || s.equals("EX")
				|| s.equals("EG")) {
			return true;
		} else {
			return false;
		}
	} // isUnarySymbol

	/**
	 * Returns true if the operator is A[.. or E[..
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isAESymbol(String s) {
		if (s.equals("A[") || s.equals("E[")) {
			return true;
		} else {
			return false;
		}
	} // isAESymbol

	/**
	 * Returns true if the operator takes two arguments like and.
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isBinarySymbol(String s) {
		if (s.equals("and") || s.equals("or") || s.equals("implies")
				|| s.equals("U")) {
			return true;
		} else {
			return false;
		}
	} // isSymbol

	/**
	 * Returns true if an open paren is encountered. Looks for (, A[, E[.
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isOpenParen(String s) {
		if (s.equals("(") || s.equals("A[") || s.equals("E[")) {
			return true;
		} else {
			return false;
		}
	} // isOpenParen

	/**
	 * Returns true if a closed paren is encountered. Looks for ) or ].
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isCloseParen(String s) {
		if (s.equals(")") || s.equals("]")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true is the string is not an operator or parentheses.
	 * 
	 * @param s
	 * @return boolean
	 */
	private static boolean isPredicate(String s) {
		if (!isOpenParen(s) && !isCloseParen(s) && !isSymbol(s)) {
			return true;
		} else {
			return false;
		}
	} // isPredicate

	/**
	 * Use to debug the object.
	 * 
	 * @param s
	 */
	private static void print_debug(String s) {
		if (debug) {
			System.out.println(s);
		}
	}

	public static void setMsg(String msg) {
		CTLFormulaStack.msg = msg;
	}

	public static String getMsg() {
		return msg;
	}
} // CTLFormulaStack
