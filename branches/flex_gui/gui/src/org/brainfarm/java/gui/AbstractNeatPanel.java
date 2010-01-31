package org.brainfarm.java.gui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.context.INeatContextListener;
import org.brainfarm.java.gui.api.INeatPanel;

public abstract class AbstractNeatPanel implements INeatPanel, INeatContextListener, ActionListener {

	public static final String EXIT_BUTTON_LABEL = " E X I T ";
	
	/**
	 * Controller for this view.
	 */
	protected IGuiController controller;
	
	/**
	 * Display name for this panel.
	 */
	protected String displayName = "";
	
	/**
	 * A reference to the frame that this view is displayed within.
	 */
	protected JFrame frame;
	
	/**
	 * The panel this view will be built inside.
	 */
	protected JPanel panel;
	
	/**
	 * Constructor.
	 * 
	 * @param frame
	 * @param controller
	 * @param context
	 */
	public AbstractNeatPanel(JFrame frame, IGuiController controller, INeatContext context) {
		this.controller = controller;
		context.addListener(this);
		
		buildInterface(frame);
	}
	
	/**
	 * Builds the view.
	 * Should be overridden in child classes to create the view specific elements.
	 * 
	 * @param frame
	 */
	protected void buildInterface(JFrame frame) {
		
		// Hold a reference to the frame.
		this.frame = frame;
		
		// Create the base panel the rest of the view will be built inside.
		panel = new JPanel();
	}
	
	/**
	 * Get the title for this view.
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Get the JPanel instance this view is built inside.
	 */
	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	/**
	 * Handle a user action.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(EXIT_BUTTON_LABEL)) {
			System.exit(0);
		}
	}
	
	/**
	 * Set some common values on a constraint.
	 * 
	 * @param constraints
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param weightx
	 * @param weighty
	 */
	protected void buildConstraints(GridBagConstraints constraints, 
									int gridx, int gridy, 
									int gridwidth, int gridheight, 
									int weightx, int weighty) {
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
	}
}
