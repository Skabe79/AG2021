/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binario;

/**
 *
 * @author skabe
 */
public class Individuo {
    private int[] genotipo;
    private int fenotipo;
    private int fitness;
    
    public Individuo(int n){
        this.genotipo= Herramientas.generarArregloBinarios(n);
        calcularFitness();
    }
    public Individuo(int[] n){
        this.genotipo= n;
        calcularFitness();
    }
    
    public Individuo(Individuo i){
        this.genotipo=i.getGenotipo();
        this.fenotipo=i.getFenotipo();
        this.fitness=i.getFitness();
    }
    
    private void calcularFenotipo(){
        //Conversion entre la representacion del arreglo de enteros a un valor decimal
        int acu = 0;
        int s = 0;
        for(int x = genotipo.length - 1; x>=0; x--){
            if (genotipo[x]==1){
              acu +=Math.pow(2, s);
            }
            s++;
        }
        this.fenotipo = acu;
    }
    public void calcularFitness(){
        calcularFenotipo();
        //evaluar el fenotipo enb la funcio f(x)=x^2
        this.fitness= this.fenotipo*this.fenotipo;
    }
    /**
     * @return the genotipo
     */
    public int[] getGenotipo() {
        return genotipo;
    }

    /**
     * @return the fenotipo
     */
    public int getFenotipo() {
        return fenotipo;
    }

    /**
     * @return the firness
     */
    public int getFitness() {
        return fitness;
    }

    /**
     * @param genotipo the genotipo to set
     */
    public void setGenotipo(int[] genotipo) {
        this.genotipo = genotipo;
    }

    /**
     * @param fenotipo the fenotipo to set
     */
    public void setFenotipo(int fenotipo) {
        this.fenotipo = fenotipo;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        String aux= "Gen: "+this.genotipo.toString()+" Fen:"+this.fenotipo+" Fit:"+this.fitness; 
        return aux;
    }
    
}
