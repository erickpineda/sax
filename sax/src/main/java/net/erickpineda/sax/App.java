package net.erickpineda.sax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * @author Erick Pineda - Programa que usa la libreria SAX.
 *
 */
public class App {
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = spf.newSAXParser();

		parser.parse(new File(App.class.getResource("/6nations06.xml")
				.getFile()), new Procesar());
	}
}
