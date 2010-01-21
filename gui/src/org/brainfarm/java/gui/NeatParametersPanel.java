package org.brainfarm.java.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.brainfarm.java.neat.api.context.INeatContext;

public class NeatParametersPanel extends AbstractNeatPanel{

	//private static Logger logger = Logger.getLogger(NeatParametersPanel.class);
	
	public static final String LOAD_DEFAULT_BUTTON_LABEL 	= "Load Default";
	public static final String LOAD_FILE_BUTTON_LABEL 		= "Load File";
	public static final String SAVE_BUTTON_LABEL 			= "Save";
	public static final String SAVE_AS_BUTTON_LABEL 		= "Save As...";

	private NeatParametersTableModel tableModel;

	/**
	 * pan1 constructor comment.
	 */
	public NeatParametersPanel(JFrame frame, IGuiController controller, INeatContext context) {

		super(frame, controller, context);
		
		displayName = "Neat Parameters";	
	}
	
	@Override
	protected void buildInterface(JFrame frame) {
		
		super.buildInterface(frame);
		
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
	
		panel.add(createButtonPanel(frame, layout));		
		panel.add(createParameterPanel(frame, layout));		

		Container contentPane = frame.getContentPane();
		BorderLayout bl = new BorderLayout();
		contentPane.setLayout(bl);
		contentPane.add(panel, BorderLayout.CENTER);
	}
	
	private JPanel createParameterPanel(JFrame frame, GridBagLayout layout) {
		
		JPanel parameterPanel = new JPanel();
		
		parameterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(" j n e a t    parameter's "),
				BorderFactory.createEmptyBorder(10, 10, 2, 2)));
		
		tableModel = new NeatParametersTableModel();
		JTable parametersTable = new JTable(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(parametersTable);
		
		GridBagLayout panelLayout = new GridBagLayout();		
		parameterPanel.setLayout(panelLayout);

		GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
		buildConstraints(scrollPaneConstraints, 0, 0, 1, 4, 35, 90);
		scrollPaneConstraints.fill = GridBagConstraints.BOTH;
		panelLayout.setConstraints(scrollPane, scrollPaneConstraints);
		parameterPanel.add(scrollPane);
		
		GridBagConstraints panelConstraints = new GridBagConstraints();
		buildConstraints(panelConstraints, 1, 0, 2, 5, 100, 0);
		panelConstraints.anchor = GridBagConstraints.WEST;
		panelConstraints.fill = GridBagConstraints.BOTH;
		
		layout.setConstraints(parameterPanel, panelConstraints);
		
		return parameterPanel;
	}
	
	private JButton buildButton(String label, Font font, GridBagLayout layout, GridBagConstraints constraints) {
		JButton button = new JButton(label);
		
		button.setFont(font);
		layout.setConstraints(button, constraints);
		button.addActionListener(this);		
		
		return button;
	}
	
	private JPanel createButtonPanel(JFrame frame, GridBagLayout layout) {
		JPanel buttonPanel = new JPanel();
		
		Font font = new Font("Dialog", Font.BOLD, 12);
		
		GridBagLayout panelLayout = new GridBagLayout();
		GridBagConstraints panelConstraints = new GridBagConstraints();
		panelConstraints.anchor = GridBagConstraints.NORTH;
		panelConstraints.fill = GridBagConstraints.BOTH;
		panelConstraints.gridheight = 2;
		panelConstraints.gridwidth = 1;
		panelConstraints.gridx = 0;		
		panelConstraints.insets = new Insets(1, 2, 1, 2);
		panelConstraints.ipadx = 0;
		panelConstraints.ipady = 0;
		panelConstraints.weightx = 0.0;
		panelConstraints.weighty = .5;
		
		buttonPanel.setLayout(panelLayout);
		
		panelConstraints.gridy = 1;
		buttonPanel.add(buildButton(LOAD_DEFAULT_BUTTON_LABEL, font, panelLayout, panelConstraints));

		panelConstraints.gridy = 3;		
		buttonPanel.add(buildButton(LOAD_FILE_BUTTON_LABEL, font, panelLayout, panelConstraints));
		
		panelConstraints.gridy = 5;		
		buttonPanel.add(buildButton(SAVE_BUTTON_LABEL, font, panelLayout, panelConstraints));

		panelConstraints.gridy = 7;
		buttonPanel.add(buildButton(SAVE_AS_BUTTON_LABEL, font, panelLayout, panelConstraints));

		panelConstraints.anchor = GridBagConstraints.SOUTH;
		panelConstraints.fill = GridBagConstraints.HORIZONTAL;
		panelConstraints.gridheight = 2;
		panelConstraints.gridy = 10;
		panelConstraints.weighty = 5;
		buttonPanel.add(buildButton(EXIT_BUTTON_LABEL, font, panelLayout, panelConstraints));

		buttonPanel.setBorder(BorderFactory.createCompoundBorder(
							  BorderFactory.createTitledBorder("Command options"), 
							  BorderFactory.createEmptyBorder(10, 10, 2, 2)
							  ));

		GridBagConstraints constraints = new GridBagConstraints();
		buildConstraints(constraints, 0, 0, 1, 5, 0, 100);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.VERTICAL;
		
		layout.setConstraints(buttonPanel, constraints);
		
		return buttonPanel;
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Check for an EXIT event first...
		super.actionPerformed(e);

		if (e.getActionCommand().equals(LOAD_DEFAULT_BUTTON_LABEL)) {
			// Load the default NEAT parameters.
			controller.loadDefaultParameters();
			
		} else if (e.getActionCommand().equals(LOAD_FILE_BUTTON_LABEL)) {
			// Load the NEAT parameters from an external file.
			controller.loadParameters(frame);
			
		} else if (e.getActionCommand().equals(SAVE_BUTTON_LABEL)) {
			// Save out the current NEAT parameters.
			controller.saveParameters();

		} else if (e.getActionCommand().equals(SAVE_AS_BUTTON_LABEL)) {
			// Write the current NEAT parameters to an external file.
			controller.saveParameters(frame);
		}
	}	

	/**
	 * A new Neat Context has been loaded.
	 * Refresh the table model with the new data so the user can edit.
	 */
	@Override
	public void contextChanged(INeatContext context) {
		tableModel.setData(context.getNeat().getParameters());
	}

	@Override
	public void experimentChanged(INeatContext context) {}
}