/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nDamas;

import java.awt.Dimension;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



/**
 *
 * @author skabe
 */
public class Grafica {
    private JFreeChart grafica;
    private XYSeriesCollection series;
    private String ejeX, ejeY,titulo;

    public Grafica(String ejeX, String ejeY, String titulo) {
        this.grafica = null;
        this.series = new XYSeriesCollection();
        this.ejeX = ejeX;
        this.ejeY = ejeY;
        this.titulo = titulo;
    }
    public void agrearSerie(String nombre){
     XYSeries serie = new XYSeries(nombre);
     this.series.addSeries(serie);
    }
    public void agregarDatoASerie(String nombre, XYDataItem dato)   {
       this.series.getSeries(nombre).add(dato);
    }
    public void agregarSerie(String nombre, double[] datos){
    
        XYSeries serie = new XYSeries(nombre);
        // agregar cada uno de los datos en la serie 
        for (int x=0; x < datos.length;x++){
            serie.add(x, datos[x]);
        }
        // agregamos la serie que se generÃ³ 
        this.series.addSeries(serie);
     
    }
    public void agregarSerie(String nombre, ArrayList<Integer> datos)
    {
    
        XYSeries serie = new XYSeries(nombre);
        // agregar cada uno de los datos en la serie 
        for (int x=0; x < datos.size() ;x++)
        {
            serie.add(x, datos.get(x));
        }
        // agregamos la serie que se generÃ³
        
        this.series.addSeries(serie);
    }
    public void agregarSerieLong(String nombre, ArrayList<Long> datos)
    {
    
        XYSeries serie = new XYSeries(nombre);
        // agregar cada uno de los datos en la serie 
        for (int x=0; x < datos.size() ;x++)
        {
            serie.add(x, datos.get(x));
        }
        // agregamos la serie que se generÃ³
        
        this.series.addSeries(serie);
    }
    public void agregarSerieDouble(String nombre, ArrayList<Double> datos)
    {
    
        XYSeries serie = new XYSeries(nombre);
        // agregar cada uno de los datos en la serie 
        for (int x=0; x < datos.size() ;x++)
        {
            serie.add(x, datos.get(x));
        }
        // agregamos la serie que se generÃ³
        
        this.series.addSeries(serie);
    }
    public JFreeChart getGrafica(){
        return this.grafica;
    }
    
    public void crearGrafica(){
        this.grafica = ChartFactory.createXYLineChart(titulo, ejeX, ejeY, series,PlotOrientation.VERTICAL,true,true,true);
    }

    public void muestraGrafica(){
    
        ChartFrame frame = new ChartFrame("Histograma de color", grafica);
        frame.setVisible(true);
        frame.setSize(500,370);
        
    }
    public ChartPanel crearChartPanel(int w, int h)
    {
        ChartPanel chartpanel = new ChartPanel(grafica);
        chartpanel.setVisible(true);
        chartpanel.setSize(w, h);
        return chartpanel;
    }
    
    public ChartPanel crearChartPanel(Dimension d)
    {
        ChartPanel chartpanel = new ChartPanel(grafica);
        chartpanel.setVisible(true);
        chartpanel.setSize(d);
        return chartpanel;
    }
}
