/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.util.Random;

/**
 *
 * @author skabe
 */
public class Muta {
    public static void muta(IndividuoTCP indiv){
        Random rand= new Random();
        boolean correcto=true;
        int pos=1,posN=1;
        while(correcto){
            pos= rand.nextInt(indiv.getGenotipo().length-1)+1;
            posN= rand.nextInt(indiv.getGenotipo().length-1)+1;
            if(pos!=posN){
                correcto=false;
            }
        }
        int aux= indiv.getGenotipo()[pos];
        indiv.getGenotipo()[pos]= indiv.getGenotipo()[posN];
        indiv.getGenotipo()[posN]=aux;
        indiv.actualizarFitness();
    }
}
