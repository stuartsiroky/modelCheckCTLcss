package modelCheckCTL.model;

import java.util.Stack;

/**
 * This class checks all parentheses but we only use ( or [ type parentheses.
 * 
 * @author From the web source unknown
 *
 */
public class ParenthesesCheck {

	public static boolean is_open_parenthesis(char c) {
		if (c == '(' || c == '[' || c == '{')
			return true;
		else
			return false;
	}

	public static boolean is_closed_parenthesis(char c) {
		if (c == ')' || c == ']' || c == '}')
			return true;
		else
			return false;
	}

	private static boolean parentheses_match(char open, char closed) {
		if (open == '(' && closed == ')')
			return true;
		else if (open == '[' && closed == ']')
			return true;
		else if (open == '{' && closed == '}')
			return true;
		else
			return false;
	}

	public static boolean parentheses_valid(String exp) {
		Stack<Character> s = new Stack<Character>();
		int i;
		char current_char;
		Character c;
		char c1;
		boolean ret = true;

		for (i = 0; i < exp.length(); i++) {
			current_char = exp.charAt(i);

			if (is_open_parenthesis(current_char)) {
				c = new Character(current_char);
				s.push(c);
			} else if (is_closed_parenthesis(current_char)) {
				if (s.isEmpty()) { // if no open parenthesis
					ret = false;
					break;
				} else { // pop
					c = (Character) s.pop();
					c1 = c.charValue();
					if (!parentheses_match(c1, current_char)) {
						ret = false;
						break;
					}
				} // else pop
			} // else if( is_closed
		} // for

		if (!s.isEmpty())
			ret = false;

		return ret;
	} // parentheses_valid

} // class
