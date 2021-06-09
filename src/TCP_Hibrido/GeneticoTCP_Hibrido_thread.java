/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP_Hibrido;

import GUI.JPanel_hilos;
import GUI.Principal;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javafx.util.Pair;

/**
 *
 * @author skabe
 */
public class GeneticoTCP_Hibrido_thread extends Thread{
    private int numGen;
    private int tamPoblacion;
    private double probMuta;
    private ArrayList<Double> fitnessGeneracional= new ArrayList<>();
    private ArrayList<IndividuoTCP_Hibrido> poblacionActual= new ArrayList<>();
    private int nodoInicial;
    private String tipoSeleccion;
    private double[][] matrizInclinaciones;
    private int[][] matrizPesos;
    private Semaphore semaf;
    private JPanel_hilos jPanel_hilos;
    
    public GeneticoTCP_Hibrido_thread(int numGen, int tamPoblacion, double probMuta, int nodoInicial, double [][] matrizInclinaciones, int [][] matrizPesos, String tipoSeleccion,ArrayList<IndividuoTCP_Hibrido> poblacionActual, ArrayList<Double> fitnessGeneracional,  Semaphore semaf, JPanel_hilos jPanel_hilos){
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMuta = probMuta;
        this.nodoInicial = nodoInicial;
        IndividuoTCP_Hibrido.setInicial(nodoInicial);
        this.matrizPesos=matrizPesos;
        IndividuoTCP_Hibrido.setMatrizPesos(this.matrizPesos);
        this.matrizInclinaciones=matrizInclinaciones;
        IndividuoTCP_Hibrido.setMatrizInclinaciones(this.matrizInclinaciones);
        this.tipoSeleccion = tipoSeleccion;
        this.poblacionActual= poblacionActual;
        this.fitnessGeneracional= fitnessGeneracional;
        this.semaf = semaf;
        this.jPanel_hilos = jPanel_hilos;
    }
    public GeneticoTCP_Hibrido_thread(int numGen, int tamPoblacion, double probMuta, int nodoInicial, double [][] matrizInclinaciones, int [][] matrizPesos, String tipoSeleccion, ArrayList<Double> fitnessGeneracional,  Semaphore semaf, JPanel_hilos jPanel_hilos){
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMuta = probMuta;
        this.nodoInicial = nodoInicial;
        IndividuoTCP_Hibrido.setInicial(nodoInicial);
        this.matrizPesos=matrizPesos;
        this.matrizInclinaciones=matrizInclinaciones;
        IndividuoTCP_Hibrido.setMatrizPesos(this.matrizPesos);
        IndividuoTCP_Hibrido.setMatrizInclinaciones(this.matrizInclinaciones);
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
                ArrayList<IndividuoTCP_Hibrido> nuevapob= new ArrayList<>();
                Seleccion.reset();
                Seleccion.setTipoSeleccion(tipoSeleccion);
                for (int j = 0; j < getTamPoblacion(); j++) {
                    IndividuoTCP_Hibrido madre= Seleccion.seleccion(poblacionActual);
                    IndividuoTCP_Hibrido padre= Seleccion.seleccion(poblacionActual);
                    int[] mask= Herramientas.generarArregloBinarios(madre.getGenotipo().length);
                    IndividuoTCP_Hibrido hijo=Cruza.cruzaMascaraBin(padre, madre, mask);
                    if(generarProbabilidadMuta()){
                        Muta.muta(hijo);
                    }
                    nuevapob.add(hijo);
                }
                sustituirPoblacion(nuevapob);
                getFitnessGeneracional().add(Herramientas.mejorPoblacion(nuevapob).getFitness());
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
        jPanel_hilos.terminoEvol();
    }
    
    private void generarPoblacion() {
        //IndividuoTCP_Hibrido.setInicial(getNodoInicial());
        //Pair<double[][],int[][]> respuesta=GeneradorMatrices.abrirArchivos();
        //IndividuoTCP_Hibrido.setMatrizPesos(respuesta.getValue());
        //IndividuoTCP_Hibrido.setMatrizInclinaciones(respuesta.getKey());
        try {
            semaf.acquire();
            for (int i = 0; i < getTamPoblacion(); i++) {
                IndividuoTCP_Hibrido nuevo= new IndividuoTCP_Hibrido();
                getPoblacionActual().add(nuevo);
            }
            semaf.release();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    private void sustituirPoblacion(ArrayList<IndividuoTCP_Hibrido> nuevaPob) {
        getPoblacionActual().clear();
       for(IndividuoTCP_Hibrido aux:nuevaPob){
            getPoblacionActual().add(new IndividuoTCP_Hibrido(aux.getGenotipo()));
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
    public ArrayList<Double> getFitnessGeneracional() {
        return fitnessGeneracional;
    }

    /**
     * @param fitnessGeneracional the fitnessGeneracional to set
     */
    public void setFitnessGeneracional(ArrayList<Double> fitnessGeneracional) {
        this.fitnessGeneracional = fitnessGeneracional;
    }

    /**
     * @return the poblacionActual
     */
    public ArrayList<IndividuoTCP_Hibrido> getPoblacionActual() {
        return poblacionActual;
    }

    /**
     * @param poblacionActual the poblacionActual to set
     */
    public void setPoblacionActual(ArrayList<IndividuoTCP_Hibrido> poblacionActual) {
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
