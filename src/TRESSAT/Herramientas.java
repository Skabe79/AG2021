/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRESSAT;

import binario.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author skabe
 */
public class Herramientas {
    
    public static int[] generarArregloBinarios(int n){
        int[] arreglo= new int[n];
        Random ran= new Random();
        for(int x=0;x<n;x++){
            arreglo[x]= ran.nextInt(2);
        }
        return arreglo;
    }
    public static Individuo3SAT mejorPoblacion(ArrayList<Individuo3SAT> pob) throws RemoteException{
        Individuo3SAT mejor= new Individuo3SAT(pob.get(0));
        for(Individuo3SAT aux: pob){
            if(aux.getFitness()<mejor.getFitness()){
                mejor= new Individuo3SAT(aux);
            }
        }
        return mejor;
    }
}
