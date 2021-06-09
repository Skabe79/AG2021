/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author skabe
 */
public class IndividuoTCP {
    private static int[][] matriz=null;
    private static int inicial=-1;
    private int genotipo[];
    private long fitness;

    public IndividuoTCP(){
        aleatorioGeno();
        calcularFitness();
    }
    public IndividuoTCP(int [] genotipo) {
        if(genotipo[0]==inicial){
            this.genotipo=genotipo;
            calcularFitness();
        }
    }
    public  boolean actualizarFitness(){
        if(getMatriz()!=null && getInicial()!=-1){
            calcularFitness();
            return true;
        }
        return false;
    }
    private void calcularFitness(){
        long fit=0;
        for (int i = 0; i < getGenotipo().length; i++) {
            if(i==getGenotipo().length-1){
                
                fit+=matriz[getGenotipo()[i]][getGenotipo()[0]];
            }else{
                fit+=matriz[getGenotipo()[i]][getGenotipo()[i+1]];
            }
        }
        this.fitness=fit;
    }
    private void aleatorioGeno(){
        if(getMatriz()==null){
            return;
        }
        this.genotipo= new int[getMatriz().length];
        ArrayList<Integer> noGenerados = new ArrayList<>();
        int n=getMatriz().length;
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
     * @return the matriz
     */
    public static int[][] getMatriz() {
        return matriz;
    }

    /**
     * @param aMatriz the matriz to set
     */
    public static void setMatriz(int[][] aMatriz) {
        matriz = aMatriz;
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
    public long getFitness() {
        return fitness;
    }
}
