package modelCheckCTL.util;

import java.io.File;

import javax.swing.JFileChooser;

import modelCheckCTL.view.ModelCheckCTLView;

/**
 * This object is used to open a file.
 * 
 * @author ssiroky
 *
 */
public class ModelCheckCTLUtil extends AbstractUtil {

	/**
	 * Constructor set the view object.
	 * 
	 * @param view
	 */
	public ModelCheckCTLUtil(ModelCheckCTLView view) {
		super(view);
	} // constructor

	/**
	 * Open a file and return the handle.
	 * 
	 * @return File handle
	 */
	public File openFile() {
		// Could not do this here??
		// int returnVal =
		// ModelCheckCTLView.getFc().showOpenDialog(ModelCheckCTLUtil.this);

		File rtnFile = null;
		ModelCheckCTLView lview = ((ModelCheckCTLView) getView());

		if (lview.getReturnVal() == JFileChooser.APPROVE_OPTION) {
			rtnFile = lview.getFc().getSelectedFile();
			lview.displayResults("Opening: " + rtnFile.getName() + ".\n");
		} else {
			lview.displayResults("Open command cancelled by user.");
		}
		lview.getLog().setCaretPosition(
				lview.getLog().getDocument().getLength());
		return rtnFile;
	} // openFile

} // class ModelCheckCTLUtil
