package org.brainfarm.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.context.INeatContext;

public class ExperimentPanel extends AbstractNeatPanel {

	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getLogger(ExperimentPanel.class);
	
	private static final String LOAD_EXPERIMENT_FILE_LABEL = "Load Experiment";
	private static final String LOAD_DEFAULT_EXPERIMENT_LABEL = " Load sess default ";
	private static final String WRITE_EXPERIMENT_LABEL = " Write sess        ";
	private static final String WRITE_EXPERIMENT_FILE_LABEL = " Write sess file...";
	private static final String LOAD_FITNESS_CLASS_LABEL = " Load class fitness";
	private static final String LOAD_DATA_INPUT_CLASS_LABEL = " Load class data input";
	private static final String LOAD_DATA_TARGET_CLASS_LABEL = " Load class data target";
	private static final String SET_EXPERIMENT_SKELETON_LABEL = " Set session file  skeleton ";
	private static final String SET_FITNESS_CLASS_SKELETON_LABEL = " Set fitness class skeleton ";
	private static final String SET_DATA_INPUT_CLASS_SKELETON_LABEL = " Set data_inp class skeleton ";
	private static final String SET_DATA_TARGET_CLASS_SKELETON = " Set data_tgt class skeleton ";
	private static final String CHECK_KEYWORD_LABEL = " C H E C K  keyword ";
	private static final String COMPILE_LABEL = " C O M P I L E ";	

	/**
	 * Panel used to display contents of .java files loaded by the user.
	 */
	private JPanel textOutputPanel;
	
	/**
	 * Panel used to display the parameters for the current experiment.
	 */
	private JPanel experimentParametersPanel;
	
	/**
	 * The JTextPane object that's used to render text.
	 */
	private JTextPane textPane;
	
	/**
	 * The TableModel used to hold the data for the experiment parameters.
	 */
	private AbstractTableModel tableModel;
	
	/**
	 * Constructor. Creates a new ExperimentPanel.
	 * 
	 * @param frame
	 * @param controller
	 * @param context
	 */
	public ExperimentPanel(JFrame frame, IGuiController controller, INeatContext context) {

		super(frame, controller, context);
		
		displayName = "Experiment Settings";
	}
	
	@Override
	protected void buildInterface(JFrame frame) {
		super.buildInterface(frame);
		
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);

		// Add the button panel.
		panel.add(buildButtonPanel(frame, layout));
		
		// Create the output panels - hold a reference to them
		// as we'll be swapping them depending on user selections.
		textOutputPanel = buildTextOutputPanel(frame, layout);	
		experimentParametersPanel = buildTablePanel(frame, layout);
		
		// Start the app showing the empty parameters table.
		panel.add(experimentParametersPanel);		

		// interface to main method of this class
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel, BorderLayout.CENTER);
	}
	
	private JPanel buildTablePanel(JFrame frame, GridBagLayout layout) {
		JPanel parameterPanel = new JPanel();
		
		parameterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Experiment Parameters"),
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
	
	private JPanel buildTextOutputPanel(JFrame frame, GridBagLayout layout) {
		JPanel textPanel = new JPanel();
		
		
		textPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(" Edit session "), BorderFactory
				.createEmptyBorder(10, 10, 2, 2)));		

		textPane = new JTextPane();
		textPane.setEditable(true);
		textPane.setBackground(new Color(255, 252, 242));

		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(2, 2, 2, 2), BorderFactory
				.createEtchedBorder()));

		GridBagLayout textLayout = new GridBagLayout();
		GridBagConstraints textConstraints = new GridBagConstraints();
		textPanel.setLayout(textLayout);

		buildConstraints(textConstraints, 0, 0, 1, 1, 100, 100);
		textConstraints.anchor = GridBagConstraints.NORTH;
		textConstraints.fill = GridBagConstraints.BOTH;
		textLayout.setConstraints(scrollPane, textConstraints);
		textPanel.add(scrollPane);
		
		GridBagConstraints constraints = new GridBagConstraints();
		buildConstraints(constraints, 1, 0, 4, 5, 100, 0);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		
		layout.setConstraints(textPanel, constraints);
		
		return textPanel;
	}
	
	private JButton buildButton(String label, Font font, GridBagLayout layout, GridBagConstraints constraints) {
		JButton button = new JButton(label);
		
		button.setFont(font);
		layout.setConstraints(button, constraints);
		
		button.addActionListener(this);
		
		return button;
	}
	
	private JPanel buildButtonPanel(JFrame frame, GridBagLayout layout) {
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Command options"), BorderFactory
				.createEmptyBorder(10, 2, 2, 2)));
		
		Font font = new Font("Dialog", Font.BOLD, 12);		
		
		GridBagLayout buttonLayout = new GridBagLayout();
		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.anchor = GridBagConstraints.NORTH;
		buttonConstraints.fill = GridBagConstraints.BOTH;
		
		buttonPanel.setLayout(buttonLayout);		
		
		buildConstraints(buttonConstraints, 0, 1, 1, 2, 100, 5);
		buttonPanel.add(buildButton(LOAD_DEFAULT_EXPERIMENT_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 3, 1, 2, 0, 5);
		buttonPanel.add(buildButton(LOAD_EXPERIMENT_FILE_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 5, 1, 2, 0, 5);
		buttonPanel.add(buildButton(WRITE_EXPERIMENT_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 7, 1, 2, 0, 5);
		buttonPanel.add(buildButton(WRITE_EXPERIMENT_FILE_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 9, 1, 2, 0, 5);
		buttonPanel.add(buildButton(LOAD_FITNESS_CLASS_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 11, 1, 2, 0, 5);
		buttonPanel.add(buildButton(LOAD_DATA_INPUT_CLASS_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 13, 1, 2, 0, 5);
		buttonPanel.add(buildButton(LOAD_DATA_TARGET_CLASS_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 15, 1, 2, 0, 5);
		buttonPanel.add(buildButton(SET_EXPERIMENT_SKELETON_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 17, 1, 2, 0, 5);
		buttonPanel.add(buildButton(SET_FITNESS_CLASS_SKELETON_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 19, 1, 2, 0, 5);
		buttonPanel.add(buildButton(SET_DATA_INPUT_CLASS_SKELETON_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 21, 1, 2, 0, 5);
		buttonPanel.add(buildButton(SET_DATA_TARGET_CLASS_SKELETON, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 23, 1, 2, 0, 5);
		buttonPanel.add(buildButton(CHECK_KEYWORD_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 25, 1, 2, 0, 5);
		buttonPanel.add(buildButton(COMPILE_LABEL, font, buttonLayout, buttonConstraints));

		buildConstraints(buttonConstraints, 0, 27, 1, 2, 0, 35);
		buttonConstraints.anchor = GridBagConstraints.SOUTH;
		buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
		buttonConstraints.ipady = 20;
		buttonPanel.add(buildButton(EXIT_BUTTON_LABEL, font, buttonLayout, buttonConstraints));
		
		GridBagConstraints constraints = new GridBagConstraints();
		buildConstraints(constraints, 0, 0, 1, 5, 0, 100);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.VERTICAL;
		
		layout.setConstraints(buttonPanel, constraints);
		
		return buttonPanel;
	}

	public String[] convertToArray(String _text) {

		String s1 = _text;
		StringTokenizer riga;
		String elem;
		int sz;
		riga = new StringTokenizer(s1, "\n");
		sz = riga.countTokens();
		String[] source_new = new String[sz];

		for (int r = 0; r < sz; r++) {
			elem = (String) riga.nextToken();
			// System.out.print("\n conv.to.string --> elem["+r+"] --> "+elem);
			source_new[r] = new String(elem + "\n");
		}
		return source_new;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Check for an EXIT event first...
		super.actionPerformed(e);

		if (e.getActionCommand().equals(LOAD_DEFAULT_EXPERIMENT_LABEL)) {
			/*logger.sendToStatus("wait....");
			EnvConstant.EDIT_STATUS = 0;
			nomef = EnvRoutine.getJneatSession();
			logger.sendToLog(" session: wait loading -> " + nomef);
			StringTokenizer st;
			String xline;
			IOseq xFile;

			xFile = new IOseq(nomef);
			boolean rc = xFile.IOseqOpenR();
			if (rc) {

				StringBuffer sb1 = new StringBuffer("");
				try {
					xline = xFile.IOseqRead();

					while (xline != "EOF") {
						sb1.append(xline + "\n");
						xline = xFile.IOseqRead();
					}

					textPane1.setText("");
					String[] source_new = convertToArray(sb1.toString());
					setSourceNew(source_new);
					logger.sendToLog(" ok file loaded");
					logger.sendToStatus("READY");

				}

				catch (Throwable e1) {
					logger.sendToStatus("READY");
					logger.sendToLog(" session: error during read " + e1);
				}

				xFile.IOseqCloseR();

			}

			else {
				logger.sendToStatus("READY");
				logger.sendToLog(" session: file not found");
			}
*/
		}

		else if (e.getActionCommand().equals(LOAD_EXPERIMENT_FILE_LABEL)) {
			
			controller.loadExperiment(frame);
			
		} else if (e.getActionCommand().equals(WRITE_EXPERIMENT_LABEL)) {

			/*EnvConstant.EDIT_STATUS = 0;

			nomef = EnvRoutine.getJneatSession();
			logger.sendToStatus("wait....");
			logger.sendToLog(" session: wait writing -> " + nomef);
			IOseq xFile;
			xFile = new IOseq(nomef);
			xFile.IOseqOpenW(false);

			try {

				String s1 = textPane1.getText();
				StringTokenizer riga;
				String elem;
				int sz;
				riga = new StringTokenizer(s1, "\n");
				sz = riga.countTokens();

				for (int r = 0; r < sz; r++) {
					elem = (String) riga.nextElement();
					String elem1 = new String(elem); // +"\n");
					xFile.IOseqWrite(elem);
				}

				logger.sendToLog(" ok file writed");

			} catch (Throwable e1) {
				logger.sendToStatus("READY");
				logger.sendToLog(" session: error during write " + e1);
			}

			xFile.IOseqCloseW();
			logger.sendToStatus("READY");*/

		}

		else if (e.getActionCommand().equals(WRITE_EXPERIMENT_FILE_LABEL)) {
			/*EnvConstant.EDIT_STATUS = 0;
			FileDialog fd = new FileDialog(f1, "save session file",
					FileDialog.SAVE);
			fd.setVisible(true);

			tmp1 = fd.getDirectory();
			tmp2 = fd.getFile();

			if (tmp1 != null && tmp2 != null) {

				logger.sendToStatus("wait....");
				nomef = tmp1 + tmp2;
				logger.sendToLog(" session: wait writing -> " + nomef);
				//
				// write to file genome in native format (for re-read)
				//
				IOseq xFile;
				xFile = new IOseq(nomef);
				xFile.IOseqOpenW(false);

				try {
					String s1 = textPane1.getText();
					StringTokenizer riga;
					String elem;
					int sz;
					riga = new StringTokenizer(s1, "\n");
					sz = riga.countTokens();

					for (int r = 0; r < sz; r++) {
						elem = (String) riga.nextElement();
						String elem1 = new String(elem); // +"\n");
						xFile.IOseqWrite(elem);
					}
					logger.sendToLog(" ok file writed");

				}

				catch (Throwable e1) {
					logger.sendToStatus("READY");
					logger.sendToLog(" session: error during write " + e1);
				}

				xFile.IOseqCloseW();
				logger.sendToStatus("READY");

			}
*/
		}

		else if (e.getActionCommand().equals(CHECK_KEYWORD_LABEL)) {
			/*logger.sendToStatus("wait...");
			String[] source_new = convertToArray(textPane1.getText());
			textPane1.setText("");
			setSourceNew(source_new);
			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(SET_EXPERIMENT_SKELETON_LABEL)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = 0;
			textPane1.setText("");
			setSourceNew(default_source);
			logger.sendToLog(" session: set to default skeleton for session");
			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(SET_FITNESS_CLASS_SKELETON_LABEL)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = EnvConstant.EDIT_CLASS_FIT;
			textPane1.setText("");

			setSourceNew(initFitness);

			logger.sendToLog(" session: set to default skeleton for fitness");
			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(SET_DATA_INPUT_CLASS_SKELETON_LABEL)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = EnvConstant.EDIT_CLASS_INP;
			textPane1.setText("");
			setSourceNew(initDataClassInput);
			logger
					.sendToLog(" session: set to default skeleton for  class/dataset generate input");
			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(SET_DATA_TARGET_CLASS_SKELETON)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = EnvConstant.EDIT_CLASS_OUT;
			textPane1.setText("");
			setSourceNew(initDataClassOutput);
			logger
					.sendToLog(" session: set to default skeleton for  class/dataset generate output");
			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(LOAD_DATA_TARGET_CLASS_LABEL)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = EnvConstant.EDIT_CLASS_OUT;
			if (curr_output_data != null) {
				load_from_disk_Class("src/gui/" + curr_output_data, "data");
			} else
				logger
						.sendToLog(" session: *warning* before load data-out , load the sesssion !");

			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(LOAD_FITNESS_CLASS_LABEL)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = EnvConstant.EDIT_CLASS_FIT;
			if (curr_fitness_class != null) {
				load_from_disk_Class("src/gui/" + curr_fitness_class, "fitness");
			} else
				logger
						.sendToLog(" session: *warning* before load fitness , load the sesssion !");

			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(LOAD_DATA_INPUT_CLASS_LABEL)) {
			/*logger.sendToStatus("wait...");
			EnvConstant.EDIT_STATUS = EnvConstant.EDIT_CLASS_INP;
			if (curr_input_data != null) {
				load_from_disk_Class(curr_input_data, "data");
			} else
				logger
						.sendToLog(" session: *warning* before load data-in , load the sesssion !");

			logger.sendToStatus("READY");*/
		}

		else if (e.getActionCommand().equals(COMPILE_LABEL)) {
			/*if (EnvConstant.EDIT_STATUS == EnvConstant.EDIT_CLASS_FIT) {
				if (curr_fitness_class != null) {
					EnvConstant.CURRENT_CLASS = curr_fitness_class;
					Async_generationClass();
				}
			}

			else if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS) {

				if (EnvConstant.EDIT_STATUS == EnvConstant.EDIT_CLASS_INP) {
					if (curr_input_data != null) {
						EnvConstant.CURRENT_CLASS = curr_input_data;
						Async_generationClass();
					}
				}

				else if (EnvConstant.EDIT_STATUS == EnvConstant.EDIT_CLASS_OUT) {
					if (curr_output_data != null) {
						EnvConstant.CURRENT_CLASS = curr_output_data;
						Async_generationClass();
					}
				}

			}

			else if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_FILE) {

				if (EnvConstant.EDIT_STATUS == EnvConstant.EDIT_CLASS_INP) {
					if (curr_input_data != null) {
						EnvConstant.CURRENT_FILE = curr_input_data;
						Async_generationFile();
					}
				}

				else if (EnvConstant.EDIT_STATUS == EnvConstant.EDIT_CLASS_OUT) {
					if (curr_output_data != null) {

						EnvConstant.CURRENT_FILE = curr_output_data;
						Async_generationFile();

					}
				}

			}*/

		}

	}

	/*public void setLog(HistoryLog _log) {
		logger = _log;
	}*/

	/*public void createClass(String _filename, String[] sourcecode) {
		try {
			FileWriter aWriter = new FileWriter(_filename, false);

			for (int r = 0; r < sourcecode.length; r++)
				aWriter.write(sourcecode[r]);

			aWriter.flush();
			aWriter.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	//public boolean compileClass(String className) {

		/*String[] source = { new String(className) };
		PrintStream ps = System.err;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setErr(new PrintStream(baos));*/

		/*
		 * 
		 * // jdk 1.1.8 //
		 * 
		 * new sun.tools.javac.Main(baos, source[0]).compile(source);
		 * System.setErr(ps); if (baos.toString().indexOf("error") == -1) return
		 * true; else {
		 * 
		 * try {
		 * 
		 * logger.sendToLog(" session: *warning* error during compilation : ");
		 * logger.sendToLog(" session: "+baos.toString());
		 * 
		 * } catch (Throwable e1) { System.err.println(e1 +
		 * " session: error in try-compile  "+e1); } return false; }
		 */

		// jdk 1.3.1_01
		//
		
		//com.sun.tools.javac.Main m1 = new com.sun.tools.javac.Main();
		//m1.compile(source);
		//System.setErr(ps);
		
		/*JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
	    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(className));
	    
	    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
	    boolean success = task.call();
	    
	    try
	    {
	    	fileManager.close();
	    }
	    catch (IOException e) {
	    	
	    }
	    
	    System.out.println("Success: " + success);

		Date xdata = new Date();
		
		if (baos.toString().indexOf("error") == -1)
			return true;
		else {

			try {
				logger
						.sendToLog(" session: *warning* error during compilation : ");
				logger.sendToLog(" session: " + baos.toString());

			}

			catch (Throwable e1) {
				System.err
						.println(e1 + " session: error in try-compile  " + e1);
			}
			return false;
		}

	}*/

	/*public void Async_generationClass() {
		Runnable lookupRun = new Runnable() {
			public void run() {
				generationClass();
			}
		};
		lookupThread = new Thread(lookupRun, " looktest");
		lookupThread.start();
	}*/

	/*public void generationClass() {

		String _classname = EnvConstant.CURRENT_CLASS;
		String nomef = null;

		logger.sendToStatus("wait....");
		try {

			logger.sendToLog(" session: start compile ->" + _classname
					+ " in dir ->" + EnvConstant.JNEAT_DIR);
			// legge corrente nome source della classe da creare
			//
			nomef = EnvRoutine.getJneatFile(_classname + ".java");
			// converte da stringa unica a vettore di stringhe
			String[] source_new = convertToArray(textPane1.getText());
			logger.sendToLog(" session: creation source " + _classname
					+ ".java");
			// genera il source.java
			//
			createClass(nomef, source_new);
			logger.sendToLog(" session: terminate creation source");
			logger.sendToLog(" session: creation class " + _classname
					+ ".class");
			// genera il file .class
			//
			compileClass(nomef);
			logger.sendToLog(" session: terminate creation class " + _classname
					+ ".class");
			// riaggiorna il pannello con quello che ha appena scritto
			textPane1.setText("");
			//setSourceNew(source_new);
			logger.sendToStatus("READY");

		}

		catch (Throwable e1) {
			logger.sendToLog(" session: error during compile fitness " + e1);
		}
		logger.sendToStatus("READY");

	}*/

	/*public void setSourceNew(String[] _source) {
		StringTokenizer riga;
		String elem;
		int sz;
		String prev_word;
		boolean fnd;
		Document doc = textPane1.getDocument();

		try {
			for (int i = 0; i < _source.length; i++) {
				// search name for fitness class;
				// search i/o class or files for input/target signal
				//

				int b1[] = new int[_source[i].length()];
				for (int j = 0; j < b1.length; j++)
					b1[j] = 0;

				String zriga = _source[i];
				int pos = 0;

				for (int k = 0; k < My_keyword.length; k++) {
					String ckey = My_keyword[k];
					pos = zriga.indexOf(ckey, 0);
					if (pos != -1) {
						for (int k1 = 0; k1 < ckey.length(); k1++)
							b1[pos + k1] = 1;
						boolean done = false;
						int offset = pos + ckey.length();
						while (!done) {
							pos = zriga.indexOf(ckey, offset);
							if (pos != -1) {
								for (int k1 = 0; k1 < ckey.length(); k1++)
									b1[pos + k1] = 1;
								offset = pos + ckey.length();
							}

							else
								done = true;

						}

					}
				}

				int n1 = 0;
				int n2 = 0;
				int v1 = 0;
				int v2 = 0;
				int k2 = 0;

				boolean comment = false;
				for (int k1 = 0; k1 < b1.length; k1++) {
					v1 = b1[k1];
					if (v1 == 1) {
						if (zriga.substring(k1, k1 + 1).equals(";")) {
							comment = true;
							break;
						} else
							comment = false;
						break;
					}
				}

				if (comment) {
					doc.insertString(doc.getLength(), zriga, textPane1
							.getStyle(My_styles[1]));
				}

				else {

					// cerca fino a che non trova n1 != n2;
					// int lun = 0;
					boolean again = true;
					for (int k1 = 0; k1 < b1.length; k1++) {
						v1 = b1[n1];
						n2 = n1;
						again = false;
						for (k2 = n1 + 1; k2 < b1.length; k2++) {
							v2 = b1[k2];
							if (v2 != v1) {
								again = true;
								break;
							}
							n2 = k2;
						}

						elem = zriga.substring(n1, n2 + 1);

						if (v1 == 0)
							doc.insertString(doc.getLength(), elem, textPane1
									.getStyle(My_styles[0]));
						else
							doc.insertString(doc.getLength(), elem, textPane1
									.getStyle(My_styles[2]));
						// System.out.print("\n n1="+n1+" n2="+n2+" found elem ->"+elem+"<- size("+elem.length()+")");
						k1 = k2;
						n1 = k2;

					}

					if (again) {
						elem = zriga.substring(b1.length - 1, b1.length);
						if (b1[b1.length - 1] == 0)
							doc.insertString(doc.getLength(), elem, textPane1
									.getStyle(My_styles[0]));
						else
							doc.insertString(doc.getLength(), elem, textPane1
									.getStyle(My_styles[2]));

						// System.out.print("\n **WW* found elem ->"+elem+"<- size("+elem.length()+")");

					}

					riga = new StringTokenizer(zriga);

					sz = riga.countTokens();
					prev_word = null;
					for (int r = 0; r < sz; r++) {
						elem = riga.nextToken();
						fnd = false;
						for (int k = 0; k < My_keyword.length; k++) {
							if (My_keyword[k].equalsIgnoreCase(elem)) {
								fnd = true;
								break;
							}
						}
System.out.println("prev_word " + prev_word);
						if ((prev_word != null)
								&& (prev_word
										.equalsIgnoreCase("data_from_file"))) {
							if ((!comment) && elem.equalsIgnoreCase("Y")) {
								EnvConstant.TYPE_OF_SIMULATION = EnvConstant.SIMULATION_FROM_FILE;
							}
						}

						if ((prev_word != null)
								&& (prev_word
										.equalsIgnoreCase("data_from_class"))) {
							if ((!comment) && elem.equalsIgnoreCase("Y")) {
								EnvConstant.TYPE_OF_SIMULATION = EnvConstant.SIMULATION_FROM_CLASS;
							}
						}

						if ((prev_word != null)
								&& (prev_word
										.equalsIgnoreCase("class_compute_fitness"))) {
							curr_fitness_class = new String(elem);
						}

						if ((prev_word != null)
								&& (prev_word.equalsIgnoreCase("data_input"))) {
							curr_input_data = new String(elem);
						}

						if ((prev_word != null)
								&& (prev_word.equalsIgnoreCase("data_target"))) {
							curr_output_data = new String(elem);
						}
						prev_word = elem;

					}

				}

			}

			textPane1.setCaretPosition(1);

		} catch (Exception e1) {
			logger
					.sendToStatus(" session: Couldn't insert initial text.:"
							+ e1);
		}

	}*/

	/*public void Async_generationFile() {
		Runnable lookupRun = new Runnable() {
			public void run() {
				generationFile();
			}
		};
		lookupThread = new Thread(lookupRun, " looktest");
		lookupThread.start();
	}*/

	/*public void generationFile() {
		String _fname = EnvConstant.CURRENT_FILE;

		logger.sendToStatus("wait....");
		logger.sendToLog(" session: start write file "
				+ EnvRoutine.getJneatFile(_fname));
		IOseq xFile;
		xFile = new IOseq(EnvRoutine.getJneatFile(_fname));
		xFile.IOseqOpenW(false);

		try {

			String s1 = textPane1.getText();
			StringTokenizer riga;
			String elem;
			int sz;
			riga = new StringTokenizer(s1, "\n");
			sz = riga.countTokens();

			for (int r = 0; r < sz; r++) {
				elem = (String) riga.nextElement();
				String elem1 = new String(elem); // +"\n");
				xFile.IOseqWrite(elem);
			}

			logger.sendToLog(" ok file writed");

		}

		catch (Throwable e1) {
			logger.sendToStatus("READY");
			logger.sendToLog(" session: error during write " + e1);
		}

		xFile.IOseqCloseW();
		logger.sendToStatus("READY");

	}*/

	/*public void load_from_disk_Class(String _filename, String _type) {
		String nomef = null;

		if (_type.equalsIgnoreCase("fitness"))
			nomef = EnvRoutine.getJneatFile(_filename + ".java");

		else {
			if (EnvConstant.TYPE_OF_SIMULATION == EnvConstant.SIMULATION_FROM_CLASS)
				nomef = EnvRoutine.getJneatFile(_filename + ".java");
			else
				nomef = EnvRoutine.getJneatFile(_filename);
		}

		StringTokenizer st;
		String xline;
		IOseq xFile;

		xFile = new IOseq(nomef);
		boolean exist = xFile.IOseqOpenR();

		if (exist) {

			StringBuffer sb1 = new StringBuffer("");
			try {

				logger.sendToStatus(" session: wait....");
				logger.sendToLog("  session: wait loading " + nomef + "...");
				xline = xFile.IOseqRead();

				while (xline != "EOF") {
					sb1.append(xline + "\n");
					xline = xFile.IOseqRead();
				}

				textPane1.setText("");
				String[] source_new = convertToArray(sb1.toString());
				//setSourceNew(source_new);

				logger.sendToLog(" session: wait loaded " + nomef);
				logger.sendToStatus("READY");

			}

			catch (Throwable e1) {
				logger.sendToLog(" session: error during read " + nomef + " "
						+ e1);
			}

			xFile.IOseqCloseR();
			logger.sendToStatus("READY");

		} // exist cycle

		else {

			try {
				logger.sendToLog("  session: warning : file  " + nomef
						+ " not exist!");
			}

			catch (Throwable e2) {
				System.err.println(e2
						+ " session: error during text processing " + e2);
			}

			logger.sendToStatus("READY");

		}

	}*/

	@Override
	public void contextChanged(INeatContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void experimentChanged(INeatContext context) {
		logger.debug("A New Experiment has been loaded - reload the view");
	}

}