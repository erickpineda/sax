package net.erickpineda.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Procesar extends DefaultHandler {
	int numElementos = 0;

	public void endDocument() throws SAXException {
		System.out.println("Total etiquetas: " + numElementos);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		numElementos++;
	}
}
