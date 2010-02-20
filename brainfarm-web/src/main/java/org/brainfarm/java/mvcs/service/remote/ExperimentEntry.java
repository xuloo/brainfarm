package org.brainfarm.java.mvcs.service.remote;

import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class ExperimentEntry implements IExternalizable {

	private String name;
	
	private String fileName;
	
	public ExperimentEntry() {
		
	}
	
	public ExperimentEntry(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}
	
	@Override
	public void readExternal(IDataInput input) {
		name = input.readUTF();
		fileName = input.readUTF();
	}

	@Override
	public void writeExternal(IDataOutput output) {
		output.writeUTF(name);
		output.writeUTF(fileName);
	}
	
	public String getName() {
		return name;
	}
	
	public String getFileName() {
		return fileName;
	}
}
