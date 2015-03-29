package modelCheckCTL.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ssiroky
 *
 */
public abstract class AbstractModel implements Model {

	@SuppressWarnings("rawtypes")
	private ArrayList listeners = new ArrayList(5);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * modelCheckCTL.model.Model#notifyChanged(modelCheckCTL.model.ModelEvent)
	 */
	public void notifyChanged(ModelEvent event) {
		System.out.println("AbstractModel.notifiyChanged ");
		@SuppressWarnings("rawtypes")
		ArrayList list = (ArrayList) listeners.clone();
		@SuppressWarnings("rawtypes")
		Iterator it = list.iterator();
		while (it.hasNext()) {
			ModelListener ml = (ModelListener) it.next();
			ml.modelChanged(event);
		}
	}

	/**
	 * Add a listener object.
	 * 
	 * @param l
	 */
	@SuppressWarnings("unchecked")
	public void addModelListener(ModelListener l) {
		System.out.println("AbstractModel.addModelListener ");
		listeners.add(l);
	}

	/**
	 * Remove a listener object.
	 * 
	 * @param l
	 */
	public void removeModelListener(ModelListener l) {
		System.out.println("AbstractModel.removeModelListener ");
		listeners.remove(l);
	}
} // AbstractModel
