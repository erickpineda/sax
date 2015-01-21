package net.erickpineda.sax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Procesar extends DefaultHandler {
	/**
	 * Ubicación del fichero que alamacenará la información.
	 */
	public static String ficheroDatos = "src/main/resources/infoPartidos";
	/**
	 * Etiqueta <i>team</i> en el XML.
	 */
	private static final String TAG_TEAM = "team";
	/**
	 * Tabla con la información de los equipos y sus puntuaciones.
	 */
	private static Hashtable<String, Equipo> tabla;
	/**
	 * Fichero de salida de escritura.
	 */
	private static FileOutputStream fos = null;
	/**
	 * Escritor del fichero.
	 */
	private static ObjectOutputStream oos = null;
	/**
	 * Lector del fichero, fichero de entrada.
	 */
	private static FileInputStream fis;
	/**
	 * Fichero de entrada.
	 */
	private static ObjectInputStream ois;
	/**
	 * Lista con los resultados en un partido.
	 */
	private List<Integer> resultadoPartido;
	/**
	 * Los equipos que participan en un partido.
	 */
	private List<Equipo> equiposPartido = null;

	/**
	 * Constructor del proceso.
	 */
	public Procesar() {
		tabla = new Hashtable<String, Equipo>();
		resultadoPartido = new ArrayList<Integer>();
		equiposPartido = new ArrayList<Equipo>();
	}

	/**
	 * Final del documento XML.
	 */
	public void endDocument() throws SAXException {

		escribirTablaEnFichero();
		tablaDeClasificacion();
	}

	/**
	 * Método, que comprueba la existencia del fichero de información.
	 */
	public void inicio() {

		File miFichero = new File(ficheroDatos);

		try {

			if (!miFichero.exists()) {
				crearFichero();
			} else {
				abrirFichero();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Crea unfichero de escritura.
	 * 
	 * @throws IOException
	 *             Excepciones de error.
	 */
	public static void crearFichero() throws IOException {

		fos = new FileOutputStream(ficheroDatos);
		oos = new ObjectOutputStream(fos);
		System.out.println("\n<- CREANDO FICHERO DE INFORMACIÓN ->\n");
	}

	/**
	 * Método que abre un fichero existente y lee la información almacenada.
	 * 
	 * @throws IOException
	 *             Excepciones de error.
	 * @throws ClassNotFoundException
	 *             Si el nombre de la clase no se encuentra.
	 */
	@SuppressWarnings("unchecked")
	public static void abrirFichero() throws IOException,
			ClassNotFoundException {

		System.out.println("\n*** REANUDANDO INFORMACIÓN ALMACENADA ***\n");

		fis = new FileInputStream(ficheroDatos);
		ois = new ObjectInputStream(fis);

		tabla = (Hashtable<String, Equipo>) ois.readObject();

	}

	/**
	 * Escribe el contenido de la <i>tabla</i> en el fichero.
	 */
	public static void escribirTablaEnFichero() {

		try {
			FileOutputStream entrada = new FileOutputStream(ficheroDatos);
			ObjectOutputStream salida = new ObjectOutputStream(entrada);
			salida.writeObject(tabla);

			entrada.close();
			salida.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Muestra por pantalla la tabla de clasificación de los equipos.
	 */
	public void tablaDeClasificacion() {

		// Convierte los valores (equipos) a un ArrayList
		List<Equipo> paises = new ArrayList<Equipo>(tabla.values());

		// Ordena la lista
		ordenarPorPuntuacion(paises);

		System.out.println(" V " + "\t" + " E " + "\t" + " D " + "\t" + " Pts "
				+ "\t" + " Equipo");

		System.out.println("-------------------------------------------");

		for (Equipo e : paises) {
			System.out.println(" " + e.getVictorias() + "\t" + e.getEmpates()
					+ "\t" + e.getDerrotas() + "\t" + e.getPuntuacion() + "\t"
					+ e.getNombrePais());
		}
	}

	/**
	 * Método que ordena la lista de equipos, por puntuación.
	 * 
	 * @param lista
	 *            Será la lista de Equipo que ordenará.
	 */
	public static void ordenarPorPuntuacion(List<Equipo> lista) {
		Collections.sort(lista, new Comparator<Equipo>() {

			/*
			 * Compara los Objetos Equipo y retorna lo que sería la puntuación
			 * más alta.
			 */
			public int compare(Equipo e1, Equipo e2) {
				return new Integer(e2.getPuntuacion()).compareTo(new Integer(e1
						.getPuntuacion()));
			}
		});
	}

	/**
	 * Inicio de una etiqueta XML.
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		// Busca la etiqueta team
		if (qName.equals(TAG_TEAM)) {
			String equipo = "";
			int puntos = 0;

			// Recoge el nombre del equipo
			if (!attributes.getValue("name").isEmpty()) {
				equipo = attributes.getValue(0);

			}

			// Recoge los puntos del equipo en el partido
			if (!attributes.getValue("score").isEmpty()) {
				String puntoPais = attributes.getValue(1).trim();
				puntos = Integer.parseInt(puntoPais);

			}
			resultadoPartido(equipo, puntos);
		}
	}

	/**
	 * Método que almacena los resultados de un partido, dentro de la
	 * <i>tabla</i>.
	 * 
	 * @param nombrePais
	 *            Será el nombre del equipo.
	 * @param puntos
	 *            Serán el marcador final del partido, para el equipo.
	 */
	public void resultadoPartido(String nombrePais, int puntos) {

		// Si no hay equipos con ese nombre, crea uno nuevo y lo guarda
		if (!tabla.containsKey(nombrePais))
			tabla.put(nombrePais, new Equipo(nombrePais));

		equiposPartido.add(new Equipo(nombrePais));
		resultadoPartido.add(puntos);

	}

	/**
	 * Método que comprueba los partidos y define quien ha ganado, perdido o si
	 * han empatado.
	 */
	public void resultados() {

		if (resultadoPartido.get(0) > resultadoPartido.get(1)) {

			tabla.get(equiposPartido.get(0).getNombrePais()).setPuntuacion(2);
			tabla.get(equiposPartido.get(0).getNombrePais()).sumarVictoria();
			tabla.get(equiposPartido.get(1).getNombrePais()).sumarDerrota();

		} else if (resultadoPartido.get(0) == resultadoPartido.get(1)) {

			tabla.get(equiposPartido.get(0).getNombrePais()).setPuntuacion(1);
			tabla.get(equiposPartido.get(1).getNombrePais()).setPuntuacion(1);
			tabla.get(equiposPartido.get(0).getNombrePais()).sumarEmpate();
			tabla.get(equiposPartido.get(1).getNombrePais()).sumarEmpate();

		} else {

			tabla.get(equiposPartido.get(1).getNombrePais()).setPuntuacion(2);
			tabla.get(equiposPartido.get(1).getNombrePais()).sumarVictoria();
			tabla.get(equiposPartido.get(0).getNombrePais()).sumarDerrota();

		}

	}

	/**
	 * Final de una etiqueta XML.
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		/*
		 * Siempre que termina un partido, borro los resultados y los equipos,
		 * para reusar los ArrayList.
		 */
		if (qName.equals("match")) {
			resultados();
			resultadoPartido.clear();
			equiposPartido.clear();
		}
	}

	/**
	 * Método que inicia el Documento XML.
	 */
	public void startDocument() throws SAXException {
		inicio();
	}

	/**
	 * Método que elimina un fichero.
	 * 
	 * @param pathFile
	 *            Será la ubicación del fichero.
	 * @param msj
	 *            Si quiere borrar el fichero al momento.
	 */
	public static void eliminarFichero(String pathFile, boolean msj) {
		File f = new File(pathFile);

		if (msj == true)
			if (f.delete())
				System.out.println("\n" + pathFile
						+ " se ha borrado correctamente!");
			else
				System.out
						.println("\n" + pathFile + " no se ha podido borrar!");

	}
}
