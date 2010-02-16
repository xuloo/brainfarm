package org.brainfarm.java.feat.api;

public interface IEvolutionContextListener {
	
	void contextChanged(IEvolutionContext context);
	
	void experimentChanged(IEvolutionContext context);
}
