/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author skabe
 */
public class Test {
    public static void main(String[] args) {
        //GeneticoTCP nuevoGeneticoTCP=new GeneticoTCP(1000, 100, .13, 0, "Aleatorio");
        //nuevoGeneticoTCP.evolucionar();
        GeneticoTCP_Thread geneticoTCP_Thread= new GeneticoTCP_Thread(1000, 100, .13, 0, AbrirMatriz.abrirMatriz(), "Aleatorio", new ArrayList<Long>(), new Semaphore(1), null);
        geneticoTCP_Thread.start();
        
    }
}
