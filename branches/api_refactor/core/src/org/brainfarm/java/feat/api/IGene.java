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
	
	/**
	 * Used during crossover to determine if two IGenes have
	 * the same function.
	 * 
	 * @param other
	 * @return true if this IGene has the same function as 'other'
	 */
	boolean sameAs(IGene other);
}
