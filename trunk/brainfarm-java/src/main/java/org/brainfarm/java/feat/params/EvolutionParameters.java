package org.brainfarm.java.feat.params;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;

/**
 * Definition of all parameters , threshold and other values.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 */
public class EvolutionParameters implements IEvolutionParameters {

	protected Map<String, IEvolutionParameter> parameters = new HashMap<String, IEvolutionParameter>(1);
	
	public IEvolutionParameter getParameter(String name) {
		return parameters.get(name);
	}
	
	public Integer getIntParameter(String param) {
		return getParameter(param).getIntValue();
	}
	
	public Double getDoubleParameter(String name) {
		return getParameter(name).getDoubleValue();
	}
	
	public void setParameters(Map<String, IEvolutionParameter> parameters) {
		this.parameters = parameters;
	}
	
	public Collection<IEvolutionParameter> getParameterCollection() {
		return parameters.values();
	}
}