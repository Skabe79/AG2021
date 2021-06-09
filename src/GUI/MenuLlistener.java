/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

/**
 *
 * @author skabe
 */
public class MenuLlistener implements ActionListener{

    private Principal principal;
    public MenuLlistener(Principal principal) {
        this.principal = principal;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JMenuItem item= (JMenuItem) ae.getSource();
        if(item.getText().equals("New Genetic Algoritm")){
            principal.newGen();
        }else if(item.getText().equals("Open Genetic Algoritm")){
            principal.abrirGen();
        }else if(item.getText().equals("Save Genetic Algoritm")){
            JPanel_hilos j_hilos=(JPanel_hilos)((JScrollPane)principal.getjTabbedPane1().getSelectedComponent()).getComponent(0);
            j_hilos.saveGen();
        }
    }
    
}
