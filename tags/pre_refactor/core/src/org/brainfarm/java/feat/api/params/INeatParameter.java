package org.brainfarm.java.feat.api.params;

import org.brainfarm.java.feat.Neat;

public interface INeatParameter {

	public String getKey();
	
	public void setKey(String key);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public abstract void set(Neat neat);
}
