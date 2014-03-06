package modelCheckCTL.model;

import modelCheckCTL.model.ModelEvent;

/**
 * Registered with the view.
 * @author ssiroky
 *
 */
public interface ModelListener {
	public void modelChanged(ModelEvent event);
}
