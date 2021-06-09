/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRESSAT;

import java.rmi.RemoteException;

/**
 *
 * @author skabe
 */
public class Individuo3SAT implements java.io.Serializable{
    private int[] genotipo;
    private int fitness;
    private static int [][] instancias=null;
    public Individuo3SAT(int n)throws RemoteException{
        this.genotipo=Herramientas.generarArregloBinarios(n);
        calcularFitness();
    }
    
    public Individuo3SAT(int[] genotipo) throws RemoteException{
        this.genotipo = genotipo;
        calcularFitness();
    }
    public Individuo3SAT(Individuo3SAT i) throws RemoteException{
        this.genotipo=i.getGenotipo();
        this.fitness=i.getFitness();
    }
    public void calcularFitness(){
        int fit=0;
        for (int i = 0; i < instancias.length; i++) {
            boolean fov=false;
            for (int j = 0; j < instancias[0].length; j++) {
                int num= instancias[i][j];
                if(num>=0){
                    if(genotipo[num]==1){
                        fov=true;
                    }
                }else{
                    num=num*-1;
                    if(genotipo[num]==0){
                        fov=true;
                    }
                }
            }
            if(fov==true){
                fit++;
            }
        }
        this.fitness=fit;
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
    public int getFitness() {
        return fitness;
    }

    /**
     * @return the instancias
     */
    public static int[][] getInstancias() {
        return instancias;
    }

    /**
     * @param aInstancias the instancias to set
     */
    public static void setInstancias(int[][] aInstancias) {
        instancias = aInstancias;
    }

    @Override
    public String toString() {
        String toString="Fitness: "+getFitness()+" \nGenotipo: ";
        for (int i = 0; i < genotipo.length; i++) {
            if(i==genotipo.length-1){
                toString+=genotipo[i];
            }else{
                toString+=genotipo[i]+",";
            }
        }
        return toString;
    }
    
}
