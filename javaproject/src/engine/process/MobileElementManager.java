package engine.process;

import java.util.ArrayList;
import java.util.List;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.building.Building;
import engine.mobile.unit.Unit;
import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;


/**
 * 
 * @author LE RAY Yann
 *
 */

public class MobileElementManager implements MobileInterface {
	private Map map;
	
	private String selectedBuilding = null;
	
	private List<Building> buildings = new ArrayList<Building>();
	
	
	private String selectedUnit = null;
	
	private List<Unit> units = new ArrayList<Unit>();
	
	private List<Block> selectedArea;
	
	private Chronometer chronometer = new Chronometer();
	private CyclicCounter timetweaker = new CyclicCounter(0,100,0);

	public MobileElementManager(Map map) {
		this.map = map;
		chronometer.init();
	}

	public void nextRound() {
		timetweaker.increment();
		if(timetweaker.getValue()==100) {
			chronometer.increment();
			timetweaker.increment();
		}
		
	}
	
	//Build part
	public void selectBuilding(String type) {
		this.selectedBuilding = type;
		System.out.println("Mode construction : " + type);
	}

	public void buildBuilding(Block position) {
		if (selectedBuilding == null) {
			return;
		}

		String faction = "Zeus"; 
		int tier = 1;

		Building nouveauBatiment = BuildingFactory.createBuilding(selectedBuilding, tier, faction, position);

		if (nouveauBatiment != null) {
			buildings.add(nouveauBatiment);
		        System.out.println("Bâtiment posé en : " + position.getLine() + ", " + position.getColumn());
		    }
		selectedBuilding = null; 
		}
	
	
	//Unit part
	public void selectUnit(String type) {
		this.selectedUnit = type;
		System.out.println("Mode spawn : " + type);
	}
	
	public void spawnUnit(Block position) {
		if (selectedUnit == null) {
			return;
		}

		String faction = "Zeus"; 
		int tier = 1;

		Unit newUnit = UnitFactory.createUnit(selectedUnit, tier, faction, position);

		if (newUnit != null) {
			units.add(newUnit);
		        System.out.println("Unité posé en : " + position.getLine() + ", " + position.getColumn());
		    }
		selectedUnit = null; 
		}
	
	
	
	public void initSelectedArea(Block firstBlock) {
		this.selectedArea = new ArrayList();
		this.selectedArea.add(firstBlock);
	}
	
	public void calculateSelectedArea(Block lastBlock) {
		Block firstBlock = this.selectedArea.get(0);
		int firstLine = Math.min(firstBlock.getLine(), lastBlock.getLine());
		int firstColomn = Math.min(firstBlock.getColumn(),lastBlock.getColumn());
		int lastLine = Math.max(firstBlock.getLine(), lastBlock.getLine());
		int lastColomn = Math.max(firstBlock.getColumn(),lastBlock.getColumn());
		//System.out.println("("+firstLine+";"+firstColomn+")\n("+lastLine+";"+lastColomn+")\n");
		
		
		for(int lineIndex=firstLine;lineIndex<lastLine;lineIndex++) {
			for(int colomnIndex=firstColomn;colomnIndex<lastColomn;colomnIndex++) {
				this.selectedArea.add(map.getBlock(lineIndex, colomnIndex));
			}
		}
	}
	
	
	//Timer part
	public CyclicCounter getHour() {
		return chronometer.getHour();
	}
		
	public CyclicCounter getMinute() {
		return chronometer.getMinute();
	}
	
	public CyclicCounter getSecond() {
		return chronometer.getSecond();
	}
	

	private static int getRandomNumber(int min, int max) {
		return (int) (Math.random() * (max + 1 - min)) + min;
	}	
	public List<Building> getBuildings() {
        return buildings;
    }
	public List<Unit> getUnits() {
        return units;
    }
	
	public Block getMousePosition(int x, int y) {
		//converts (x,y) coordonates into the corresponding block
		int line = y / GameConfiguration.BLOCK_SIZE;
		int column = x / GameConfiguration.BLOCK_SIZE;
		return map.getBlock(line, column);
	}
	
	public List<Block> getSelectedArea() {
		return selectedArea;
	}
	
	
	
}