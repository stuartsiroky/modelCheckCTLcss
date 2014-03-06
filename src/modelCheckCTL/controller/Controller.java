package modelCheckCTL.controller;

import modelCheckCTL.model.Model;
import modelCheckCTL.view.View;
import modelCheckCTL.util.Util;

/**
 * This interface allows communication between the controller and the following:
 * CTLModel,
 * CTLView,
 * CTLUtil
 * @author ssiroky
 *
 */
public interface Controller {
	void setModel(Model model);
	Model getModel();
	View getView();
	void setView(View view);
	Util getUtil();
	void setUtil(Util util);
	}
