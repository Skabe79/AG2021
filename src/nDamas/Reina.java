/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

import java.util.Arrays;

/**
 *
 * @author carli
 */
public class Reina {

    private int[] genotipo;
    private int fitness;
    private int n;

    public Reina(int[] genotipo) {
        this.genotipo = genotipo;
        calcularFitness();
    }

    public Reina(Reina reinita) {
        this.genotipo = reinita.getGenotipo();
        this.fitness = reinita.getFitness();
        //this.n = reinita.getN();
    }

    public Reina(int n) {
        this.genotipo = Tools.generaPosi(n);
        calcularFitness();  
    }

    /**
     * @return the genotipo
     */
    public int[] getGenotipo() {
        return genotipo;
    }

    /**
     * @param genotipo the genotipo to set
     */
    public void setGenotipo(int[] genotipo) {
        this.genotipo = genotipo;
    }

    /**
     * @return the fitness
     */
    public int getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    /**
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(int n) {
        this.n = n;
    }

    public int calculaAtaqueHor(int[] genotipo) {
        int ah = 0;
        int aux1 = 0, aux2 = 0, aux = 0;
        for (int i = 0; i < genotipo.length; i++) {
            //System.out.println(genotipo[i]);
            for (int j = 0; j < genotipo.length; j++) {
                if ((genotipo[i] == genotipo[j]) && (j < i)) {
                   // if (aux1 == 0) {
                        aux1++;
                   // }
                } else if ((genotipo[i] == genotipo[j]) && (j > i)) {
                    //if (aux2 == 0) {
                        aux2++;
                    //}
                }
            }
            ah += aux1 + aux2;
            aux1 = 0;
            aux2 = 0;
        }
        //System.out.println("llevamos :" + ah);
        return ah;
    }
    public int calculaAtaqueDiagonal1(int[] genotipo) {
        int ad1 = 0;
        int aux1 = 0, aux2=0;
        for(int i = 0; i<genotipo.length; i++){
            for (int j = 0; j < genotipo.length; j++) {
                //System.out.println("("+i+","+genotipo[i]+")="+(genotipo[i]-i)+"("+j+","+genotipo[j]+")="+(genotipo[j]-j));
                if (((genotipo[i]-i) == genotipo[j]-j) && (j < i)) {
                    //if (aux1 == 0) {
                        aux1++;
                        //System.out.println("diagonalTrasera1");
                        //System.out.println(Math.abs(genotipo[i]-i));
                    //}
                } else if (((genotipo[i]-i) == (genotipo[j]-j)) && (j > i)) {
                   // if (aux2 == 0) {
                        aux2++;
                        //System.out.println("diagonalDelantera1");
                   // }
                }
            }
            
            ad1 += aux1 + aux2;
            //System.out.println("i "+i+" LLevamos = "+ad1);
            aux1 = 0;
            aux2 = 0;
        }
        //System.out.println("llevamos "+ad1);
        return ad1;
    }

    public int calculaAtaqueDiagonal2(int[] genotipo) {
        int ad2 = 0;
        int aux1 = 0, aux2=0;
        for(int i = 0; i<genotipo.length; i++){
            for (int j = 0; j < genotipo.length; j++) {
                if (((Math.abs(genotipo[i]+i)) == (Math.abs(genotipo[j]+j))) && (j < i)) {
                   // if (aux1 == 0) {
                        aux1++;
                       // System.out.println("diagonalTrasera");
                    //}
                } else if (((Math.abs(genotipo[i]+i)) == (Math.abs(genotipo[j]+j))) && (j > i)) {
                   // if (aux2 == 0) {
                        aux2++;
                        //System.out.println("diagonalDelantera");
                    //}
                }
            }
            
            ad2 += aux1 + aux2;
            aux1 = 0;
            aux2 = 0;
        }
       /// System.out.println("llevamos "+ad2);
        return ad2;
    }
    
    public void calcularFitness(){
        this.fitness = calculaAtaqueHor(this.genotipo)+calculaAtaqueDiagonal1(this.genotipo) + calculaAtaqueDiagonal2(this.genotipo);
    }

    @Override
    public String toString() {
        return "Reina{" + "genotipo=" + Arrays.toString(genotipo) + ", fitness=" + fitness+'}';
    }
    
    
}

