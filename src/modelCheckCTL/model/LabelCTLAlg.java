package modelCheckCTL.model;

import java.util.ArrayList;
import java.util.List;

import kripke.KripkeElement;
import kripke.KripkeStruct;
import tarjan.TarjanAlg;
import data_obj.AdjacencyList;
import data_obj.Edge;
import data_obj.Node;

/**
 * This object brings together the kripke structure the formula and the starting
 * state to determine if a current formula holds or if it fails for a given
 * model.
 * 
 * @author ssiroky
 *
 */

public class LabelCTLAlg {
	private ArrayList<ArrayList<Node>> scc;
	private KripkeStruct ks;
	private ArrayList<KripkeElement> wks; // working set
	private KripkeElement keStart;
	private AdjacencyList transList;
	private CTLFormula formula;
	private String msg = "";

	private static String NOT = "not";
	private static String AND = "and";
	private static String EX = "EX";
	private static String EU = "EU";
	private static String AF = "AF";

	/**
	 * Constructor - will run the evaluation
	 * 
	 * @param ks
	 *            KripkeStruct
	 * @param startState
	 *            String a state name in the Kripke Structure
	 * @param formula
	 *            CTLFormula to test on the Kripke Structure
	 * @throws ModelException
	 */
	public LabelCTLAlg(KripkeStruct ks, String startState, CTLFormula formula)
			throws ModelException {
		super();

		setKs(ks);
		getKs().resetKripkeStruct();
		setKeStart(startState);
		setFormula(formula);
		setTransList(ks);

		setSCC();
		wks = getListActiveElements();

		labelKS(formula);

		msg = checkForFormula(formula, getKeStart());

	} // constructor

	/**
	 * After the constructor has set everything up run the actual check.
	 * 
	 * @param f
	 *            CTLFormula
	 * @param ks
	 *            KripkeStruct
	 * @return String will indicate pass or fail.
	 */
	private String checkForFormula(CTLFormula f, KripkeElement ks) {
		ArrayList<CTLFormula> satList = ks.getSatArrayList();
		boolean result = false;
		for (CTLFormula l : satList) {
			if (l.toString().equals(f.toString())) {
				result = true;
				break;
			}
		}
		if (result) {
			return "\nThe formula: { "
					+ f.toString()
					+ " } **SATIFIES** \nthe kripke structure starting from state "
					+ ks.getLabel() + "\n";
		} else {
			return "\nThe formula: { "
					+ f.toString()
					+ " } **FAILS** to satifies \nthe kripke structure starting from state "
					+ ks.getLabel() + "\n";
		}
	} // checkForFormula

	/**
	 * Label the kripke structure with the following formula. This is a
	 * recursive method.
	 * 
	 * @param f
	 *            CTLFormula to test on KripkeStruct
	 */
	private void labelKS(CTLFormula f) {
		// Go through kripke structure and label

		if (f.getRight() != null) {
			labelKS(f.getRight());
		}
		if (f.getLeft() != null) {
			labelKS(f.getLeft());
		}

		// now go through the kripke structure and label
		if (checkOperand(f, NOT)) {
			labelNot(f.getRight());
		} else if (checkOperand(f, AND)) {
			labelAnd(f.getLeft(), f.getRight());
		} else if (checkOperand(f, EX)) {
			labelEX(f.getRight());
		} else if (checkOperand(f, EU)) {
			labelEU(f.getLeft(), f.getRight());
		} else if (checkOperand(f, AF)) {
			labelAF(f.getRight());
		} else { // check for predicate
			labelPrepositions(f);
		}
	} // labelKS

	/**
	 * Label the Kripke Structure with the prepositional atoms into the satified
	 * list.
	 * 
	 * @param f
	 *            CTLFormula
	 */
	private void labelPrepositions(CTLFormula f) {
		// go through all of the elements regardless of ability to get there
		for (KripkeElement k : wks) {
			ArrayList<String> list = k.getPrepAtomsArrayList();
			ArrayList<CTLFormula> slist = k.getSatArrayList();
			if (prepositionLabelCheck(f, list, slist)) {
				k.addSatArrayList(f);
			}
		} // for
	} // labelPrepositions

	/**
	 * Check if we should label the state with the preposition. If already
	 * labeled will return false.
	 * 
	 * @param f
	 *            CTLFormula we are looking for.
	 * @param list
	 *            list of prepositons formulas
	 * @param slist
	 *            list of satisfied formulas
	 * @return boolean
	 */
	private boolean prepositionLabelCheck(CTLFormula f, ArrayList<String> list,
			ArrayList<CTLFormula> slist) {
		boolean result = false;
		if (!formulaExists(f, slist)) {
			for (String l : list) {
				if (l.equals(f.toString())) {
					result = true;
					break;
				}
			}
		}
		return result;
	} // prepositionLabelCheck

	/**
	 * Look for the right hand side of the formula in the state if not there
	 * label with not.
	 * 
	 * @param right
	 *            CTLFormula right hand side
	 */
	private void labelNot(CTLFormula right) {
		// go through all of the elements regardless of ability to get there
		for (KripkeElement k : wks) {
			ArrayList<CTLFormula> satList = k.getSatArrayList();
			if (notLabelCheck(right, satList)) {
				k.addSatArrayList(new CTLFormula("not", right));
			}
		} // for
	} // labelNot

	/**
	 * Check for label and return if it should be labeled.
	 * 
	 * @param f
	 *            CTLFormula
	 * @param list
	 *            lsit of CTLFormulas the state satisfies.
	 * @return boolean
	 */
	private boolean notLabelCheck(CTLFormula f, ArrayList<CTLFormula> list) {
		boolean result = true;
		if (!formulaExists(new CTLFormula("not", f), list)) {
			for (CTLFormula l : list) {
				if (l.toString().equals(f.toString())) {
					result = false;
					break;
				}
			}
			return result;
		} else {
			return false;
		}
	} // notLabelCheck

	/**
	 * Check if the state contains the left and right CTLFormulas and label it
	 * if it has them.
	 * 
	 * @param left
	 *            CTLFormula
	 * @param right
	 *            CTLFormula
	 */
	private void labelAnd(CTLFormula left, CTLFormula right) {
		// go through all of the elements regardless of ability to get there
		for (KripkeElement k : wks) { // while(ke != null) {
			ArrayList<CTLFormula> satList = k.getSatArrayList();
			if (andLabelCheck(left, right, satList)) {
				k.addSatArrayList(new CTLFormula("and", left, right));
			}
		} // for
	} // labelAnd

	/**
	 * Indicate if the state should be labeled with and
	 * 
	 * @param left
	 *            CTLFormula
	 * @param right
	 *            CTLFormula
	 * @param list
	 *            list of formulas the state satisfies
	 * @return boolean
	 */
	private boolean andLabelCheck(CTLFormula left, CTLFormula right,
			ArrayList<CTLFormula> list) {
		boolean rt = false;
		boolean lt = false;
		if (!formulaExists(new CTLFormula("and", left, right), list)) {
			for (CTLFormula l : list) {
				if (l.toString().equals(left.toString())) {
					lt = true;
				}
				if (l.toString().equals(right.toString())) {
					rt = true;
				}
				if (lt && rt) {
					break;
				}
			}
		}
		return lt && rt;
	} // andLabelCheck

	/**
	 * Check all the states to see if they should be labeled with EX
	 * 
	 * @param right
	 *            CTLFormula
	 */
	private void labelEX(CTLFormula right) {
		for (KripkeElement k : wks) {
			List<Edge> l = transList.getAdjacent(k);
			if (l != null) {
				for (Edge e : l) {
					KripkeElement kto = (KripkeElement) e.getTo();
					ArrayList<CTLFormula> satList = kto.getSatArrayList();
					if (exLabelCheck(right, satList)
							&& !formulaExists(new CTLFormula("EX", right),
									k.getSatArrayList())) {
						k.addSatArrayList(new CTLFormula("EX", right));
						break;
					}
				} // kto
			}// list not null
		} // klist
	} // labelEX

	/**
	 * Indicate if the state should be labeled with the EX formula.
	 * 
	 * @param r
	 *            CTLFormula
	 * @param list
	 *            list of satisfied formulas for the state.
	 * @return boolean
	 */
	private boolean exLabelCheck(CTLFormula r, ArrayList<CTLFormula> list) {
		boolean result = false;
		if (!formulaExists(new CTLFormula("EX", r), list)) {
			for (CTLFormula l : list) {
				if (l.toString().equals(r.toString())) {
					result = true;
					break;
				}
			}
			return result;
		} else {
			return false;
		}
	} // exLabelCheck

	/**
	 * Check the states to see if they should be labeled with EU formula.
	 * 
	 * @param left
	 *            CTLFormula
	 * @param right
	 *            CTLFormula
	 */
	private void labelEU(CTLFormula left, CTLFormula right) {
		// any state labeled with phi2 label with EU
		// label any state with EU that has phi1 and its successor has EU
		boolean addedEU = false;
		ArrayList<KripkeElement> addedList = new ArrayList<KripkeElement>();
		// initial
		for (KripkeElement k : wks) {
			ArrayList<CTLFormula> satList = k.getSatArrayList();
			if (euLabelCheck(right, satList)
					&& !formulaExists(new CTLFormula("EU", left, right),
							k.getSatArrayList())) {
				addedEU = labelWithEU(k, left, right, addedList);
			}
		} // for
			// go until no change
		while (addedEU) {
			addedEU = false;
			for (KripkeElement k : wks) {
				// if this element has phi1 then see if its next has EU
				if (euLabelCheck(left, k.getSatArrayList())) {
					List<Edge> l = transList.getAdjacent(k);
					if (l != null) {
						for (Edge e : l) {
							KripkeElement kto = (KripkeElement) e.getTo();
							ArrayList<CTLFormula> satList = kto
									.getSatArrayList();
							if (euLabelCheck(new CTLFormula("EU", left, right),
									satList)
									&& !addedList.contains(k)
									&& !formulaExists(new CTLFormula("EU",
											left, right), k.getSatArrayList())) {
								addedEU = labelWithEU(k, left, right, addedList);
								break;
							} // doesn't contain AF
						} // kto
					}// list not null
				} // has phi1
			} // klist
		}// while
	} // labelEU

	/**
	 * Indicate if the state should be labeled with the EU formula.
	 * 
	 * @param f
	 *            CTLFormula
	 * @param list
	 *            list of formulas that satisfy the state
	 * @return boolean
	 */
	private boolean euLabelCheck(CTLFormula f, ArrayList<CTLFormula> list) {
		Boolean result = false;
		for (CTLFormula l : list) {
			if (l.toString().equals(f.toString())) {
				result = true;
				break;
			}
		}
		return result;
	} // euLabelCheck

	/**
	 * Indicate if state should be labeled with EU given its satisfies and the
	 * next states.
	 * 
	 * @param k
	 *            KripkeElement
	 * @param l
	 *            CTLFormula
	 * @param r
	 *            CTLFormula
	 * @param list
	 *            list of satisfied formulas for the state
	 * @return boolean
	 */
	private boolean labelWithEU(KripkeElement k, CTLFormula l, CTLFormula r,
			ArrayList<KripkeElement> list) {
		k.addSatArrayList(new CTLFormula("EU", l, r));
		list.add(k);
		return true;
	} // labelWithEU

	/**
	 * Check states to see if they should be labeled with AF
	 * 
	 * @param right
	 *            CTLFormula
	 */
	private void labelAF(CTLFormula right) {
		// first label all nodes with AF if the contain phi
		// then label all the SCC if one of their nodes has AF
		// finally label all the remaining nodes until there is no change
		boolean addedAF = false;
		ArrayList<KripkeElement> addedList = new ArrayList<KripkeElement>();
		// initial
		for (KripkeElement k : wks) {
			ArrayList<CTLFormula> satList = k.getSatArrayList();
			if (afLabelCheck(right, satList)) {
				addedAF = labelWithAF(k, right, addedList);
			}
		} // for
			// go until no change
		while (addedAF) {
			addedAF = false;
			for (KripkeElement k : wks) {
				List<Edge> l = transList.getAdjacent(k);
				if (l != null) {
					if (!addedList.contains(k)
							&& checkAllForAF(right, l)
							&& !formulaExists(new CTLFormula("AF", right),
									k.getSatArrayList())) {
						addedAF = labelWithAF(k, right, addedList);
					}
				}// list not null
			} // klist
		}// while

	} // labelAF

	/**
	 * Indicate if the state should be labeled with the AF formula.
	 * 
	 * @param r
	 *            CTLFormula
	 * @param list
	 *            list of satisfied formulas for the state
	 * @return boolean
	 */
	private boolean afLabelCheck(CTLFormula r, ArrayList<CTLFormula> list) {
		Boolean result = false;
		if (!formulaExists(new CTLFormula("AF", r), list)) {
			for (CTLFormula l : list) {
				if (l.toString().equals(r.toString())) {
					result = true;
					break;
				}
			}
			return result;
		} else {
			return false;
		}
	} // afLabelCheck

	/**
	 * Indicate if the state should be labeled with AF
	 * 
	 * @param k
	 *            KripkeElement next
	 * @param r
	 *            CTLFormula
	 * @param list
	 *            list of satisfied formulas for state
	 * @return boolean
	 */
	private boolean labelWithAF(KripkeElement k, CTLFormula r,
			ArrayList<KripkeElement> list) {
		k.addSatArrayList(new CTLFormula("AF", r));
		list.add(k);
		return true;
	} // labelWithAF

	/**
	 * Indicate if all the next states satisfy the AF formula
	 * 
	 * @param r
	 *            CTLFormula
	 * @param l
	 *            list of next states
	 * @return boolean
	 */
	private boolean checkAllForAF(CTLFormula r, List<Edge> l) {
		boolean result = true;
		for (Edge e : l) {
			KripkeElement kto = (KripkeElement) e.getTo();
			ArrayList<CTLFormula> satList = kto.getSatArrayList();
			if (!afLabelCheck(new CTLFormula("AF", r), satList)) {
				result = false;
				break;
			} // doesn't contain AF
		} // list
		return result;
	} // checkAllForAF

	/**
	 * Indicate if a formula exits in the current state
	 * 
	 * @param f
	 *            CTLFormula
	 * @param list
	 *            list of satisfied formulas
	 * @return boolean
	 */
	private boolean formulaExists(CTLFormula f, ArrayList<CTLFormula> list) {
		boolean guard = false;
		for (CTLFormula l : list) {
			if (l.toString().equals(f.toString())) {
				guard = true;
			}
		}
		return guard;
	} // formulaExists

	/**
	 * Check to see if we match for a state.
	 * 
	 * @param f
	 *            CTLFormula
	 * @param s
	 *            String
	 * @return boolean
	 */
	private boolean checkOperand(CTLFormula f, String s) {
		return f.getOp_var().equals(s);
	}

	/**
	 * Run the tarjan algorithm to create the list of Strongly Connected
	 * Components.
	 */
	private void setSCC() {
		TarjanAlg t = new TarjanAlg();
		scc = new ArrayList<ArrayList<Node>>();
		scc = t.tarjan((Node) keStart, ks.getTransList());
		// displaySCC();
	} // setSCC

	/**
	 * Debug list out the SCC found
	 */
	@SuppressWarnings("unused")
	private void displaySCC() {
		int i = 0;
		for (ArrayList<Node> lk : scc) {
			System.out.println("loop " + i);
			for (Node k : lk) {
				KripkeElement kk = (KripkeElement) k;
				System.out.println(kk.getLabel() + ", ");
			}
			System.out.println("\n");
			i++;
		}
	} // displaySCC

	/**
	 * Get a list of all the active elements given a certain starting point in
	 * the graph. Only reachable states from the start state.
	 * 
	 * @return Array list of KripkeElement
	 */
	public ArrayList<KripkeElement> getListActiveElements() {
		ArrayList<KripkeElement> kl = new ArrayList<KripkeElement>();

		for (ArrayList<Node> lk : scc) {
			for (Node k : lk) {
				// System.out.println("active elements " +
				// ((KripkeElement)k).toString());
				if (!kl.contains((KripkeElement) k)) {
					kl.add((KripkeElement) k);
				}
			}
		}
		return kl;
	} // getListActiveElements

	// Getters and Setters
	public KripkeStruct getKs() {
		return ks;
	}

	public void setKs(KripkeStruct ks) {
		this.ks = ks;
	}

	public KripkeElement getKeStart() {
		return keStart;
	}

	public void setKeStart(KripkeElement ke) {
		this.keStart = ke;
	}

	public void setKeStart(String s) throws ModelException {
		this.keStart = ks.getKe(s);
	}

	public CTLFormula getFormula() {
		return formula;
	}

	public void setFormula(CTLFormula formula) {
		this.formula = formula;
	}

	public AdjacencyList getTransList() {
		return transList;
	}

	public void setTransList(AdjacencyList transList) {
		this.transList = transList;
	}

	public void setTransList(KripkeStruct k) {
		this.transList = k.getTransList();
	}

	public ArrayList<KripkeElement> getWks() {
		return wks;
	}

	public void setWks(ArrayList<KripkeElement> wks) {
		this.wks = wks;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

} // class TarjanAlg
