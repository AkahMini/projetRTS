package gui.instrument;

import engine.mobile.Player;
import engine.mobile.unit.Artillery;
import engine.mobile.unit.Cavalry;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;

public class ChartManager {

    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;
    
    //values of the chart
    private int[] counts = new int[4]; // [infantry, cavalry, artillery, worker]
    private  String seriesName = ""; //the name of the player related to the chart
    public ChartManager() {
        this.dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart("Unités sur le terrain","Type d'unité","Nombre",dataset,PlotOrientation.VERTICAL,true, true, false);
        this.chartPanel = new ChartPanel(chart);
    }

    
    public void updateUnitChart(Player player) {
    	/**
    	 * count the number of units of the player
    	 */
        ArrayList<Unit> units = new ArrayList<>(player.getCreatedUnits());

        int infantryCount = 0;
        int cavalryCount = 0;
        int artilleryCount = 0;
        int workerCount = 0;
        seriesName = player.getPseudo();

        for (Unit u :new ArrayList<>(units)) {
            if (u instanceof Infantry)       
            	infantryCount++;
            else if (u instanceof Cavalry)   
            	cavalryCount++;
            else if (u instanceof Artillery) 
            	artilleryCount++;
            else if (u instanceof Worker)    
            	workerCount++;
        }
        synchronized(counts) {
            counts[0] = infantryCount;
            counts[1] = cavalryCount;
            counts[2] = artilleryCount;
            counts[3] = workerCount;
        }
     
    }
    
    public JFreeChart getChart() {
        /**
         * construct the dataset
         */
        synchronized(counts) {
        	
            dataset.addValue(counts[0], seriesName, "Infantry");
            dataset.addValue(counts[1], seriesName, "Cavalry");
            dataset.addValue(counts[2], seriesName, "Artillery");
            dataset.addValue(counts[3], seriesName, "Worker");
            
        }
        return chartPanel.getChart();
    }
    public void refreshDataset() {
        synchronized(counts) {
            dataset.clear();
            dataset.addValue(counts[0], seriesName, "Infantry");
            dataset.addValue(counts[1], seriesName, "Cavalry");
            dataset.addValue(counts[2], seriesName, "Artillery");
            dataset.addValue(counts[3], seriesName, "Worker");
        }
    }
    public ChartPanel getChartPanel() {
        return chartPanel;
    }
    
}