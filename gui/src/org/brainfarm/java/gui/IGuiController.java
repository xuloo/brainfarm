package org.brainfarm.java.gui;

import javax.swing.JFrame;

import org.brainfarm.java.neat.api.INeatController;

public interface IGuiController extends INeatController {

	void loadDefaultParameters();
	
	void saveParameters();
	
	void loadParameters(JFrame frame);
	
	void saveParameters(JFrame frame);
	
	void loadExperiment(JFrame frame);
}
