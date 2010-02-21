package org.brainfarm.java.feat.api.params;


public interface IEvolutionParameters {

	IEvolutionParameter getParameter(String name);
	
	Integer getIntParameter(String param);
	
	Double getDoubleParameter(String name);
}
