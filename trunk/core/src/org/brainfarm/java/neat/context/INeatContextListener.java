package org.brainfarm.java.neat.context;

import org.brainfarm.java.neat.api.context.INeatContext;

public interface INeatContextListener {
	
	void contextChanged(INeatContext context);
	
	void experimentChanged(INeatContext context);
}
