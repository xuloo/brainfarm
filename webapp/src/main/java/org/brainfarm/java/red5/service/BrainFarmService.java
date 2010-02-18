package org.brainfarm.java.red5.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.EvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.EvolutionController;
import org.brainfarm.java.feat.params.EvolutionParameter;
import org.brainfarm.java.mvcs.service.remote.ExperimentEntry;
import org.brainfarm.java.red5.api.service.IBrainFarmService;
import org.brainfarm.java.red5.api.service.message.IMessage;

public class BrainFarmService implements IBrainFarmService {
	
	private final String ROOT_PATH = System.getProperty("red5.root");

	private IEvolutionContext context;
	
	private IEvolutionController controller;
	
	private String webappPath;
	
	private List<ExperimentEntry> experiments = new ArrayList<ExperimentEntry>();
	
	public BrainFarmService() {
		
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
		                ExperimentEntry experiment = new ExperimentEntry(name, experimentEntry.getName());
		                experiments.add(experiment);
		            } catch (Exception x) {
		                System.err.println("Failed to read manifest for "+
		                		experimentEntry.getAbsolutePath()+": "+x);
		            }
				}
			}
		}
	}
	
	public List<EvolutionParameter> loadNeatParameters() {
		System.out.println("loading neat parameters");
		controller.loadDefaultParameters();
		
		//return context.getNeat().getParameters();
		return null;
	}
	
	public List<ExperimentEntry> getAvailableExperiments() {
		return experiments;
	}
	
	public void loadExperiment(String location) {
		String path = webappPath + "/experiments/" + location;
		System.out.println("PATH: " + path);
		controller.loadExperiment(path);
	}
	
	public Object receiveMessage(IMessage message) {
		message.setService(this);
		
		Object response = message.read();
		
		return response;
	}
	
	public void setWebappPath(String webappPath) {
		this.webappPath = ROOT_PATH + webappPath;
	}

	@Override
	public IEvolutionContext getFeatContext() {
		return context;
	}
}
