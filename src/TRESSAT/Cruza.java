/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRESSAT;

import binario.Individuo;
import java.rmi.RemoteException;

/**
 *
 * @author skabe
 */
public class Cruza {
    public static Individuo3SAT cruzaMascaraBin(Individuo3SAT padre, Individuo3SAT madre, int[] mascara) throws RemoteException {
        int[] gen1 = new int[madre.getGenotipo().length];
        int[] gen2 = new int[madre.getGenotipo().length];
        
        //Recorrer la mascara
        for(int i=0;i<mascara.length; i++){
            if(mascara[i]==0){
                gen1[i]= padre.getGenotipo()[i];
                gen2[i]= madre.getGenotipo()[i];
            }else{
                gen2[i]= padre.getGenotipo()[i];
                gen1[i]= madre.getGenotipo()[i];
            }
        }
        Individuo3SAT i1= new Individuo3SAT(gen1);
        Individuo3SAT i2= new Individuo3SAT(gen2);
        if(i1.getFitness()>i2.getFitness()){
            return i1;
        }
        return i2;
    }
}
