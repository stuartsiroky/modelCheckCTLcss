package modelCheckCTL.view;

import modelCheckCTL.controller.Controller;
import modelCheckCTL.model.Model;

/**
 * @author ssiroky
 *
 */
public interface View {
	Controller getController();

	void setController(Controller controller);

	Model getModel();

	void setModel(Model model);
} // View
