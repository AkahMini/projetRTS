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
    private  String seriesName = "";
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

        int inf = 0;
        int cav = 0;
        int art = 0;
        int wor = 0;
        seriesName = player.getPseudo();

        for (Unit u :new ArrayList<>(units)) {
            if (u instanceof Infantry)       
            	inf++;
            else if (u instanceof Cavalry)   
            	cav++;
            else if (u instanceof Artillery) 
            	art++;
            else if (u instanceof Worker)    
            	wor++;
        }
        synchronized(counts) {
            counts[0] = inf;
            counts[1] = cav;
            counts[2] = art;
            counts[3] = wor;
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

    public ChartPanel getChartPanel() {
        return chartPanel;
    }
    
}