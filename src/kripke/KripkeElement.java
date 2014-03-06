package kripke;

import java.util.ArrayList;

import modelCheckCTL.model.CTLFormula;
import data_obj.Node;

/** The kripke structure will be a graph, each state in the graph can have 
 * transitions to many states and from many states but there should not be 
 * duplicates.
 * @author ssiroky
 *
 */

public class KripkeElement extends Node {

	private static int nodeCnt = 0;
	private KripkeElement nxtKE; // pointer to arbitrary next object in struct
	private String label;       // name of state
	private ArrayList<String> prepAtomsArrayList;     // list of this states prepositions
	private ArrayList<CTLFormula> satArrayList;       // list of ctl formulas


	/**
	 * Constructor.
	 */
	public KripkeElement() {
		super(nodeCnt);
		nodeCnt++;
		nxtKE = null;
		label = "";
		prepAtomsArrayList = new ArrayList<String>();
		satArrayList       = new ArrayList<CTLFormula>();
	} //constructor
	
	/**
	 * Constructor - Construct a node for the kripke structure with a certain label.
	 * @param name
	 */
	public KripkeElement(String name) {
		super(nodeCnt);
		nodeCnt++;
		nxtKE = null;
		setLabel(name);
		prepAtomsArrayList = new ArrayList<String>();
		satArrayList       = new ArrayList<CTLFormula>();
		addPrepAtomsArrayList("true");
	} //constructor
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s;
		s = "\nState : " + getLabel();
		s = s.concat(listPrepositions());
		s = s.concat(listSat());
		return s;
	} //toString

	/**
	 * Add to the list of prepositons for the node.
	 * @param prepAtom
	 */
	public void addPrepAtomsArrayList(String prepAtom) {
		this.prepAtomsArrayList.add(prepAtom);
	}

	/**
	 * Add to the formulas that this node satisfies.
	 * @param sat CTLFormula
	 */
	public void addSatArrayList(CTLFormula sat) {
		this.satArrayList.add(sat);
	}

	/**
	 * Returns a comma separated list of prepositions for the node.
	 * @return String
	 */
	private String listPrepositions() {
		String s;
		s = "\nPrepositions : ";
		for(String t : prepAtomsArrayList) {
			s = s.concat("  " + t + ", ");
		}
		return s;
	} //listPrepositions

	/**
	 * Returns a comma separated list of satisfied formulas for the node.
	 * @return String
	 */
	private String listSat() {
		String s;
		s = "\nSatifies : ";
		for(CTLFormula t : satArrayList) {
			s = s.concat("  " + t.toString() + ", ");
		}
		return s;
	} //listSat

	//Getter and Setters
	public String getLabel() { return label; }
	public void setLabel(String label) { this.label = label;}
	public ArrayList<String> getPrepAtomsArrayList() { return prepAtomsArrayList; }
	public void setPrepAtomsArrayList(ArrayList<String> prepAtomsArrayList) { this.prepAtomsArrayList = prepAtomsArrayList; }
	public ArrayList<CTLFormula> getSatArrayList() { return satArrayList; }
	public void setSatArrayList(ArrayList<CTLFormula> satArrayList) { this.satArrayList = satArrayList; }
	public void clearSatArrayList() { satArrayList = new ArrayList<CTLFormula>(); }
	public KripkeElement getNxtKE() { return nxtKE; }
	public void setNxtKE(KripkeElement nxtKE) { this.nxtKE = nxtKE; }
	
	
} //class KripkeStruct
