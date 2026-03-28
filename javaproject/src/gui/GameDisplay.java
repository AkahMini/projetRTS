package gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;


import engine.map.Map;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.DefenseTower;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.MobileInterface;

/**
 * 
 * Builder-like pattern class. Used to assemble the painting method from PaintStrategy for the MainGui.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class GameDisplay extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map map;
	private MobileInterface manager;
	private PaintStrategy paintStrategy = new PaintStrategy();
	private MenuStrategy menuStrategy = new MenuStrategy();

	public GameDisplay(Map map, MobileInterface manager) {
		this.map = map;
		this.manager = manager;
	}
	
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		paintStrategy.paint(map, g);
		
		paintStrategy.paint(manager.getHour(), manager.getMinute(), manager.getSecond(), g);
		paintStrategy.paint(manager.getPlayer(),g);
		
		if(manager.getNotification()!=null) {
			paintStrategy.paint(manager.getNotification(),manager.isNotificationGood(), g);
		}
		
		
		paintStrategy.paint(manager.getSelectedArea(), g);
		
		for (Building building : manager.getBuildings()) {
            paintStrategy.paint(building, g);
            if(building instanceof DefenseTower) {
            	paintStrategy.paintAttack((DefenseTower)building, g);
            }
        }
		for (RessourceDeposit deposit: manager.getRessourceDeposit()) {
			paintStrategy.paint(deposit, g);
		}
		for (Unit unit : new ArrayList<>(manager.getUnits())) {
			//We copy Unit list because it can be manipulated elsewhere while we iterate it
			paintStrategy.paint(unit, g);
			//similar if of the calculDegats method in UnitManager
			if (unit.getIsInCombat() && unit.getAttackCounter()>=(Unit.getAttackTime())-10) {
				paintStrategy.paintAttack(unit, g);
			}
			if(unit instanceof Worker) {
				paintStrategy.paintWorkingWorker((Worker)unit, g);
			}
		}
		for(Unit selectedUnit:manager.getUnitsInSelectedArea()) {
			paintStrategy.paintSelectedUnit(selectedUnit, g);
		}
		
		
		if(manager.getUnitsInSelectedArea()!=null && !manager.getUnitsInSelectedArea().isEmpty()) {
			paintStrategy.paintUnitInfo(manager.getUnitsInSelectedArea(), g);
		}
		if(manager.getSelectedBuild()!=null||manager.getSelectedWorker()!=null) {
			paintStrategy.paintSelectedInfo(manager, g);
		}
		if(manager.getTypeSelection()!=null) {
			paintStrategy.paintGrid(map, g);
		}
		
		if(manager.isGameStoped()) {
			menuStrategy.paintPauseMenu(g);
		}

	}	
}