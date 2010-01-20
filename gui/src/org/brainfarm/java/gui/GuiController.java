package org.brainfarm.java.gui;

import java.awt.FileDialog;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.context.SpringNeatContext;
import org.brainfarm.java.neat.controller.SpringNeatController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GuiController extends SpringNeatController implements IGuiController {
	
	private static Logger logger = Logger.getLogger(GuiController.class);
	
	public GuiController(INeatContext context) {
		this.context = context;
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
	
	@Override
	public void loadDefaultParameters() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		((SpringNeatContext)context).setApplicationContext(appContext);
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
