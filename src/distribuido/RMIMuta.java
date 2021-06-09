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
public interface RMIMuta extends Remote{
    public Individuo3SAT muta_3SAT(Individuo3SAT indiv) throws RemoteException;
    public void setInstancias_3SAT(int [][] instancias)throws RemoteException;
    public IndividuoTCP muta_TCP(IndividuoTCP indiv) throws RemoteException;
    public IndividuoTCP_Hibrido muta_TCP_Hibrido(IndividuoTCP_Hibrido indiv) throws RemoteException;
}
