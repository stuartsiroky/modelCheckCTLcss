package modelCheckCTL.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import modelCheckCTL.controller.ModelCheckCTLController;
import modelCheckCTL.model.ModelCheckCTLModel;
import modelCheckCTL.model.ModelEvent;

/**
 * This object is the main object and creates the GUI for the user. It allows a
 * file representing a Kripke Structure to be loaded. It takes a CTL formula and
 * a starting state from the user. Buttons exist to submit all of these items.
 * The GUI also contains a log output area for the results of the actions taken.
 * 
 * @author ssiroky
 *
 */

public class ModelCheckCTLView extends JFrameView {

	private static final long serialVersionUID = 1L;
	private JTextArea log;
	private JTextField ctlFormula = new JTextField();
	private JTextField startState = new JTextField();
	private JFileChooser fc;
	public static final String CTL_FORMULA = "Submit CTL Formula";
	public static final String KRIPKE_STRUCT = "Load Kripke Structure";
	public static final String CHECK_STATE = "Check starting state";
	public JButton loadKSButton;
	public JButton loadCTLButton;
	public JButton stateButton;
	private int returnVal;

	/**
	 * Constructor - Creates the GUI.
	 * 
	 * @param model
	 * @param controller
	 */
	public ModelCheckCTLView(ModelCheckCTLModel model,
			ModelCheckCTLController controller) {
		super(model, controller);

		JPanel textPanel = new JPanel();
		// Field to set ctl formula
		ctlFormula.setText("Enter CTL Formula");// TODO set default for testing
		ctlFormula.setEditable(true);
		// this.getContentPane().add(ctlFormula,BorderLayout.NORTH);

		startState.setText("Enter Starting State");// TODO set default for
													// testing
		startState.setEditable(true);
		// this.getContentPane().add(startState,BorderLayout.AFTER_LAST_LINE);

		textPanel.setLayout(new GridLayout(1, 2, 5, 5));
		this.getContentPane().add(textPanel, BorderLayout.NORTH);
		textPanel.add(ctlFormula, null);
		textPanel.add(startState, null);

		JPanel buttonPanel = new JPanel();
		Handler handler = new Handler();
		// Create the open button.
		loadKSButton = new JButton(KRIPKE_STRUCT);
		loadKSButton.addActionListener(handler);

		// Create the save button.
		loadCTLButton = new JButton(CTL_FORMULA);
		loadCTLButton.addActionListener(handler);

		// Create the save button.
		stateButton = new JButton(CHECK_STATE);
		stateButton.addActionListener(handler);

		// For layout purposes, put the buttons in a separate panel
		buttonPanel.setLayout(new GridLayout(1, 3, 5, 5));

		// Add the elements
		this.getContentPane().add(buttonPanel, BorderLayout.CENTER);
		buttonPanel.add(loadKSButton, null);
		buttonPanel.add(loadCTLButton, null);
		buttonPanel.add(stateButton, null);

		// Create the log
		setLog(new JTextArea(40, 20));
		getLog().setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(getLog());
		this.getContentPane().add(logScrollPane, BorderLayout.SOUTH);

		// Create a file chooser
		fc = new JFileChooser();

		pack();

	} // constructor

	// Now implement the necessary event handling code
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * modelCheckCTL.model.ModelListener#modelChanged(modelCheckCTL.model.ModelEvent
	 * )
	 */
	public void modelChanged(ModelEvent event) {
		String msg = event.getOutput() + "";
		getLog().append(msg);
	}

	/**
	 * Display results to the log text area.
	 * 
	 * @param s
	 */
	public void displayResults(String s) {
		getLog().append(s + "\n");
	} // displayResults

	// Inner classes for Event Handling
	/**
	 * Inner class only used here.
	 * 
	 * @author ssiroky
	 *
	 */
	class Handler implements ActionListener {
		// Event handling is handled locally
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loadKSButton) {
				returnVal = fc.showOpenDialog(ModelCheckCTLView.this);
			}
			((ModelCheckCTLController) getController()).operation(e
					.getActionCommand());
		}
	}// class Handler

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ModelCheckCTLController();
	}

	// getters and setters
	public JTextArea getLog() {
		return log;
	}

	public void setLog(JTextArea log) {
		this.log = log;
	}

	public JTextField getCtlFormula() {
		return ctlFormula;
	}

	public JTextField getStartState() {
		return startState;
	}

	public void setStartState(JTextField startState) {
		this.startState = startState;
	}

	public void setCtlFormula(JTextField ctlFormula) {
		this.ctlFormula = ctlFormula;
	}

	public JFileChooser getFc() {
		return fc;
	}

	public void setFc(JFileChooser fc) {
		this.fc = fc;
	}

	public int getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(int returnVal) {
		this.returnVal = returnVal;
	}

} // ModelCheckCTLView

