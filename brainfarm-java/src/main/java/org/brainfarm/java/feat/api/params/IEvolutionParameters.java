package org.brainfarm.java.feat.api.params;

import java.util.Collection;


public interface IEvolutionParameters {

	IEvolutionParameter getParameter(String name);
	
	Integer getIntParameter(String param);
	
	Double getDoubleParameter(String name);
	
	Collection<IEvolutionParameter> getParameterCollection();
}
