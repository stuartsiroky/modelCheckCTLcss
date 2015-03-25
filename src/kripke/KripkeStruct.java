package kripke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import modelCheckCTL.model.ModelException;
import data_obj.AdjacencyList;
import data_obj.Edge;

/**
 * The parser will take a text file that describes a kripke structure and build
 * a directed graph. It will use the KripkeStruct object to do this.
 * 
 * @author ssiroky
 *
 */

public class KripkeStruct {

	private KripkeElement ke;
	private static AdjacencyList transList;
	private KripkeElement headKE;
	private int commasSeen;
	private String msg;

	/**
	 * Constructor
	 */
	public KripkeStruct() {
		super();
		ke = null;
		setKs(null);
		commasSeen = 0;
		transList = new AdjacencyList();
	} // constructor

	/**
	 * Parse the file that describes the kripke structure and create a directed
	 * graph.
	 * 
	 * @param in
	 *            String
	 * @throws ModelException
	 */
	public void parseKripkeFile(String in) throws ModelException {
		in = in.replace(", +", ",");
		switch (commasSeen) {
		case 0: // states
			addKripkeStates(in);
			break;
		case 1: // transitions
			addKripkeTrans(in);
			break;
		case 2: // predicates
			addKripkePred(in);
			break;

		default:
			msg = msg
					.concat("Malformed File : should have the form - s1,s2,...; t1 : s1-s2, ... ; s1 : p q, ...;\n");
			throw new ModelException(msg);
		}

		if (in.contains(";")) {
			commasSeen++;
		}
	} // parseKripkeFile

	/**
	 * Create the nodes of the kripke graph
	 * 
	 * @param in
	 *            String
	 */
	private void addKripkeStates(String in) {
		String[] working;
		working = in.split(",");
		for (String myString : working) {
			myString = cleanString(myString);
			if (myString.length() > 0) {
				// Check to see if exists in list
				// if not then add it

				if (ke == null) {
					ke = new KripkeElement(myString);
					setKs(ke); // headKE = ke;
					// System.out.println("adding first state " + myString +
					// "\n");
				} else {
					// always add at the head
					KripkeElement t = new KripkeElement(myString);
					t.setNxtKE(headKE);
					// ke = t;
					setKs(t); // headKE = ke;
					// System.out.println("adding state " + myString + "\n");
				}
			}
		} // for

	} // addKripkeStates

	/**
	 * Create the edge list of transitions between the nodes of the graph.
	 * 
	 * @param in
	 *            String
	 * @throws ModelException
	 */
	private void addKripkeTrans(String in) throws ModelException {
		String[] working;
		working = in.split(",");
		for (String myString : working) {
			myString = cleanString(myString);
			if (myString.length() > 0) {
				// find the first state then the second state
				String[] trans;
				String state1 = null, state2 = null;
				int setState;
				trans = myString.split(" ");
				setState = 0;
				for (String tt : trans) {
					if (setState == 1) {
						state1 = tt;
						setState = 0;
					} else if (setState == 2) {
						state2 = tt;
						setState = 0;
					}
					if (tt.equals(":")) {
						setState = 1;
					} else if (tt.equals("-")) {
						setState = 2;
					}
				} // for(..trans
				addTrans(state1, state2);
				// System.out.println("-ttt- " + myString + " -ttt- ");
				// System.out.println("ttt State1- " + state1 + " ttt State2- "
				// + state2);
			}
		} // for(...working

	} // addKripkeTrans

	/**
	 * Adds the transition to the list of edges
	 * 
	 * @param state1
	 *            String
	 * @param state2
	 *            String
	 * @throws ModelException
	 */
	private void addTrans(String state1, String state2) throws ModelException {
		// System.out.println("add transition " + state1 + " to " + state2);
		KripkeElement k1 = null, k2 = null;

		ke = getKs();
		while (ke != null && (k1 == null || k2 == null)) {
			if (ke.getLabel().equals(state1)) {
				k1 = ke;
			}
			if (ke.getLabel().equals(state2)) {
				k2 = ke;
			}
			ke = ke.getNxtKE();
		} // while (ke.getNxtKS() != null || (k1 != null && k2 != null));
		if ((k1 != null) && (k2 != null)) {
			transList.addEdge(k1, k2, 0);
		} else if ((k1 == null) && (k2 == null)) {
			msg = msg.concat("States " + state1 + " and " + state2
					+ " dont exist and can't be added to transition list\n");
			throw new ModelException(msg);
		} else if ((k1 == null)) {
			msg = msg.concat("State " + state1
					+ " doesnt exist and can't be added to transition list\n");
			throw new ModelException(msg);
			// System.out.println(" state 1 doesnt exist ");
		} else if ((k2 == null)) {
			msg = msg.concat("State " + state2
					+ " doesnt exist and can't be added to transition list\n");
			throw new ModelException(msg);
			// System.out.println(" state 2 doesnt exist");
		}
	} // addTrans

	/**
	 * Label the node with the prepositions given.
	 * 
	 * @param in
	 *            String
	 * @throws ModelException
	 */
	private void addKripkePred(String in) throws ModelException {
		String[] working;
		working = in.split(",");

		for (String myString : working) {
			myString = cleanString(myString);
			if (myString.length() > 0) {
				// find the first state to add pred
				String[] pred;
				String state1 = null;
				int setPrep;
				pred = myString.split(" ");
				setPrep = 0;
				for (String pp : pred) {
					if (setPrep == 1) {
						addPrep(state1, pp);
					}
					if (pp.equals(":")) {
						setPrep = 1;
					}
					if (setPrep == 0) {
						state1 = pp;
					}
				} // for(..pred
			}
		} // for

	} // addKripkePred

	/**
	 * Add the prepositions to the correct node/state object in the graph.
	 * 
	 * @param state1
	 *            String
	 * @param prep
	 *            String
	 * @throws ModelException
	 */
	private void addPrep(String state1, String prep) throws ModelException {
		KripkeElement k1 = null;

		ke = getKs();
		while (ke != null && k1 == null) {
			if (ke.getLabel().equals(state1)) {
				k1 = ke;
			}
			ke = ke.getNxtKE();
		} // while

		if ((k1 != null)) {
			k1.addPrepAtomsArrayList(prep);
		} else {
			msg = msg.concat("State " + state1
					+ " doesnt exist and can't add predicates\n");
			throw new ModelException(msg);
		}

	} // addPred

	/**
	 * Remove all the extra spaces from the input to simplify the parsing.
	 * 
	 * @param myString
	 *            String
	 * @return String
	 */
	private String cleanString(String myString) {
		myString = myString.trim();
		myString = myString.replaceAll(" +", " ");
		myString = myString.replaceAll(";", "");
		return myString;
	} // cleanString

	/**
	 * @return KripkeElement the head object in the list.
	 */
	public KripkeElement getKs() {
		return headKE;
	}

	public void setKs(KripkeElement ke) {
		this.headKE = ke;
	}

	/**
	 * Given a string - path to a file open the file and return true if
	 * successful.
	 * 
	 * @param f
	 *            String
	 * @return boolean
	 * @throws ModelException
	 */
	public boolean openKripkeFile(String f) throws ModelException {
		StringBuilder contents = new StringBuilder();

		commasSeen = 0;
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(f));
			readKSFile(contents, input);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("File not found.\n" + f);
			return false;
		}
		// System.out.println(contents.toString());
		// System.out.println(this.toString());
		return true;

	} // openKripkeFile(String

	/**
	 * Given a file handle open a file and return true if successful.
	 * 
	 * @param f
	 *            File handle
	 * @return boolean
	 * @throws ModelException
	 */
	public boolean openKripkeFile(File f) throws ModelException {
		StringBuilder contents = new StringBuilder();

		commasSeen = 0;
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(f));
			readKSFile(contents, input);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("File not found.\n" + f);
			return false;
		}
		// System.out.println(contents.toString());
		// System.out.println(this.toString());
		return true;

	} // openKripkeFile(File

	/**
	 * Read the file and parse it out.
	 * 
	 * @param contents
	 *            StringBuilder
	 * @param input
	 *            BufferedReader
	 * @throws IOException
	 * @throws ModelException
	 */
	private void readKSFile(StringBuilder contents, BufferedReader input)
			throws IOException, ModelException {
		try {
			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS
			 * the newline. it returns null only for the END of the stream. it
			 * returns an empty String if two newlines appear in a row.
			 */
			while ((line = input.readLine()) != null) {
				msg = "";
				parseKripkeFile(line);
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}
	} // readKSFile

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ke = headKE;

		String s = "";
		while (ke != null) {

			s = s.concat(ke.toString());
			s = s.concat("\nTransitions : ");
			List<Edge> l = transList.getAdjacent(ke);
			if (l != null) {
				for (Edge e : l) {
					KripkeElement k = (KripkeElement) e.getTo();
					s = s.concat("  " + ke.getLabel() + " -> " + k.getLabel()
							+ ", ");
				}
			}// list not null
			s = s.concat("\n");
			ke = ke.getNxtKE();
		} // while
		return s;
	} // toString

	/**
	 * Get the list of transitions for all the states in the graph.
	 * 
	 * @return AdjacencyList
	 */
	public AdjacencyList getTransList() {
		return transList;
	}

	public KripkeElement getKe() {
		return ke;
	}

	/**
	 * Search for a state in the graph with a given name. If found return it,
	 * else throw an exception.
	 * 
	 * @param s
	 *            String name of state
	 * @return KripkeElement
	 * @throws ModelException
	 */
	public KripkeElement getKe(String s) throws ModelException {
		ke = getKs();
		while (ke != null) {
			if (ke.getLabel().equals(s)) {
				return ke;
			}
			ke = ke.getNxtKE();
		}
		msg = msg.concat("No state " + s
				+ " is avaliable in Kripke structure\n");
		throw new ModelException(msg);
	}

	/**
	 * Reset the kripke structure to evaluate another formula. The list of
	 * satisfied formulas needs to be cleared and the index needs to be reset so
	 * the Strongly Connected Components can be re-calculated.
	 */
	public void resetKripkeStruct() {
		ke = getKs();
		while (ke != null) {
			ke.clearSatArrayList();
			ke.resetNode();
			ke = ke.getNxtKE();
		}
		ke = getKs();
	} // resetKripkeStruct

	/**
	 * This message is set by the Exception and by successful parsing and
	 * labeling of the kripke structure.
	 * 
	 * @return String
	 */
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

} // class KripkeParser
