package org.brainfarm.java.api;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface ISpringXMLCapable {
	
	Node toSpringXML(Document xml);
}
