/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import TCP.AbrirMatriz;
import TCP.GeneticoTCP_Thread;
import TCP.IndividuoTCP;
import TCP_Hibrido.GeneradorMatrices;
import TCP_Hibrido.GeneticoTCP_Hibrido_thread;
import TCP_Hibrido.IndividuoTCP_Hibrido;
import TRESSAT.Genetico3SAT;
import TRESSAT.Herramientas;
import TRESSAT.Individuo3SAT;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.filechooser.FileFilter;
import org.jfree.ui.ExtensionFileFilter;

/**
 *
 * @author skabe
 */
public class JPanel_hilos extends javax.swing.JPanel {

    /**
     * Creates new form jPanel_hilos
     */
    private Principal principal;
    private int numGens;
    private int tamPob;
    private double probMuta;
    private String seleccion;
    private String tipoGenetico;
    private Semaphore semoforo,semaforoGrafica;
    private File parametros=null;
    private Genetico3SAT genetico3SAT;
    private GeneticoTCP_Thread geneticoTCP;
    private GeneticoTCP_Hibrido_thread geneticoTCP_Hibrido;
    private int [][] matriz=null;
    private int inicial_TCP;
    private double[][] matrizInclinaciones;
    private int[][] matrizPesos;
    private boolean terminado=false;
    private boolean abrir=false;
    private boolean nuevo=true;
    private boolean graficar=false;
    private boolean pause=false;
    private String nombre;
    ArrayList<JPanel_hilos> hilosPosibles= new ArrayList<>();
    private DefaultComboBoxModel<String> boxModel=new DefaultComboBoxModel<>(new String[]{"Aleatoria", "Por Ruleta"});
    private DefaultComboBoxModel<String> boxModelTipoInsercion=new DefaultComboBoxModel<>(new String[]{"Sustituir al principio", "Sustituir al final", "añadir al final", "sustitucion aleatoria"});
    JScrollPane_ListaIndiv jScrollPane_ListaIndiv=new JScrollPane_ListaIndiv();
    public JPanel_hilos() {
        initComponents();
    }
    public void saveGen(){
        JFileChooser seleccion= new JFileChooser();
        seleccion.setDialogTitle("GUARDAR Algoritmo genetico ");
        seleccion.setDialogType(JFileChooser.SAVE_DIALOG);
        //seleccion.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FileFilter file1=null;
        seleccion.setAcceptAllFileFilterUsed(false);
        switch (this.getTipoGenetico()){
            case "Binario":
                file1= new ExtensionFileFilter("Genetico binario", "binar");
                seleccion.setSelectedFile(new File("EjemploBinario.binar"));
                break;
            case "N Damas":
                file1= new ExtensionFileFilter("Genetico N Damas", "ndm");
                seleccion.setSelectedFile(new File("EjemploNDamas.ndm"));
                break;
            case "TCP":
                file1= new ExtensionFileFilter("Genetico TCP", "tcp");
                seleccion.setSelectedFile(new File("EjemploTCP.tcp"));
                break;
            case "3 SAT":                
                file1= new ExtensionFileFilter("Genetico 3SAT", "sat");
                seleccion.setSelectedFile(new File("Ejemplo3SAT.sat"));
                break;
            case "TCP Hibrido":
                file1= new ExtensionFileFilter("Genetico TCP Hibrido", "tcph");
                seleccion.setSelectedFile(new File("EjemploTCPHibrido.tcph"));
            default:
                seleccion.setAcceptAllFileFilterUsed(true);
                seleccion.setSelectedFile(new File("Ejemplo.sat"));
        }
        seleccion.addChoosableFileFilter(file1);
        int respuesta= seleccion.showSaveDialog(null);
        File archivo=null;
        switch(respuesta){ 
            case JFileChooser.APPROVE_OPTION:
                archivo = seleccion.getSelectedFile();
                switch (getTipoGenetico()){
                    case "Binario":
                        saveGenBinario(archivo);
                        break;
                    case "N Damas":
                        saveGenNDamas(archivo);
                        break;
                    case "TCP":
                        saveGenTCP(archivo);
                        break;
                    case "3 SAT": 
                        saveGen3SAT(archivo);
                        break;
                    case "TCP Hibrido":
                        saveGenTCP_Hibrido(archivo);
                        break;
                    default:
                        break;
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                System.out.println("Cancelado");
                break;
            default :
                System.out.println("Error");
                break;
        }        
    }
    public void terminoEvol(){
        setNuevo(false);
        setTerminado(true);
        getjButton_StartGen().setEnabled(true);
        getPrincipal().getjMenuItem_SaveGen().setEnabled(true);
        getjButton_Pause().setEnabled(false);
        actualizarDatosMejorInd();
        try {
            if(isGraficar()){
                Thread.sleep(1000);
                setGraficar(false);
                getSemaforoGrafica().acquire();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void iniciarDesdeNewGen(String tipoGenetico, int numGens, int tamPob, double probMuta,String seleccion, Principal principal){
        this.setTipoGenetico(tipoGenetico);
        this.setNumGens(numGens);
        this.setTamPob(tamPob);
        this.setProbMuta(probMuta);
        this.setSeleccion(seleccion);
        this.setPrincipal(principal);
        switch (this.getTipoGenetico()){
            case "Binario":
                executeBinario();
                break;
            case "N Damas":
                executeNDamas();
                break;
            case "TCP":
                executeTCP();
                break;
            case "3 SAT":                
                execute3SAT();
                break;
            case "TCP Hibrido":
                executeTCP_Hibrido();
                break;
            default:
        }
        
    }
    private void executeBinario(){
        
    }
    private void executeNDamas(){
        
    }
    private void executeTCP(){
        setNuevo(true);
        getjButton_StartGen().setEnabled(false);
        getjButton_ContinuarCC().setEnabled(false);
        getjButton_ContinuarSC().setEnabled(false);
        getjButton_ModDatos().setEnabled(false);
        getjSpinner_NumGens().setEnabled(false);
        getjSpinner_TamPob().setEnabled(false);
        getjSpinner_ProbMuta().setEnabled(false);
        getjComboBox_TipoSeleccion().setEnabled(false);
        setSemoforo(new Semaphore(1));
        getjLabel_TCP().setText("Ese genetico necesita una matriz base para funcionar, favor de cargarlas:");
        setjButton_TCP(new JButton());
        getjButton_TCP().setText("Cargar matriz");
        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
        getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
        jPanel_ExtrasLayout.setHorizontalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(getjButton_TCP())
                    .addComponent(getjLabel_TCP()))
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel_ExtrasLayout.setVerticalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getjLabel_TCP())
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjButton_TCP())
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        if(getjButton_TCP().getActionListeners().length==0){
            getjButton_TCP().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    setMatriz(AbrirMatriz.abrirMatriz());
                    if (getMatriz() != null) {
                        pedirNinicialTCP();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "\nNo se ha encontrado el archivo",
                                "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }
    }
    private void executeTCP_Hibrido(){
        setNuevo(true);
        getjButton_StartGen().setEnabled(false);
        getjButton_ContinuarCC().setEnabled(false);
        getjButton_ContinuarSC().setEnabled(false);
        getjButton_ModDatos().setEnabled(false);
        getjSpinner_NumGens().setEnabled(false);
        getjSpinner_TamPob().setEnabled(false);
        getjSpinner_ProbMuta().setEnabled(false);
        getjComboBox_TipoSeleccion().setEnabled(false);
        setSemoforo(new Semaphore(1));
        getjLabel_TCP().setText("Ese genetico necesita un archivo .mtzm que contiene 2 matrices base para funcionar, favor de cargarlas:");
        setjButton_TCP(new JButton());
        getjButton_TCP().setText("Cargar matrices");
        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
        getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
        jPanel_ExtrasLayout.setHorizontalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(getjButton_TCP())
                    .addComponent(getjLabel_TCP()))
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel_ExtrasLayout.setVerticalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getjLabel_TCP())
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjButton_TCP())
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        if(getjButton_TCP().getActionListeners().length==0){
            getjButton_TCP().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    Pair<double[][],int[][]> respuesta= GeneradorMatrices.abrirArchivos();
                    setMatrizPesos(respuesta.getValue());
                    setMatrizInclinaciones(respuesta.getKey());
                    if (getMatrizPesos() != null && getMatrizInclinaciones()!=null) {
                        pedirNinicialTCP_Hibrido();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "\nNo se ha encontrado el archivo",
                                "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }
    }
    private void pedirNinicialTCP_Hibrido(){
        getjPanel_Extras().removeAll();
        getjLabel_TCP().setText("Ahora necesita ingresar el nodo inicial para este genetico:");
        getjSpinner_TCP().setModel(new javax.swing.SpinnerNumberModel(0, 0, getMatrizPesos().length-1, 1));
        setjButton_TCP(new JButton());
        getjButton_TCP().setText("Ingresar Inicial");
        getjPanel_Extras().setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
        getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
        jPanel_ExtrasLayout.setHorizontalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(getjButton_TCP())
                    .addComponent(getjLabel_TCP())
                    .addComponent(getjSpinner_TCP()))
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel_ExtrasLayout.setVerticalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getjLabel_TCP())
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjSpinner_TCP())
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjButton_TCP())
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getjPanel_Extras().updateUI();
        getjButton_TCP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getjPanel_Extras().removeAll();
                getjPanel_Extras().setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
                getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
                jPanel_ExtrasLayout.setHorizontalGroup(
                    jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 786, Short.MAX_VALUE)
                );
                jPanel_ExtrasLayout.setVerticalGroup(
                    jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 215, Short.MAX_VALUE)
                );
                
                getjPanel_Extras().updateUI();
                setInicial_TCP((int) getjSpinner_TCP().getValue());
                getjButton_StartGen().setEnabled(true);
            }
        });
    }
    private void pedirNinicialTCP(){
        getjPanel_Extras().removeAll();
        getjLabel_TCP().setText("Ahora necesita ingresar el nodo inicial para este genetico:");
        getjSpinner_TCP().setModel(new javax.swing.SpinnerNumberModel(0, 0, getMatriz().length-1, 1));
        setjButton_TCP(new JButton());
        getjButton_TCP().setText("Ingresar Inicial");
        getjPanel_Extras().setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
        getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
        jPanel_ExtrasLayout.setHorizontalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(getjButton_TCP())
                    .addComponent(getjLabel_TCP())
                    .addComponent(getjSpinner_TCP()))
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel_ExtrasLayout.setVerticalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getjLabel_TCP())
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjSpinner_TCP())
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjButton_TCP())
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getjPanel_Extras().updateUI();
        getjButton_TCP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getjPanel_Extras().removeAll();
                getjPanel_Extras().setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
                getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
                jPanel_ExtrasLayout.setHorizontalGroup(
                    jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 786, Short.MAX_VALUE)
                );
                jPanel_ExtrasLayout.setVerticalGroup(
                    jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 215, Short.MAX_VALUE)
                );
                
                getjPanel_Extras().updateUI();
                setInicial_TCP((int) getjSpinner_TCP().getValue());
                getjButton_StartGen().setEnabled(true);
            }
        });
    }
    private void execute3SAT(){
        setNuevo(true);
        getjButton_StartGen().setEnabled(false);
        getjButton_ContinuarCC().setEnabled(false);
        getjButton_ContinuarSC().setEnabled(false);
        getjButton_ModDatos().setEnabled(false);
        getjSpinner_NumGens().setEnabled(false);
        getjSpinner_TamPob().setEnabled(false);
        getjSpinner_ProbMuta().setEnabled(false);
        getjComboBox_TipoSeleccion().setEnabled(false);
        setSemoforo(new Semaphore(1));
        getjLabel_3SAT().setText("Este genetico necesita parametros para funcionar, favor de cargarlas:");
        getjButton_3SAT().setText("Cargar parametros");
        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
        getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
        jPanel_ExtrasLayout.setHorizontalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(getjButton_3SAT())
                    .addComponent(getjLabel_3SAT()))
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel_ExtrasLayout.setVerticalGroup(jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getjLabel_3SAT())
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getjButton_3SAT())
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        if(getjButton_3SAT().getActionListeners().length==0){
            getjButton_3SAT().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    JFileChooser file = new JFileChooser();
                    file.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    file.showOpenDialog(file);
                    //abrimos el archivo seleccionado
                    setParametros(file.getSelectedFile());

                    if (getParametros() != null) {
                        getjPanel_Extras().removeAll();
                        getjPanel_Extras().setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(getjPanel_Extras());
                        getjPanel_Extras().setLayout(jPanel_ExtrasLayout);
                        jPanel_ExtrasLayout.setHorizontalGroup(
                            jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGap(0, 786, Short.MAX_VALUE)
                        );
                        jPanel_ExtrasLayout.setVerticalGroup(
                            jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGap(0, 215, Short.MAX_VALUE)
                        );
                        getjPanel_Extras().updateUI();
                        getjButton_StartGen().setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "\nNo se ha encontrado el archivo",
                                "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
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

        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel_grafica = new javax.swing.JPanel();
        jPanel_Intercambio = new javax.swing.JPanel();
        jLabel_texto1 = new javax.swing.JLabel();
        jScrollPane1 = jScrollPane_ListaIndiv;
        jComboBox_TipoInsercion = new javax.swing.JComboBox<>();
        jComboBox_GeneticDestino = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton_InsertarIndividuos = new javax.swing.JButton();
        jButton_Actualizar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_Genotipo = new javax.swing.JTextArea();
        jLabel_MejorIndividuo = new javax.swing.JLabel();
        jPanel_Principales = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton_ModDatos = new javax.swing.JButton();
        jSpinner_NumGens = new javax.swing.JSpinner();
        jSpinner_TamPob = new javax.swing.JSpinner();
        jSpinner_ProbMuta = new javax.swing.JSpinner();
        jComboBox_TipoSeleccion = new javax.swing.JComboBox<>();
        jButton_ContinuarCC = new javax.swing.JButton();
        jButton_ContinuarSC = new javax.swing.JButton();
        jButton_StartGen = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton_Pause = new javax.swing.JButton();
        jPanel_Extras = new javax.swing.JPanel();

        jPanel_grafica.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel_graficaLayout = new javax.swing.GroupLayout(jPanel_grafica);
        jPanel_grafica.setLayout(jPanel_graficaLayout);
        jPanel_graficaLayout.setHorizontalGroup(
            jPanel_graficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel_graficaLayout.setVerticalGroup(
            jPanel_graficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel_Intercambio.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel_Intercambio.setPreferredSize(new java.awt.Dimension(500, 0));

        jLabel_texto1.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        jLabel_texto1.setText("Intercambio de Individuos");

        jScrollPane1.setEnabled(false);

        jComboBox_TipoInsercion.setModel(boxModelTipoInsercion);
        jComboBox_TipoInsercion.setEnabled(false);

        jComboBox_GeneticDestino.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Lucida Fax", 0, 14)); // NOI18N
        jLabel1.setText("Genetico Destino");

        jLabel2.setFont(new java.awt.Font("Lucida Fax", 0, 14)); // NOI18N
        jLabel2.setText("Tipo de Insercion");

        jButton_InsertarIndividuos.setText("Insertar Individuos");
        jButton_InsertarIndividuos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_InsertarIndividuosActionPerformed(evt);
            }
        });

        jButton_Actualizar.setText("Actualizar");
        jButton_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_IntercambioLayout = new javax.swing.GroupLayout(jPanel_Intercambio);
        jPanel_Intercambio.setLayout(jPanel_IntercambioLayout);
        jPanel_IntercambioLayout.setHorizontalGroup(
            jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_IntercambioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_IntercambioLayout.createSequentialGroup()
                        .addGroup(jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox_GeneticDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_texto1)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox_TipoInsercion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_IntercambioLayout.createSequentialGroup()
                        .addComponent(jButton_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_InsertarIndividuos)))
                .addContainerGap())
        );
        jPanel_IntercambioLayout.setVerticalGroup(
            jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_IntercambioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_texto1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_GeneticDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_TipoInsercion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_IntercambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_InsertarIndividuos)
                    .addComponent(jButton_Actualizar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextArea_Genotipo.setEditable(false);
        jTextArea_Genotipo.setColumns(20);
        jTextArea_Genotipo.setRows(2);
        jScrollPane2.setViewportView(jTextArea_Genotipo);

        jLabel_MejorIndividuo.setText("El mejor Individuo de la generacion es:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(43, 43, 43))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_MejorIndividuo)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_MejorIndividuo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_Principales.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        jLabel3.setText("Número de Generaciones:");

        jLabel4.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        jLabel4.setText("Tamaño de Población:");

        jLabel5.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        jLabel5.setText("Probabilidad de Muta:");

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        jLabel6.setText("Tipo de Selección:");

        jButton_ModDatos.setFont(new java.awt.Font("Lucida Fax", 1, 14)); // NOI18N
        jButton_ModDatos.setText("Modificar Datos");
        jButton_ModDatos.setEnabled(false);
        jButton_ModDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModDatosActionPerformed(evt);
            }
        });

        jSpinner_NumGens.setModel(new javax.swing.SpinnerNumberModel(1, 1, 2147483647, 100));
        jSpinner_NumGens.setEnabled(false);

        jSpinner_TamPob.setModel(new javax.swing.SpinnerNumberModel(10, 2, 2147483647, 10));
        jSpinner_TamPob.setEnabled(false);

        jSpinner_ProbMuta.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.01d));
        jSpinner_ProbMuta.setEnabled(false);

        jComboBox_TipoSeleccion.setModel(boxModel);
        jComboBox_TipoSeleccion.setEnabled(false);

        jButton_ContinuarCC.setText("Actualizar y continuar");
        jButton_ContinuarCC.setEnabled(false);
        jButton_ContinuarCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ContinuarCCActionPerformed(evt);
            }
        });

        jButton_ContinuarSC.setText("Continuar sin cambios");
        jButton_ContinuarSC.setEnabled(false);
        jButton_ContinuarSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ContinuarSCActionPerformed(evt);
            }
        });

        jButton_StartGen.setText("Iniciar Genetico");
        jButton_StartGen.setEnabled(false);
        jButton_StartGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_StartGenActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        jLabel7.setText("Agoritmo Genetico.");

        jButton_Pause.setText("Pausar");
        jButton_Pause.setEnabled(false);
        jButton_Pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_PauseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_PrincipalesLayout = new javax.swing.GroupLayout(jPanel_Principales);
        jPanel_Principales.setLayout(jPanel_PrincipalesLayout);
        jPanel_PrincipalesLayout.setHorizontalGroup(
            jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_PrincipalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_PrincipalesLayout.createSequentialGroup()
                        .addComponent(jButton_StartGen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Pause, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ContinuarSC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ContinuarCC, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_PrincipalesLayout.createSequentialGroup()
                        .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_ModDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_TipoSeleccion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSpinner_ProbMuta)
                            .addComponent(jSpinner_TamPob)
                            .addComponent(jSpinner_NumGens))))
                .addGap(22, 22, 22))
        );
        jPanel_PrincipalesLayout.setVerticalGroup(
            jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_PrincipalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_ModDatos)
                    .addComponent(jLabel7))
                .addGap(9, 9, 9)
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinner_NumGens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner_TamPob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner_ProbMuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox_TipoSeleccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_PrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ContinuarCC)
                    .addComponent(jButton_ContinuarSC)
                    .addComponent(jButton_StartGen)
                    .addComponent(jButton_Pause))
                .addContainerGap())
        );

        jPanel_Extras.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel_ExtrasLayout = new javax.swing.GroupLayout(jPanel_Extras);
        jPanel_Extras.setLayout(jPanel_ExtrasLayout);
        jPanel_ExtrasLayout.setHorizontalGroup(
            jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 786, Short.MAX_VALUE)
        );
        jPanel_ExtrasLayout.setVerticalGroup(
            jPanel_ExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_grafica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Intercambio, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_Principales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Extras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_Principales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_Extras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_Intercambio, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addComponent(jPanel_grafica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ModDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModDatosActionPerformed
        try {
            if(!isTerminado()){
                getSemoforo().acquire();
            }
            if(isGraficar()){
                setGraficar(false);
                getSemaforoGrafica().acquire();
            }
            getjSpinner_NumGens().setEnabled(true);
            getjSpinner_TamPob().setEnabled(true);
            getjSpinner_ProbMuta().setEnabled(true);
            getjComboBox_TipoSeleccion().setEnabled(true);
            getjButton_ContinuarSC().setEnabled(true);
            getjButton_ContinuarCC().setEnabled(true);
            getPrincipal().getjMenuItem_SaveGen().setEnabled(true);
            getjButton_Pause().setEnabled(false);
            actualizarDatosMejorInd();
        } catch (InterruptedException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton_ModDatosActionPerformed
    public void iniciarGrafica(){
        this.setSemaforoGrafica(new Semaphore(1));
        GraficaHilo hiloG=new GraficaHilo(this, this.getSemaforoGrafica());
        this.setGraficar(true);
        hiloG.setName("HiloGrafica");
        hiloG.start();
    }
    private void startGen3SAT() {
        if(isAbrir()){
            getGenetico3SAT().setName("Genetico "+this.getTipoGenetico());
            getGenetico3SAT().start();
            getjButton_StartGen().setEnabled(false);
            getjButton_Pause().setEnabled(true);
            setAbrir(false);
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        }else if(isNuevo()){
            setGenetico3SAT(new Genetico3SAT(getNumGens(), getTamPob(), getProbMuta(), getParametros(), getSemoforo(), getSeleccion(), this));
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            this.getjButton_Pause().setEnabled(true);
            getjButton_ModDatos().setEnabled(true);
            actualizarDatosMejorInd();
            getGenetico3SAT().setName("Genetico "+this.getTipoGenetico());
            getGenetico3SAT().start();
            getjButton_StartGen().setEnabled(false);
            iniciarGrafica();
            getjButton_Pause().setEnabled(true);
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        }else if(isTerminado()){
            this.setNumGens(getGenetico3SAT().getNumGen());
            this.setTamPob(getGenetico3SAT().getTamPoblacion());
            this.setSeleccion(getGenetico3SAT().getTipoSeleccion());
            ArrayList<Individuo3SAT> poblacionActual= getGenetico3SAT().getPoblacionActual();
            int rango= getGenetico3SAT().getRango();
            int numInstancias=getGenetico3SAT().getNumInstancias();
            int numElementos= getGenetico3SAT().getNumElementos();
            this.setProbMuta(getGenetico3SAT().getProbMutal());
            int[][] instancias= getGenetico3SAT().getInstancias();
            ArrayList<Integer> fitnessGeneracional= getGenetico3SAT().getFitnessGeneracional();
            setGenetico3SAT(new Genetico3SAT(getNumGens(), getTamPob(), getSeleccion(), poblacionActual, rango, numInstancias, numElementos, getProbMuta(), instancias, fitnessGeneracional, this.getSemoforo(), this));
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            getGenetico3SAT().setName("Genetico continuacion "+this.getTipoGenetico());
            getGenetico3SAT().start();
            this.setTerminado(false);
            getjButton_ModDatos().setEnabled(true);
            getjButton_StartGen().setEnabled(false);
            getjSpinner_NumGens().setEnabled(false);
            getjSpinner_TamPob().setEnabled(false);
            getjSpinner_ProbMuta().setEnabled(false);
            getjComboBox_TipoSeleccion().setEnabled(false);
            getPrincipal().getjMenuItem_SaveGen().setEnabled(false);
            getjButton_Pause().setEnabled(true);
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        }
    }

    private void startGenTCP() {
        if (isAbrir()) {
            getGeneticoTCP().setName("Genetico "+this.getTipoGenetico());
            getGeneticoTCP().start();
            getjButton_StartGen().setEnabled(false);
            setAbrir(false);
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        } else if(isNuevo()) {
            setGeneticoTCP(new GeneticoTCP_Thread(getNumGens(), getTamPob(), getProbMuta(), getInicial_TCP(), getMatriz(), getSeleccion(), new ArrayList<>(), getSemoforo(), this));
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            getjButton_ModDatos().setEnabled(true);
            actualizarDatosMejorInd();
            getGeneticoTCP().setName("Genetico "+this.getTipoGenetico());
            getGeneticoTCP().start();
            getjButton_StartGen().setEnabled(false);
            iniciarGrafica();
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        } else if(isTerminado()){
            this.setNumGens(getGeneticoTCP().getNumGen());
            this.setTamPob(getGeneticoTCP().getTamPoblacion());
            this.setSeleccion(getGeneticoTCP().getTipoSeleccion());
            ArrayList<IndividuoTCP> poblacionActual= getGeneticoTCP().getPoblacionActual();
            this.setProbMuta(getGeneticoTCP().getProbMuta());
            ArrayList<Long> fitnessGeneracional= getGeneticoTCP().getFitnessGeneracional();
            setGeneticoTCP(new GeneticoTCP_Thread(getNumGens(), getTamPob(), getProbMuta(), getInicial_TCP(), getMatriz(), getSeleccion(), getGeneticoTCP().getPoblacionActual(), fitnessGeneracional, getSemoforo(), this));
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            getGeneticoTCP().setName("Genetico continuacion "+this.getTipoGenetico());
            getGeneticoTCP().start();
            this.setTerminado(false);
            getjButton_ModDatos().setEnabled(true);
            getjButton_StartGen().setEnabled(false);
            getjSpinner_NumGens().setEnabled(false);
            getjSpinner_TamPob().setEnabled(false);
            getjSpinner_ProbMuta().setEnabled(false);
            getjComboBox_TipoSeleccion().setEnabled(false);
            getPrincipal().getjMenuItem_SaveGen().setEnabled(false);
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        }
    }

    private void startGenTCP_Hibrido(){
        if (isAbrir()) {
            
        } else if(isNuevo()) {
            setGeneticoTCP_Hibrido(new GeneticoTCP_Hibrido_thread(getNumGens(), getTamPob(), getProbMuta(), getInicial_TCP(), getMatrizInclinaciones(), getMatrizPesos(), getSeleccion(), new ArrayList<>(), getSemoforo(), this));
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            getjButton_ModDatos().setEnabled(true);
            actualizarDatosMejorInd();
            getGeneticoTCP_Hibrido().setName("Genetico "+this.getTipoGenetico());
            getGeneticoTCP_Hibrido().start();
            getjButton_StartGen().setEnabled(false);
            iniciarGrafica();
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        } else if(isTerminado()){
            this.setNumGens(getGeneticoTCP_Hibrido().getNumGen());
            this.setTamPob(getGeneticoTCP_Hibrido().getTamPoblacion());
            this.setSeleccion(getGeneticoTCP_Hibrido().getTipoSeleccion());
            ArrayList<IndividuoTCP_Hibrido> poblacionActual= getGeneticoTCP_Hibrido().getPoblacionActual();
            this.setProbMuta(getGeneticoTCP_Hibrido().getProbMuta());
            ArrayList<Double> fitnessGeneracional= getGeneticoTCP_Hibrido().getFitnessGeneracional();
            setGeneticoTCP_Hibrido(new GeneticoTCP_Hibrido_thread(getNumGens(), getTamPob(), getProbMuta(), getInicial_TCP(), getMatrizInclinaciones(), getMatrizPesos(), getSeleccion(), poblacionActual, fitnessGeneracional, getSemoforo(), this));
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            getGeneticoTCP_Hibrido().setName("Genetico continuacion "+this.getTipoGenetico());
            getGeneticoTCP_Hibrido().start();
            this.setTerminado(false);
            getjButton_ModDatos().setEnabled(true);
            getjButton_StartGen().setEnabled(false);
            getjSpinner_NumGens().setEnabled(false);
            getjSpinner_TamPob().setEnabled(false);
            getjSpinner_ProbMuta().setEnabled(false);
            getjComboBox_TipoSeleccion().setEnabled(false);
            getPrincipal().getjMenuItem_SaveGen().setEnabled(false);
            if(!isGraficar()){
                setGraficar(true);
                getSemaforoGrafica().release();
            }
        }
    }
    private void startGenNDamas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void startGenBinario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void jButton_ContinuarCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ContinuarCCActionPerformed
        setNumGens((int) getjSpinner_NumGens().getValue());
        setTamPob((int) getjSpinner_TamPob().getValue());
        setProbMuta((double) getjSpinner_ProbMuta().getValue());
        setSeleccion((String) getjComboBox_TipoSeleccion().getSelectedItem());
        switch (getTipoGenetico()){
            case "Binario":
            break;
            case "N Damas":
            break;
            case "TCP":
            getGeneticoTCP().setNumGen(getNumGens());
            getGeneticoTCP().setTamPoblacion(getTamPob());
            getGeneticoTCP().setProbMuta(getProbMuta());
            getGeneticoTCP().setTipoSeleccion(getSeleccion());
            break;
            case "3 SAT":
            getGenetico3SAT().setNumGen(getNumGens());
            getGenetico3SAT().setProbMutal(getProbMuta());
            getGenetico3SAT().setTamPoblacion(getTamPob());
            getGenetico3SAT().setTipoSeleccion(getSeleccion());
            break;
            case "TCP Hibrido":
            getGeneticoTCP_Hibrido().setNumGen(getNumGens());
            getGeneticoTCP_Hibrido().setTamPoblacion(getTamPob());
            getGeneticoTCP_Hibrido().setProbMuta(getProbMuta());
            getGeneticoTCP_Hibrido().setTipoSeleccion(getSeleccion());
            break;
            default:
        }
        getjButton_ModDatos().setEnabled(true);
        getjButton_ContinuarCC().setEnabled(false);
        getjButton_ContinuarSC().setEnabled(false);
        getjSpinner_NumGens().setEnabled(false);
        getjSpinner_TamPob().setEnabled(false);
        getjSpinner_ProbMuta().setEnabled(false);
        getjComboBox_TipoSeleccion().setEnabled(false);
        getPrincipal().getjMenuItem_SaveGen().setEnabled(false);
        getjButton_Pause().setEnabled(true);
        if(!isTerminado()){getSemoforo().release();}
        if(!isGraficar()){
            setGraficar(true);
            getSemaforoGrafica().release();
        }
    }//GEN-LAST:event_jButton_ContinuarCCActionPerformed

    private void jButton_ContinuarSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ContinuarSCActionPerformed
        this.getjSpinner_NumGens().setValue(getNumGens());
        this.getjSpinner_TamPob().setValue(getTamPob());
        this.getjSpinner_ProbMuta().setValue(getProbMuta());
        this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
        getjButton_ModDatos().setEnabled(true);
        getjButton_ContinuarCC().setEnabled(false);
        getjButton_ContinuarSC().setEnabled(false);
        getjSpinner_NumGens().setEnabled(false);
        getjSpinner_TamPob().setEnabled(false);
        getjSpinner_ProbMuta().setEnabled(false);
        getjComboBox_TipoSeleccion().setEnabled(false);
        getPrincipal().getjMenuItem_SaveGen().setEnabled(false);
        getjButton_Pause().setEnabled(true);
        if(!isTerminado()){getSemoforo().release();}
        if(!isGraficar()){
            setGraficar(true);
            getSemaforoGrafica().release();
        }
    }//GEN-LAST:event_jButton_ContinuarSCActionPerformed

    private void jButton_StartGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_StartGenActionPerformed
        switch (getTipoGenetico()){
            case "Binario":
            startGenBinario();
            break;
            case "N Damas":
            startGenNDamas();
            break;
            case "TCP":
            startGenTCP();
            break;
            case "3 SAT":
            startGen3SAT();
            break;
            case "TCP Hibrido":
            startGenTCP_Hibrido();
            break;
            default:
        }

    }//GEN-LAST:event_jButton_StartGenActionPerformed

    private void jButton_PauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PauseActionPerformed
        try {
            if(isPause() == false){
                getSemoforo().acquire();
                getSemaforoGrafica().acquire();
                getjButton_ModDatos().setEnabled(false);
                getjButton_Pause().setText("Reanudar");
                setPause(true);
                this.actualizarDatosMejorInd();
                this.actualizarIndividuosGeneticos();
                this.jScrollPane_ListaIndiv.setEnabled(true);
                this.jComboBox_TipoInsercion.setEnabled(true);
                actualizarGeneticosDisponibles();
                this.jComboBox_GeneticDestino.setEnabled(true);
                this.jButton_InsertarIndividuos.setEnabled(true);
                this.jButton_Actualizar.setEnabled(true);
            }else{
                getSemoforo().release();
                getSemaforoGrafica().release();
                getjButton_ModDatos().setEnabled(true);
                getjButton_Pause().setText("Pause");
                setPause(false);
                this.jScrollPane_ListaIndiv.setEnabled(false);
                this.jComboBox_TipoInsercion.setEnabled(false);
                this.jComboBox_GeneticDestino.setEnabled(false);
                this.jButton_InsertarIndividuos.setEnabled(false);
                this.jButton_Actualizar.setEnabled(false);
                this.jScrollPane_ListaIndiv.setEnabled(false);
            }
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton_PauseActionPerformed

    private void jButton_InsertarIndividuosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_InsertarIndividuosActionPerformed
        enviarIndividuos();
    }//GEN-LAST:event_jButton_InsertarIndividuosActionPerformed

    private void jButton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActualizarActionPerformed
        this.actualizarDatosMejorInd();
        this.actualizarIndividuosGeneticos();
        this.actualizarGeneticosDisponibles();
    }//GEN-LAST:event_jButton_ActualizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Actualizar;
    private javax.swing.JButton jButton_ContinuarCC;
    private javax.swing.JButton jButton_ContinuarSC;
    private javax.swing.JButton jButton_InsertarIndividuos;
    private javax.swing.JButton jButton_ModDatos;
    private javax.swing.JButton jButton_Pause;
    private javax.swing.JButton jButton_StartGen;
    private javax.swing.JComboBox<String> jComboBox_GeneticDestino;
    private javax.swing.JComboBox<String> jComboBox_TipoInsercion;
    private javax.swing.JComboBox<String> jComboBox_TipoSeleccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel_MejorIndividuo;
    private javax.swing.JLabel jLabel_texto1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_Extras;
    private javax.swing.JPanel jPanel_Intercambio;
    private javax.swing.JPanel jPanel_Principales;
    private javax.swing.JPanel jPanel_grafica;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner_NumGens;
    private javax.swing.JSpinner jSpinner_ProbMuta;
    private javax.swing.JSpinner jSpinner_TamPob;
    private javax.swing.JTextArea jTextArea_Genotipo;
    // End of variables declaration//GEN-END:variables
    private JLabel jLabel_3SAT=new JLabel();
    private JButton jButton_3SAT=new JButton();
    private JLabel jLabel_TCP=new JLabel();
    private JButton jButton_TCP=new JButton();
    private JSpinner jSpinner_TCP=new JSpinner();
    /**
     * @return the genetico
     */
    public Genetico3SAT getGenetico3SAT() {
        return genetico3SAT;
    }
    
    /**
     * @return the jPanel_Grafica
     */
    public javax.swing.JPanel getjPanel_Grafica() {
        return getjPanel_grafica();
    }

    /**
     * @return the tipoGenetico
     */
    public String getTipoGenetico() {
        return tipoGenetico;
    }

    /**
     * @return the geneticoTCP
     */
    public GeneticoTCP_Thread getGeneticoTCP() {
        return geneticoTCP;
    }

    /**
     * @return the matriz
     */
    public int[][] getMatriz() {
        return matriz;
    }

    /**
     * @param matriz the matriz to set
     */
    public void setMatriz(int[][] matriz) {
        this.setMatriz(matriz);
    }    

    private void saveGen3SAT(File archivo) {
        FileWriter file= null;
        try {
            file = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(file);
            String linea=getNumGens()+","+getTamPob()+","+getProbMuta()+","+this.getSeleccion()+"";
            bw.write(this.getTipoGenetico());
            bw.newLine();
            bw.write(linea);
            bw.newLine();
            linea="";
            for (int i = 0; i < this.getGenetico3SAT().getFitnessGeneracional().size(); i++) {
                linea=linea+this.getGenetico3SAT().getFitnessGeneracional().get(i)+",";
            }   bw.write(linea);
            bw.newLine();
            linea=getGenetico3SAT().getRango()+","+getGenetico3SAT().getNumInstancias()+","+getGenetico3SAT().getNumElementos();
            bw.write(linea);
            bw.newLine();
            for (int i = 0; i < getGenetico3SAT().getInstancias().length; i++) {
                linea="";
                for (int j = 0; j < getGenetico3SAT().getInstancias()[0].length; j++) {
                    linea=linea+getGenetico3SAT().getInstancias()[i][j]+" ";
                }
                bw.write(linea);
                bw.newLine();
            }   ArrayList<Individuo3SAT> poblacion=getGenetico3SAT().getPoblacionActual();
            for (int i = 0; i < poblacion.size(); i++) {
                linea="";
                for (int j = 0; j < poblacion.get(i).getGenotipo().length; j++) {
                    linea=linea+poblacion.get(i).getGenotipo()[j]+",";
                }
                bw.write(linea);
                bw.newLine();
            }   bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void saveGenTCP_Hibrido(File archivo){
        FileWriter file= null;
        try {
            file = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(file);
            String linea=getNumGens()+","+getTamPob()+","+getProbMuta()+","+this.getSeleccion()+"";
            bw.write(this.getTipoGenetico());
            bw.newLine();
            bw.write(linea);
            bw.newLine();
            linea="";
            for (int i = 0; i < this.getGeneticoTCP().getFitnessGeneracional().size(); i++) {
                linea=linea+this.getGeneticoTCP().getFitnessGeneracional().get(i)+",";
            }   bw.write(linea);
            bw.newLine();
            linea=getMatriz().length+",";
            bw.write(linea);
            bw.newLine();
            for (int i = 0; i < getMatriz().length; i++) {
                linea="";
                for (int j = 0; j < getMatriz()[0].length; j++) {
                    linea=linea+getMatriz()[i][j]+",";
                }
                bw.write(linea);
                bw.newLine();
            }   
            ArrayList<IndividuoTCP> poblacion=getGeneticoTCP().getPoblacionActual();
            for (int i = 0; i < poblacion.size(); i++) {
                linea="";
                for (int j = 0; j < poblacion.get(i).getGenotipo().length; j++) {
                    linea=linea+poblacion.get(i).getGenotipo()[j]+",";
                }
                bw.write(linea);
            }   bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void saveGenTCP(File archivo) {
        FileWriter file= null;
        try {
            file = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(file);
            String linea=getNumGens()+","+getTamPob()+","+getProbMuta()+","+this.getSeleccion()+"";
            bw.write(this.getTipoGenetico());
            bw.newLine();
            bw.write(linea);
            bw.newLine();
            linea="";
            for (int i = 0; i < this.getGeneticoTCP_Hibrido().getFitnessGeneracional().size(); i++) {
                linea=linea+this.getGeneticoTCP_Hibrido().getFitnessGeneracional().get(i)+",";
            }   bw.write(linea);
            bw.newLine();
            linea=getMatrizPesos().length+",";
            bw.write(linea);
            bw.newLine();
            for (int i = 0; i < getMatrizPesos().length; i++) {
                linea="";
                for (int j = 0; j < getMatrizPesos()[0].length; j++) {
                    linea=linea+getMatriz()[i][j]+",";
                }
                bw.write(linea);
                bw.newLine();
            }   
            ArrayList<IndividuoTCP> poblacion=getGeneticoTCP().getPoblacionActual();
            for (int i = 0; i < poblacion.size(); i++) {
                linea="";
                for (int j = 0; j < poblacion.get(i).getGenotipo().length; j++) {
                    linea=linea+poblacion.get(i).getGenotipo()[j]+",";
                }
                bw.write(linea);
            }   bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveGenNDamas(File archivo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveGenBinario(File archivo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void abrirGenTCP(File abre) {
        BufferedReader br = null;
        try {
            StringTokenizer tokenizer;
            String linea;
            br = new BufferedReader(new FileReader(abre));
            linea=br.readLine();
            this.setTipoGenetico(linea);
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea, ",");
            this.setNumGens(Integer.parseInt(tokenizer.nextToken()));
            this.setTamPob(Integer.parseInt(tokenizer.nextToken()));
            this.setProbMuta(Double.parseDouble(tokenizer.nextToken()));
            this.setSeleccion(tokenizer.nextToken());
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea,",");
            ArrayList<Long> fitnessGeneracional=new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                fitnessGeneracional.add(Long.parseLong(tokenizer.nextToken()));
            }   linea=br.readLine();
            tokenizer=new StringTokenizer(linea, ",");
            int numNodos= Integer.parseInt(tokenizer.nextToken());
            this.setMatriz(new int[numNodos][numNodos]);
            for (int i = 0; i < numNodos; i++) {
                linea= br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                for (int j = 0; j < numNodos; j++) {
                    this.getMatriz()[i][j]=Integer.parseInt(tokenizer.nextToken());
                }
            }   IndividuoTCP.setMatriz(getMatriz());
            ArrayList<IndividuoTCP> poblacion= new ArrayList<>();
            for (int i = 0; i < getTamPob(); i++) {
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
            }   this.setSemoforo(new Semaphore(1));
            this.setInicial_TCP(poblacion.get(0).getGenotipo()[0]);
            this.setGeneticoTCP(new GeneticoTCP_Thread(getNumGens(), getTamPob(), getProbMuta(), getInicial_TCP(), getMatriz(), getSeleccion(), poblacion, fitnessGeneracional, getSemoforo(), this));
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            this.getjButton_StartGen().setEnabled(true);
            this.getjButton_ModDatos().setEnabled(true);
            setAbrir(true);
            setNuevo(false);
            iniciarGrafica();
            actualizarDatosMejorInd();
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
            this.setTipoGenetico(linea);
            linea=br.readLine();
            tokenizer= new StringTokenizer(linea, ",");
            this.setNumGens(Integer.parseInt(tokenizer.nextToken()));
            this.setTamPob(Integer.parseInt(tokenizer.nextToken()));
            this.setProbMuta(Double.parseDouble(tokenizer.nextToken()));
            this.setSeleccion(tokenizer.nextToken());
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
            for (int i = 0; i < getTamPob(); i++) {
                linea= br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                int[] aux= new int[rango+1];
                for (int j = 0; j < rango+1; j++) {
                    aux[j]=Integer.parseInt(tokenizer.nextToken());
                }
                poblacion.add(new Individuo3SAT(aux));
            }   setSemoforo(new Semaphore(1));
            setGenetico3SAT(new Genetico3SAT(getNumGens(), getTamPob(), getSeleccion(), poblacion, rango, numInstancias, numElementos, getProbMuta(), instancias, fitnessGeneracional, getSemoforo(), this));
            this.getjSpinner_TamPob().setValue(getTamPob());
            this.getjSpinner_NumGens().setValue(getNumGens());
            this.getjSpinner_ProbMuta().setValue(getProbMuta());
            this.getjComboBox_TipoSeleccion().setSelectedItem(getSeleccion());
            this.getjButton_StartGen().setEnabled(true);
            this.getjButton_ModDatos().setEnabled(true);
            setAbrir(true);
            setNuevo(false);
            iniciarGrafica();
            actualizarDatosMejorInd();
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
    public void actualizarDatosMejorInd(){
        switch (getTipoGenetico()){
            case "3 SAT":
                this.getjTextArea_Genotipo().setText(Herramientas.mejorPoblacion(genetico3SAT.getPoblacionActual()).toString());
                break;
            case "TCP Hibrido":
                this.getjTextArea_Genotipo().setText(TCP_Hibrido.Herramientas.mejorPoblacion(getGeneticoTCP_Hibrido().getPoblacionActual()).toString());
                break;
            case "TCP":
                break;
            default:
                break;
        }
    }
    public void actualizarIndividuosGeneticos(){
        ArrayList<JPanel_Individuos> individuos=new ArrayList<>();
        switch(tipoGenetico){
            case "3 SAT":
                for (int i = 0; i < genetico3SAT.getPoblacionActual().size(); i++) {
                    individuos.add(new JPanel_Individuos(genetico3SAT.getPoblacionActual().get(i)));
                }
                break;
            default:
                break;
        }
        jScrollPane_ListaIndiv.setTipoGenetico(tipoGenetico);
        jScrollPane_ListaIndiv.setIndividuos(individuos);
    }
    public void actualizarGeneticosDisponibles(){
        ArrayList<JPanel_hilos> hilos= getPrincipal().getHilos();
        hilosPosibles= new ArrayList<>();
        if(hilos.size()>1){
            for (int i = 0; i < hilos.size(); i++) {
                if(this.equals(hilos.get(i))){}else{
                    if(tipoGenetico==hilos.get(i).tipoGenetico && hilos.get(i).isPause()){
                        switch (tipoGenetico){
                            case "3 SAT":
                                if(this.genetico3SAT.getRango()==hilos.get(i).getGenetico3SAT().getRango() &&
                                        this.genetico3SAT.getNumInstancias()== hilos.get(i).getGenetico3SAT().getNumInstancias() &&
                                        this.genetico3SAT.getNumElementos()== hilos.get(i).getGenetico3SAT().getNumElementos() ){
                                    hilosPosibles.add(hilos.get(i));
                                }
                                break;
                            default:
                                break;
                        }
                        
                    }
                }
            }
        }
        String[] posibles= new String[hilosPosibles.size()];
        for (int i = 0; i < hilosPosibles.size(); i++) {
            posibles[i]=hilosPosibles.get(i).getNombre();
        }
        DefaultComboBoxModel<String> boxModelposibles=new DefaultComboBoxModel<>(posibles);
        jComboBox_GeneticDestino.setModel(boxModelposibles);
    }
    
    private ArrayList<JPanel_Individuos> obtenerIndividuos(){
        ArrayList<JPanel_Individuos> individuos=  this.jScrollPane_ListaIndiv.getIndividuos();
        ArrayList<JPanel_Individuos> seleccionados= new ArrayList<>();
        for (int i = 0; i < individuos.size(); i++) {
            if(individuos.get(i).isSelected()){
                seleccionados.add(individuos.get(i));
            }
        }
        return seleccionados;
    }
    private void enviarIndividuos(){
        ArrayList<Object> individuosEnviar=new ArrayList<>();
        ArrayList<JPanel_Individuos> seleccionados= obtenerIndividuos();
        for (int i = 0; i < seleccionados.size(); i++) {
            individuosEnviar.add(seleccionados.get(i).getIndividuo());
        }
        for (int i = 0; i < hilosPosibles.size(); i++) {
            if(((String)jComboBox_GeneticDestino.getSelectedItem()).equals(hilosPosibles.get(i).getNombre())){
                hilosPosibles.get(i).recibirIndividuo(individuosEnviar, (String)jComboBox_TipoInsercion.getSelectedItem());
                break;
            }
        }
    }
    private void recibirIndividuo(ArrayList<Object> individuos, String tipoInsercion){
        switch (tipoInsercion){
            case "Sustituir al principio":
                
                break;
            case "Sustituir al final":
                break;
            case "añadir al final": 
                switch(tipoGenetico){
                    case "3 SAT":
                        this.tamPob=genetico3SAT.añadirFinal(individuos);
                        this.jSpinner_TamPob.setValue(this.tamPob);
                        break;
                }
                break;
            case "sustitucion aleatoria":
                break;
        }
    }
    /**
     * @return the geneticoTCP_Hibrido
     */
    public GeneticoTCP_Hibrido_thread getGeneticoTCP_Hibrido() {
        return geneticoTCP_Hibrido;
    }

    /**
     * @return the principal
     */
    public Principal getPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set
     */
    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    /**
     * @return the numGens
     */
    public int getNumGens() {
        return numGens;
    }

    /**
     * @param numGens the numGens to set
     */
    public void setNumGens(int numGens) {
        this.numGens = numGens;
    }

    /**
     * @return the tamPob
     */
    public int getTamPob() {
        return tamPob;
    }

    /**
     * @param tamPob the tamPob to set
     */
    public void setTamPob(int tamPob) {
        this.tamPob = tamPob;
    }

    /**
     * @return the probMuta
     */
    public double getProbMuta() {
        return probMuta;
    }

    /**
     * @param probMuta the probMuta to set
     */
    public void setProbMuta(double probMuta) {
        this.probMuta = probMuta;
    }

    /**
     * @return the seleccion
     */
    public String getSeleccion() {
        return seleccion;
    }

    /**
     * @param seleccion the seleccion to set
     */
    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    /**
     * @param tipoGenetico the tipoGenetico to set
     */
    public void setTipoGenetico(String tipoGenetico) {
        this.tipoGenetico = tipoGenetico;
    }

    /**
     * @return the semoforo
     */
    public Semaphore getSemoforo() {
        return semoforo;
    }

    /**
     * @param semoforo the semoforo to set
     */
    public void setSemoforo(Semaphore semoforo) {
        this.semoforo = semoforo;
    }

    /**
     * @return the semaforoGrafica
     */
    public Semaphore getSemaforoGrafica() {
        return semaforoGrafica;
    }

    /**
     * @param semaforoGrafica the semaforoGrafica to set
     */
    public void setSemaforoGrafica(Semaphore semaforoGrafica) {
        this.semaforoGrafica = semaforoGrafica;
    }

    /**
     * @return the parametros
     */
    public File getParametros() {
        return parametros;
    }

    /**
     * @param parametros the parametros to set
     */
    public void setParametros(File parametros) {
        this.parametros = parametros;
    }

    /**
     * @param genetico3SAT the genetico3SAT to set
     */
    public void setGenetico3SAT(Genetico3SAT genetico3SAT) {
        this.genetico3SAT = genetico3SAT;
    }

    /**
     * @param geneticoTCP the geneticoTCP to set
     */
    public void setGeneticoTCP(GeneticoTCP_Thread geneticoTCP) {
        this.geneticoTCP = geneticoTCP;
    }

    /**
     * @param geneticoTCP_Hibrido the geneticoTCP_Hibrido to set
     */
    public void setGeneticoTCP_Hibrido(GeneticoTCP_Hibrido_thread geneticoTCP_Hibrido) {
        this.geneticoTCP_Hibrido = geneticoTCP_Hibrido;
    }

    /**
     * @return the inicial_TCP
     */
    public int getInicial_TCP() {
        return inicial_TCP;
    }

    /**
     * @param inicial_TCP the inicial_TCP to set
     */
    public void setInicial_TCP(int inicial_TCP) {
        this.inicial_TCP = inicial_TCP;
    }

    /**
     * @return the matrizInclinaciones
     */
    public double[][] getMatrizInclinaciones() {
        return matrizInclinaciones;
    }

    /**
     * @param matrizInclinaciones the matrizInclinaciones to set
     */
    public void setMatrizInclinaciones(double[][] matrizInclinaciones) {
        this.matrizInclinaciones = matrizInclinaciones;
    }

    /**
     * @return the matrizPesos
     */
    public int[][] getMatrizPesos() {
        return matrizPesos;
    }

    /**
     * @param matrizPesos the matrizPesos to set
     */
    public void setMatrizPesos(int[][] matrizPesos) {
        this.matrizPesos = matrizPesos;
    }

    /**
     * @return the terminado
     */
    public boolean isTerminado() {
        return terminado;
    }

    /**
     * @param terminado the terminado to set
     */
    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    /**
     * @return the abrir
     */
    public boolean isAbrir() {
        return abrir;
    }

    /**
     * @param abrir the abrir to set
     */
    public void setAbrir(boolean abrir) {
        this.abrir = abrir;
    }

    /**
     * @return the nuevo
     */
    public boolean isNuevo() {
        return nuevo;
    }

    /**
     * @param nuevo the nuevo to set
     */
    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    /**
     * @return the graficar
     */
    public boolean isGraficar() {
        return graficar;
    }

    /**
     * @param graficar the graficar to set
     */
    public void setGraficar(boolean graficar) {
        this.graficar = graficar;
    }

    /**
     * @return the pause
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * @param pause the pause to set
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * @return the boxModel
     */
    public DefaultComboBoxModel<String> getBoxModel() {
        return boxModel;
    }

    /**
     * @param boxModel the boxModel to set
     */
    public void setBoxModel(DefaultComboBoxModel<String> boxModel) {
        this.boxModel = boxModel;
    }

    /**
     * @return the jButton_ContinuarCC
     */
    public javax.swing.JButton getjButton_ContinuarCC() {
        return jButton_ContinuarCC;
    }

    /**
     * @param jButton_ContinuarCC the jButton_ContinuarCC to set
     */
    public void setjButton_ContinuarCC(javax.swing.JButton jButton_ContinuarCC) {
        this.jButton_ContinuarCC = jButton_ContinuarCC;
    }

    /**
     * @return the jButton_ContinuarSC
     */
    public javax.swing.JButton getjButton_ContinuarSC() {
        return jButton_ContinuarSC;
    }

    /**
     * @param jButton_ContinuarSC the jButton_ContinuarSC to set
     */
    public void setjButton_ContinuarSC(javax.swing.JButton jButton_ContinuarSC) {
        this.jButton_ContinuarSC = jButton_ContinuarSC;
    }

    /**
     * @return the jButton_InsertarIndividuos
     */
    public javax.swing.JButton getjButton_InsertarIndividuos() {
        return jButton_InsertarIndividuos;
    }

    /**
     * @param jButton_InsertarIndividuos the jButton_InsertarIndividuos to set
     */
    public void setjButton_InsertarIndividuos(javax.swing.JButton jButton_InsertarIndividuos) {
        this.jButton_InsertarIndividuos = jButton_InsertarIndividuos;
    }

    /**
     * @return the jButton_ModDatos
     */
    public javax.swing.JButton getjButton_ModDatos() {
        return jButton_ModDatos;
    }

    /**
     * @param jButton_ModDatos the jButton_ModDatos to set
     */
    public void setjButton_ModDatos(javax.swing.JButton jButton_ModDatos) {
        this.jButton_ModDatos = jButton_ModDatos;
    }

    /**
     * @return the jButton_Pause
     */
    public javax.swing.JButton getjButton_Pause() {
        return jButton_Pause;
    }

    /**
     * @param jButton_Pause the jButton_Pause to set
     */
    public void setjButton_Pause(javax.swing.JButton jButton_Pause) {
        this.jButton_Pause = jButton_Pause;
    }

    /**
     * @return the jButton_StartGen
     */
    public javax.swing.JButton getjButton_StartGen() {
        return jButton_StartGen;
    }

    /**
     * @param jButton_StartGen the jButton_StartGen to set
     */
    public void setjButton_StartGen(javax.swing.JButton jButton_StartGen) {
        this.jButton_StartGen = jButton_StartGen;
    }

    /**
     * @return the jComboBox_GeneticDestino
     */
    public javax.swing.JComboBox<String> getjComboBox_GeneticDestino() {
        return jComboBox_GeneticDestino;
    }

    /**
     * @param jComboBox_GeneticDestino the jComboBox_GeneticDestino to set
     */
    public void setjComboBox_GeneticDestino(javax.swing.JComboBox<String> jComboBox_GeneticDestino) {
        this.jComboBox_GeneticDestino = jComboBox_GeneticDestino;
    }

    /**
     * @return the jComboBox_TipoInsercion
     */
    public javax.swing.JComboBox<String> getjComboBox_TipoInsercion() {
        return jComboBox_TipoInsercion;
    }

    /**
     * @param jComboBox_TipoInsercion the jComboBox_TipoInsercion to set
     */
    public void setjComboBox_TipoInsercion(javax.swing.JComboBox<String> jComboBox_TipoInsercion) {
        this.jComboBox_TipoInsercion = jComboBox_TipoInsercion;
    }

    /**
     * @return the jComboBox_TipoSeleccion
     */
    public javax.swing.JComboBox<String> getjComboBox_TipoSeleccion() {
        return jComboBox_TipoSeleccion;
    }

    /**
     * @param jComboBox_TipoSeleccion the jComboBox_TipoSeleccion to set
     */
    public void setjComboBox_TipoSeleccion(javax.swing.JComboBox<String> jComboBox_TipoSeleccion) {
        this.jComboBox_TipoSeleccion = jComboBox_TipoSeleccion;
    }

    /**
     * @return the jLabel1
     */
    public javax.swing.JLabel getjLabel1() {
        return jLabel1;
    }

    /**
     * @param jLabel1 the jLabel1 to set
     */
    public void setjLabel1(javax.swing.JLabel jLabel1) {
        this.jLabel1 = jLabel1;
    }

    /**
     * @return the jLabel2
     */
    public javax.swing.JLabel getjLabel2() {
        return jLabel2;
    }

    /**
     * @param jLabel2 the jLabel2 to set
     */
    public void setjLabel2(javax.swing.JLabel jLabel2) {
        this.jLabel2 = jLabel2;
    }

    /**
     * @return the jLabel3
     */
    public javax.swing.JLabel getjLabel3() {
        return jLabel3;
    }

    /**
     * @param jLabel3 the jLabel3 to set
     */
    public void setjLabel3(javax.swing.JLabel jLabel3) {
        this.jLabel3 = jLabel3;
    }

    /**
     * @return the jLabel4
     */
    public javax.swing.JLabel getjLabel4() {
        return jLabel4;
    }

    /**
     * @param jLabel4 the jLabel4 to set
     */
    public void setjLabel4(javax.swing.JLabel jLabel4) {
        this.jLabel4 = jLabel4;
    }

    /**
     * @return the jLabel5
     */
    public javax.swing.JLabel getjLabel5() {
        return jLabel5;
    }

    /**
     * @param jLabel5 the jLabel5 to set
     */
    public void setjLabel5(javax.swing.JLabel jLabel5) {
        this.jLabel5 = jLabel5;
    }

    /**
     * @return the jLabel6
     */
    public javax.swing.JLabel getjLabel6() {
        return jLabel6;
    }

    /**
     * @param jLabel6 the jLabel6 to set
     */
    public void setjLabel6(javax.swing.JLabel jLabel6) {
        this.jLabel6 = jLabel6;
    }

    /**
     * @return the jLabel7
     */
    public javax.swing.JLabel getjLabel7() {
        return jLabel7;
    }

    /**
     * @param jLabel7 the jLabel7 to set
     */
    public void setjLabel7(javax.swing.JLabel jLabel7) {
        this.jLabel7 = jLabel7;
    }

    /**
     * @return the jLabel_MejorIndividuo
     */
    public javax.swing.JLabel getjLabel_MejorIndividuo() {
        return jLabel_MejorIndividuo;
    }

    /**
     * @param jLabel_MejorIndividuo the jLabel_MejorIndividuo to set
     */
    public void setjLabel_MejorIndividuo(javax.swing.JLabel jLabel_MejorIndividuo) {
        this.jLabel_MejorIndividuo = jLabel_MejorIndividuo;
    }

    /**
     * @return the jLabel_texto1
     */
    public javax.swing.JLabel getjLabel_texto1() {
        return jLabel_texto1;
    }

    /**
     * @param jLabel_texto1 the jLabel_texto1 to set
     */
    public void setjLabel_texto1(javax.swing.JLabel jLabel_texto1) {
        this.jLabel_texto1 = jLabel_texto1;
    }

    /**
     * @return the jPanel1
     */
    public javax.swing.JPanel getjPanel1() {
        return jPanel1;
    }

    /**
     * @param jPanel1 the jPanel1 to set
     */
    public void setjPanel1(javax.swing.JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    /**
     * @return the jPanel_Extras
     */
    public javax.swing.JPanel getjPanel_Extras() {
        return jPanel_Extras;
    }

    /**
     * @param jPanel_Extras the jPanel_Extras to set
     */
    public void setjPanel_Extras(javax.swing.JPanel jPanel_Extras) {
        this.jPanel_Extras = jPanel_Extras;
    }

    /**
     * @return the jPanel_Intercambio
     */
    public javax.swing.JPanel getjPanel_Intercambio() {
        return jPanel_Intercambio;
    }

    /**
     * @param jPanel_Intercambio the jPanel_Intercambio to set
     */
    public void setjPanel_Intercambio(javax.swing.JPanel jPanel_Intercambio) {
        this.jPanel_Intercambio = jPanel_Intercambio;
    }

    /**
     * @return the jPanel_Principales
     */
    public javax.swing.JPanel getjPanel_Principales() {
        return jPanel_Principales;
    }

    /**
     * @param jPanel_Principales the jPanel_Principales to set
     */
    public void setjPanel_Principales(javax.swing.JPanel jPanel_Principales) {
        this.jPanel_Principales = jPanel_Principales;
    }
    
    /**
     * @return the jPanel_grafica
     */
    public javax.swing.JPanel getjPanel_grafica() {
        return jPanel_grafica;
    }

    /**
     * @param jPanel_grafica the jPanel_grafica to set
     */
    public void setjPanel_grafica(javax.swing.JPanel jPanel_grafica) {
        this.jPanel_grafica = jPanel_grafica;
    }

    /**
     * @return the jScrollBar1
     */
    public javax.swing.JScrollBar getjScrollBar1() {
        return jScrollBar1;
    }

    /**
     * @param jScrollBar1 the jScrollBar1 to set
     */
    public void setjScrollBar1(javax.swing.JScrollBar jScrollBar1) {
        this.jScrollBar1 = jScrollBar1;
    }

    /**
     * @return the jScrollPane1
     */
    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    /**
     * @param jScrollPane1 the jScrollPane1 to set
     */
    public void setjScrollPane1(javax.swing.JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    /**
     * @return the jScrollPane2
     */
    public javax.swing.JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    /**
     * @param jScrollPane2 the jScrollPane2 to set
     */
    public void setjScrollPane2(javax.swing.JScrollPane jScrollPane2) {
        this.jScrollPane2 = jScrollPane2;
    }

    /**
     * @return the jSpinner_NumGens
     */
    public javax.swing.JSpinner getjSpinner_NumGens() {
        return jSpinner_NumGens;
    }

    /**
     * @param jSpinner_NumGens the jSpinner_NumGens to set
     */
    public void setjSpinner_NumGens(javax.swing.JSpinner jSpinner_NumGens) {
        this.jSpinner_NumGens = jSpinner_NumGens;
    }

    /**
     * @return the jSpinner_ProbMuta
     */
    public javax.swing.JSpinner getjSpinner_ProbMuta() {
        return jSpinner_ProbMuta;
    }

    /**
     * @param jSpinner_ProbMuta the jSpinner_ProbMuta to set
     */
    public void setjSpinner_ProbMuta(javax.swing.JSpinner jSpinner_ProbMuta) {
        this.jSpinner_ProbMuta = jSpinner_ProbMuta;
    }

    /**
     * @return the jSpinner_TamPob
     */
    public javax.swing.JSpinner getjSpinner_TamPob() {
        return jSpinner_TamPob;
    }

    /**
     * @param jSpinner_TamPob the jSpinner_TamPob to set
     */
    public void setjSpinner_TamPob(javax.swing.JSpinner jSpinner_TamPob) {
        this.jSpinner_TamPob = jSpinner_TamPob;
    }

    /**
     * @return the jTextArea_Genotipo
     */
    public javax.swing.JTextArea getjTextArea_Genotipo() {
        return jTextArea_Genotipo;
    }

    /**
     * @param jTextArea_Genotipo the jTextArea_Genotipo to set
     */
    public void setjTextArea_Genotipo(javax.swing.JTextArea jTextArea_Genotipo) {
        this.jTextArea_Genotipo = jTextArea_Genotipo;
    }

    /**
     * @return the jLabel_3SAT
     */
    public JLabel getjLabel_3SAT() {
        return jLabel_3SAT;
    }

    /**
     * @param jLabel_3SAT the jLabel_3SAT to set
     */
    public void setjLabel_3SAT(JLabel jLabel_3SAT) {
        this.jLabel_3SAT = jLabel_3SAT;
    }

    /**
     * @return the jButton_3SAT
     */
    public JButton getjButton_3SAT() {
        return jButton_3SAT;
    }

    /**
     * @param jButton_3SAT the jButton_3SAT to set
     */
    public void setjButton_3SAT(JButton jButton_3SAT) {
        this.jButton_3SAT = jButton_3SAT;
    }

    /**
     * @return the jLabel_TCP
     */
    public JLabel getjLabel_TCP() {
        return jLabel_TCP;
    }

    /**
     * @param jLabel_TCP the jLabel_TCP to set
     */
    public void setjLabel_TCP(JLabel jLabel_TCP) {
        this.jLabel_TCP = jLabel_TCP;
    }

    /**
     * @return the jButton_TCP
     */
    public JButton getjButton_TCP() {
        return jButton_TCP;
    }

    /**
     * @param jButton_TCP the jButton_TCP to set
     */
    public void setjButton_TCP(JButton jButton_TCP) {
        this.jButton_TCP = jButton_TCP;
    }

    /**
     * @return the jSpinner_TCP
     */
    public JSpinner getjSpinner_TCP() {
        return jSpinner_TCP;
    }

    /**
     * @param jSpinner_TCP the jSpinner_TCP to set
     */
    public void setjSpinner_TCP(JSpinner jSpinner_TCP) {
        this.jSpinner_TCP = jSpinner_TCP;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
