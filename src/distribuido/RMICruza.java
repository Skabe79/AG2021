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
    public Individuo3SAT muta_3SAT(Individuo3SAT madre, Individuo3SAT padre, int[] mascara) throws RemoteException;
    public IndividuoTCP muta_TCP(IndividuoTCP madre, IndividuoTCP padre, int[] mascara) throws RemoteException;
    public IndividuoTCP_Hibrido muta_TCP_Hibrido(IndividuoTCP_Hibrido madre, IndividuoTCP_Hibrido padre, int[] mascara) throws RemoteException;
}
