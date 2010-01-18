package org.brainfarm.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.params.AbstractNeatParameter;

public class NeatParametersPanel extends AbstractNeatPanel implements TableModelListener {

	private static Logger log = Logger.getLogger(NeatParametersPanel.class);
	
	private IGuiController controller;
	
	JFrame frame;
	//JPanel buttonPanel; // pannello comandi
	// detailPanel; // pannello grafico

	//JButton b1;
	//JButton b2;
	//JButton b3;
	//JButton b4;
	//JButton b5;

	JTextArea textArea;

	NeatParametersTableModel tableModel;
	JTable parametersTable;
	//Neat neatInstance;
	JScrollPane scrollPane;
	//JScrollPane paneScroll2;

	Container contentPane;
	//protected HistoryLog logger;

	/**
	 * pan1 constructor comment.
	 */
	public NeatParametersPanel(JFrame frame, IGuiController controller, INeatContext context) {

		this.controller = controller;
		
		context.addListener(this);
		
		displayName = "Neat Parameters";

		JPanel buttonPanel = new JPanel();
		JPanel detailPanel = new JPanel();

		JButton loadDefaultButton = new JButton(" Load default  ");
		loadDefaultButton.addActionListener(this);

		JButton loadFileButton = new JButton(" Load file.... ");
		loadFileButton.addActionListener(this);

		JButton writeButton = new JButton(" Write         ");
		writeButton.addActionListener(this);

		JButton writeFileButton = new JButton(" Write file... ");
		writeFileButton.addActionListener(this);

		JButton b5 = new JButton(" E X I T ");
		b5.addActionListener(this);

		Font fc = new Font("Dialog", Font.BOLD, 12);
		loadDefaultButton.setFont(fc);
		loadFileButton.setFont(fc);
		writeButton.setFont(fc);
		writeFileButton.setFont(fc);
		b5.setFont(fc);

		//
		// definizione layout del pannello comandi
		//
		GridBagLayout gbl_p2 = new GridBagLayout();
		GridBagConstraints gbc_p2 = new GridBagConstraints();
		buttonPanel.setLayout(gbl_p2);

		buttonPanel.setBorder(BorderFactory.createCompoundBorder(
							  BorderFactory.createTitledBorder("Command options"), 
							  BorderFactory.createEmptyBorder(10, 10, 2, 2)
							  ));

		detailPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(" j n e a t    parameter's "),
				BorderFactory.createEmptyBorder(10, 10, 2, 2)));

		gbc_p2.anchor = GridBagConstraints.NORTH;
		gbc_p2.fill = GridBagConstraints.BOTH;
		gbc_p2.gridheight = 2;
		gbc_p2.gridwidth = 1;
		gbc_p2.gridx = 0;
		gbc_p2.gridy = 1;
		gbc_p2.insets = new Insets(1, 2, 1, 2);
		gbc_p2.ipadx = 0;
		gbc_p2.ipady = 0;
		gbc_p2.weightx = 0.0;
		gbc_p2.weighty = .5;
		buttonPanel.add(loadDefaultButton);
		gbl_p2.setConstraints(loadDefaultButton, gbc_p2);

		gbc_p2.gridy = 3;
		buttonPanel.add(loadFileButton);
		gbl_p2.setConstraints(loadFileButton, gbc_p2);

		gbc_p2.gridy = 5;
		buttonPanel.add(writeButton);
		gbl_p2.setConstraints(writeButton, gbc_p2);

		gbc_p2.gridy = 7;
		buttonPanel.add(writeFileButton);
		gbl_p2.setConstraints(writeFileButton, gbc_p2);

		gbc_p2.anchor = GridBagConstraints.SOUTH;
		gbc_p2.fill = GridBagConstraints.HORIZONTAL;

		gbc_p2.gridheight = 2;
		gbc_p2.gridy = 10;
		gbc_p2.weighty = 5;

		buttonPanel.add(b5);
		gbl_p2.setConstraints(b5, gbc_p2);

		tableModel = new NeatParametersTableModel();
		parametersTable = new JTable(tableModel);	

		scrollPane = new JScrollPane(parametersTable);

		GridBagLayout gbl_p3 = new GridBagLayout();
		GridBagConstraints limiti = new GridBagConstraints();
		detailPanel.setLayout(gbl_p3);

		buildConstraints(limiti, 0, 0, 1, 4, 35, 90);
		limiti.fill = GridBagConstraints.BOTH;
		gbl_p3.setConstraints(scrollPane, limiti);
		detailPanel.add(scrollPane);

		buildConstraints(limiti, 1, 0, 2, 4, 55, 0);
		limiti.fill = GridBagConstraints.BOTH;
		limiti.anchor = GridBagConstraints.CENTER;

		textArea = new JTextArea("", 10, 60);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setWrapStyleWord(true);
		textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		textArea.setVisible(true);

		textArea.setBackground(new Color(255, 242, 232));

		panel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		panel.setLayout(gbl);

		limiti = new GridBagConstraints();
		buildConstraints(limiti, 0, 0, 1, 5, 0, 100);
		limiti.anchor = GridBagConstraints.WEST;
		limiti.fill = GridBagConstraints.VERTICAL;
		panel.add(buttonPanel);
		gbl.setConstraints(buttonPanel, limiti);

		limiti = new GridBagConstraints();
		buildConstraints(limiti, 1, 0, 2, 5, 100, 0);
		limiti.anchor = GridBagConstraints.WEST;
		limiti.fill = GridBagConstraints.BOTH;
		panel.add(detailPanel);
		gbl.setConstraints(detailPanel, limiti);

		contentPane = frame.getContentPane();
		BorderLayout bl = new BorderLayout();
		contentPane.setLayout(bl);
		contentPane.add(panel, BorderLayout.CENTER);
	}

	public void valueChanged(ListSelectionEvent e) {
		
		log.debug("List Selection event " + e.getValueIsAdjusting());
		
		int irow = 0;
		Object s_descr = null;
		Object s2 = null;
		AbstractNeatParameter ox = null;
		String r2 = null;
		String r3 = null;
		String tipo = null;

		if (e.getValueIsAdjusting()) {
			return;
		}

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();

		if (!lsm.isSelectionEmpty()) {
			irow = lsm.getMinSelectionIndex();
			ox = tableModel.data.get(irow);
			//s_descr = neatInstance.getDescription((String) ox.o1);
			/*s2 = ox.getVal();

			if (s2 instanceof Integer) {
				tipo = new String(" integer ");
			}
			if (s2 instanceof Double) {
				tipo = new String(" double");
			}

			r2 = "\n Current setting is " + s2;
			r3 = s_descr + r2 + tipo;
			textArea.setText(r3);

			paneScroll2.revalidate();
			paneScroll2.validate();*/

		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o1 = null;
		Object o2 = null;
		String xret = null;
		String name;
		String tmp1;
		String tmp2;
		boolean rc = false;

		JButton Pulsante = (JButton) e.getSource();

		if (e.getActionCommand().equals(" E X I T ")) {
			System.exit(0);
		}

		else if (e.getActionCommand().equals(" Load default  ")) {
			controller.loadDefaultParameters();
		}
		else if (e.getActionCommand().equals(" Load file.... ")) {
			controller.loadParameters(frame);
		}

		else if (e.getActionCommand().equals(" Write         ")) {
			/*name = EnvRoutine.getDefaultParameterFileName();
			logger.sendToLog(" writing file parameter " + name + "...");
			Neat.updateParam(tableModel);
			Neat.writeParam(name);
			logger.sendToLog(" okay : file writed");
			logger.sendToStatus("READY");
*/
		}

		else if (e.getActionCommand().equals(" Write file... ")) {

			/*FileDialog fd = new FileDialog(f1, "load file parameter",
					FileDialog.SAVE);
			fd.setVisible(true);

			tmp1 = fd.getDirectory();
			tmp2 = fd.getFile();

			if (tmp1 != null && tmp2 != null) {

				name = tmp1 + tmp2;
				logger.sendToLog(" writing file parameter " + name + "...");
				neatInstance.updateParam(tableModel);
				neatInstance.writeParam(name);
				logger.sendToLog(" okay : file writed");
				logger.sendToStatus("READY");

			}*/
		}

	}

	public void buildConstraints(GridBagConstraints gbc, int gx, int gy,
			int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
	}

	/**
	 * A new Neat Context has been loaded.
	 * Refresh the table model with the new data so the user can edit.
	 */
	@Override
	public void contextChanged(INeatContext context) {
		tableModel.data.clear();
		tableModel.rows = -1;
		tableModel.setData(context.getNeat().getParameters());
		parametersTable.getModel().addTableModelListener(this);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        int mColIndex = e.getColumn();
        System.out.println("table data changed");
        log.debug("Table Changed: " + firstRow + " " + lastRow + " " + mColIndex + " " + e.getType());
        
        switch (e.getType()) {
        
        	default:
        		break;
        }

	}

	@Override
	public void experimentChanged(INeatContext context) {
		// TODO Auto-generated method stub
		
	}
}