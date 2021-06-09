/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag2021;

import binario.GeneticoBinario;
import binario.Herramientas;
import binario.Individuo;

/**
 *
 * @author skabe
 */
public class AG2021 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GeneticoBinario gb= new GeneticoBinario(10, 5, 0.10);
        gb.evolucionar();
        
        System.out.println(Herramientas.mejorPoblacion(gb.getPoblacionActua()).toString());
        /*int geno[]= new int[]{0,1,0,1};
        Individuo i= new Individuo(geno);
        System.out.println(i.getFitness());
        */
    }
    
    //reciba funcion de enteros de unos y ceros traduccion representacion binaria, cambiar a fenotipo para luego hacer finess con una funcion que queramos, recibe fenotipico valor.
    public static int calcuFeno(int [] genotipo){
        int suma=0;
        for(int i=0;i<genotipo.length;i++){
            if(genotipo[i]==1){
                suma+=Math.pow(2,genotipo.length-i-1);
            }
        }
        return suma;
    }
    
    public static double calcuFitness(int valorFeno){
        double result=0;
        result= Math.pow(valorFeno,valorFeno);
        return result; 
    }
}
