/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.util.ArrayList;

/**
 *
 * @author skabe
 */
public class GeneticoTCP {
    private int numGen;
    private int tamPoblacion;
    private double probMuta;
    private ArrayList<Long> fitnessGeneracional= new ArrayList<>();
    private ArrayList<IndividuoTCP> poblacionActual= new ArrayList<>();
    private int nodoInicial;
    private String tipoSeleccion;

    public GeneticoTCP(int numGen, int tamPoblacion, double probMuta, int nodoInicial, String tipoSeleccion) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMuta = probMuta;
        this.nodoInicial = nodoInicial;
        this.tipoSeleccion = tipoSeleccion;
        generarPoblacion();
    }
    public void evolucionar(){
        getFitnessGeneracional().add(Herramientas.mejorPoblacion(getPoblacionActual()).getFitness());
        for (int i = 0; i < getNumGen(); i++) {
            ArrayList<IndividuoTCP> nuevaPob= new ArrayList<>();
            Seleccion.reset();
            Seleccion.setTipoSeleccion(getTipoSeleccion());
            for (int j = 0; j < getTamPoblacion(); j++) {
                IndividuoTCP madre= Seleccion.seleccion(getPoblacionActual());
                IndividuoTCP padre= Seleccion.seleccion(getPoblacionActual());
                int[] mask= Herramientas.generarArregloBinarios(madre.getGenotipo().length);
                IndividuoTCP hijo= Cruza.cruzaMascaraBin(padre, madre, mask);
                if(generarProbabilidadMuta()){
                    Muta.muta(hijo);
                }
                nuevaPob.add(hijo);
            }
            sustituirPoblacion(nuevaPob);
            getFitnessGeneracional().add(Herramientas.mejorPoblacion(nuevaPob).getFitness());
        }
    }
    private void generarPoblacion(){
        IndividuoTCP.setInicial(nodoInicial);
        IndividuoTCP.setMatriz(AbrirMatriz.abrirMatriz());
        for (int i = 0; i < getTamPoblacion(); i++) {
            IndividuoTCP nuevo= new IndividuoTCP();
            getPoblacionActual().add(nuevo);
        }
    }
    private void sustituirPoblacion(ArrayList<IndividuoTCP> nuevaPob) {
        getPoblacionActual().clear();
       for(IndividuoTCP aux:nuevaPob){
            getPoblacionActual().add(new IndividuoTCP(aux.getGenotipo()));
       }
    }
    private boolean generarProbabilidadMuta() {
        if (getProbMuta()>=Math.random()){
           return true;
       } else{ return false;}
    }
    /**
     * @return the numGen
     */
    public int getNumGen() {
        return numGen;
    }

    /**
     * @param numGen the numGen to set
     */
    public void setNumGen(int numGen) {
        this.numGen = numGen;
    }

    /**
     * @return the tamPoblacion
     */
    public int getTamPoblacion() {
        return tamPoblacion;
    }

    /**
     * @param tamPoblacion the tamPoblacion to set
     */
    public void setTamPoblacion(int tamPoblacion) {
        this.tamPoblacion = tamPoblacion;
    }

    /**
     * @return the probMuta
     */
    public double getProbMuta() {
        return probMuta;
    }

    /**
     * @param probMuta the probMuta to set
     */
    public void setProbMuta(double probMuta) {
        this.probMuta = probMuta;
    }

    /**
     * @return the fitnessGeneracional
     */
    public ArrayList<Long> getFitnessGeneracional() {
        return fitnessGeneracional;
    }

    /**
     * @param fitnessGeneracional the fitnessGeneracional to set
     */
    public void setFitnessGeneracional(ArrayList<Long> fitnessGeneracional) {
        this.fitnessGeneracional = fitnessGeneracional;
    }

    /**
     * @return the poblacionActual
     */
    public ArrayList<IndividuoTCP> getPoblacionActual() {
        return poblacionActual;
    }

    /**
     * @param poblacionActual the poblacionActual to set
     */
    public void setPoblacionActual(ArrayList<IndividuoTCP> poblacionActual) {
        this.poblacionActual = poblacionActual;
    }

    /**
     * @return the nodoInicial
     */
    public int getNodoInicial() {
        return nodoInicial;
    }

    /**
     * @param nodoInicial the nodoInicial to set
     */
    public void setNodoInicial(int nodoInicial) {
        this.nodoInicial = nodoInicial;
    }

    /**
     * @return the tipoSeleccion
     */
    public String getTipoSeleccion() {
        return tipoSeleccion;
    }

    /**
     * @param tipoSeleccion the tipoSeleccion to set
     */
    public void setTipoSeleccion(String tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }
    
}
