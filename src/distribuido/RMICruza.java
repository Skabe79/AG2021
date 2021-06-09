/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribuido;

import TCP.IndividuoTCP;
import TCP_Hibrido.IndividuoTCP_Hibrido;
import TRESSAT.Individuo3SAT;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author skabe
 */
public interface RMICruza extends Remote{
    public Individuo3SAT cruza_3SAT(Individuo3SAT madre, Individuo3SAT padre, int[] mascara) throws RemoteException;
    public void setInstancias_3SAT(int [][] instancias)throws RemoteException;
    public IndividuoTCP cruza_TCP(IndividuoTCP madre, IndividuoTCP padre, int[] mascara) throws RemoteException;
    public IndividuoTCP_Hibrido cruza_TCP_Hibrido(IndividuoTCP_Hibrido madre, IndividuoTCP_Hibrido padre, int[] mascara) throws RemoteException;
}
