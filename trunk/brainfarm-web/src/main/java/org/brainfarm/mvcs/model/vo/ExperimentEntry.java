package org.brainfarm.mvcs.model.vo;

import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class ExperimentEntry implements IExternalizable {

	private String name = "";
	
	private String fileName = "";
	
	private String description = "";
	
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

	@Override
	public void readExternal(IDataInput input) {
		name = input.readUTF();
		fileName = input.readUTF();
		description = input.readUTF();
	}

	@Override
	public void writeExternal(IDataOutput output) {
		output.writeUTF(name);
		output.writeUTF(fileName);
		output.writeUTF(description);
	}
}
