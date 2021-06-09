/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author carli
 */
public class GeneticoReina {
    private int tamanoPob;
    private int numGeneraciones;
    private double probMuta;
    private int bits;
    private ArrayList<Reina> poblacionActual;
    private double[] fitgen;

    public GeneticoReina(int tamanoPob, int numGeneraciones, double probMuta, int bits, ArrayList<Reina> poblacionActual) {
        this.tamanoPob = tamanoPob;
        this.numGeneraciones = numGeneraciones;
        this.probMuta = probMuta;
        this.bits = bits;
        this.poblacionActual = poblacionActual;
        this.fitgen = new double[numGeneraciones];
    }
    
    
    
    public GeneticoReina(int tamanoPob, int numGeneraciones, 
            double probMuta,int bits) {
        this.tamanoPob = tamanoPob;
        this.numGeneraciones = numGeneraciones;
        this.probMuta = probMuta;
        this.bits =bits;
        this.poblacionActual = new ArrayList<>();
        this.fitgen= new double[numGeneraciones];
    }
    
    public void evolucionar(boolean aleatorio){
    
    if(aleatorio){
        generarPoblacionInicial();    
    }    
// proceso evolutivo que tiene relación con el numero de generaciones
    this.fitgen[0]= (double) Tools.mejorPoblacion(poblacionActual).getFitness();
    for(int g=1;g<this.getNumGeneraciones();g++){
        ArrayList<Reina> nuevaPob = new ArrayList<>();
        // garantizar que se va a generar una población nueva 
        for (int i=0; i<this.getTamanoPob();i++){
            // Seleccion de una madre 
            Reina madre = Seleccion.seleccionAleatoria(this.getPoblacionActual());
            // Seleccion de un padre
            Reina padre = Seleccion.seleccionAleatoria(this.getPoblacionActual());
            // cruza (Retornar el mejor ind de la cruza)
            int[] mask = binario.Herramientas.generarArregloBinarios(madre.getGenotipo().length);
            Reina hijo = Cruza.cruzaPorMascaraBinaria(madre, padre, mask);
            // Se aplica una muta evaluando una probabilidad
            if (generarProbabilidadMuta()){
               Muta.mutaSimple(hijo);
            }
            nuevaPob.add(hijo);
        }
        // actualización de la población
        sustituirPoblacion(nuevaPob);
        this.fitgen[g]= (double) Tools.mejorPoblacion(nuevaPob).getFitness();
    }
    
    
    
    
    }

    private void generarPoblacionInicial() {
       // generar un población aleatoria de individuos
       for(int i=0; i < this.getTamanoPob();i++){
           this.getPoblacionActual().add(new Reina(this.getBits()));
       }
       
    }

    private boolean generarProbabilidadMuta() {
       
        if (this.getProbMuta()>Math.random()){
           return true;
       } else{ return false;}
        
    }

    private void sustituirPoblacion(ArrayList<Reina> nuevaPob) {
        this.getPoblacionActual().clear();
       for(Reina aux:nuevaPob){
           this.getPoblacionActual().add(new Reina(aux));
       }
    }

    /**
     * @return the poblacionActual
     */
    public ArrayList<Reina> getPoblacionActual() {
        return poblacionActual;
    }

    /**
     * @return the fitgen
     */
    public double[] getFitgen() {
        return fitgen;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return "GeneticoReina"+poblacionActual.size()+"_"+getBits()+"_"+dtf.format(LocalDateTime.now());
    }

    /**
     * @return the tamanoPob
     */
    public int getTamanoPob() {
        return tamanoPob;
    }

    /**
     * @return the numGeneraciones
     */
    public int getNumGeneraciones() {
        return numGeneraciones;
    }

    /**
     * @return the probMuta
     */
    public double getProbMuta() {
        return probMuta;
    }

    /**
     * @return the bits
     */
    public int getBits() {
        return bits;
    }
    
}
