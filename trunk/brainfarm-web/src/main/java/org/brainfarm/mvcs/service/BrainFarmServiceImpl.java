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
import org.brainfarm.java.util.XMLUtils;
import org.brainfarm.mvcs.model.vo.ExperimentEntry;
import org.red5.server.adapter.IApplication;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.ServiceUtils;
import org.red5.server.api.so.ISharedObject;

public class BrainFarmServiceImpl implements IBrainFarmService, IApplication, IExperimentRunnerListener {

	private final String ROOT_PATH = System.getProperty("red5.root");

	private IEvolutionContext context;

	private IEvolutionController controller;

	private String webappPath;

	private IExperimentRunner experimentRunner;

	private List<ExperimentEntry> experiments = new ArrayList<ExperimentEntry>();

	private MultiThreadedApplicationAdapter application;

	private ISharedObject so;

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
		// controller.setExperimentDirectory(webappPath + "/working");
	}

	public void updateExperimentList() {

		System.out.println("Updating experiments list");
		experiments.clear();

		File experimentsDir = new File(webappPath + "/experiments");

		System.out.println("experiments dir "
				+ experimentsDir.getAbsolutePath());

		if (experimentsDir.exists()) {
			String[] list = experimentsDir.list();

			for (String entry : list) {
				File experimentEntry = new File(webappPath + "/experiments/"
						+ entry);
				if (!experimentEntry.isDirectory()) {
					try {
						JarFile jar = new JarFile(experimentEntry);
						final Manifest manifest = jar.getManifest();
						final Attributes mattr = manifest.getMainAttributes();
						String name = mattr.getValue("Experiment-Name");
						ExperimentEntry experiment;

						if (name == null) {
							experiment = new ExperimentEntry(experimentEntry
									.getName().substring(
											0,
											experimentEntry.getName().indexOf(
													".jar")), experimentEntry
									.getName());
						} else {
							experiment = new ExperimentEntry(name,
									experimentEntry.getName());
						}
						System.out
								.println("experiment " + experiment.getName());
						experiments.add(experiment);
					} catch (Exception x) {
						System.err.println("Failed to read manifest for "
								+ experimentEntry.getAbsolutePath() + ": " + x);
					}
				}
			}
		}
	}

	public Collection<IEvolutionParameter> getEvolutionParameters() {

		System.out.println("loading neat parameters");

		controller.loadEvolutionParameters(null);

		return context.getEvolutionParameters().getParameterCollection();
	}

	public List<ExperimentEntry> getExperimentList() {

		System.out.println("returning experiment list - " + experiments.size()
				+ " experiments");
		return experiments;
	}

	public void loadExperiment(String location) {
		String path = webappPath + "/experiments/" + location;
		System.out.println("PATH: " + path);
		controller.loadExperiment(path);
	}

	public void runExperiment() {
		System.out.println("running experiment");

		File workingDir = new File(webappPath + "/working");
		
		experimentRunner = new ExperimentRunner(Red5.getConnectionLocal(), workingDir, context.getEvolution());
		experimentRunner.addListener(this);
		experimentRunner.run();
	}
	
	@Override
	public void experimentRunComplete(IExperimentRunner runner) {
		System.out.println("Experiment Run is complete");
		
		String resultString = XMLUtils.createPrintableString(runner.getResultXML());
		
		System.out.println("Experiment Result:\n" + resultString);
		
		ServiceUtils.invokeOnConnection(runner.getConnection(), "evolutionComplete", new Object[]{resultString});
	}

	public void setWebappPath(String webappPath) {
		this.webappPath = ROOT_PATH + webappPath;
	}

	@Override
	public float getEvolutionProgress() {
		return experimentRunner.getProgress();
	}

	public void setApplication(MultiThreadedApplicationAdapter application) {
		this.application = application;
		this.application.addListener(this);
	}

	@Override
	public boolean appConnect(IConnection connection, Object[] arg1) {
		System.out.println("APPCONNECT");
		so = application.getSharedObject(connection.getScope(), "evolution", false);
		return true;
	}

	@Override
	public void appDisconnect(IConnection arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean appJoin(IClient arg0, IScope arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void appLeave(IClient arg0, IScope arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean appStart(IScope arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void appStop(IScope arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean roomConnect(IConnection arg0, Object[] arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void roomDisconnect(IConnection arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean roomJoin(IClient arg0, IScope arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void roomLeave(IClient arg0, IScope arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean roomStart(IScope arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void roomStop(IScope arg0) {
		// TODO Auto-generated method stub
		
	}
}
