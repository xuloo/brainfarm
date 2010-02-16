package org.brainfarm.java.feat.api.context;

import org.brainfarm.java.feat.api.context.IEvolutionContext;

public interface IEvolutionContextListener {
	
	void contextChanged(IEvolutionContext context);
	
	void experimentChanged(IEvolutionContext context);
}
