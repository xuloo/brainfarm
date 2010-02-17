package org.brainfarm.java.feat.api.params;

import org.red5.server.api.ICastingAttributeStore;

public interface IEvolutionParameters extends ICastingAttributeStore {

	
	
	ICastingEvolutionParameter getParameter(String name);
	
	Integer getIntParameter(String param);
	
	Double getDoubleParameter(String name);
}
