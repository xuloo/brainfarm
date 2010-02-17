package org.brainfarm.java.feat.params;


import org.brainfarm.java.feat.api.params.ICastingEvolutionParameter;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.red5.server.AttributeStore;

/**
 * Definition of all parameters , threshold and other values.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 */
public class EvolutionParameters extends AttributeStore implements IEvolutionParameters {

	public ICastingEvolutionParameter getParameter(String name) {
		return (ICastingEvolutionParameter) getAttribute(name);
	}
	
	public Integer getIntParameter(String param) {
		return getParameter(param).getIntValue();
	}
	
	public Double getDoubleParameter(String name) {
		return getParameter(name).getDoubleValue();
	}
		
	/*public List<EvolutionParameter> parameters;
	
	public EvolutionParameters() {}
	
	public List<EvolutionParameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<EvolutionParameter> parameters) {
		this.parameters = parameters;
		
		for (EvolutionParameter parameter : parameters) {
			setParameter(parameter);			
		}
	}
	
	public void setParameter(EvolutionParameter parameter) {
		parameter.set(this);
	}*/
}