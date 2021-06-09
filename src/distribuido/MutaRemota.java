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
public class MutaRemota extends UnicastRemoteObject implements RMIMuta{

    public MutaRemota() throws RemoteException {
        super();
    }
    @Override
    public Individuo3SAT muta_3SAT(Individuo3SAT indiv) throws RemoteException {
        Random ran= new Random();
        int pos= ran.nextInt(indiv.getGenotipo().length);
        if(indiv.getGenotipo()[pos]==1){
            indiv.getGenotipo()[pos]=0; 
        }else{
            indiv.getGenotipo()[pos]=1;
        }
        indiv.calcularFitness();
        return indiv;
    }

    @Override
    public IndividuoTCP muta_TCP(IndividuoTCP indiv) throws RemoteException {
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
        return indiv;
    }

    @Override
    public IndividuoTCP_Hibrido muta_TCP_Hibrido(IndividuoTCP_Hibrido indiv) throws RemoteException {
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
        return indiv;
    }
    @Override
    public void setInstancias_3SAT(int[][] instancias) throws RemoteException {
        Individuo3SAT.setInstancias(instancias);
    }
}
