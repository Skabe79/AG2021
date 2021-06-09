/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRESSAT;

import GUI.GraficaHilo;
import GUI.JPanel_hilos;
import GUI.Principal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author skabe
 */
public class Genetico3SAT extends Thread{
    private int numGen;
    private int tamPoblacion;
    private ArrayList<Integer> fitnessGeneracional=new ArrayList<>();
    private ArrayList<Individuo3SAT> poblacionActual= new ArrayList<>();
    private int rango;
    private int numInstancias;
    private int numElementos;
    private double probMutal;
    private int [][] instancias;
    private Semaphore semaf;
    private String tipoSeleccion;
    private JPanel_hilos panel_hilos;
    public Genetico3SAT(int numGen, int tamPoblacion, double probMutal, File file, Semaphore semaf, String tipoSeleccion,JPanel_hilos panel_hilos) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMutal = probMutal;
        this.semaf=semaf;
        this.tipoSeleccion=tipoSeleccion;
        this.panel_hilos=panel_hilos;
        try {
            BufferedReader br= new BufferedReader(new FileReader(file));
            String linea=br.readLine();
            linea=br.readLine();
            StringTokenizer token= new StringTokenizer(linea, " ");
            this.rango=Integer.parseInt(token.nextToken());
            this.numInstancias=Integer.parseInt(token.nextToken());
            this.numElementos=Integer.parseInt(token.nextToken());
            instancias= new int[this.numInstancias][this.numElementos];
            for (int i = 0; i < this.numInstancias; i++) {
                linea= br.readLine();
                token= new StringTokenizer(linea, " ");
                for (int j = 0; j < this.numElementos; j++) {
                    this.instancias[i][j]= Integer.parseInt(token.nextToken());
                }
            }
            generarPoblacion();
        } catch (Exception e) {
        }
    }
    public Genetico3SAT(int numGen, int tamPoblacion, double probMutal, int rango, int numInstancias, int numElementos, int [][] instancias, Semaphore semaf, String tipoSeleccion,ArrayList<Individuo3SAT> poblacion, Principal principal){
        this.numGen=numGen;
        this.tamPoblacion=tamPoblacion;
        this.probMutal=probMutal;
        this.rango=rango;
        this.numInstancias=numInstancias;
        this.numElementos=numElementos;
        this.instancias=instancias;
        this.semaf=semaf;
        this.tipoSeleccion=tipoSeleccion;
        this.poblacionActual=poblacion;
    }
    public Genetico3SAT(int numGen, int tamPoblacion, double probMutal) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.probMutal = probMutal;
        File file= abrirParametros();
        try {
            BufferedReader br= new BufferedReader(new FileReader(file));
            String linea=br.readLine();
            linea=br.readLine();
            StringTokenizer token= new StringTokenizer(linea, " ");
            this.rango=Integer.parseInt(token.nextToken());
            this.numInstancias=Integer.parseInt(token.nextToken());
            this.numElementos=Integer.parseInt(token.nextToken());
            instancias= new int[this.numInstancias][this.numElementos];
            for (int i = 0; i < this.numInstancias; i++) {
                linea= br.readLine();
                token= new StringTokenizer(linea, " ");
                for (int j = 0; j < this.numElementos; j++) {
                    this.instancias[i][j]= Integer.parseInt(token.nextToken());
                }
            }
            generarPoblacion();
        } catch (Exception e) {
        }
    }

    public Genetico3SAT(int numGen, int tamPoblacion, double probMutal, ArrayList<Individuo3SAT> poblacionActual) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.poblacionActual = poblacionActual;
        this.probMutal = probMutal;
        File file= abrirParametros();
    }
    public Genetico3SAT(int numGen, int tamPoblacion,String tipoSeleccion, ArrayList<Individuo3SAT> poblacionActual, int rango, int numInstancias, int numElementos, double probMutal, int[][] instancias, ArrayList<Integer> fitnessGeneracional,Semaphore semaf,JPanel_hilos panel_hilos) {
        this.numGen = numGen;
        this.tamPoblacion = tamPoblacion;
        this.poblacionActual = poblacionActual;
        this.rango = rango;
        this.numInstancias = numInstancias;
        this.numElementos = numElementos;
        this.probMutal = probMutal;
        this.instancias = instancias;
        this.fitnessGeneracional=fitnessGeneracional;
        this.tipoSeleccion=tipoSeleccion;
        this.semaf=semaf;
        this.panel_hilos=panel_hilos;
        Individuo3SAT.setInstancias(getInstancias());
    }
    public File abrirParametros(){
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.dir")));
        file.showOpenDialog(file);
        //abrimos el archivo seleccionado
        File abre = file.getSelectedFile();

        if (abre != null) {
            return abre;
        } else {
            JOptionPane.showMessageDialog(null,
                    "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }
    private void generarPoblacion(){
        try {
            semaf.acquire();
            Individuo3SAT.setInstancias(getInstancias());
            for (int i = 0; i < getTamPoblacion(); i++) {
                Individuo3SAT nuevo= new Individuo3SAT(getRango()+1);
                getPoblacionActual().add(nuevo);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Genetico3SAT.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            semaf.release();
        }
    }
    
    @Override
    public void run(){
        getFitnessGeneracional().add(Herramientas.mejorPoblacion(getPoblacionActual()).getFitness());
        for (int i = 0; i < this.getNumGen(); i++) {
            try {
                semaf.acquire();
                //System.out.println("adquirido");
                ArrayList<Individuo3SAT> nuevaPob= new ArrayList<>();
                Seleccion.reset();
                Seleccion.setTipoSeleccion(getTipoSeleccion());
                for (int j = 0; j < this.getTamPoblacion(); j++) {
                    Individuo3SAT madre= Seleccion.seleccion(getPoblacionActual());
                    Individuo3SAT padre= Seleccion.seleccion(getPoblacionActual());
                    int[] mask= Herramientas.generarArregloBinarios(madre.getGenotipo().length);
                    Individuo3SAT hijo= panel_hilos.getPrincipal().getrMICruza().cruza_3SAT(madre, padre, mask);//Cruza.cruzaMascaraBin(padre, madre, mask);
                    if(generarProbabilidadMuta()){
                        hijo= panel_hilos.getPrincipal().getrMIMuta().muta_3SAT(hijo);
                        //Muta.muta(hijo);
                    }
                    nuevaPob.add(hijo);
                }
                sustituirPoblacion(nuevaPob);
                getFitnessGeneracional().add( Herramientas.mejorPoblacion(nuevaPob).getFitness());
            } catch (InterruptedException ex) {
                Logger.getLogger(Genetico3SAT.class.getName()).log(Level.SEVERE, null, ex);
            }catch (RemoteException ex){
                ex.printStackTrace();
            }finally{
                semaf.release();
                try {
                   Thread.sleep(1);
               } catch (InterruptedException ex) {
                   Logger.getLogger(GraficaHilo.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
        }
        getPanel_hilos().terminoEvol();
    }
    
    private void sustituirPoblacion(ArrayList<Individuo3SAT> nuevaPob) {
        getPoblacionActual().clear();
       for(Individuo3SAT aux:nuevaPob){
            getPoblacionActual().add(new Individuo3SAT(aux));
       }
    }
    
    /**
     * @param numGen the numGen to set
     */
    public void setNumGen(int numGen) {
        this.numGen = numGen;
    }

    /**
     * @param tamPoblacion the tamPoblacion to set
     */
    public void setTamPoblacion(int tamPoblacion) {
        this.tamPoblacion = tamPoblacion;
        if(this.getTamPoblacion()<getPoblacionActual().size()){
            while(this.getTamPoblacion()<getPoblacionActual().size()){
                getPoblacionActual().remove(getPoblacionActual().size()-1);
            }
        }else if(this.getTamPoblacion()>getPoblacionActual().size()){
            while (this.getTamPoblacion()>getPoblacionActual().size()) {                
                getPoblacionActual().add(new Individuo3SAT(getRango()+1));
            }
        }
    }

    /**
     * @param probMutal the probMutal to set
     */
    public void setProbMutal(double probMutal) {
        this.probMutal = probMutal;
    }
    private boolean generarProbabilidadMuta() {
        if (getProbMutal()>=Math.random()){
           return true;
       } else{ return false;}
    }

    /**
     * @param tipoSeleccion the tipoSeleccion to set
     */
    public void setTipoSeleccion(String tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }

    /**
     * @return the numGen
     */
    public int getNumGen() {
        return numGen;
    }

    /**
     * @return the tamPoblacion
     */
    public int getTamPoblacion() {
        return tamPoblacion;
    }

    /**
     * @return the fitnessGeneracional
     */
    public ArrayList<Integer> getFitnessGeneracional() {
        return fitnessGeneracional;
    }

    /**
     * @return the poblacionActual
     */
    public ArrayList<Individuo3SAT> getPoblacionActual() {
        return poblacionActual;
    }

    /**
     * @return the rango
     */
    public int getRango() {
        return rango;
    }

    /**
     * @return the numInstancias
     */
    public int getNumInstancias() {
        return numInstancias;
    }

    /**
     * @return the numElementos
     */
    public int getNumElementos() {
        return numElementos;
    }

    /**
     * @return the probMutal
     */
    public double getProbMutal() {
        return probMutal;
    }

    /**
     * @return the instancias
     */
    public int[][] getInstancias() {
        return instancias;
    }

    /**
     * @return the tipoSeleccion
     */
    public String getTipoSeleccion() {
        return tipoSeleccion;
    }

    /**
     * @return the panel_hilos
     */
    public JPanel_hilos getPanel_hilos() {
        return panel_hilos;
    }
    public int añadirFinal(ArrayList<Object> individuosNuevos){
        for (int i = 0; i < individuosNuevos.size(); i++) {
            poblacionActual.add(new Individuo3SAT(((Individuo3SAT)individuosNuevos.get(i)).getGenotipo()));
        }
        this.tamPoblacion=poblacionActual.size();
        return poblacionActual.size();
    }
    public int sustituirPrincipio(ArrayList<Object> individuosNuevos){
        if(individuosNuevos.size()>poblacionActual.size()){
            for (int i = 0; i < poblacionActual.size(); i++) {
                poblacionActual.set(i, new Individuo3SAT(((Individuo3SAT)individuosNuevos.get(i)).getGenotipo()));
            }
            for (int i = poblacionActual.size()-1; i < individuosNuevos.size(); i++) {
                poblacionActual.add(new Individuo3SAT(((Individuo3SAT)individuosNuevos.get(i)).getGenotipo()));
            }
        }else{
            for (int i = 0; i < individuosNuevos.size(); i++) {
                poblacionActual.set(i, new Individuo3SAT(((Individuo3SAT)individuosNuevos.get(i)).getGenotipo()));
            }
        }
        this.tamPoblacion=poblacionActual.size();
        return poblacionActual.size();
    }
    public int sustituirFinal(ArrayList<Individuo3SAT> individuosNuevos){
        return -1;
    }
    public int sustitucionAleatoria(ArrayList<Individuo3SAT> individuosNuevos){
        return -1;
    }
}
