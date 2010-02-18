package org.brainfarm.java.red5.api.service.message;

import java.util.List;

public interface IMessageQueue {
	
	public List<IMessage> getMessages();
	
	public void addMessage(IMessage message);
	
	public void clearMessages();
}
