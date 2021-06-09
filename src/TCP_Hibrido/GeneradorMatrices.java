/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP_Hibrido;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import javafx.util.Pair;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jfree.ui.ExtensionFileFilter;

/**
 *
 * @author skabe
 */
public class GeneradorMatrices {
    public int[][] matrizPesos;
    public double[][] matrizInclinaciones;

    public GeneradorMatrices(int n, int rango) {
        matrizPesos=new int[n][n];
        matrizInclinaciones=new double[n][n];
        Random random= new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i==j){
                    matrizInclinaciones[i][j]=0;
                    matrizPesos[i][j]=0;
                }else{
                    int peso=random.nextInt(rango+1);
                    double inclinacion= random.nextDouble()*2;
                    matrizPesos[i][j]=peso;
                    matrizPesos[j][i]=peso;
                    matrizInclinaciones[i][j]=inclinacion;
                    matrizInclinaciones[j][i]=2-inclinacion;
                }
            }
        }
    }
    public static Pair<double[][],int [][]> abrirArchivos(){
        JFileChooser file = new JFileChooser();
        FileFilter file1= new ExtensionFileFilter("Matrices TCP_Mixto", "mtzm");
        file.addChoosableFileFilter(file1);
        file.setAcceptAllFileFilterUsed(false);
        file.showOpenDialog(null);
        File abre = file.getSelectedFile();
        double[][] inclinaciones;
        int[][] pesos;
        Pair<double[][],int [][]> result=new Pair(new double[1][1], new  int[1][1]);
        if (abre != null) {
            BufferedReader br = null;
            try {
                StringTokenizer tokenizer;
                String linea;
                br = new BufferedReader(new FileReader(abre));
                linea=br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                int tam= tokenizer.countTokens();
                inclinaciones= new double[tam][tam];
                pesos= new int[tam][tam];
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        pesos[i][j]=Integer.parseInt(tokenizer.nextToken());
                    }
                    linea=br.readLine();
                    tokenizer= new StringTokenizer(linea, ",");
                }
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        inclinaciones[i][j]= Double.parseDouble(tokenizer.nextToken());
                    }
                    if(i!=tam-1){
                        linea=br.readLine();
                        tokenizer=new StringTokenizer(linea, ",");
                    }
                }
                result=new Pair(inclinaciones,pesos);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }
    public void crearArchivos(){
        JFileChooser seleccion= new JFileChooser();
        seleccion.setDialogTitle("GUARDAR Algoritmo genetico ");
        seleccion.setDialogType(JFileChooser.SAVE_DIALOG);
        //seleccion.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FileFilter file1=null;
        seleccion.setAcceptAllFileFilterUsed(false);
        file1= new ExtensionFileFilter("Matrices para TCP_Mixtas", "mtzm");
        seleccion.setSelectedFile(new File("EjemploMatrizTCPM.mtzm"));
        seleccion.addChoosableFileFilter(file1);
        seleccion.showSaveDialog(null);
        File archivo= seleccion.getSelectedFile();
        if(archivo==null){
            return;
        }
        FileWriter file= null;
        try {
            file = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(file);
            String linea;
            for (int i = 0; i < matrizPesos.length; i++) {
                linea="";
                for (int j = 0; j < matrizPesos.length; j++) {
                    linea=linea+matrizPesos[i][j]+",";
                }
                bw.write(linea);
                bw.newLine();
            }
            for (int i = 0; i < matrizInclinaciones.length; i++) {
                linea="";
                for (int j = 0; j < matrizInclinaciones.length; j++) {
                    linea=linea+matrizInclinaciones[i][j]+",";
                }
                bw.write(linea);
                bw.newLine();
            }bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        GeneradorMatrices generador= new GeneradorMatrices(20, 100);
        generador.crearArchivos();
    }
    
}
