package modelCheckCTL.controller;

import modelCheckCTL.model.Model;
import modelCheckCTL.util.Util;
import modelCheckCTL.view.View;

/**
 * @author ssiroky
 *
 */

public abstract class AbstractController implements Controller {

	private View view;
	private Model model;
	private Util util;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * modelCheckCTL.controller.Controller#setModel(modelCheckCTL.model.Model)
	 */
	public void setModel(Model model) {
		System.out.println("AbstractController.setModel");
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.controller.Controller#getModel()
	 */
	public Model getModel() {
		System.out.println("AbstractController.getModel");
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.controller.Controller#getView()
	 */
	public View getView() {
		System.out.println("AbstractController.getView");
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.controller.Controller#setView(modelCheckCTL.view.View)
	 */
	public void setView(View view) {
		System.out.println("AbstractController.setView");
		this.view = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.controller.Controller#setUtil(modelCheckCTL.util.Util)
	 */
	public void setUtil(Util util) {
		System.out.println("AbstractController.setUtil");
		this.util = util;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.controller.Controller#getUtil()
	 */
	public Util getUtil() {
		System.out.println("AbstractController.getUtil");
		return util;
	}

} // AbstractController
