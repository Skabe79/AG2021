/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribuido;

import TCP.IndividuoTCP;
import TCP_Hibrido.IndividuoTCP_Hibrido;
import TRESSAT.Individuo3SAT;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 *
 * @author skabe
 */
public class CruzaRemota extends  UnicastRemoteObject implements RMICruza{

    public CruzaRemota() throws RemoteException{
        super();
    }
    @Override
    public Individuo3SAT cruza_3SAT(Individuo3SAT madre, Individuo3SAT padre, int[] mascara) throws RemoteException {
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
        Individuo3SAT i1= new Individuo3SAT(gen1);
        Individuo3SAT i2= new Individuo3SAT(gen2);
        if(i1.getFitness()>i2.getFitness()){
            return i1;
        }
        return i2;
    }

    @Override
    public IndividuoTCP cruza_TCP(IndividuoTCP madre, IndividuoTCP padre, int[] mascara) throws RemoteException {
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

    @Override
    public IndividuoTCP_Hibrido cruza_TCP_Hibrido(IndividuoTCP_Hibrido madre, IndividuoTCP_Hibrido padre, int[] mascara) throws RemoteException {
        int[] gen1 = new int[madre.getGenotipo().length];
        int[] gen2 = new int[madre.getGenotipo().length];
        IndividuoTCP_Hibrido res;
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
            IndividuoTCP_Hibrido hijo1=new IndividuoTCP_Hibrido(gen1);
            IndividuoTCP_Hibrido hijo2=new IndividuoTCP_Hibrido(gen2);
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
    private static boolean validar(IndividuoTCP_Hibrido hijo){
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
