package gui;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.building.Building;
import engine.mobile.unit.Unit;
import engine.process.MobileInterface;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * @author LE RAY Yann
 *
 */
public class GameDisplay extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map map;
	private MobileInterface manager;
	private PaintStrategy paintStrategy = new PaintStrategy();

	public GameDisplay(Map map, MobileInterface manager) {
		this.map = map;
		this.manager = manager;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		paintStrategy.paint(map, g);
		
		CyclicCounter hour = manager.getHour();
		CyclicCounter sinute = manager.getMinute();
		CyclicCounter second = manager.getSecond();
		paintStrategy.paint(hour, sinute, second, g);
		
		paintStrategy.paint(manager.getSelectedArea(), g);
		
		for (Building building : manager.getBuildings()) {
            paintStrategy.paint(building, g);
        }
		for(Unit unit : manager.getUnits()) {
			paintStrategy.paint(unit, g);
		}
		
		if(manager.getUnitsInSelectedArea()!=null && !manager.getUnitsInSelectedArea().isEmpty()) {
			paintStrategy.paintUnitInfo(manager.getUnitsInSelectedArea(), g);
		}
		
		
		

	}	
}