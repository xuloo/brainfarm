package org.brainfarm.java.gui;

import java.awt.FileDialog;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.controller.SpringNeatController;

public class GuiController extends SpringNeatController implements IGuiController {
	
	private static Logger logger = Logger.getLogger(GuiController.class);
	
	public GuiController(INeatContext context) {
		super(context);
	}
	
	@Override 
	public void loadParameters(JFrame frame) {
		FileDialog fileDialog = new FileDialog(frame, "Load Neat Parameters", FileDialog.LOAD);
		fileDialog.setVisible(true);
		
		String directory = fileDialog.getDirectory();
		String file = fileDialog.getFile();

		if (directory != null && file != null) {
			logger.debug("Loading Neat parameters from " + directory + " " + file);
		}
	}
	
	public void loadExperiment(JFrame frame) {
		FileDialog fileDialog = new FileDialog(frame, "Load Experiment", FileDialog.LOAD);
		fileDialog.setVisible(true);
		
		String directory = fileDialog.getDirectory();
		String file = fileDialog.getFile();
		
		if (directory != null && file != null) {
			logger.debug("Loading Experiment from " + directory + " " + file);
			loadExperiment(directory + "/" + file);
		}
	}
}
