package modelCheckCTL.util;

import modelCheckCTL.view.View;

/**
 * @author ssiroky
 *
 */

public abstract class AbstractUtil implements Util {

	private View view;

	/**
	 * @param view
	 *            set the view object
	 */
	public AbstractUtil(View view) {
		setView(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.util.Util#getView()
	 */
	public View getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.util.Util#setView(modelCheckCTL.view.View)
	 */
	public void setView(View view) {
		this.view = view;
	}

} // class AbstractUtil
