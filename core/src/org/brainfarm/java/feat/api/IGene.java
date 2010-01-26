package org.brainfarm.java.feat.api;

public interface IGene {
	
	ILink getLink();
	void setLink(ILink link);
	
	double getInnovationNumber();
	void setInnovationNumber(double innovationNumber);	
	
	double getMutationNumber();
	void setMutationNumber(double mutationNumber);
	
	boolean isEnabled();
	void setEnabled(boolean enabled);
}
