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
public class Cruza {
    public static IndividuoTCP cruzaMascaraBin(IndividuoTCP padre, IndividuoTCP madre, int[] mascara) {
        int[] gen1 = new int[madre.getGenotipo().length];
        int[] gen2 = new int[madre.getGenotipo().length];
        IndividuoTCP res;
        while(true){
            for(int i=0;i<mascara.length; i++){
                if(mascara[i]==0){
                    gen1[i]= padre.getGenotipo()[i];
                    gen2[i]= madre.getGenotipo()[i];
                }else{
                    gen2[i]= padre.getGenotipo()[i];
                    gen1[i]= madre.getGenotipo()[i];
                }
            }
            IndividuoTCP hijo1=new IndividuoTCP(gen1);
            IndividuoTCP hijo2=new IndividuoTCP(gen2);
            if (validar(hijo1) && validar(hijo2)) {
                    if(hijo1.getFitness()<hijo2.getFitness()){
                    return hijo1;
                }
                return hijo2;
            }
            Random random= new Random();
            for (int i = 0; i < mascara.length; i++) {
                mascara[i]=random.nextInt(2);
            }
        }
    }
    private static boolean validar(IndividuoTCP hijo){
    for(int i = 0;i<hijo.getGenotipo().length;i++){
        for(int j = 0;j<hijo.getGenotipo().length;j++){
            if(i!=j && hijo.getGenotipo()[i] == hijo.getGenotipo()[j]){
                return false;
            }
        }
    }
    return true;
    }
}
