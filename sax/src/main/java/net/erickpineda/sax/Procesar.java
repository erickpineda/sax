package net.erickpineda.sax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Procesar extends DefaultHandler {

	private int numElementos = 0;

	private int equiposEnPartido = 0;

	private static String ficheroDatos = "src/main/resources/infoPartidos";

	private static FileWriter ficheroAEscribir = null;

	private static PrintWriter out = null;

	private static InputStream ficheroALeer = null;

	private static BufferedReader in = null;

	private boolean tagTeam = false;

	private static final String TAG_TEAM = "team";

	private StringBuilder team = new StringBuilder();

	private static Hashtable<String, Integer> tabla;

	private List<Integer> resultadoPartido;

	private String[][] bidi;

	public Procesar() {

		inicio();
	}

	public void endDocument() throws SAXException {

		System.out.println(team.length());

		System.out.println("Total etiquetas: " + numElementos);
	}

	public void inicio() {

		File miFichero = new File(ficheroDatos);

		try {

			if (!miFichero.exists()) {

				ficheroAEscribir = new FileWriter(ficheroDatos);
				out = new PrintWriter(ficheroAEscribir);

				System.out.println(" Fichero Creado    ");

			} else {
				ficheroALeer = Procesar.class.getResourceAsStream(ficheroDatos
						.replace("src/main/resources", ""));

				in = new BufferedReader(new InputStreamReader(ficheroALeer));

				System.out.println(" Fichero Leido    ");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ficheroAEscribir != null || ficheroALeer != null) {

				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void leerFichero() {

	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		// System.out.println("Start Element :" + qName);

		tagTeam = TAG_TEAM.equals(qName);

		if (qName.equals(TAG_TEAM)) {
			equiposEnPartido++;

			String pais = "";
			int puntos = 0;

			if (!attributes.getValue("name").isEmpty()) {
				pais = attributes.getValue(0);

			}
			if (!attributes.getValue("score").isEmpty()) {
				String puntoPais = attributes.getValue(1).trim();
				puntos = Integer.parseInt(puntoPais);

			}

			puntosPaises(pais, puntos);
		}
		numElementos++;
	}

	public void puntosPaises(String nombrePais, int puntos) {

		if (tabla.containsKey(nombrePais)) {
			tabla.get(nombrePais);
		} else {
			tabla.put(nombrePais, puntos);
		}

		resultadoPartido.add(puntos);

	}

	public void resultados() {

		int pos = 0;

		if (resultadoPartido.get(0) > resultadoPartido.get(1)) {

			bidi[pos][0] = resultadoPartido.get(0).toString();

			System.out.println("Ganador: " + resultadoPartido.get(0) + " -> "
					+ bidi[pos][0]);
			System.out.println("Perdedor: " + resultadoPartido.get(1));

		} else if (resultadoPartido.get(0) < resultadoPartido.get(1)) {
			System.out.println("Ganador: " + resultadoPartido.get(1));
			System.out.println("Perdedor: " + resultadoPartido.get(0));
		} else {
			System.out.println("Empate: " + resultadoPartido.get(0));
			System.out.println("Empate: " + resultadoPartido.get(1));
		}

		pos++;

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equals("match")) {
			resultados();
			resultadoPartido.clear();
			tabla.clear();
		}

		if (tagTeam) {
			tagTeam = false;

		}

		// System.out.println("End Element :" + qName);

	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		if (tagTeam)
			team.append(new String(ch, start, length));

	}

	public void startDocument() throws SAXException {
		tabla = new Hashtable<String, Integer>();
		resultadoPartido = new ArrayList<Integer>();
		bidi = new String[5][2];
	}
}
