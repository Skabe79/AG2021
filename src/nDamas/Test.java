/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

/**
 *
 * @author carli
 */
public class Test {

    public static void main(String args[]) {
//        int[] gen = new int[]{3,7,1,4,6,2,0,5};
//        Reina r = new Reina(gen);
//        //Reina r2 = new Reina(8);
//        //int fitness = r.calculaAtaqueHor(r.getGenotipo()) + r.calculaAtaqueDiagonal1(r.getGenotipo()) + r.calculaAtaqueDiagonal2(r.getGenotipo());
//        //r.setFitness(fitness);
//        System.out.println(r.getFitness());
//        //System.out.println(r2.getFitness());

       GeneticoReina gb1 = new GeneticoReina(10, 100, 0.12,30);
       //GeneticoReina gb2 = new GeneticoReina(10, 100, 0.12,30);
       //GeneticoReina gb3 = new GeneticoReina(10, 100, 0.12,30);
       gb1.evolucionar(true);
       //gb2.evolucionar(true);
       //gb3.evolucionar(true);
       Grafica g1= new Grafica("Generacion", "Fitness", "N Damas - gb1");
       g1.crearGrafica();
       g1.agregarSerie("gb1", gb1.getFitgen());
       g1.muestraGrafica();
       System.out.println(Tools.mejorPoblacion(gb1.getPoblacionActual()).toString());
       Persistente.guardarDatos(gb1);
       GeneticoReina prueba= Persistente.rescatarDatos(0.12, 10000);
       prueba.evolucionar(true);
       Grafica pruebag= new Grafica("Generacion", "Fitness", "N Damas - prueba");
       pruebag.crearGrafica();
       pruebag.agregarSerie("prueba", prueba.getFitgen());
       pruebag.muestraGrafica();
       System.out.println(Tools.mejorPoblacion(prueba.getPoblacionActual()).toString());
       /*
      // System.out.println();
      Grafica g1= new Grafica("Generacion", "Fitness", "N Damas - gb1");
      Grafica g2= new Grafica("Generacion", "Fitness", "N Damas - gb2");
      Grafica g3= new Grafica("Generacion", "Fitness", "N Damas - gb3");
      g1.crearGrafica();
      g2.crearGrafica();
      g3.crearGrafica();
      g1.agregarSerie("gb1", gb1.getFitgen());
      g2.agregarSerie("gb2", gb2.getFitgen());
      g3.agregarSerie("gb3", gb3.getFitgen());
      g1.muestraGrafica();
      g2.muestraGrafica();
      g3.muestraGrafica();
      System.out.println(tools.mejorPoblacion(gb1.getPoblacionActual()).toString());
      System.out.println(tools.mejorPoblacion(gb2.getPoblacionActual()).toString());
      System.out.println(tools.mejorPoblacion(gb3.getPoblacionActual()).toString());*/
    }
}
