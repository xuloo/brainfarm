package org.brainfarm.mvcs.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.brainfarm.java.feat.EvolutionContext;
import org.brainfarm.java.feat.EvolutionController;
import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.brainfarm.java.feat.params.EvolutionParameter;
import org.brainfarm.mvcs.model.vo.ExperimentEntry;

public class BrainFarmServiceImpl implements IBrainFarmService {
	
	private final String ROOT_PATH = System.getProperty("red5.root");

	private IEvolutionContext context;
	
	private IEvolutionController controller;
	
	private String webappPath;
	
	private List<ExperimentEntry> experiments = new ArrayList<ExperimentEntry>();
	
	public BrainFarmServiceImpl() {
		
	}
	
	public void init() {
		System.out.println("initialising BrainFarmService");
		
		initBrainFarm();
		updateExperimentList();
	}
	
	private void initBrainFarm() {
		System.out.println("initialising BrainFarm");
		
		context = new EvolutionContext();
		
		controller = new EvolutionController(context);
		//controller.setExperimentDirectory(webappPath + "/working");
	}
	
	public void updateExperimentList() {
		experiments.clear();
		
		File experimentsDir = new File(webappPath + "/experiments");

		if (experimentsDir.exists()) {
			String[] list = experimentsDir.list();
			
			for (String entry : list) {
				File experimentEntry = new File(webappPath + "/experiments/" + entry);
				if (!experimentEntry.isDirectory()) {
					try {
		                JarFile jar = new JarFile(experimentEntry);
		                final Manifest manifest = jar.getManifest();
		                final Attributes mattr = manifest.getMainAttributes();
		                String name = mattr.getValue("Experiment-Name");
		                ExperimentEntry experiment;
		                
		                if (name == null) {
		                	experiment = new ExperimentEntry(experimentEntry.getName(), experimentEntry.getName());
		                } else {
		                	experiment = new ExperimentEntry(name, experimentEntry.getName());
		                }
		                
		                experiments.add(experiment);
		            } catch (Exception x) {
		                System.err.println("Failed to read manifest for "+
		                		experimentEntry.getAbsolutePath()+": "+x);
		            }
				}
			}
		}
	}
	
	public Collection<IEvolutionParameter> getEvolutionParameters() {
		
		System.out.println("loading neat parameters");
		
		controller.loadDefaultParameters();
		
		return context.getEvolutionParameters().getParameterCollection();
	}
	
	public List<ExperimentEntry> getExperimentList() {
		
		System.out.println("returning experiment list - " + experiments.size() + " experiments");
		return experiments;
	}
	
	public void loadExperiment(String location) {
		String path = webappPath + "/experiments/" + location;
		System.out.println("PATH: " + path);
		controller.loadExperiment(path);
	}
	
	public void setWebappPath(String webappPath) {
		this.webappPath = ROOT_PATH + webappPath;
	}
}
