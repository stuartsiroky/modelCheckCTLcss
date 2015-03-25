package modelCheckCTL.model;

import java.awt.event.ActionEvent;

/**
 * This allows communication with the view.
 * 
 * @author ssiroky
 *
 */
public class ModelEvent extends ActionEvent {

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Constructor -
	 * 
	 * @param obj
	 * @param id
	 * @param msg
	 * @param val
	 */
	public ModelEvent(Object obj, int id, String msg, String val) {
		super(obj, id, msg);
		message = val;
		// can have case statement here to deal with differ id's
	}

	/**
	 * get the message from the model
	 * 
	 * @return String
	 */
	public String getOutput() {
		return message;
	}

} // class ModelEvent
