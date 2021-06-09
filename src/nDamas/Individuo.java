/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

/**
 *
 * @author skabe
 */
public class Individuo {
    private int[] genotipo;
    private int fenotipo;
    private int fitness;
    private int[][] tablero;

    public Individuo(int[] genotipo) {
        this.genotipo = genotipo;
        this.tablero  = new int[genotipo.length][genotipo.length];
        calcularFeno();
        calcularFitness();
    }

    public Individuo(int n) {
        this.genotipo = genotipoAleatorio(n);
        this.tablero= new int[n][n];
        calcularFeno();
        calcularFitness();
    }

    private void calcularFeno() {
        for (int i = 0; i < this.genotipo.length; i++) {
            this.tablero[this.genotipo[i]][i]=1;
        }
        for (int i = 0; i < this.tablero.length; i++) {
            int cont=0;
            for (int j = 0; j < this.tablero[0].length; j++) {
                if(this.tablero[i][j]==1){
                    cont++;
                }
            }
            if(cont>0){
                this.fenotipo=this.fenotipo+(cont*(cont-1));    
            }
        }
        System.out.println(this.fenotipo+"");
    }

    private void calcularFitness() {
        for (int x = 0; x < genotipo.length; x++) {
        }
    }

    private int[] genotipoAleatorio(int n) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static void main(String[] args) {
        int[] genotipo= new int[]{2,2,2,4,5,5,7,7,8,0};
        Individuo ind= new Individuo(genotipo);
        
    }
}
