package org.brainfarm.java.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.BasicConfigurator;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.context.SpringNeatContext;
import org.brainfarm.java.util.log.HistoryLog;

public class MainGui extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1999868377596652480L;

	JFrame f1;

	private Parameter a_parameter;
	private ExperimentPanel a_session;
	private Generation a_generation;
	private Grafi a_grafi;

	JTabbedPane jtabbedPane1;

	public static void main(String[] args) {
		
		try {
			JFrame jp = new JFrame("  J N E A T   Java simulator for   NeuroEvolution of Augmenting Topologies  ");
			new MainGui(jp);

			// jp.getContentPane().add(pn1);
			jp.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});

			jp.pack();
			jp.setSize(800, 600);
			jp.setVisible(true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public MainGui(JFrame frame) {

		BasicConfigurator.configure();
		
		f1 = frame;
		
		INeatContext context = new SpringNeatContext();
		IGuiController controller = new GuiController(context);
		
		a_parameter = new Parameter(f1, controller, context);
		a_session = new ExperimentPanel(f1, controller, context);
		a_generation = new Generation(f1, controller, context);
		a_grafi = new Grafi(f1);

		logger = new HistoryLog();

		a_parameter.setLog(logger);
		a_session.setLog(logger);
		a_generation.setLog(logger);
		a_grafi.setLog(logger);

		jtabbedPane1 = new JTabbedPane();
		jtabbedPane1.addTab("jneat parameter", a_parameter.pmain);
		jtabbedPane1.addTab("session parameter", a_session.pmain);
		jtabbedPane1.addTab("start simulation", a_generation.pmain);
		jtabbedPane1.addTab("view graph", a_grafi.pmain);
		jtabbedPane1.setSelectedIndex(0);

		Container contentPane = f1.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JSplitPane paneSplit1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jtabbedPane1, logger);
		paneSplit1.setOneTouchExpandable(true);
		paneSplit1.setContinuousLayout(true);
		paneSplit1.setDividerSize(10);
		jtabbedPane1.setMinimumSize(new Dimension(400, 50));
		logger.setMinimumSize(new Dimension(100, 50));

		paneSplit1.setDividerLocation(410);

		paneSplit1.setBorder(BorderFactory.createCompoundBorder(
							 BorderFactory.createEmptyBorder(2, 2, 2, 2), 
							 BorderFactory.createEmptyBorder(2, 2, 2, 2)));

		contentPane.add(paneSplit1, BorderLayout.CENTER);

	}

	protected HistoryLog logger;
}