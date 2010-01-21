package org.brainfarm.java.gui;

import java.awt.FileDialog;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
			    
			    context.contextChanged();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadDefaultParameters() {
		List<AbstractNeatParameter> defaultParameters = (List<AbstractNeatParameter>)getSpringNeatContext().getApplicationContext().getBean("default.parameters");
		context.getNeat().setParameters(defaultParameters);
		
		context.contextChanged();
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
