package org.brainfarm.mvcs.service;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.util.writers.EvolutionResultWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileSystemEvolutionResultWriter extends EvolutionResultWriter {

	public static final String HAS_WINNER_ATTRIBUTE_NAME = "hasWinner";
	public static final String WINNER_FILE_ATTRIBUTE_NAME = "winnerFile";
	
	protected EpochData[][] epochData;
	
	public FileSystemEvolutionResultWriter(EpochData[][] epochData) {
		this.epochData = epochData;
	}
	
	@Override
	protected Element writeEpoch(Document xml, IEvolution evolution, int run, int epoch) {
		Element epochNode = super.writeEpoch(xml, evolution, run, epoch);
		
		EpochData ed = epochData[run][epoch];
		
		epochNode.setAttribute(HAS_WINNER_ATTRIBUTE_NAME, Boolean.toString(ed.hasWinner));
		
		if (ed.hasWinner) {
			epochNode.setAttribute(WINNER_FILE_ATTRIBUTE_NAME, ed.winnerFileName);
		}
		
		return epochNode;
	}
}
