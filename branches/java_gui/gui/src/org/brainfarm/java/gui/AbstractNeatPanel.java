package org.brainfarm.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.brainfarm.java.gui.api.INeatPanel;
import org.brainfarm.java.neat.context.INeatContextListener;

public abstract class AbstractNeatPanel implements INeatPanel, INeatContextListener, ActionListener {

	public static final String EXIT_BUTTON_LABEL = " E X I T ";
	
	protected String displayName = "";
	
	protected JPanel panel;
	
	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(EXIT_BUTTON_LABEL)) {
			System.exit(0);
		}
	}
}
