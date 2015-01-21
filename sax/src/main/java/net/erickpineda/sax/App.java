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

	/**
	 * Todos los ficheros XML a leer.
	 */
	private static String[] nombresFicheros = { "/6nations06.xml",
			"/6nations07.xml", "/6nations08.xml", "/6nations09.xml",
			"/6nations10.xml", "/6nations11.xml" };

	/**
	 * Método principal del programa.
	 */
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = spf.newSAXParser();

		for (String fichero : nombresFicheros)
			parser.parse(new File(App.class.getResource(fichero).getFile()),
					new Procesar());

		Procesar.eliminarFichero(Procesar.ficheroDatos, true);
	}
}
