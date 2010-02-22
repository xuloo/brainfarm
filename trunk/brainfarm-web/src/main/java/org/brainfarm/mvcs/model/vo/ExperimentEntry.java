package org.brainfarm.mvcs.model.vo;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class ExperimentEntry {

	private String name;
	
	private String fileName;
	
	public ExperimentEntry() {
		
	}
	
	public ExperimentEntry(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFileName() {
		return fileName;
	}
}
