package engine.process;

import java.util.ArrayList;
import config.DefaultGameSettings;
import config.GameConfiguration;

import engine.map.Block;
import engine.map.Map;
import engine.mobile.CPU;
import engine.mobile.MobileElement;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
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
    
	private Map map;
    
	
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private ArrayList<Unit> unitsInSelectedArea = new ArrayList<Unit>();
    private ArrayList<Building> buildingsInSelectedArea = new ArrayList<Building>();
    private ArrayList<RessourceDeposit> ressourceDeposits = new ArrayList<RessourceDeposit>();
    
    private Building selectedBuild =null;
    private Worker selectedWorker =null;
    private int selectedTier =0;//for worker button
    private String typeSelection =null; //I swear its the last attribute I add in this class
    
    private ArrayList<Unit> units = new ArrayList<Unit>();
    
    private ArrayList<Block> selectedArea;
    
    private Chronometer chronometer = new Chronometer();
    private CyclicCounter timetweaker = new CyclicCounter(0,68,0);

    private Player player;
    private CPU cpu;
    
    //this is for the two other manager, made separately for easier manipulation
    private BuildingInterface buildingManager;
    private UnitsInterface unitManager;
    private CPUManager cpuManager;
    
    public MobileElementManager(Map map, DefaultGameSettings gameSettings) {
        this.gameSettings=gameSettings;
    	this.map = map;
        this.player = new Player("Jhon Doe", "Zeus");
        this.cpu = new CPU("Ian", "Hades",5,5,5);
        chronometer.init();
        this.buildingManager = new BuildingManager(this);
        this.unitManager = new UnitsManager(this,this.gameSettings);
        this.cpuManager=new CPUManager(this);
    }

    public void firstRound() {    	
    	initMap();
    }

	
    public void nextRound() {
        timetweaker.increment();
        processBySeconds();
        unitCombatSystem();
        unitManager.moveAllUnits();
        buildingManager.allTowerAttack(buildings);
        cpuManager.attackReaction(cpu);
        cpuManager.workerManagement(cpu);
    }

	

	private void processBySeconds() {
		/*
		 * Every slow process that don't need to be check every tick, for performance purpose
		 */
		//chronometer update
		nextTierCheck(player);
		if(timetweaker.getValue() == 68) {
            chronometer.increment();
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
                    MobileElement target = unitManager.scanForEnemy(unit);
                    if (target != null) {
                    	unitManager.setCombatState(unit, target);
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
            for(Building tower: buildings) {
            	if(tower instanceof DefenseTower) {
            		buildingManager.setTowerTarget((DefenseTower) tower);
            	}
            }
        }
	}
    
	private void nextTierCheck(Player player){
		/*
		 * Next tier is reach when every building of the lower tiers are built
		 */
		int nbOfBuiltBuildings=player.getBuiltBuilding().size();
		if(player.getCurrentTier()==1) {
			if(nbOfBuiltBuildings==2) { //there are 2 tiers 1 buildings for all factions, not counting HQ
				player.setCurrentTier(2);
			}
		}
		else if(player.getCurrentTier()==5) { //there are three tiers 2 buildings and two tiers 1 buildings for all factions
			player.setCurrentTier(3);
		}
		
		
	}
	private void unitCombatSystem() {
		for(int i=0;i<units.size();i++) {
        	Unit unit=units.get(i);
        	if (unit.getTarget() != null && unit.getIsInCombat()) {
        		MobileElement target = unit.getTarget();

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
    	if(selectedWorker!=null){
    		if(y<=620) {
    			if(x<=1080) {
    				unitManager.workerConstructionction("button1",player,selectedWorker);
    			} else if (x>=1100 && x<=1160) {
    				unitManager.workerConstructionction("button2",player,selectedWorker);
    			} else if (x>=1180) {
    				unitManager.workerConstructionction("button3",player,selectedWorker);
    			}
    		} else if (y>=640) {
    			if(x<=1080) {
    				unitManager.workerConstructionction("button4",player,selectedWorker);
    			} else if (x>=1100 && x<=1160) {
    				unitManager.workerConstructionction("button5",player,selectedWorker);
    			} else if (x>=1180) {
    				unitManager.workerConstructionction("button6",player,selectedWorker);
    			}
    		}
    	}else if(selectedBuild!=null) {
    		if(y<=620) {
    			if(x<=1080) {
    				buildingManager.action("button1",player);
    			} else if (x>=1100 && x<=1160) {
    				buildingManager.action("button2",player);
    			} else if (x>=1180) {
    				buildingManager.action("button3",player);
    			}
    		} else if (y>=640) {
    			if(x<=1080) {
    				buildingManager.action("button4",player);
    			} else if (x>=1100 && x<=1160) {
    				buildingManager.action("button5",player);
    			} else if (x>=1180) {
    				buildingManager.action("button6",player);
    			}
    		}
    	}
    }
    private void initMap() {
		//We add player's HQ, & ressource deposits
    	Block playerHQposition = map.getBlock(10, 10); //TMP player's HQ
    	//Block playerTowerPosition = map.getBlock(20, 15);//TMP
    	Building playerHQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1, "Zeus", playerHQposition);
    	//Building playerTower = BuildingFactory.createBuilding(BuildingFactory.DEFENSE_BUILDING, 2, DefaultGameSettings.ZEUS, playerTowerPosition);
    	playerHQ.setUnderConstruction(false);
    	
    	Building ennemyHQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1, DefaultGameSettings.HADES, map.getBlock(63, 84));
    	Building ennemyTower1 = BuildingFactory.createBuilding(BuildingFactory.DEFENSE_BUILDING, 2, DefaultGameSettings.HADES, map.getBlock(47, 90));
    	Building ennemyTower2 = BuildingFactory.createBuilding(BuildingFactory.DEFENSE_BUILDING, 2, DefaultGameSettings.HADES, map.getBlock(55, 78));
    	
    	
    	//Block playerLaboPos = map.getBlock(15, 10);//tmp too
    	//Building playerLabo = BuildingFactory.createBuilding(BuildingFactory.RESEARCH_BUILDING,2,"Zeus",playerLaboPos);
    	
 
    	RessourceDeposit deposit1 = new RessourceDeposit(map.getBlock(30, 20),RessourceDeposit.FAITH);
    	RessourceDeposit deposit2 = new RessourceDeposit(map.getBlock(56,16),RessourceDeposit.FAITH);
    	RessourceDeposit deposit3 = new RessourceDeposit(map.getBlock(40,55),RessourceDeposit.FAITH);
    	RessourceDeposit deposit4 = new RessourceDeposit(map.getBlock(16,80),RessourceDeposit.AMBROSIA);
    	RessourceDeposit deposit5 = new RessourceDeposit(map.getBlock(65,63),RessourceDeposit.AMBROSIA);
    	ArrayList<Unit> ennemyTroups = new ArrayList<Unit>();
    	
    	for(int i=0;i<7;i++) {
    		Block spawnBlock = map.getBlock(11+(int)(Math.random()*5),28+(int)(Math.random()*5));
    		Unit unit=UnitFactory.createUnit(UnitFactory.INFANTRY_UNIT, 2, DefaultGameSettings.ZEUS, spawnBlock);
    		this.units.add(unit);
    		player.getCreatedUnits().add(unit);
    	}
    	for(int i=0;i<5;i++) {
    		Block spawnBlock = map.getBlock(19+(int)(Math.random()*5),21+(int)(Math.random()*5));
    		this.units.add(UnitFactory.createUnit(UnitFactory.ARTILLERY_UNIT, 1, DefaultGameSettings.ZEUS, spawnBlock));
    	}
    	for(int i=0;i<3;i++) {
    		Block spawnBlock = map.getBlock(22+(int)(Math.random()*5),7+(int)(Math.random()*5));
    		Unit unit=UnitFactory.createUnit(UnitFactory.CAVALRY_UNIT, 3, DefaultGameSettings.ZEUS, spawnBlock);
    		this.units.add(unit);
    		player.getCreatedUnits().add(unit);
    	}
    	
    	
    	for(int i=0;i<6;i++) {
    		Block spawnBlock = map.getBlock(50+(int)(Math.random()*5),84+(int)(Math.random()*5));
    		Unit unit=UnitFactory.createUnit(UnitFactory.INFANTRY_UNIT, 1, DefaultGameSettings.HADES, spawnBlock);
    		this.units.add(unit);
    		cpu.getCreatedUnits().add(unit);
    		System.out.println(cpu.getCreatedUnits());

    	}
    	for(int i=0;i<6;i++) {
    		Block spawnBlock = map.getBlock(53+(int)(Math.random()*5),65+(int)(Math.random()*5));
    		Unit unit=UnitFactory.createUnit(UnitFactory.ARTILLERY_UNIT, 3, DefaultGameSettings.HADES, spawnBlock);
    		this.units.add(unit);
    		cpu.getCreatedUnits().add(unit);
    		System.out.println(cpu.getCreatedUnits());

    	}
    	for(int i=0;i<3;i++) {
    		Block spawnBlock = map.getBlock(64+(int)(Math.random()*5),69+(int)(Math.random()*5));
    		Unit unit=UnitFactory.createUnit(UnitFactory.CAVALRY_UNIT, 3, DefaultGameSettings.HADES, spawnBlock);
    		this.units.add(unit);
    		cpu.getCreatedUnits().add(unit);
    		System.out.println(cpu.getCreatedUnits());

    	}
    	
    	
    	this.buildings.add(playerHQ);
    	//this.buildings.add(playerTower);
    	//this.buildings.add(playerLabo);
    	this.buildings.add(ennemyHQ);
    	cpu.getBuiltBuilding().add(ennemyHQ);
    	this.buildings.add(ennemyTower1); this.buildings.add(ennemyTower2);
    	
    	this.ressourceDeposits.add(deposit1);
    	this.ressourceDeposits.add(deposit2);
    	this.ressourceDeposits.add(deposit3);
    	this.ressourceDeposits.add(deposit4);
    	this.ressourceDeposits.add(deposit5);
	}
    
    public boolean ifBlockInGamePanel(Block block) {
    	if(block.getColumn()<=100 && block.getLine()>=7) {
    		return true;
    	}else {
    		return false;
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
    public void spawnUnit(Block position, String faction) {
        unitManager.spawnUnit(position, faction);
    }
    
    @Override
    public void buildBuilding(Block position,int tier,String faction, Player p) {
        buildingManager.buildBuilding(position, tier, faction, player);
    }

    @Override
    public void unitMoveOrder(Block destination) {
        unitManager.unitMoveOrder(destination,map);
    }

    @Override
    public void addQueue(UnitProducer building, Block position, String unitType, Player p) {
        buildingManager.addQueue(building, position, unitType,p);
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
        if(column>map.getColumnCount())
        	column=map.getColumnCount()-1;
        if(column<0)
        	column=0;
        if(line>map.getLineCount())
        	line=map.getLineCount()-1;
        if(line<0)
        	line=0;
        return map.getBlock(line, column);
    }
    
    public ArrayList<Block> getSelectedArea() {
        return selectedArea;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    //used for graphic interface
    public ArrayList<Unit> getUnitInSelectedArea(){
    	return this.unitsInSelectedArea;
    }
	public Building getSelectedBuild() {
		return selectedBuild;
	}

	public void setSelectedBuild(Building selectedBuild) {
		this.selectedBuild = selectedBuild;
	}
	
	public Worker getSelectedWorker() {
		return selectedWorker;
	}

	public void setSelectedWorker(Worker selectedWorker) {
		this.selectedWorker=selectedWorker;
	}

	public int getSelectedTier() {
		return selectedTier;
	}

	public void setSelectedTier(int selectedTier) {
		this.selectedTier = selectedTier;
	}

	public String getTypeSelection() {
		return typeSelection;
	}

	public void setTypeSelection(String typeSelection) {
		this.typeSelection = typeSelection;
	}
	
	public void addUnitsInSelectedArea(Unit unit) {
		this.unitsInSelectedArea.add(unit);
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public CPUManager getCpuManager() {
		return cpuManager;
	}

	public void setCpuManager(CPUManager cpuManager) {
		this.cpuManager = cpuManager;
	}
}