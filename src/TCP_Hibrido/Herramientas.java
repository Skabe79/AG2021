/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP_Hibrido;

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
    public static IndividuoTCP_Hibrido mejorPoblacion(ArrayList<IndividuoTCP_Hibrido> pob){
        IndividuoTCP_Hibrido mejor= new IndividuoTCP_Hibrido(pob.get(0).getGenotipo());
        for(IndividuoTCP_Hibrido aux: pob){
            if(aux.getFitness()<mejor.getFitness()){
                mejor= new IndividuoTCP_Hibrido(aux.getGenotipo());
            }
        }
        return mejor;
    }
}
