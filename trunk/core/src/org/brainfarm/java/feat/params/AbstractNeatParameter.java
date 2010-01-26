package org.brainfarm.java.feat.params;

import org.brainfarm.java.feat.Neat;

public abstract class AbstractNeatParameter {

	protected String name;
	
	protected String description;
	
	public AbstractNeatParameter() {
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
	
	public abstract void set(Neat neat);
}
