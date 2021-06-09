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
public class ServidorRMICruza {
    public static void main(String[] args) {
        try {
            CruzaRemota cr=new CruzaRemota();
            Registry registry= LocateRegistry.createRegistry(1100);
            registry.rebind("Cruza Remota", cr);
            System.out.println("Servidor de cruza remota Activo");
        } catch (Exception e) {
        }
    }
}
