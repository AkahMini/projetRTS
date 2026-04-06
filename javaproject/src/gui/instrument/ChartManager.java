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

    public ChartManager() {
        this.dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart("Unités sur le terrain","Type d'unité","Nombre",dataset,PlotOrientation.VERTICAL,true, true, false);
        this.chartPanel = new ChartPanel(chart);
    }

    /**
     * Update the chart with the new unitCount
     * Called every second
     */
    public void updateUnitChart(Player player) {
    	// Compter en dehors du thread Swing (pas de risque ici)
        ArrayList<Unit> units = player.getCreatedUnits();
        int infantryCount  = 0;
        int cavalryCount   = 0;
        int artilleryCount = 0;
        int workerCount    = 0;

        for (Unit u : units) {
            if (u instanceof Infantry)       infantryCount++;
            else if (u instanceof Cavalry)   cavalryCount++;
            else if (u instanceof Artillery) artilleryCount++;
            else if (u instanceof Worker)    workerCount++;
        }

        // Capture pour le lambda
        final int inf = infantryCount, cav = cavalryCount,
                  art = artilleryCount, wrk = workerCount;
        final String series = player.getPseudo();

        // Mise à jour du dataset uniquement dans le thread Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            dataset.clear();
            dataset.addValue(inf, series, "Infantry");
            dataset.addValue(cav, series, "Cavalry");
            dataset.addValue(art, series, "Artillery");
            dataset.addValue(wrk, series, "Worker");
        });
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }
    public JFreeChart getChart() {
        return chartPanel.getChart();
    }
}