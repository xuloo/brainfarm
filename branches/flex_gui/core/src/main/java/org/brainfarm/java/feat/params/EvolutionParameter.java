package org.brainfarm.java.feat.params;

import org.brainfarm.java.feat.api.params.ICastingEvolutionParameter;
import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;
import org.red5.server.AttributeStore;

public class EvolutionParameter extends AttributeStore implements IEvolutionParameter, ICastingEvolutionParameter, IExternalizable {

	public String getName() {
		return getStringAttribute(NAME);
	}
	
	public void setName(String name) {
		setAttribute(NAME, name);
	}
	
	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}
	
	public void setDescription(String description) {
		setAttribute(DESCRIPTION, description);
	}
	
	public Object getValue() {
		return getAttribute(VALUE);
	}
	
	public void setValue(Object value) {
		setAttribute(VALUE, value);
	}
	
	public Integer getIntValue() {
		return getIntAttribute(VALUE);
	}
	
	public Double getDoubleValue() {
		return getDoubleAttribute(VALUE);
	}
	
	public void readExternal(IDataInput input) {
		setName(input.readUTF());
		setDescription(input.readUTF());
	}
	
	public void writeExternal(IDataOutput output) {
		output.writeUTF(getName());
		output.writeUTF(getDescription());
	}
}
