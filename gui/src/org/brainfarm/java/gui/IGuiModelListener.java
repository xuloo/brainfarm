package org.brainfarm.java.gui;

import org.brainfarm.java.feat.api.context.IEvolutionContext;

public interface IGuiModelListener {
	
	void contextChanged(IEvolutionContext context);
	
	void experimentChanged(IEvolutionContext context);
}
