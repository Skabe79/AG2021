/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author skabe
 */
public class Persistente {
    public static void guardarDatos(GeneticoReina gRg){
        
        String ruta="Geneticos Reina/"+gRg.toString()+".gr";
        try {
            FileWriter file= new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(file);
            bw.write(gRg.getBits()+","+gRg.getTamanoPob());
            bw.newLine();
            for (int i = 0; i < gRg.getTamanoPob(); i++) {
                for (int j = 0; j < gRg.getBits(); j++) {
                    bw.write(gRg.getPoblacionActual().get(i).getGenotipo()[j]+",");
                }
                bw.newLine();
                
            }
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static GeneticoReina rescatarDatos(double probMuta, int numGen) {
        File archivo= seleccionar();
        int bits=0;
        int tampob=0;
        ArrayList<Reina> poblacionActual= new ArrayList<>();
        try {
            BufferedReader br= new BufferedReader(new FileReader(archivo));
            String linea= br.readLine();
            StringTokenizer tokens= new StringTokenizer(linea, ",");
            bits= Integer.parseInt(tokens.nextToken());
            tampob= Integer.parseInt(tokens.nextToken());
            int aux=tampob;
            while(aux!=0){
                tokens= new StringTokenizer(br.readLine(), ",");
                int [] geno= new int[bits];
                for (int i = 0; i < bits; i++) {
                    geno[i]=Integer.parseInt(tokens.nextToken());
                }
                poblacionActual.add(new Reina(geno));
                aux--;                
            }
            return new GeneticoReina(tampob, numGen, probMuta, bits, poblacionActual);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static File seleccionar() {
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.dir")));
        file.showOpenDialog(file);
        //abrimos el archivo seleccionado
        File abre = file.getSelectedFile();

        if (abre != null) {
            return abre;
        } else {
            JOptionPane.showMessageDialog(null,
                    "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }
}
