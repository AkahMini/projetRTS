package engine.process;

import java.util.ArrayList;
import java.util.HashMap;

import config.DefaultGameSettings;
import config.GameConfiguration;

import engine.map.Block;
import engine.map.Map;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.StatsLoader;
import engine.mobile.unit.Unit;
import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * Manager pattern class. This is the main manager that manage the whole engine.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class MobileElementManager implements MobileInterface {
    private DefaultGameSettings gameSettings;
    private HashMap<String,ArrayList<Float>> unitStats = StatsLoader.loadUnitStats();
    
	private Map map;
    
	
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private ArrayList<Unit> unitsInSelectedArea = new ArrayList<Unit>();
    private ArrayList<Building> buildingsInSelectedArea = new ArrayList<Building>();
    private ArrayList<RessourceDeposit> ressourceDeposits = new ArrayList<RessourceDeposit>();
    
    private Building selectedBuild =null;
    
    private ArrayList<Unit> units = new ArrayList<Unit>();
    
    private ArrayList<Block> selectedArea;
    
    private Chronometer chronometer = new Chronometer();
    private CyclicCounter timetweaker = new CyclicCounter(0,100,0);

    private Player player;
    
    //this is for the two other manager, maked separatly for easier manipulation
    private BuildingInterface buildingManager;
    private UnitsInterface unitManager;
    
    public MobileElementManager(Map map, DefaultGameSettings gameSettings) {
        this.gameSettings=gameSettings;
    	this.map = map;
        this.player = new Player("Jhon Doe", "Zeus");
        chronometer.init();
        this.buildingManager = new BuildingManager(this);
        this.unitManager = new UnitsManager(this,this.gameSettings);
    }

    public void firstRound() {
    	System.out.println("Affichage des statistiques des unités:");
    	StatsLoader.printUnitsValues(this.unitStats);
    	
    	
   
    	//We add player's HQ, & ressource deposits
    	Block playerHQposition = map.getBlock(10, 10); //TMP player's HQ
    	Building playerHQ = BuildingFactory.createBuilding("HQ", 1, "Zeus", playerHQposition);   	
    	
    	Block faithDepositLocation = map.getBlock(30, 20);//TMP
    	RessourceDeposit deposit1 = new RessourceDeposit(faithDepositLocation,RessourceDeposit.FAITH);
    	
    	Block ambroiseDepositLocation = map.getBlock(10, 35);
    	RessourceDeposit deposit2= new RessourceDeposit(ambroiseDepositLocation,RessourceDeposit.AMBROSIA);
    	
    	this.buildings.add(playerHQ);
    	this.ressourceDeposits.add(deposit1);
    	this.ressourceDeposits.add(deposit2);
    	
    }
    
    public void nextRound() {
        timetweaker.increment();
        if(timetweaker.getValue() == 100) {
            chronometer.increment();
            timetweaker.increment();
            //Units manager
            for(int i=0;i<units.size();i++) {
            	Unit unit=units.get(i);
            	
            	if(unit.getHp()<=0) {
            		System.out.println("Unit '"+unit.getUnitName()+"' removed");
            		units.remove(i);
            		
            		i--;
            		continue;
            	}
                // Ennemy scan
                if (unit.getTarget() == null && unit.getDestination() == null ) {
                    Unit enemy = unitManager.scanForEnemy(unit);
                    if (enemy != null) {
                    	unitManager.combatSystem(unit, enemy);
                    }
                }
            }
            
            // Buildings management
            for(Building building : buildings) {
            	buildingManager.reduceConstructionTime(building);
            	if(building instanceof UnitProducer) {
            		UnitProducer producer = (UnitProducer) building;
            		buildingManager.removeQueue(producer);
            	}else if (building instanceof HQ) {
            		HQ hQ = (HQ) building;
            		buildingManager.removeQueue(hQ.getWorkerProducer());
            	}
            }
        }
        for(int i=0;i<units.size();i++) {
        	Unit unit=units.get(i);
        	// 3. Combat
        	if (unit.getTarget() != null && unit.getIsInCombat()) {
        		Unit target = (Unit) unit.getTarget();

        		if (target.getHp() <= 0) {
        			unit.setTarget(null);
        			unit.setIsInCombat(false);
        		} else {
        			double distance = getDistance(unit.getPosition(), target.getPosition());

        			if (distance <= unit.getATKRange()) {
        				unit.setDestination(null);
        				unitManager.damageCalculation(unit);
        			}else {
        				// if not in range, we pursue
        				unit.setDestination(target.getPosition());
        			}
        		}
        	}
        }
        unitManager.moveAllUnits();
    }
    
     
    
    public double getDistance(Block b1, Block b2) {
        // Chebyshev distance to avoid issue with diagonal calculation
        int dx = Math.abs(b1.getColumn() - b2.getColumn());
        int dy = Math.abs(b1.getLine() - b2.getLine());
        return Math.max(dx, dy);
    }

    
    public void initSelectedArea(Block firstBlock) {
        this.selectedArea = new ArrayList<Block>();
        this.selectedArea.add(firstBlock);
    }
    
    public void calculateSelectedArea(Block lastBlock) {
        Block firstBlock = this.selectedArea.get(0);
        
        int x1 = firstBlock.getLine();
        int y1 = firstBlock.getColumn();
        int x2 = lastBlock.getLine();
        int y2 = lastBlock.getColumn();
        
        if (x1 > x2) { int tmp = x1; x1 = x2; x2 = tmp; }
        if (y1 > y2) { int tmp = y1; y1 = y2; y2 = tmp; }

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                this.selectedArea.add(map.getBlock(x, y));
            }
        }
        unitManager.unitsInSelectedArea();
        buildingManager.buildingsInSelectedArea();
    }
    

    
    public int isBlockCollider(Block block) {
        int nbOfUnits = this.units.size();
        int nbOfBats = this.buildings.size();
        for(int j = 0; j < nbOfBats; j++) {
            if(this.buildings.get(j).getPosition() == block){
                return 1;
            }
        }
        for(int i = 0; i < nbOfUnits; i++) {
            if(this.units.get(i).getPosition() == block){
                return 1;
            }
        }
        return 0;
    }
    
    //Gui call this to tell the manager that the player clicked in the selection button area
    //2 line 3 column
    //we indicate if its in the first or second line then for the col
    // do nothing if in the white space
    public void areaButtonPressed(int x, int y) {
    	if(selectedBuild!=null) {
    		if(y<=620) {
    			if(x<=1080) {
    				buildingManager.action("button1");
    				
    			} else if (x>=1100 && x<=1160) {
    				buildingManager.action("button2");
    			} else if (x>=1180) {
    				buildingManager.action("button3");
    			}
    		} else if (y>=640) {
    			if(x<=1080) {
    				buildingManager.action("button4");
    			} else if (x>=1100 && x<=1160) {
    				buildingManager.action("button5");
    			} else if (x>=1180) {
    				buildingManager.action("button6");
    			}
    		}
    	}
    }
    
    
    
    //method for the communation between this class and BuildingManager
    
    public void addInBuildings(Building n) {
    	this.buildings.add(n);
    }
    public void addInUnits(Unit u) {
    	this.units.add(u);
    }
    
    public Map getMap() {
    	return this.map;
    }
    public ArrayList<Building> getBuildings() {
        return buildings;
    }
    
    public ArrayList<RessourceDeposit> getRessourceDeposit(){
    	return this.ressourceDeposits;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Unit> getUnitsInSelectedArea(){
        return unitsInSelectedArea;
    }
    
    public ArrayList<Building> getBuildingsInSelectedArea(){
    	return buildingsInSelectedArea;
    }
    
    @Override
    public void selectUnit(String type) {
        unitManager.selectUnit(type);
    }

    @Override
    public void selectBuilding(String type) {
        buildingManager.selectBuilding(type);
    }

    @Override
    public void spawnUnit(Block spawnBlock) {
        unitManager.spawnUnit(spawnBlock);
    }
    
    @Override
    public void spawnUnitEnnemy(Block position) {
        unitManager.spawnUnitEnnemy(position);
    }

    @Override
    public void buildBuilding(Block position,int tier,String faction) {
        buildingManager.buildBuilding(position, tier, faction);
    }

    @Override
    public void unitMoveOrder(Block destination) {
        unitManager.unitMoveOrder(destination);
    }

    @Override
    public void addQueue(UnitProducer building, Block position, String unitType) {
        buildingManager.addQueue(building, position, unitType);
    }
    
    // --- Timer part ---
    public CyclicCounter getHour() {
        return chronometer.getHour();
    }
        
    public CyclicCounter getMinute() {
        return chronometer.getMinute();
    }
    
    public CyclicCounter getSecond() {
        return chronometer.getSecond();
    }
    
    //other method
    public Block getMousePosition(int x, int y) {
        int line = x / GameConfiguration.BLOCK_SIZE;
        int column = y / GameConfiguration.BLOCK_SIZE;
        return map.getBlock(line, column);
    }
    
    public ArrayList<Block> getSelectedArea() {
        return selectedArea;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    public HashMap<String,ArrayList<Float>> getUnitStats(){
    	return this.unitStats;
    }

    
    //used for graphic interface
	public Building getSelectedBuild() {
		return selectedBuild;
	}

	public void setSelectedBuild(Building selectedBuild) {
		this.selectedBuild = selectedBuild;
	}
}