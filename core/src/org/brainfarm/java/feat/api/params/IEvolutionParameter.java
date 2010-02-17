package org.brainfarm.java.feat.api.params;

import org.red5.server.api.ICastingAttributeStore;

public interface IEvolutionParameter extends ICastingAttributeStore {

	public final String NAME 			= "name";
	public final String DESCRIPTION 	= "description";
	public final String VALUE 			= "value";
	
	String getName();
	
	void setName(String name);
	
	String getDescription();
	
	void setDescription(String description);
	
	Object getValue();
	
	void setValue(Object value);
}
