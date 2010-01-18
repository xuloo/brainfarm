package org.brainfarm.java.util.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.evolution.IEvolution;
import org.brainfarm.java.util.FileUtils;

public class FileWritingResultGenerator implements IResultGenerator {

	private static Logger logger = Logger.getLogger(FileWritingResultGenerator.class);
	
	private IFileNameGenerator filenameGenerator;
	
	@Override
	public Result getResult(IEvolution evolution) {
		
		File xmlOutputFile = FileUtils.resolvePath(filenameGenerator.generateFileName(evolution));
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(xmlOutputFile);
		} catch (FileNotFoundException e) {
			logger.error("Error occured: " + e.getMessage());
			return null;
		}
		
		return new StreamResult(fos);
	}

	public void setFilenameGenerator(IFileNameGenerator fileNameGenerator) {
		this.filenameGenerator = fileNameGenerator;
	}
}
