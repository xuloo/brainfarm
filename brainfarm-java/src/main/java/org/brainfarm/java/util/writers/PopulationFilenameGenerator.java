package org.brainfarm.java.util.writers;

import java.io.File;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolution;

public class PopulationFilenameGenerator implements IFileNameGenerator {

	private static Logger logger = Logger.getLogger(PopulationFilenameGenerator.class);
	
	private String basePath = new File("experiment").getAbsolutePath();
	
	@Override
	public String generateFileName(IEvolution evolution) {
		logger.debug("Generating file name from base " + basePath);
		return new File("experiment").getAbsolutePath() + "/result/" + evolution.getRun() + "/" + evolution.getEpoch() + ".xml";
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
