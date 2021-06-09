package GUI;

import TCP.GeneticoTCP_Thread;
import TCP.IndividuoTCP;
import TRESSAT.Genetico3SAT;
import TRESSAT.Individuo3SAT;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import org.jfree.ui.ExtensionFileFilter;

/**
 *
 * @author skabe
 */
public class Principal extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    private ImageIcon iconPrincipal;
    private ImageIcon iconNew;
    private ImageIcon iconOpen;
    private ImageIcon iconSave;    
    private JFrameNuevoGen frameNuevoGen;
    private ArrayList<Gestor> gestores= new ArrayList<>();
    private ArrayList<JPanel_hilos> hilos= new ArrayList<>();
    public Principal() {
        setIcons();
        initComponents();
        addsListener();
    }
    
    private void addsListener() {
        MenuLlistener menuLlistener= new MenuLlistener(this);
        this.jMenuItem_newGen.addActionListener(menuLlistener);
        this.jMenuItem_OpenGen.addActionListener(menuLlistener);
        this.getjMenuItem_SaveGen().addActionListener(menuLlistener);
    }
    private void setIcons(){
        Image aux1= new ImageIcon(getClass().getResource("/iconos/Logo.png")).getImage();
        Image newAux1= aux1.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        iconPrincipal= new ImageIcon(newAux1);
        aux1= new ImageIcon(getClass().getResource("/iconos/logoAbrir.png")).getImage();
        newAux1= aux1.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        iconOpen=new ImageIcon(newAux1);
        aux1= new ImageIcon(getClass().getResource("/iconos/nuevo.png")).getImage();
        newAux1= aux1.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        iconNew=new ImageIcon(newAux1);
        aux1= new ImageIcon(getClass().getResource("/iconos/saveLogo.png")).getImage();
        newAux1= aux1.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        iconSave=new ImageIcon(newAux1);
    }
    
    protected void abrirGen(){
        JFileChooser file = new JFileChooser();
        FileFilter file1= new ExtensionFileFilter("Genetico 3SAT", "sat");
        FileFilter file2= new ExtensionFileFilter("Genetico N Damas", "ndm");
        FileFilter file3= new ExtensionFileFilter("Genetico binario", "binar");
        FileFilter file4= new ExtensionFileFilter("Genetico TCP", "tcp");
        file.addChoosableFileFilter(file1);
        file.addChoosableFileFilter(file2);
        file.addChoosableFileFilter(file3);
        file.addChoosableFileFilter(file4);
        file.setAcceptAllFileFilterUsed(false);
        file.showOpenDialog(null);
        //abrimos el archivo seleccionado
        File abre = file.getSelectedFile();
        if (abre != null) {
            BufferedReader br = null;
            try {
                StringTokenizer tokenizer;
                String linea;
                br = new BufferedReader(new FileReader(abre));
                linea=br.readLine();
                switch (linea){
                    case "Binario":
                        abrirGenBinario(abre);
                        break;
                    case "N Damas":
                        abrirGenNDamas(abre);
                        break;
                    case "TCP":
                        abrirGenTCP(abre);
                        break;
                    case "3 SAT":
                        abrirGen3SAT(abre);
                        break;
                    default:
                        break;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
    }
    protected void newGen(){
        frameNuevoGen= new JFrameNuevoGen(this);
        frameNuevoGen.setVisible(true);
    }
    protected void datosNewGen(ArrayList<Object> recived){
        if ((int) recived.get(0)==1) {
            String tipoGenetico=(String)recived.get(1);
            int numGens=(int)recived.get(2);
            int tamPob =(int) recived.get(3);
            double probMuta= (double) recived.get(4);
            String seleccion= (String) recived.get(5);
            String nameHilo= (String) recived.get(6);
            JPanel_hilos jPanel_hilos= new JPanel_hilos();
            switch (tipoGenetico){
                case "3 SAT":
                    //Genetico3SAT genetico3SAT=new Genetico3SAT(numGens, tamPob, seleccion, poblacionActual, tamPob, numGens, numGens, probMuta, instancias, fitnessGeneracional, semaf, jPanel_hilos)
                    break;
                default:
                    break;        
            }
            Gestor newGest= new Gestor(null, jPanel_hilos, tipoGenetico);
            gestores.add(newGest);
            getHilos().add(jPanel_hilos);
            this.getjTabbedPane1().addTab(nameHilo, new JScrollPane(newGest.getPanel()));
            jPanel_hilos.setNombre(nameHilo);
            jPanel_hilos.iniciarDesdeNewGen(tipoGenetico,numGens,tamPob,probMuta,seleccion, this);
        }else{
            JOptionPane.showMessageDialog(this,"Se cancelo la creacion del nuevo Genetico");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem_newGen = new javax.swing.JMenuItem();
        jMenuItem_OpenGen = new javax.swing.JMenuItem();
        jMenuItem_SaveGen = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Geneticos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(iconPrincipal.getImage());
        setSize(new java.awt.Dimension(800, 1000));

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jMenuItem_newGen.setIcon(iconNew);
        jMenuItem_newGen.setText("New Genetic Algoritm");
        jMenu1.add(jMenuItem_newGen);

        jMenuItem_OpenGen.setIcon(iconOpen);
        jMenuItem_OpenGen.setText("Open Genetic Algoritm");
        jMenu1.add(jMenuItem_OpenGen);

        jMenuItem_SaveGen.setIcon(iconSave);
        jMenuItem_SaveGen.setText("Save Genetic Algoritm");
        jMenuItem_SaveGen.setEnabled(false);
        jMenu1.add(jMenuItem_SaveGen);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1421, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem_OpenGen;
    private javax.swing.JMenuItem jMenuItem_SaveGen;
    private javax.swing.JMenuItem jMenuItem_newGen;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

    //actualizar abrir gen TCP y crear el hibrido
    private void abrirGenTCP(File abre) {
        BufferedReader br = null;
        try {
            StringTokenizer tokenizer;
            String linea;
            br = new BufferedReader(new FileReader(abre));
            linea=br.readLine();
            String tipoGenetico=linea;
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea, ",");
            int numGens=Integer.parseInt(tokenizer.nextToken());
            int tamPob=Integer.parseInt(tokenizer.nextToken());
            double probMuta=Double.parseDouble(tokenizer.nextToken());
            String seleccion=tokenizer.nextToken();
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea,",");
            ArrayList<Long> fitnessGeneracional=new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                fitnessGeneracional.add(Long.parseLong(tokenizer.nextToken()));
            }   linea=br.readLine();
            tokenizer=new StringTokenizer(linea, ",");
            int numNodos= Integer.parseInt(tokenizer.nextToken());
            int [][] matriz= new int[numNodos][numNodos];
            for (int i = 0; i < numNodos; i++) {
                linea= br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                for (int j = 0; j < numNodos; j++) {
                    matriz[i][j]=Integer.parseInt(tokenizer.nextToken());
                }
            }   IndividuoTCP.setMatriz(matriz);
            ArrayList<IndividuoTCP> poblacion= new ArrayList<>();
            for (int i = 0; i < tamPob; i++) {
                linea= br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                int[] aux= new int[numNodos];
                for (int j = 0; j < numNodos; j++) {
                    aux[j]=Integer.parseInt(tokenizer.nextToken());
                    if(i==0 && j==0){
                        IndividuoTCP.setInicial(aux[0]);
                    }
                }
                poblacion.add(new IndividuoTCP(aux));
            }   Semaphore semoforo= new Semaphore(1);
            int inicial_TCP=poblacion.get(0).getGenotipo()[0];
            JPanel_hilos jPanel_hilos=new JPanel_hilos();
            GeneticoTCP_Thread geneticoTCP= new GeneticoTCP_Thread(numGens, tamPob, probMuta, inicial_TCP, matriz, seleccion, poblacion, fitnessGeneracional, semoforo, jPanel_hilos);
            jPanel_hilos.setNumGens(numGens);
            jPanel_hilos.setTamPob(tamPob);
            jPanel_hilos.setSeleccion(seleccion);
            jPanel_hilos.setProbMuta(probMuta);
            jPanel_hilos.setGeneticoTCP(geneticoTCP);
            jPanel_hilos.setPrincipal(this);
            jPanel_hilos.getjSpinner_TamPob().setValue(tamPob);
            jPanel_hilos.getjSpinner_NumGens().setValue(numGens);
            jPanel_hilos.getjSpinner_ProbMuta().setValue(probMuta);
            jPanel_hilos.getjComboBox_TipoSeleccion().setSelectedItem(seleccion);
            jPanel_hilos.getjButton_StartGen().setEnabled(true);
            jPanel_hilos.getjButton_ModDatos().setEnabled(true);
            jPanel_hilos.setAbrir(true);
            jPanel_hilos.setNuevo(false);
            jPanel_hilos.iniciarGrafica();
            jPanel_hilos.actualizarDatosMejorInd();
            Gestor gestor=new Gestor(geneticoTCP, jPanel_hilos, tipoGenetico);
            gestores.add(gestor);
            getHilos().add(jPanel_hilos);
            this.getjTabbedPane1().addTab(""+(gestores.size()-1), new JScrollPane(gestor.getPanel()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void abrirGenNDamas(File abre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void abrirGenBinario(File abre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void abrirGen3SAT(File abre) {
        BufferedReader br = null;
        try {
            StringTokenizer tokenizer;
            String linea;
            br = new BufferedReader(new FileReader(abre));
            linea=br.readLine();
            String tipoGenetico=linea;
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea, ",");
            int numGens=Integer.parseInt(tokenizer.nextToken());
            int tamPob=Integer.parseInt(tokenizer.nextToken());
            double probMuta=Double.parseDouble(tokenizer.nextToken());
            String seleccion=tokenizer.nextToken();
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea,",");
            ArrayList<Integer> fitnessGeneracional=new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                fitnessGeneracional.add(Integer.parseInt(tokenizer.nextToken()));
            }   linea=br.readLine();
            tokenizer=new StringTokenizer(linea, ",");
            int rango= Integer.parseInt(tokenizer.nextToken());
            int numInstancias= Integer.parseInt(tokenizer.nextToken());
            int numElementos= Integer.parseInt(tokenizer.nextToken());
            int [][] instancias= new int[numInstancias][numElementos];
            for (int i = 0; i < numInstancias; i++) {
                linea= br.readLine();
                tokenizer= new StringTokenizer(linea, " ");
                for (int j = 0; j < numElementos; j++) {
                    instancias[i][j]=Integer.parseInt(tokenizer.nextToken());
                }
            }   Individuo3SAT.setInstancias(instancias);
            ArrayList<Individuo3SAT> poblacion= new ArrayList<>();
            for (int i = 0; i < tamPob; i++) {
                linea= br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                int[] aux= new int[rango+1];
                for (int j = 0; j < rango+1; j++) {
                    aux[j]=Integer.parseInt(tokenizer.nextToken());
                }
                poblacion.add(new Individuo3SAT(aux));
            }   Semaphore semoforo= new Semaphore(1);
            JPanel_hilos jPanel_hilos=new JPanel_hilos();
            Genetico3SAT genetico3SAT= new Genetico3SAT(numGens, tamPob, seleccion, poblacion, rango, numInstancias, numElementos, probMuta, instancias, fitnessGeneracional,semoforo, jPanel_hilos);
            jPanel_hilos.setNumGens(numGens);
            jPanel_hilos.setTamPob(tamPob);
            jPanel_hilos.setSeleccion(seleccion);
            jPanel_hilos.setProbMuta(probMuta);
            jPanel_hilos.setTipoGenetico(tipoGenetico);
            jPanel_hilos.setSemoforo(semoforo);
            jPanel_hilos.setGenetico3SAT(genetico3SAT);
            jPanel_hilos.setPrincipal(this);
            jPanel_hilos.getjSpinner_TamPob().setValue(tamPob);
            jPanel_hilos.getjSpinner_NumGens().setValue(numGens);
            jPanel_hilos.getjSpinner_ProbMuta().setValue(probMuta);
            jPanel_hilos.getjComboBox_TipoSeleccion().setSelectedItem(seleccion);
            jPanel_hilos.getjButton_StartGen().setEnabled(true);
            jPanel_hilos.getjButton_ModDatos().setEnabled(true);
            jPanel_hilos.setAbrir(true);
            jPanel_hilos.setNuevo(false);
            jPanel_hilos.iniciarGrafica();
            jPanel_hilos.actualizarDatosMejorInd();
            Gestor gestor=new Gestor(genetico3SAT, jPanel_hilos, tipoGenetico);
            gestores.add(gestor);
            this.getjTabbedPane1().addTab(""+(gestores.size()-1),new JScrollPane(gestor.getPanel()));
            jPanel_hilos.getjSpinner_TamPob().setValue(tamPob);
            jPanel_hilos.getjSpinner_NumGens().setValue(numGens);
            jPanel_hilos.getjSpinner_ProbMuta().setValue(probMuta);
            jPanel_hilos.getjComboBox_TipoSeleccion().setSelectedItem(seleccion);
            jPanel_hilos.getjButton_StartGen().setEnabled(true);
            jPanel_hilos.getjButton_ModDatos().setEnabled(true);
            jPanel_hilos.setAbrir(true);
            jPanel_hilos.setNuevo(false);
            jPanel_hilos.iniciarGrafica();
            jPanel_hilos.actualizarDatosMejorInd();
            getHilos().add(jPanel_hilos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the jMenuItem_SaveGen
     */
    public javax.swing.JMenuItem getjMenuItem_SaveGen() {
        return jMenuItem_SaveGen;
    }

    /**
     * @return the jTabbedPane1
     */
    public javax.swing.JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    /**
     * @return the hilos
     */
    public ArrayList<JPanel_hilos> getHilos() {
        return hilos;
    }
    
}
