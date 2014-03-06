package modelCheckCTL.controller;

import java.io.File;

import modelCheckCTL.model.*;
import modelCheckCTL.view.*; 
import modelCheckCTL.util.*;

/**
 * The controller is a go between the model and the view object.
 * @author ssiroky
 *
 */

public class ModelCheckCTLController extends AbstractController {

	private File ksLoaded = null;
	
	public ModelCheckCTLController(){
		setKsLoaded(null);
		setModel(new ModelCheckCTLModel());
		setView(new ModelCheckCTLView((ModelCheckCTLModel)getModel(), this));
		setUtil(new ModelCheckCTLUtil((ModelCheckCTLView)getView()));
		((JFrameView)getView()).setVisible(true);
	} //constructor
	
	/**
	 * This function takes the signals from the gui and calls the model
	 * to do the work. It will then inform the view what has happened.
	 * @param option This is the button on the gui that is pushed by the user.
	 */
	public void operation(String option){
		ModelCheckCTLView lview = ((ModelCheckCTLView)getView());
		ModelCheckCTLModel lmodel = ((ModelCheckCTLModel)getModel());
		ModelCheckCTLUtil lutil   = ((ModelCheckCTLUtil)getUtil());
		
		if(option.equals(ModelCheckCTLView.CTL_FORMULA)){
			// get the text field and parse the formula
			// spit out the result to the text area
			// Will need to do the calculation on the kripke stucture
			String formula;
			//lview.displayResults("Loading CTL Formula");
			formula = lview.getCtlFormula().getText();
			lmodel.parseFormula(formula);
			
		}
		else if(option.equals(ModelCheckCTLView.KRIPKE_STRUCT)){
			// load the kripke structure and then print it out
			// notify of any errors
			lview.displayResults("Loading Kripke Stucture");
			
			setKsLoaded(lutil.openFile());
			//System.out.println(ksLoaded.toString());
			if(getKsLoaded() != null) {
				lmodel.parseFile(getKsLoaded());
			}
		}
		else if(option.equals(ModelCheckCTLView.CHECK_STATE)){
			// set the starting state and run the label alg in the model	
			String ss;
			
			ss = lview.getStartState().getText();
			//lview.displayResults("Checking for Starting State - " + ss);
			
			lmodel.checkKS(ss);
			
		}

	} //operation

	/**
	 * Set the file handle.
	 * @param ksLoaded
	 */
	public void setKsLoaded(File ksLoaded) { this.ksLoaded = ksLoaded; }
	/**
	 * Returns a handle to the file that was loaded.
	 * @return File
	 */
	public File getKsLoaded() { return ksLoaded; }

} //ModelCheckCTLController
