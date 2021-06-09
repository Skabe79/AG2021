/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binario;

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
    public static Individuo mejorPoblacion(ArrayList<Individuo> pob){
        Individuo mejor= new Individuo(pob.get(0));
        for(Individuo aux: pob){
            if(aux.getFitness()<mejor.getFitness()){
                mejor= new Individuo(aux);
            }
        }
        return mejor;
    }
}
