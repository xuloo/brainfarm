package org.brainfarm.java.gui;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.brainfarm.java.gui.api.INeatPanel;
import org.brainfarm.java.neat.context.INeatContextListener;

public abstract class AbstractNeatPanel implements INeatPanel, INeatContextListener, ActionListener {

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
}
