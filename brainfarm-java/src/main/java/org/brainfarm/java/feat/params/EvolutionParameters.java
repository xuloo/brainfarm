package org.brainfarm.java.feat.params;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.red5.server.AttributeStore;

/**
 * Definition of all parameters , threshold and other values.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 */
public class EvolutionParameters extends AttributeStore implements IEvolutionParameters {

	protected ConcurrentMap<String, IEvolutionParameter> parameters = new ConcurrentHashMap<String, IEvolutionParameter>(1);
	
	public IEvolutionParameter getParameter(String name) {
		return parameters.get(name);
	}
	
	public Integer getIntParameter(String param) {
		return getParameter(param).getIntValue();
	}
	
	public Double getDoubleParameter(String name) {
		return getParameter(name).getDoubleValue();
	}
}