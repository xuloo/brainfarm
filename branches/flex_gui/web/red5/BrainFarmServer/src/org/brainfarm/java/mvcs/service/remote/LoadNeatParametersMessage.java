package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.red5.message.BaseMessage;
import org.red5.io.amf3.IDataInput;

public class LoadNeatParametersMessage extends BaseMessage {

	public LoadNeatParametersMessage() {
		
	}
	
	@Override
	public Object read() {
		System.out.println("LoadNeatParametersMessage.read()");
		return service.loadNeatParameters();
	}
	
	@Override 
	public void readExternal(IDataInput input) {
		
		System.out.println("reading LoadNeatParametersMessage");
		super.readExternal(input);
		
		
	}
}
