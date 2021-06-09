/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribuido;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *
 * @author skabe
 */
public class ServidorRMIMuta {
    public static void main(String[] args) {
        try {
            MutaRemota mr=new MutaRemota();
            Registry registry= LocateRegistry.createRegistry(1100);
            registry.rebind("Muta Remota", mr);
            System.out.println("Servidor de muta remota Activo");
        } catch (Exception e) {
        }
    }
}
