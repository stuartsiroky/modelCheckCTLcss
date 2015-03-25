package modelCheckCTL.model;

import java.io.File;

import kripke.KripkeElement;
import kripke.KripkeStruct;

/**
 * The model will create a directed graph to represent the kripke structure
 * Parse and simplify a CTL formula and set the starting state and label the
 * kripke structure to find out if it is satisfied. It will send the results
 * back to the view object.
 * 
 * @author ssiroky
 *
 */
public class ModelCheckCTLModel extends AbstractModel {

	private KripkeStruct ks;
	private CTLParser parser;
	private String ctlFormula = "";

	/**
	 * Give a file that represents a kripke structure parse it and create a
	 * kripke structure for it.
	 * 
	 * @param f
	 *            File handle
	 */
	public void parseFile(File f) {
		ks = new KripkeStruct();
		String msg = "";
		try {
			if (ks.openKripkeFile(f)) {
				// Notify of change
				msg = "\n====================";
				msg = msg.concat(ks.toString());
				msg = msg.concat("====================\n");
			}
		} catch (ModelException e) {
			msg = ks.getMsg();
			msg = msg.concat(e.getMessage()); // TODO
			ks = null;
			e.printStackTrace();
		} finally {
			modelViewNotify(msg);
		}
	} // parseFile

	/**
	 * Given a CTL formula have the model process it
	 * 
	 * @param f
	 *            String
	 * @return boolean
	 */
	@SuppressWarnings("finally")
	public boolean parseFormula(String f) {
		parser = new CTLParser();
		ctlFormula = "";
		String msg = "";
		// if the formula parses and kripke structure is not null
		// then go ahead and run the labeling
		try {
			ctlFormula = parser.translate(f);
			msg = "\n====================";
			msg = msg.concat("\nSubmitted Formula : " + ctlFormula + "\n");
			msg = msg.concat("====================\n");
		} catch (ModelException e) {
			msg = parser.getMsg();
			msg = msg.concat(e.getMessage()); // TODO
			parser = null;
			ctlFormula = "";
			e.printStackTrace();
		} finally {
			modelViewNotify(msg);
			if (ctlFormula.equals("")) {
				return false;
			} else {
				return true;
			}
		}

	} // parseFormula

	/**
	 * Run the model to check the Kripke Structure need a valid kripke
	 * structure, starting state and formula.
	 * 
	 * @param ss
	 *            String for the starting state
	 */
	public void checkKS(String ss) {
		String msg = "";
		LabelCTLAlg lctl = null;

		if (ks != null && parser != null) {
			msg = "\n====================";
			msg = msg.concat("\nChecking formula with starting state : " + ss
					+ "\n");
			msg = msg.concat("====================\n");
			modelViewNotify(msg);
			msg = "";

			try {
				lctl = new LabelCTLAlg(ks, ss, parser.getCtlFormula());
				msg = "\n====================";
				msg = msg.concat("Results of labeling ");
				msg = msg.concat("====================\n");
				for (KripkeElement k : lctl.getWks()) {
					msg = msg.concat(k.toString() + "\n");
				}
				msg = msg.concat("\n====================");
				msg = msg.concat(lctl.getMsg());
				msg = msg.concat("====================\n");

			} catch (ModelException e) {
				msg = ks.getMsg();
				msg = msg.concat(parser.getMsg()); // TODO
				msg = msg.concat(e.getMessage());
				parser = null;
				ctlFormula = "";
				e.printStackTrace();
			} finally {
				modelViewNotify(msg);
			}
		} else {
			if (ks == null) {
				String msg1 = "Please load a valid kripke stucture.\n";
				msg = msg.concat(msg1);
			}
			if (parser == null) {
				String msg2 = "Please load a valid CTL formula.\n";
				msg = msg.concat(msg2);
			}
		}

		modelViewNotify(msg);

	} // checkKS

	/**
	 * Notification message to the view object.
	 * 
	 * @param msg
	 *            String
	 */
	private void modelViewNotify(String msg) {
		ModelEvent me = new ModelEvent(this, 1, "", msg);
		notifyChanged(me);
	}

} // ModelCheckCTLModel
