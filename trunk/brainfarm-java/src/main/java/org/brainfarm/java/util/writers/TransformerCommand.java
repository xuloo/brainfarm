package org.brainfarm.java.util.writers;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.util.ICommand;

public class TransformerCommand implements ICommand {

	private static Logger logger = Logger.getLogger(TransformerCommand.class);
	
	protected Transformer transformer;
	
	private ISourceGenerator source;
	
	private IResultGenerator result;
	
	private IEvolution evolution;
	
	public TransformerCommand(IEvolution evolution) {
		
		this.evolution = evolution;
		
		TransformerFactory factory = TransformerFactory.newInstance();
		
		try {
			transformer = factory.newTransformer();
		} catch (TransformerException e) {
			logger.error("Error transform: " + e.getMessage());
		}
	}
	
	@Override
	public void execute() {
		try {
			transformer.transform(source.getSource(evolution), result.getResult(evolution));
		} catch (TransformerException e) {
			logger.error("Problem transforming xml - " + e.getMessage());
		}
	}

	public void setResult(IResultGenerator result) {
		this.result = result;
	}

	public IResultGenerator getResult() {
		return result;
	}

	public void setSource(ISourceGenerator source) {
		this.source = source;
	}

	public ISourceGenerator getSource() {
		return source;
	}	
}
