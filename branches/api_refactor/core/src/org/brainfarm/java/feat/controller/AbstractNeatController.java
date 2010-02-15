package org.brainfarm.java.feat.controller;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.INeatController;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.context.IExperiment;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

public abstract class AbstractNeatController implements INeatController {

	protected static Logger logger = Logger.getLogger(AbstractNeatController.class);

	protected INeatContext context;

	protected JclObjectFactory factory;

	protected JarClassLoader jarClassLoader;

	protected void refresh(IExperiment experiment) {
	
	}
}
