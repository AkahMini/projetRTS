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
import engine.process.MenuInterface;
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
	private MenuInterface menu;
	private PaintStrategy paintStrategy = new PaintStrategy();
	private MenuStrategy menuStrategy = new MenuStrategy();
	
	//Basically a matrix which indicate if the block is in vision or not
	boolean[][] visible =new boolean[100][72];

	//true if fog of war 
	boolean fogOfWar=false;
	
	public GameDisplay(Map map, MobileInterface manager, MenuInterface menu) {
		this.map = map;
		this.manager = manager;
		this.menu= menu;
	}
	
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//this is for the game display
		if(menu.getCurrentState().equals("PLAYING")) {

			if(fogOfWar) {
				//reset vision at each repaint
				//not optimal but simpler, may be changed if it feels akward visually
				for (int x = 0; x < 100; x++) {
					for (int y = 0; y < 72; y++) {
							visible[x][y] = false;
					}
				}
			}
			
			//define MobileElement we need to paint + do things
			ArrayList<Unit> units = new ArrayList<>(manager.getUnits());
			ArrayList<Unit> selectedUnits = new ArrayList<>(manager.getUnitsInSelectedArea());
			ArrayList<Unit> playerUnits = new ArrayList<>(manager.getPlayer().getCreatedUnits());
			
			ArrayList<Building> buildings = new ArrayList<>(manager.getBuildings());
			ArrayList<RessourceDeposit> deposits = new ArrayList<>(manager.getRessourceDeposit());
			ArrayList<Building> playerBuildings = new ArrayList<>(manager.getPlayer().getBuiltBuilding());
			
			if(fogOfWar) {
				for(Unit playerUnit: playerUnits) {
					revealAround(playerUnit.getPosition().getColumn(),playerUnit.getPosition().getLine(),playerUnit.getVision());
				}
				for(Building playerBuilding: playerBuildings) {
					revealAround(playerBuilding.getPosition().getColumn(),playerBuilding.getPosition().getLine(),playerBuilding.getVision());
					//System.out.println("nom= "+playerBuilding.getBuildingName()+" vision= "+playerBuilding.getVision());
				}
			}
			
			
			paintStrategy.paint(map, g);
			
			if(fogOfWar) {
				for (int x = 0; x < 100; x++) {
					for (int y = 0; y < 72; y++) {
						if(y>=7) {
							if(!visible[x][y]) {
								paintStrategy.paint(x,y,g);
							}
						}
					}
				}
			}
			
			paintStrategy.paint(manager.getPlayer(),g);
			paintStrategy.paint(manager.getHour(), manager.getMinute(), manager.getSecond(), g);
			
			
			if(manager.getNotification()!=null) {
				paintStrategy.paint(manager.getNotification(),manager.isNotificationGood(), g);
			}
			
			
			paintStrategy.paint(manager.getSelectedArea(), g);
			
			for (Building building : buildings) {
				if(fogOfWar) {
					if(visible[building.getPosition().getColumn()][building.getPosition().getLine()]) {
						paintStrategy.paint(building, g);
					}
				}else {
					paintStrategy.paint(building,g);
				}
	            if(building instanceof DefenseTower) {
	            	paintStrategy.paintAttack((DefenseTower)building, g);
	            }
	        }
			for (RessourceDeposit deposit: deposits) {
				if(fogOfWar) {
					if(visible[deposit.getPosition().getColumn()][deposit.getPosition().getLine()]) {
						paintStrategy.paint(deposit, g);
					}
				}else {
					paintStrategy.paint(deposit, g);
				}
			}
			for (Unit unit : units) {
				
				if(fogOfWar) {
					if(visible[unit.getPosition().getColumn()][unit.getPosition().getLine()]) {
						paintStrategy.paint(unit, g);
						if(unit instanceof Worker) {
							paintStrategy.paintWorkingWorker((Worker)unit, g);
						}
					}
				}else {
					paintStrategy.paint(unit, g);
					if(unit instanceof Worker) {
						paintStrategy.paintWorkingWorker((Worker)unit, g);
					}
				}

				//similar if of the calculDegats method in UnitManager
				if (unit.getIsInCombat() && unit.getAttackCounter()>=(Unit.getAttackTime())-10) {
					paintStrategy.paintAttack(unit, g);
				}
			}
			for(Unit selectedUnit: selectedUnits) {
				if(fogOfWar) {
					if(visible[selectedUnit.getPosition().getColumn()][selectedUnit.getPosition().getLine()]) {
						paintStrategy.paintSelectedUnit(selectedUnit, g);
					}
				}else {
					paintStrategy.paintSelectedUnit(selectedUnit,g);
				}
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
			
		//this is for menu display
		}else {
			if(menu.getCurrentState().equals("MENU")) {
				menuStrategy.paintMainMenu(g);
			}else if (menu.getCurrentState().equals("CHOOSE")) {
				menuStrategy.paintChooseMenu(menu, g);
			}else if (menu.getCurrentState().equals("END")) {
				menuStrategy.paintEndMenu(g);
			}
		}
	}
	
	public void resetManager(MobileInterface manager) {
		this.manager=manager;
	}
	
	/*
	 * used to turn true the blocks that the player should have vision
	 * we check if the values are all in the game grid
	 * 
	 * @param cx the position x of the object(blocks not absolute)
	 * @param cy the position y of the object(blocks not absolute)
	 * @param range the vision range of the object
	 */
	void revealAround(int cx, int cy, float frange) {
		int range= (int) frange;
	    for (int x = cx - range; x <= cx + range; x++) {
	    	if(x>=0 && x<100) {
		        for (int y = cy - range; y <= cy + range; y++) {
		        	if(y>=7 && y<72) {
			            int dx = x - cx;
			            int dy = y - cy;
			            
			            //euclidian norm elevated to the square
			            if (dx * dx + dy * dy <= range * range) {
			                visible[x][y] = true;
			            }
		        	}
		        }
	    	}
	    }
	}
	
}