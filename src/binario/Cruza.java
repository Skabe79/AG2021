/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binario;

/**
 *
 * @author skabe
 */
public class Cruza {
    
    public static Individuo cruzaPorMascaraBin(Individuo madre, Individuo padre, int[] mascara){
        
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
        Individuo i1= new Individuo(gen1);
        Individuo i2= new Individuo(gen2);
        if(i1.getFitness()<i2.getFitness()){
            return i1;
        }
        return i2;
    }
}
