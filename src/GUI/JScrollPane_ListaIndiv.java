/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneLayout;

/**
 *
 * @author skabe
 */
public class JScrollPane_ListaIndiv extends JScrollPane{
    private ArrayList<JPanel_Individuos> individuos=new ArrayList<>();
    private String tipoGenetico;
    public void agregarIndividuo(JPanel_Individuos individuo){
        getIndividuos().add(individuo);
        actualizarLayout();
    }
    public void actualizarLayout(){
        JPanel panel=new JPanel();
        GroupLayout jLayout= new GroupLayout(panel);
        GroupLayout.ParallelGroup group_horizotal= jLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup group_vertical =jLayout.createSequentialGroup();
        group_vertical.addContainerGap();
        for (int i = 0; i < individuos.size(); i++) {
            group_horizotal.addComponent(individuos.get(i));
            group_vertical.addComponent(individuos.get(i));
            group_vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        }
        panel.setLayout(jLayout);
        jLayout.setHorizontalGroup(jLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jLayout.createSequentialGroup().addContainerGap().addGroup(group_horizotal)
                .addContainerGap(0, Short.MAX_VALUE)));
        jLayout.setVerticalGroup(jLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(group_vertical));
        this.setViewportView(panel);
    }
    /**
     * @return the individuos
     */
    public ArrayList<JPanel_Individuos> getIndividuos() {
        return individuos;
    }

    /**
     * @param individuos the individuos to set
     */
    public void setIndividuos(ArrayList<JPanel_Individuos> individuos) {
        this.individuos = individuos;
        actualizarLayout();
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
