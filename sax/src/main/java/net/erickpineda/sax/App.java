package net.erickpineda.sax;

import java.io.IOException;
import java.io.InputStream;

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
    private String[] nombresFicheros = { "/6nations06.xml", "/6nations07.xml", "/6nations08.xml",
            "/6nations09.xml", "/6nations10.xml", "/6nations11.xml" };

    /**
     * Método principal del programa.
     */
    public static void main(String[] args) {
        try {
            new App().inicio();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void inicio() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        Procesar procesar = new Procesar();

        for (String fichero : nombresFicheros) {
            InputStream entrada = App.class.getResourceAsStream(fichero);
            parser.parse(entrada, new Procesar());
        }
        procesar.eliminarFichero();
    }
}
