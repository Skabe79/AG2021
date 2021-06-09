/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jfree.ui.ExtensionFileFilter;

/**
 *
 * @author skabe
 */
public class AbrirMatriz {
    public static int[][] abrirMatriz(){
        JFileChooser file= new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileFilter filter= new ExtensionFileFilter("Matriz", "mtz");
        file.addChoosableFileFilter(filter);
        file.setAcceptAllFileFilterUsed(false);
        file.showOpenDialog(null);
        File abre= file.getSelectedFile();
        if(abre !=null){
            StringTokenizer tokenizer;
            String linea;
            try {
                BufferedReader br= new BufferedReader(new FileReader(abre));
                linea=br.readLine();
                tokenizer= new StringTokenizer(linea, ",");
                int lineas= tokenizer.countTokens();
                int matriz[][]=new int[lineas][lineas];
                for (int i = 0; i < lineas; i++) {
                    for (int j = 0; j < lineas; j++) {
                        matriz[i][j]= Integer.parseInt(tokenizer.nextToken());
                    }if(i!=lineas-1){
                        linea= br.readLine();
                        tokenizer= new StringTokenizer(linea, ",");
                    }
                }
                return matriz;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AbrirMatriz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AbrirMatriz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
