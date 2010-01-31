package org.brainfarm.java.feat.context;

import org.brainfarm.java.feat.api.context.INeatContext;

public interface INeatContextListener {
	
	void contextChanged(INeatContext context);
	
	void experimentChanged(INeatContext context);
}
