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
		System.out.println("AbstractUtil.getView ");
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.util.Util#setView(modelCheckCTL.view.View)
	 */
	public void setView(View view) {
		System.out.println("AbstractUtil.setView ");
		this.view = view;
	}

} // class AbstractUtil
