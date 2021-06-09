/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRESSAT;

import binario.Individuo;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import nDamas.Persistente;

/**
 *
 * @author skabe
 */
public class Seleccion {
    private static String tipoSeleccion="Aleatoria";
    private static int total;
    private static double probi[];
    private static boolean persistente=false;
    public static Individuo3SAT seleccion(ArrayList<Individuo3SAT> poblacion) throws RemoteException{
        switch (tipoSeleccion){
            case "Aleatoria":
                Random ran= new Random();
                int pos = ran.nextInt(poblacion.size());
                return new Individuo3SAT(poblacion.get(pos));
            case "Por Ruleta":
                if(!persistente){
                    total=0;
                    probi= new double[poblacion.size()];
                    for (int i = 0; i < poblacion.size(); i++) {
                        total+= poblacion.get(i).getFitness();
                    }
                    for (int i = 0; i < poblacion.size(); i++) {
                        probi[i]= (poblacion.get(i).getFitness()*100)/total;
                    }
                    persistente=true;
                }
                double ruleta=Math.random();
                int ganador=0;
                double valorActual=0;
                for (int i = 0; i < poblacion.size(); i++) {
                    valorActual+=probi[i];
                    if(ruleta<valorActual){
                        ganador=i;
                        break;
                    }
                }
                return new Individuo3SAT(poblacion.get(ganador));
            default:
                    
        }
        return null;
    }
    public static void setTipoSeleccion(String select){
        tipoSeleccion=select;
    }
    public static void reset(){
        persistente=false;
    }
}
