package org.brainfarm.java.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.apache.log4j.BasicConfigurator;
import org.brainfarm.java.feat.api.context.IEvolutionContext;
import org.brainfarm.java.feat.context.EvolutionContext;
import org.brainfarm.java.gui.api.INeatPanel;

public class MainGui {

	private static final long serialVersionUID = -1999868377596652480L;

	/**
	 * System Entry Point for the Java Gui.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			JFrame frame = new JFrame("Brain Farm - NeuroEvolution of Augmenting Topologies");
			
			new MainGui(frame);

			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});

			frame.pack();
			frame.setSize(800, 600);
			frame.setVisible(true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates a Java GUI for interacting with Brain Farm.
	 * 
	 * @param frame
	 */
	public MainGui(JFrame frame) {

		// Configure the Logger.
		BasicConfigurator.configure();
		
		// Create a context and controller for the GUI.
		IEvolutionContext context = new EvolutionContext();
		IGuiController controller = new GuiController(context);

		// Create a tabbed pane and add the necessary GUI panels as tabs.
		JTabbedPane tabbedPane = new JTabbedPane();
		
		// Add the Neat Parameters Panel.
		INeatPanel parametersPanel = new NeatParametersPanel(frame, controller, context);
		tabbedPane.addTab(parametersPanel.getDisplayName(), parametersPanel.getPanel());
		
		// Add the Experiment Panel.
		INeatPanel experimentPanel = new ExperimentPanel(frame, controller, context);
		tabbedPane.addTab(experimentPanel.getDisplayName(), experimentPanel.getPanel());
		
		// Add the Evolution Panel.
		INeatPanel evolutionPanel = new EvolutionPanel(frame, controller, context);
		tabbedPane.addTab(evolutionPanel.getDisplayName(), evolutionPanel.getPanel());
		
		// Add the Graph Panel.
		INeatPanel graphPanel = new GraphPanel(frame, controller, context);
		tabbedPane.addTab(graphPanel.getDisplayName(), graphPanel.getPanel());
		
		// We want to start the app looking at the neat parameters panel.
		tabbedPane.setSelectedIndex(0);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}
}