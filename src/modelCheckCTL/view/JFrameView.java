package modelCheckCTL.view;

import javax.swing.JFrame;

import modelCheckCTL.controller.Controller;
import modelCheckCTL.model.AbstractModel;
import modelCheckCTL.model.Model;
import modelCheckCTL.model.ModelListener;

/**
 * @author ssiroky
 *
 */
abstract public class JFrameView extends JFrame implements View, ModelListener {

	private static final long serialVersionUID = 1L;
	private Model model;
	private Controller controller;

	/**
	 * Constructor set the model and controller.
	 * 
	 * @param model
	 * @param controller
	 */
	public JFrameView(Model model, Controller controller) {
		setModel(model);
		setController(controller);
	}

	/**
	 * Register the listener with the model.
	 */
	public void registerWithModel() {
		System.out.println("JFrameView.registerWithModel ");
		((AbstractModel) model).addModelListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.view.View#getController()
	 */
	public Controller getController() {
		System.out.println("JFrameView.getController ");
		return controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * modelCheckCTL.view.View#setController(modelCheckCTL.controller.Controller
	 * )
	 */
	public void setController(Controller controller) {
		System.out.println("JFrameView.setController ");
		this.controller = controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.view.View#getModel()
	 */
	public Model getModel() {
		System.out.println("JFrameView.getModel ");
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modelCheckCTL.view.View#setModel(modelCheckCTL.model.Model)
	 */
	public void setModel(Model model) {
		System.out.println("JFrameView.setModel ");
		this.model = model;
		registerWithModel();
	}

} // JFrameView
