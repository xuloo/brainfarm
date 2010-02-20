package org.brainfarm.java.red5.message;

import org.brainfarm.java.red5.api.service.IBrainFarmService;
import org.brainfarm.java.red5.api.service.message.IMessage;
import org.brainfarm.java.red5.net.NetObject;
import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;

public class BaseMessage extends NetObject implements IMessage {

	protected String senderId = "-1";
	
	protected IBrainFarmService service;
	
	public BaseMessage() {
		
	}
	
	public BaseMessage(String senderId) {
		this.senderId = senderId;
	}
	
	public String getSenderId() {
		return senderId;
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	public void write() {
		
	}
	
	public Object read() {
		return null;
	}
	
	public void setService(IBrainFarmService service) {
		this.service = service;
	}

	public void readExternal(IDataInput input) {
		System.out.println("Reading Base Message");
		senderId = input.readUTF();
	}

	public void writeExternal(IDataOutput output) {
		System.out.println("Writing external");
		output.writeUTF(senderId);
	}

}
