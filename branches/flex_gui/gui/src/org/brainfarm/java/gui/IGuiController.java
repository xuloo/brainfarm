package org.brainfarm.java.gui;

import javax.swing.JFrame;

import org.brainfarm.java.feat.api.IEvolutionController;

public interface IGuiController extends IEvolutionController {
	
	void loadParameters(JFrame frame);
	
	void loadExperiment(JFrame frame);
}
