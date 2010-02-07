package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.red5.message.BaseMessage;

public class LoadNeatParametersMessage extends BaseMessage {

	public LoadNeatParametersMessage() {
		
	}
	
	@Override
	public Object read() {
		System.out.println("LoadNeatParametersMessage.read()");
		return service.loadNeatParameters();
	}
}
