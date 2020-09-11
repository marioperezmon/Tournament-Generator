package com.example.proyectofinaldm;

public class Jugador {
    private String nombre;
    private String apellidos;
    private int numVictorias;
    private int numTorneos;
    private boolean participa;
    private boolean eliminar;

    public Jugador(){

    }

    public Jugador(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numVictorias = 0;
        this.numTorneos = 0;
        this.participa = false;
        this.eliminar = false;
    }

    public Jugador(String nombre, String apellidos, int numVictorias, int numTorneos){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numVictorias = numVictorias;
        this.numTorneos = numTorneos;
        this.participa = false;
        this.eliminar = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getNumVictorias() {
        return numVictorias;
    }

    public void setNumVictorias(int numVictorias) {
        this.numVictorias = numVictorias;
    }

    public int getNumTorneos() {
        return numTorneos;
    }

    public void setNumTorneos(int numTorneos) {
        this.numTorneos = numTorneos;
    }

    public void sumarVictoria(){
        int victorias = 0;
        victorias = this.getNumVictorias();
        victorias++;
        this.setNumVictorias(victorias);
    }

    public void sumarTorneo(){
        int victorias = 0;
        victorias = this.getNumTorneos();
        victorias++;
        this.setNumTorneos(victorias);
    }

    public String toString(){
        return nombre + " " + apellidos;
    }

    public String toString2(){
        return nombre + " " + apellidos + " " + numVictorias + " " + numTorneos;
    }

    public boolean getParticipa() {
        return participa;
    }

    public void setParticipa(boolean participa) {
        this.participa = participa;
    }

    public void setEliminar(boolean eliminar) { this.eliminar = eliminar; }
}