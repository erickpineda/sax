package net.erickpineda.sax;

import java.io.Serializable;

public class Equipo implements Serializable {

    /**
     * ID del Objeto.
     */
    private static final long serialVersionUID = 9019804583007999109L;
    /**
     * Variable que será el nombre del País.
     */
    private String nombrePais;
    /**
     * Puntos que tendrá el País.
     */
    private int puntuacion;
    /**
     * Cantidad de victorias del País.
     */
    private int victoria;
    /**
     * Cantidad de derrotas del País.
     */
    private int derrota;
    /**
     * Cantidad de empates del País.
     */
    private int empate;

    /**
     * Constructor de equipos por medio del nombre.
     * 
     * @param miPais
     *            Parámetro que recibe el nombre del País.
     */
    public Equipo(String miPais) {
        this.nombrePais = miPais;
    }

    /**
     * Método que suma la cantidad de victorias de un País.
     */
    public void sumarVictoria() {
        this.victoria += 1;
    }

    /**
     * Método que suma la cantidad de derrotas de un País.
     */
    public void sumarDerrota() {
        this.derrota += 1;
    }

    /**
     * Método que suma la cantidad de empates de un País.
     */
    public void sumarEmpate() {
        this.empate += 1;
    }

    /**
     * Método que retorna el nombre del País.
     * 
     * @return Retorna el nombre del País.
     */
    public String getNombrePais() {
        return this.nombrePais;
    }

    /**
     * Método que retorna la cantidad de victorias de un País.
     * 
     * @return Retorna las victorias del País.
     */
    public int getVictorias() {
        return this.victoria;
    }

    /**
     * Método que retorna la cantidad de derrotas de un País.
     * 
     * @return Retorna las derrotas del País.
     */
    public int getDerrotas() {
        return this.derrota;
    }

    /**
     * Método que retorna la cantidad de empates de un País.
     * 
     * @return Retorna los empates del País.
     */
    public int getEmpates() {
        return this.empate;
    }

    /**
     * Método que retorna la cantidad de puntos de un País.
     * 
     * @return Retorna los puntos del País.
     */
    public int getPuntuacion() {
        return this.puntuacion;
    }

    /**
     * Método que permite cambiar el nombre de un País.
     * 
     * @param nombrePais
     *            Parámetro que pasa el nombre del País a cambiar.
     */
    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    /**
     * Método que permite cambiar las victorias de un País.
     * 
     * @param victoria
     *            Parámetro que pasa a cambiar la(s) victoria(s) del País.
     */
    public void setVictoria(int victoria) {
        this.victoria = victoria;
    }

    /**
     * Método que permite cambiar las derrotas de un País.
     * 
     * @param derrota
     *            Parámetro que pasa a cambiar la(s) derrota(s) del País.
     */
    public void setDerrota(int derrota) {
        this.derrota = derrota;
    }

    /**
     * Método que permite cambiar los empates de un País.
     * 
     * @param empate
     *            Parámetro que pasa a cambiar los empates del País.
     */
    public void setEmpate(int empate) {
        this.empate = empate;
    }

    /**
     * Método que permite cambiar la puntuación de un País.
     * 
     * @param puntos
     *            Parámetro que pasa a cambiar los puntos del País.
     */
    public void setPuntuacion(int puntos) {
        this.puntuacion += puntos;
    }

    /**
     * Método que permite concatena el nombre del País y su puntuación.
     */
    public String toString() {
        return " " + this.getVictorias() + "\t" + this.getEmpates() + "\t" + this.getDerrotas()
                + "\t" + this.getPuntuacion() + "\t" + this.getNombrePais();
    }
}
