package org.brainfarm.java.feat.params;

import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;

public abstract class EvolutionParameter implements IExternalizable {

	protected String name;
	
	protected String description;
	
	public EvolutionParameter() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String key) {
		this.name = key;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public abstract String getVal();
	
	public abstract void setVal(String val);
	
	public abstract void set(EvolutionParameters ep);
	
	public void readExternal(IDataInput input) {
		name = input.readUTF();
		description = input.readUTF();
	}
	
	public void writeExternal(IDataOutput output) {
		output.writeUTF(name);
		output.writeUTF(description);
	}
}
