package modelCheckCTL.view;

import javax.swing.*;

import modelCheckCTL.model.Model;
import modelCheckCTL.model.AbstractModel;
import modelCheckCTL.model.ModelListener;
import modelCheckCTL.view.View;
import modelCheckCTL.controller.Controller;

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
	 * @param model
	 * @param controller
	 */
	public JFrameView (Model model, Controller controller){
		setModel(model);
		setController(controller);
	}

	/**
	 * Register the listener with the model.
	 */
	public void registerWithModel(){
		((AbstractModel)model).addModelListener(this);
	}

	/* (non-Javadoc)
	 * @see modelCheckCTL.view.View#getController()
	 */
	public Controller getController(){return controller;}
	/* (non-Javadoc)
	 * @see modelCheckCTL.view.View#setController(modelCheckCTL.controller.Controller)
	 */
	public void setController(Controller controller){this.controller = controller;}
	/* (non-Javadoc)
	 * @see modelCheckCTL.view.View#getModel()
	 */
	public Model getModel(){return model;}
	/* (non-Javadoc)
	 * @see modelCheckCTL.view.View#setModel(modelCheckCTL.model.Model)
	 */
	public void setModel(Model model) {
		this.model = model;
		registerWithModel();
	}

} //JFrameView
