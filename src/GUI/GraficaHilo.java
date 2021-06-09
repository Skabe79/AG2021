/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import nDamas.Grafica;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author skabe
 */
public class GraficaHilo extends Thread{
    JPanel_hilos jPanel_hilos;
    Semaphore semaforo;


    public GraficaHilo(JPanel_hilos jPanel_hilos, Semaphore semaforo) {
        this.jPanel_hilos =jPanel_hilos;
        this.semaforo =semaforo;
    }
    
    @Override
    public void run() {
        while (true) {            
           try {
                semaforo.acquire();
           //     System.out.println("adquirido");
                Grafica grafica= new Grafica("Generacion", "Fitness", "Algoritmo genetico");
                switch (jPanel_hilos.getTipoGenetico()){
                    case "3 SAT":
                        grafica.agregarSerie("Generacion",jPanel_hilos.getGenetico3SAT().getFitnessGeneracional());
                        break;
                    case "TCP":
                        grafica.agregarSerieLong("Generacion",jPanel_hilos.getGeneticoTCP().getFitnessGeneracional());
                        break;
                    case "TCP Hibrido":
                        grafica.agregarSerieDouble("Generacion",jPanel_hilos.getGeneticoTCP_Hibrido().getFitnessGeneracional());
                        break;
                    default:break;
                }
                grafica.crearGrafica();
                ChartPanel cp= grafica.crearChartPanel(jPanel_hilos.getjPanel_Grafica().getSize());

                jPanel_hilos.getjPanel_Grafica().removeAll();
                jPanel_hilos.getjPanel_Grafica().add(cp, BorderLayout.CENTER);
                jPanel_hilos.getjPanel_Grafica().updateUI();
                Thread.sleep(100);
            } catch(NullPointerException e) {
                System.out.println("Fallo null pointer exeption");
            } catch (InterruptedException ex) {
                Logger.getLogger(GraficaHilo.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                semaforo.release();
               try {
                   Thread.sleep(1);
               } catch (InterruptedException ex) {
                   Logger.getLogger(GraficaHilo.class.getName()).log(Level.SEVERE, null, ex);
               }
            } 
        }
    }
    
}
