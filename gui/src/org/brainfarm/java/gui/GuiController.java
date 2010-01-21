package org.brainfarm.java.gui;

import java.awt.FileDialog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.context.SpringNeatContext;
import org.brainfarm.java.neat.controller.SpringNeatController;
import org.brainfarm.java.neat.params.AbstractNeatParameter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GuiController extends SpringNeatController implements IGuiController {
	
	private static Logger logger = Logger.getLogger(GuiController.class);
	
	/**
	 * Flag to indicate whether the user is currently using 
	 * the default properties.
	 */
	private boolean defaultProperties = true;
	
	private String currentParametersFile;
	
	public GuiController(INeatContext context) {
		this.context = context;
		
		init();
	}
	
	private void init() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		getSpringNeatContext().setApplicationContext(appContext);
	}
	
	@Override 
	public void loadParameters(JFrame frame) {
		FileDialog fileDialog = new FileDialog(frame, "Load Neat Parameters", FileDialog.LOAD);
		fileDialog.setVisible(true);
		
		String directory = fileDialog.getDirectory();
		String file = fileDialog.getFile();

		if (directory != null && file != null) {
			logger.debug("Loading Neat parameters from " + directory + "/" + file);
			Properties properties = null;
			
			try {
				InputStream stream = new FileInputStream(directory + "/" + file);
				properties = new Properties();
				properties.load(stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (properties != null) {
				
				Neat neat = context.getNeat();
				Enumeration<?> e = properties.propertyNames();

			    while (e.hasMoreElements()) {
			    	String key = (String) e.nextElement();
			    	String value = properties.getProperty(key);
			    	
			    	logger.debug(key + " -- " + value);
			    	
			    	neat.setParameter(key, value);
			    }
			    
			    defaultProperties = false;
			    currentParametersFile = directory + "/" + file;
			    
			    context.contextChanged();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadDefaultParameters() {
		List<AbstractNeatParameter> defaultParameters = (List<AbstractNeatParameter>)getSpringNeatContext().getApplicationContext().getBean("default.parameters");
		context.getNeat().setParameters(defaultParameters);
		
		defaultProperties = true;
		context.contextChanged();
	}
	
	/**
	 * If the user has loaded NEAT parameters from an external properties file
	 * then the current NEAT parameters are saved out to that file.
	 */
	public void saveParameters() {
		if (!defaultProperties && currentParametersFile != null) {
			saveParameters(currentParametersFile);
		}
	}
	
	/**
	 * Prompt the user to choose a location to save the current NEAT parameters to.
	 */
	public void saveParameters(JFrame frame) {
		FileDialog fileDialog = new FileDialog(frame, "Save Neat Parameters", FileDialog.SAVE);
		fileDialog.setVisible(true);
		
		String directory = fileDialog.getDirectory();
		String file = fileDialog.getFile();

		if (directory != null && file != null) {
			saveParameters(directory + "/" + file);
		}
	}
	
	/**
	 * Does the actual saving of the file as a java.util.Properties file.
	 * 
	 * @param file
	 */
	protected void saveParameters(String file) {
		logger.debug("Saving current NEAT parameters to " + file);
		
		Properties properties = new Properties();
		
		for (AbstractNeatParameter parameter : context.getNeat().getParameters()) {
			properties.setProperty(parameter.getName(), parameter.getVal());
		}
		
		try {
			OutputStream out = new FileOutputStream(file);
			properties.store(out, null);
			out.close();
		} catch (IOException e) {
			logger.error("Problem saving NEAT properties \n" + e.getMessage());
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
	
	public SpringNeatContext getSpringNeatContext() {
		return (SpringNeatContext)context;
	}
}
