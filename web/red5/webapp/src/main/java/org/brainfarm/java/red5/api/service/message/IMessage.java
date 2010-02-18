package org.brainfarm.java.red5.api.service.message;

import org.brainfarm.java.red5.api.service.IBrainFarmService;
import org.red5.io.amf3.IExternalizable;

public interface IMessage extends IExternalizable {

	public Object read();
	
	public void write();
	
	public void setService(IBrainFarmService service);
	
	public String getSenderId();
	
	public void setSenderId(String id);
}
