package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.red5.message.BaseMessage;
import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class ResultSummaryMessage extends BaseMessage {

	private int numberOfRuns = 0;
	
	private int numberOfEpochs = 0;
	
	@Override
	public void readExternal(IDataInput input) {
		super.readExternal(input);
		
		setNumberOfRuns(input.readInt());
		setNumberOfEpochs(input.readInt());
	}
	
	@Override
	public void writeExternal(IDataOutput output) {
		super.writeExternal(output);
		
		output.writeInt(getNumberOfRuns());
		output.writeInt(getNumberOfEpochs());
	}

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setNumberOfEpochs(int numberOfEpochs) {
		this.numberOfEpochs = numberOfEpochs;
	}

	public int getNumberOfEpochs() {
		return numberOfEpochs;
	}
}
