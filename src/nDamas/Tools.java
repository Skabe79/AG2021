/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author carli
 */
public class Tools {
    public static int[] generaPosi(int n){
        int[] arreglo = new int[n];
        Random ran = new Random();
        for(int x=0; x< n ;x++){
            arreglo[x]= ran.nextInt(n);
        }
        return arreglo;
    }
    
     public static Reina mejorPoblacion(ArrayList<Reina> pob){
        Reina mejor = new Reina(pob.get(0));
        for(Reina aux: pob){
            if (aux.getFitness()< mejor.getFitness()){
                mejor = new Reina(aux);
            }
        }
        return mejor;
    }
}
