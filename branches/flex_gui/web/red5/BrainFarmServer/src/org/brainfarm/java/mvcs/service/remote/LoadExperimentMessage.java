package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.red5.message.BaseMessage;
import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;

public class LoadExperimentMessage extends BaseMessage {

	private String experiment = "";
	
	public LoadExperimentMessage() {
		
	}
	
	@Override
	public Object read() {
		System.out.println("Loading an experiment " + experiment);
		service.loadExperiment(experiment);
		return "hello";
	}
	
	@Override
	public void readExternal(IDataInput input) {
		super.readExternal(input);
		
		experiment = input.readUTF();
	}
	
	@Override
	public void writeExternal(IDataOutput output) {
		super.writeExternal(output);
		
		output.writeUTF(experiment);
	}
}
