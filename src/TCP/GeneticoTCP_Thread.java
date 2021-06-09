/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import GUI.JPanel_hilos;
import GUI.Principal;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skabe
 */
public class GeneticoTCP_Thread extends Thread{
    private int numGen;
    private int tamPoblacion;
    private double probMuta;
    private ArrayList<Long> fitnessGeneracional= new ArrayList<>();
    private ArrayList<IndividuoTCP> poblacionActual= new ArrayList<>();
    private int nodoInicial;
    private int [][] matriz;
    private String tipoSeleccion;
    private Semaphore semaf;
    private JPanel_hilos jPanel_hilos;

    public GeneticoTCP_Thread(int numGen, int tamPoblacion, double probMuta, int nodoInicial, int [][] matriz, String tipoSeleccion,ArrayList<IndividuoTCP> poblacionActual, ArrayList<Long> fitnessGeneracional,  Semaphore semaf, JPanel_hilos jPanel_hilos) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMuta = probMuta;
        this.nodoInicial = nodoInicial;
        IndividuoTCP.setInicial(nodoInicial);
        this.matriz=matriz;
        IndividuoTCP.setMatriz(matriz);
        this.tipoSeleccion = tipoSeleccion;
        this.poblacionActual= poblacionActual;
        this.fitnessGeneracional= fitnessGeneracional;
        this.semaf = semaf;
        this.jPanel_hilos = jPanel_hilos;
    }
    public GeneticoTCP_Thread(int numGen, int tamPoblacion, double probMuta, int nodoInicial, int [][] matriz, String tipoSeleccion, ArrayList<Long> fitnessGeneracional,  Semaphore semaf, JPanel_hilos jPanel_hilos) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMuta = probMuta;
        this.nodoInicial = nodoInicial;
        IndividuoTCP.setInicial(nodoInicial);
        this.matriz=matriz;
        IndividuoTCP.setMatriz(matriz);
        this.tipoSeleccion = tipoSeleccion;
        this.fitnessGeneracional= fitnessGeneracional;
        this.semaf = semaf;
        this.jPanel_hilos = jPanel_hilos;
        generarPoblacion();
    }
    
    
    @Override
    public void run() {
        getFitnessGeneracional().add(Herramientas.mejorPoblacion(poblacionActual).getFitness());
        for (int i = 0; i < getNumGen(); i++) {
            try {
                semaf.acquire();
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
                getFitnessGeneracional().add( Herramientas.mejorPoblacion(nuevaPob).getFitness());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally{
                semaf.release();
                try {
                   Thread.sleep(1);
                } catch (InterruptedException ex) {
                   ex.printStackTrace();
                }
            }
        }
        getjPanel_hilos().terminoEvol();
    }
    private void generarPoblacion(){
        try {
            semaf.acquire();
            for (int i = 0; i < getTamPoblacion(); i++) {
                IndividuoTCP nuevo= new IndividuoTCP();
                getPoblacionActual().add(nuevo);
            }
            semaf.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneticoTCP_Thread.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * @return the jPanel_hilos
     */
    public JPanel_hilos getjPanel_hilos() {
        return jPanel_hilos;
    }

    
}
