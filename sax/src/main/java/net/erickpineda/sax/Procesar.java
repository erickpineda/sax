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
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Procesar extends DefaultHandler {
    /**
     * Ubicación del fichero que alamacenará la información.
     */
    public String ficheroDatos = "infoPartidos";
    /**
     * Etiqueta <i>team</i> en el XML.
     */
    private static final String TAG_TEAM = "team";
    /**
     * Tabla con la información de los equipos y sus puntuaciones.
     */
    private static Map<String, Equipo> tabla;
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

        if (!miFichero.exists()) {
            crearFichero();
        } else {
            abrirFichero();
        }

        cerrarFichero();
    }

    /**
     * Crea unfichero de escritura.
     * 
     * @throws IOException Excepciones de error.
     */
    public void crearFichero() {
        try {
            fos = new FileOutputStream(ficheroDatos);
            oos = new ObjectOutputStream(fos);
            System.out.println("\n<- CREANDO FICHERO DE INFORMACIÓN ->\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que abre un fichero existente y lee la información almacenada.
     * 
     * @throws IOException Excepciones de error.
     * @throws ClassNotFoundException Si el nombre de la clase no se encuentra.
     */

    @SuppressWarnings("unchecked")
    public void abrirFichero() {
        try {
            System.out.println("\n*** REANUDANDO INFORMACIÓN ALMACENADA ***\n");
            fis = new FileInputStream(ficheroDatos);
            ois = new ObjectInputStream(fis);
            tabla = (Map<String, Equipo>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe el contenido de la <i>tabla</i> en el fichero.
     */
    private void escribirTablaEnFichero() {
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

        System.out.println(" V \tE\tD\tPts\tEquipo");
        System.out.println("-------------------------------------------");

        paises.forEach(p -> System.out.println(p));
    }

    /**
     * Método que ordena la lista de equipos, por puntuación.
     * 
     * @param lista Será la lista de Equipo que ordenará.
     */
    public void ordenarPorPuntuacion(List<Equipo> lista) {
        Collections.sort(lista, new Comparator<Equipo>() {
            /**
             * Compara los Objetos Equipo y retorna lo que sería la puntuación
             * más alta.
             */
            public int compare(Equipo e1, Equipo e2) {
                return e2.getPuntuacion() - e1.getPuntuacion();
            }
        });
    }

    /**
     * Inicio de una etiqueta XML.
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
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
     * @param nombrePais Será el nombre del equipo.
     * @param puntos Serán el marcador final del partido, para el equipo.
     */
    private void resultadoPartido(String nombrePais, int puntos) {
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
    private void resultados() {
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
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Siempre que termina un partido, borro los resultados y los equipos,
        // para reusar los ArrayList.
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
     * Método que borra el fichero de datos del programa.
     */
    public void eliminarFichero() {
        File f = new File(ficheroDatos);
        if (f.delete()) {
            System.out.println("\n-> " + ficheroDatos + " se ha borrado correctamente!");
        } else {
            System.out.println("\n-> " + ficheroDatos + " no se ha podido borrar!");
        }
    }

    /**
     * Método para cerrar el fichero de datos.
     */
    public void cerrarFichero() {
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