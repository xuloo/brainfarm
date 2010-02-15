package org.brainfarm.java.gui;

import org.brainfarm.java.feat.api.context.INeatContext;

public interface IGuiModelListener {
	
	void contextChanged(INeatContext context);
	
	void experimentChanged(INeatContext context);
}
