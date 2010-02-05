package org.brainfarm.java.red5.service;

import java.util.List;

import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.context.SpringNeatContext;
import org.brainfarm.java.feat.controller.SpringNeatController;
import org.brainfarm.java.feat.params.AbstractNeatParameter;
import org.brainfarm.java.red5.api.service.IBrainFarmService;
import org.brainfarm.java.red5.api.service.message.IMessage;

public class BrainFarmService implements IBrainFarmService {

	private INeatContext context;
	
	private SpringNeatController controller;
	
	public BrainFarmService() {
		context = new SpringNeatContext();
		controller = new SpringNeatController(context);
	}
	
	public List<AbstractNeatParameter> loadNeatParameters() {
		System.out.println("loading neat parameters");
		controller.loadDefaultParameters();
		
		return context.getNeat().getParameters();
	}
	
	public Object receiveMessage(IMessage message) {
		System.out.println("message: " + message);
		message.setService(this);
		return message.read();
	}
}
