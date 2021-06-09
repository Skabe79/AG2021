/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binario;

import java.util.ArrayList;

/**
 *
 * @author skabe
 */
public class GeneticoBinario {
    private int tamPoblacion;
    private int numGeneraciones;
    private double probMuta;
    private ArrayList<Individuo> poblacionActua;
    public GeneticoBinario(int tamPoblacion, int numGeneraciones, double probMuta) {
        this.tamPoblacion = tamPoblacion;
        this.numGeneraciones = numGeneraciones;
        this.probMuta = probMuta;
        poblacionActua= new ArrayList<>();
    }
    
    
    public void evolucionar(){
        //Proceso evolutivo que tiene relacion con el numero de generaciones
        generarPoblacionInicial();
        for(int g=1; g<this.numGeneraciones;g++){
            ArrayList<Individuo> nuevaPoblacion= new ArrayList<>();
            //Garantizar la generacion de una generacion nueva
            for(int i=0; i<this.tamPoblacion;i++){
                //Seleccion de una madre
                Individuo madre= Seleccion.seleccionAleatoria(this.getPoblacionActua());
                //seleccion de un padre
                Individuo padre= Seleccion.seleccionAleatoria(this.getPoblacionActua());
                //cruza que retorne el mejor individuo de la cruza
                int[] mascara = Herramientas.generarArregloBinarios(madre.getGenotipo().length);
                Individuo hijo= Cruza.cruzaPorMascaraBin(madre, padre, mascara);
                //Se aplica una muta evaluando una probabilidad
                if(generarProbabilidadMuta()){
                    Muta.mutaSimple(hijo);
                }
                nuevaPoblacion.add(hijo);
            }
            sustituirPoblacion(nuevaPoblacion);
        }
    }

    private void generarPoblacionInicial() {
        for(int i=0; i<this.tamPoblacion; i++){
            this.getPoblacionActua().add(new Individuo(8));
            
        }
    }

    private boolean generarProbabilidadMuta() {
        if(this.probMuta>Math.random()){
            return true;
        }
        return false;
    }

    private void sustituirPoblacion(ArrayList<Individuo> nuevaPob) {
        this.getPoblacionActua().clear();
        for(Individuo aux: nuevaPob){
            this.getPoblacionActua().add(new Individuo(aux));
        }
    }

    /**
     * @return the poblacionActua
     */
    public ArrayList<Individuo> getPoblacionActua() {
        return poblacionActua;
    }
}
