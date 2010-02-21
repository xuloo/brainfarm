package org.brainfarm.java.feat.api.params;

public interface IEvolutionParameter {

	public final String NAME 			= "name";
	public final String DESCRIPTION 	= "description";
	public final String VALUE 			= "value";
	
	String getName();
	
	void setName(String name);
	
	String getDescription();
	
	void setDescription(String description);
	
	Object getValue();
	
	Integer getIntValue();
	
	Double getDoubleValue();
	
	void setValue(Object value);
}
