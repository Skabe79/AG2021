/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP_Hibrido;

import java.util.ArrayList;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author skabe
 */

public class IndividuoTCP_Hibrido {
    private static int[][] matrizPesos=null;
    private static double[][] matrizInclinaciones=null;
    private static int inicial=-1;
    private int genotipo[];
    private Pair<double[],int []> fenotipo;
    private double fitness;

    public IndividuoTCP_Hibrido() {
        aleatorioGeno();
        calcularFenotipoo();
        calcularFitness();
    }
    public IndividuoTCP_Hibrido(int[] genotipo) {
        this.genotipo = genotipo;
        calcularFenotipoo();
        calcularFitness();
    }
    private void calcularFenotipoo(){
        double[] inclinacion=new double[genotipo.length];
        int[] pesos=new int[genotipo.length];
        for (int i = 0; i < genotipo.length; i++) {
            if(i==getGenotipo().length-1){
                pesos[i]=matrizPesos[getGenotipo()[i]][getGenotipo()[0]];
                inclinacion[i]=matrizInclinaciones[getGenotipo()[i]][getGenotipo()[0]];
            }else{
                pesos[i]=matrizPesos[getGenotipo()[i]][getGenotipo()[i+1]];
                inclinacion[i]=matrizInclinaciones[getGenotipo()[i]][getGenotipo()[i+1]];
            }
        }
        fenotipo=new Pair(inclinacion,pesos);
    }
    private void calcularFitness(){
        double fit=0;
        for (int i = 0; i < genotipo.length; i++) {
            fit+= fenotipo.getKey()[i]*fenotipo.getValue()[i];
        }
        fitness=fit;
    }
    public boolean actualizarFitness(){
        if(getMatrizPesos()!=null &&getMatrizInclinaciones()!=null && getInicial()!=-1){
            calcularFenotipoo();
            calcularFitness();
            return true;
        }
        return false;
    }
    private void aleatorioGeno(){
        if(matrizPesos==null && matrizInclinaciones==null){
            return;
        }
        this.genotipo= new int[matrizPesos.length];
        ArrayList<Integer> noGenerados = new ArrayList<>();
        int n=matrizPesos.length;
        for (int i = 0; i < n; i++) {
            noGenerados.add(i);
        }
        this.genotipo[0]=getInicial();
        Random rnd = new Random();
        noGenerados.remove(new Integer(getInicial()));
        for (int i = 1; i < n; i++) {
            int ext= rnd.nextInt(noGenerados.size());
            this.genotipo[i]=noGenerados.get(ext);
            noGenerados.remove(new Integer(noGenerados.get(ext)));
        }
    }
    /**
     * @return the matrizPesos
     */
    public static int[][] getMatrizPesos() {
        return matrizPesos;
    }

    /**
     * @param aMatrizPesos the matrizPesos to set
     */
    public static void setMatrizPesos(int[][] aMatrizPesos) {
        matrizPesos = aMatrizPesos;
    }

    /**
     * @return the matrizInclinaciones
     */
    public static double[][] getMatrizInclinaciones() {
        return matrizInclinaciones;
    }

    /**
     * @param aMatrizInclinaciones the matrizInclinaciones to set
     */
    public static void setMatrizInclinaciones(double[][] aMatrizInclinaciones) {
        matrizInclinaciones = aMatrizInclinaciones;
    }

    /**
     * @return the inicial
     */
    public static int getInicial() {
        return inicial;
    }

    /**
     * @param aInicial the inicial to set
     */
    public static void setInicial(int aInicial) {
        inicial = aInicial;
    }

    /**
     * @return the genotipo
     */
    public int[] getGenotipo() {
        return genotipo;
    }

    /**
     * @return the fitness
     */
    public double getFitness() {
        return fitness;
    }
    public static void main(String[] args) {
        GeneradorMatrices generador= new GeneradorMatrices(15, 50);
        IndividuoTCP_Hibrido.setInicial(0);
        IndividuoTCP_Hibrido.setMatrizPesos(generador.matrizPesos);
        IndividuoTCP_Hibrido.setMatrizInclinaciones(generador.matrizInclinaciones);
        IndividuoTCP_Hibrido individuoTCP_Mixto=new IndividuoTCP_Hibrido();
        individuoTCP_Mixto.actualizarFitness();
    }

    @Override
    public String toString() {
        String respuesta="";
        respuesta="fitness: "+getFitness()+" \nGenotipo: ";
        for (int i = 0; i < getGenotipo().length; i++) {
            respuesta+=getGenotipo()[i]+",";
        }
        return respuesta;
    }
    
}
