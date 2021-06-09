/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import TRESSAT.Genetico3SAT;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.JScrollPane;

/**
 *
 * @author skabe
 */
public class Gestor {
    private Object genetico;
    private JPanel_hilos panel;
    private String tipoGenetico;

    public Gestor(Object genetico, JPanel_hilos panel, String tipoGenetico) {
        this.genetico = genetico;
        this.panel = panel;
        this.tipoGenetico = tipoGenetico;
    }

    /**
     * @return the genetico
     */
    public Object getGenetico() {
        return genetico;
    }

    /**
     * @param genetico the genetico to set
     */
    public void setGenetico(Object genetico) {
        this.genetico = genetico;
    }

    /**
     * @return the panel
     */
    public JPanel_hilos getPanel() {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(JPanel_hilos panel) {
        this.panel = panel;
    }

    /**
     * @return the tipoGenetico
     */
    public String getTipoGenetico() {
        return tipoGenetico;
    }

    /**
     * @param tipoGenetico the tipoGenetico to set
     */
    public void setTipoGenetico(String tipoGenetico) {
        this.tipoGenetico = tipoGenetico;
    }
    
}
