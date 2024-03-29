package org.brainfarm.java.feat.params;

import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;

public class EvolutionParameter implements IEvolutionParameter, IExternalizable {

	protected String name;
	
	protected String description;
	
	protected Object value;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Integer getIntValue() {
		return (Integer)value;
	}
	
	public Double getDoubleValue() {
		return (Double)value;
	}

	@Override
	public void readExternal(IDataInput input) {
		name = input.readUTF();
		description = input.readUTF();
	}

	@Override
	public void writeExternal(IDataOutput output) {
		output.writeUTF(name);
		output.writeUTF(description);
	}
}
