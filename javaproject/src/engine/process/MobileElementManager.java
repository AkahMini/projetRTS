package engine.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

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
import engine.mobile.building.PopulationBuilding;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;
import gui.MainGUI;
import log.LoggerUtility;

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
	private static Logger logger = LoggerUtility.getLogger(MobileElementManager.class, "html");
	private Map map;
    int mode;//0 for 1V1 1 for 1V1V1
	
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private ArrayList<Unit> unitsInSelectedArea = new ArrayList<Unit>();
    private ArrayList<Building> buildingsInSelectedArea = new ArrayList<Building>();
    private ArrayList<RessourceDeposit> ressourceDeposits = new ArrayList<RessourceDeposit>();
    
    private Building selectedBuild =null;
    private Worker selectedWorker =null;
    private int selectedTier =0;//for worker button
    private String typeSelection =null;
    private String notification =null;
    private boolean isNotificationGood=false;
    private boolean isGameStoped = false;
    
    private ArrayList<Unit> units = new ArrayList<Unit>();
    
    private ArrayList<Block> selectedArea;
    
    private Chronometer chronometer = new Chronometer();
    private CyclicCounter timetweaker = new CyclicCounter(0,68,0);

    private Player player;
    private CPU cpu1;
    private CPU cpu2;
    
    //this is for the two other manager, made separately for easier manipulation
    private BuildingInterface buildingManager;
    private UnitsInterface unitManager;
    private CPUManager cpuManager1;
    private CPUManager cpuManager2;
    
    public MobileElementManager(Map map, DefaultGameSettings gameSettings,String faction, int mode) {
        this.gameSettings=gameSettings;
    	this.map = map;
        this.player = new Player("Jhon Doe", faction);
        this.mode =mode;
        
        List<String> factions = new ArrayList<>(Arrays.asList(DefaultGameSettings.ZEUS, DefaultGameSettings.HADES,DefaultGameSettings.POSEIDON));
        factions.remove(faction);
        Random random = new Random();
        String cpuFaction = factions.get(random.nextInt(factions.size()));
        
        System.out.println("cpu1 "+cpuFaction);
        this.cpu1 = new CPU("Ian", cpuFaction,5,5,5);
        if(mode==1) {
        	factions.remove(cpuFaction);
        	String cpuFaction2=factions.get(0);
        	this.cpu2 = new CPU("RP", cpuFaction2,5,5,5);
        	System.out.println("cpu2 "+cpuFaction2);
        }
        chronometer.init();
        this.buildingManager = new BuildingManager(this);
        this.unitManager = new UnitsManager(this,this.gameSettings);
        this.cpuManager1=new CPUManager(this);
        if(mode==1) {
        	this.cpuManager2=new CPUManager(this);
        }
    }

    public void firstRound() {    	
    	initMap();
    	logger.info("Map initialized");
    }

	
    public void nextRound() {
        timetweaker.increment();
        processBySeconds();
        unitCombatSystem();
        
        unitManager.moveAllUnits(player);
        unitManager.moveAllUnits(cpu1);
        if(mode==1) {
        	unitManager.moveAllUnits(cpu2);
        }
        
        killUnits(player);
        killUnits(cpu1);
        if(mode==1) {
        	killUnits(cpu2);
        }
        
        buildingManager.allTowerAttack(buildings);
       	if(timetweaker.getValue() == 5) {
       		cpuManager1.attackReaction(cpu1);
            cpuManager1.workerManagement(cpu1);
            if(mode==1) {
            	cpuManager2.attackReaction(cpu2);
                cpuManager2.workerManagement(cpu2);
            }
       	}
       	if(timetweaker.getValue() == 10) {
       		cpuManager1.buildManagement(cpu1);
            cpuManager1.otherBuildingsManagement(cpu1);
            cpuManager1.militaryProductionManagement(cpu1);
            if(mode==1) {
            	cpuManager2.buildManagement(cpu2);
                cpuManager2.otherBuildingsManagement(cpu2);
                cpuManager2.militaryProductionManagement(cpu2);
            }
       	}

        
    }

	

	private void processBySeconds() {
		/*
		 * Every slow process that don't need to be check every tick, for performance purpose
		 */
		//chronometer update
		if(timetweaker.getValue() == 64) {
            chronometer.increment();
            nextTierCheck(player);
    		nextTierCheck(cpu1);
    		if(mode==1) {
    			nextTierCheck(cpu2);
    		}
            //Units manager
            for(Unit unit: new ArrayList<>(units)) {
                
            	// Ennemy scan
                if (unit.getTarget() == null ) {
                    MobileElement target = unitManager.scanForEnemy(unit);
                    if (target != null) {
                    	unitManager.setCombatState(unit, target);
                    }
                }
            }
                        
            // Buildings management
            for(Building building : new ArrayList<>(buildings)) {
            	if(building.getHp()<=0) {
            		buildings.remove(building);
            	}
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
            if(cpu1.getCreatedUnits().size()>40) {
            	cpuManager1.attack(cpu1);
            }
            cpuManager1.workerProductionManagement(cpu1);
            buildingManager.researchTime(cpu1);
            
            if(mode==1) {
            	if(cpu1.getCreatedUnits().size()>40) {
                	cpuManager2.attack(cpu2);
                }
                cpuManager2.workerProductionManagement(cpu2);
                buildingManager.researchTime(cpu2);
            }
            buildingManager.researchTime(player);
        }
	}

	
    
	private void nextTierCheck(Player player){

		if(player.getCurrentTier() == 1) {
			int nbProducers = countBuildingTypeByTier(player, UnitProducer.class, 1);
			int nbPop = countBuildingTypeByTier(player, PopulationBuilding.class, 1);

			if(nbProducers >= 1 && nbPop >= 1) { 
				player.setCurrentTier(2);
				setNotifText("passage tier 2", true);
				System.out.println(player.getFactionName() + " passe tier 2");
			}
		}

		else if(player.getCurrentTier() == 2) {
			int nbLabos = countBuildingTypeByTier(player, ResearchBuilding.class, 2);
			int nbTowers = countBuildingTypeByTier(player, DefenseTower.class, 2);
			int nbProducersT2 = countBuildingTypeByTier(player, UnitProducer.class, 2);

			if(nbLabos >= 1 && nbTowers >= 1 && nbProducersT2 >= 1) {
				player.setCurrentTier(3);
				setNotifText("passage tier 3", true);
				System.out.println(player.getFactionName() + " passe tier 3");
			}
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
	
	private void killUnits(Player p) {
        Iterator<Unit> it = p.getCreatedUnits().iterator(); 
        while(it.hasNext()) {
            Unit u = it.next();
            if(u.getHp() <= 0) {
                it.remove();       
                units.remove(u);
                p.setCurrentPopulation(p.getCurrentPopulation()-u.getPopCost());
            }
        }
    }
	public String winningFaction(){
		/**
		 * return the name of the only faction that have active buildings in the map, return "null" otherwise
		 */
		String winingfaction="null";
		boolean activeZEUS = false;
		boolean activeHADES = false;
		boolean activePOSEIDON = false;
		
		for(Building building: buildings) {
			//checks if faction still possess buildings
			if(building.getFaction().equals(DefaultGameSettings.ZEUS)) {
				activeZEUS=true;
			}
			if(building.getFaction().equals(DefaultGameSettings.POSEIDON)) {
				activePOSEIDON=true;
			}
			if(building.getFaction().equals(DefaultGameSettings.HADES)) {
				activeHADES=true;
			}
		}
		
		if(activeZEUS&&!(activeHADES||activePOSEIDON)) {
			logger.info("ZEUS faction won");
			return gameSettings.ZEUS;
		}
		if(activePOSEIDON&&!(activeZEUS||activeHADES)) {
			logger.info("POSEIDON faction won");
			return gameSettings.POSEIDON;
		}
		if(activeHADES&&!(activeZEUS||activePOSEIDON)) {
			logger.info("HADES faction won");
			return gameSettings.HADES;
		}
		return winingfaction;
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
        this.selectedArea= new ArrayList<Block>();//Reset the selected area
        
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
    	/*
    	 * Create clickable area where button are drawn in bottom right part of the screen
    	 */
    	
    	if(selectedWorker!=null){
    		if(y<=620) {
    			if(x<=1080) {
    				unitManager.workerConstruction("button1",player,selectedWorker);
    			} else if (x>=1100 && x<=1160) {
    				unitManager.workerConstruction("button2",player,selectedWorker);
    			} else if (x>=1180) {
    				unitManager.workerConstruction("button3",player,selectedWorker);
    			}
    		} else if (y>=640) {
    			if(x<=1080) {
    				unitManager.workerConstruction("button4",player,selectedWorker);
    			} else if (x>=1100 && x<=1160) {
    				unitManager.workerConstruction("button5",player,selectedWorker);
    			} else if (x>=1180) {
    				unitManager.workerConstruction("button6",player,selectedWorker);
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
    	if(mode==0) {
	        Block playerHQposition = map.getBlock(18, 18);
	        Building playerHQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1,player.getFactionName(), playerHQposition);
	        playerHQ.setUnderConstruction(false);
	        player.getBuiltBuilding().add(playerHQ);
	        
	        Block ennemyHQposition = map.getBlock(60, 82);
	        Building ennemyHQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1, cpu1.getFactionName(), ennemyHQposition);
	        ennemyHQ.setUnderConstruction(false);
	        cpu1.getBuiltBuilding().add(ennemyHQ);
	
	        ArrayList<RessourceDeposit> allDeposits = new ArrayList<>();
	
	        // Joueur
	        allDeposits.add(new RessourceDeposit(map.getBlock(12, 18), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(14, 13), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(18, 12), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(22, 13), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(24, 18), RessourceDeposit.AMBROSIA));
	
	        // CPU
	        allDeposits.add(new RessourceDeposit(map.getBlock(66, 82), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(64, 87), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(60, 88), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(56, 87), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(54, 82), RessourceDeposit.AMBROSIA));
	
	        // Joueur B2 (Centre-gauche)
	        allDeposits.add(new RessourceDeposit(map.getBlock(31, 23), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(33, 28), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(37, 28), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(39, 23), RessourceDeposit.FAITH));
	
	        // CPU B2 (Centre-droite)
	        allDeposits.add(new RessourceDeposit(map.getBlock(35, 77), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(37, 72), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(40, 72), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(44, 77), RessourceDeposit.FAITH));
	
	        // Joueur B3 (Bottom-Left)
	        allDeposits.add(new RessourceDeposit(map.getBlock(48, 18), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(49, 23), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(52, 23), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(51, 18), RessourceDeposit.FAITH));
	
	        // CPU B3 (Top-Right)
	        allDeposits.add(new RessourceDeposit(map.getBlock(18, 82), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(20, 77), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(24, 77), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(22, 82), RessourceDeposit.FAITH));
	
	        // along the center diagonal
	        allDeposits.add(new RessourceDeposit(map.getBlock(17, 48), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(18, 52), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(22, 52), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(23, 48), RessourceDeposit.FAITH));
	
	        allDeposits.add(new RessourceDeposit(map.getBlock(52, 48), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(53, 52), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(57, 52), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(58, 48), RessourceDeposit.AMBROSIA));
	
	        for (RessourceDeposit d : allDeposits) {
	            d.setMaxWorkers(2); 
	            d.setCurrentWorkers(0);
	            this.ressourceDeposits.add(d);
	        }
	        /*
	        for(int i=0; i<7; i++) {
	            Block spawnBlock = map.getBlock(22+(int)(Math.random()*3), 22+(int)(Math.random()*3));
	            Unit unit = UnitFactory.createUnit(UnitFactory.ARTILLERY_UNIT, 1, DefaultGameSettings.ZEUS, spawnBlock);
	            this.units.add(unit);
	            player.getCreatedUnits().add(unit);
	        }
	        /*
	        for(int i=0; i<5; i++) {
	            Block spawnBlock = map.getBlock(54+(int)(Math.random()*3), 75+(int)(Math.random()*3));
	            Unit unit = UnitFactory.createUnit(UnitFactory.INFANTRY_UNIT, 1, DefaultGameSettings.HADES, spawnBlock);
	            this.units.add(unit);
	            cpu.getCreatedUnits().add(unit);
	        }
	        */
	        Unit w1 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1, cpu1.getFactionName(), ennemyHQ.getPosition());
	        Unit w2 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1,cpu1.getFactionName(), ennemyHQ.getPosition());
	        ((Worker) w1).setCurrentHQ((HQ) ennemyHQ);
	        ((Worker) w2).setCurrentHQ((HQ) ennemyHQ);
	        this.units.add(w1); 
	        cpu1.getCreatedUnits().add(w1);
	        this.units.add(w2); 
	        cpu1.getCreatedUnits().add(w2);
	        cpu1.setCurrentPopulation(2);
	
	        Unit w3 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1, player.getFactionName(), playerHQ.getPosition());
	        Unit w4 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1,player.getFactionName(), playerHQ.getPosition());
	        ((Worker) w3).setCurrentHQ((HQ) playerHQ);
	        ((Worker) w4).setCurrentHQ((HQ) playerHQ);
	        this.units.add(w3); 
	        player.getCreatedUnits().add(w3);
	        this.units.add(w4); 
	        player.getCreatedUnits().add(w4);
	        player.setCurrentPopulation(2);
	        
	        this.buildings.add(playerHQ);
	        this.buildings.add(ennemyHQ);
	        System.out.println("Vision HQ: " + playerHQ.getVision());
    	
    	}else if(mode==1) {
    		
    		Block playerHQposition = map.getBlock(12, 50);
	        Building playerHQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1,player.getFactionName(), playerHQposition);
	        playerHQ.setUnderConstruction(false);
	        player.getBuiltBuilding().add(playerHQ);
	        
	        Block ennemyHQposition = map.getBlock(60, 10);
	        Building ennemyHQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1, cpu1.getFactionName(), ennemyHQposition);
	        ennemyHQ.setUnderConstruction(false);
	        cpu1.getBuiltBuilding().add(ennemyHQ);
	
	        Block ennemy2HQposition = map.getBlock(64, 80);
	        Building ennemy2HQ = BuildingFactory.createBuilding(BuildingFactory.HQ_BUILDING, 1, cpu2.getFactionName(), ennemy2HQposition);
	        ennemy2HQ.setUnderConstruction(false);
	        cpu1.getBuiltBuilding().add(ennemy2HQ);
	        
	        ArrayList<RessourceDeposit> allDeposits = new ArrayList<>();
	        
	        //player
	        allDeposits.add(new RessourceDeposit(map.getBlock(12, 44), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(12, 56), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(8, 47), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(8, 54), RessourceDeposit.AMBROSIA));
	        
	        //cpu1
	        allDeposits.add(new RessourceDeposit(map.getBlock(56, 10), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(64, 10), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(58, 5), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(62, 5), RessourceDeposit.AMBROSIA));
	        
	        //cpu2
	        allDeposits.add(new RessourceDeposit(map.getBlock(66, 75), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(69, 80), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(66, 86), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(61, 85), RessourceDeposit.AMBROSIA));
	        
	        //other
	        allDeposits.add(new RessourceDeposit(map.getBlock(24, 30), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(26, 24), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(32, 26), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(32, 31), RessourceDeposit.AMBROSIA));
	       
	        allDeposits.add(new RessourceDeposit(map.getBlock(25, 65), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(27, 68), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(32, 67), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(33, 61), RessourceDeposit.AMBROSIA));
	        
	        allDeposits.add(new RessourceDeposit(map.getBlock(54, 50), RessourceDeposit.AMBROSIA));
	        allDeposits.add(new RessourceDeposit(map.getBlock(58, 49), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(58, 44), RessourceDeposit.FAITH));
	        allDeposits.add(new RessourceDeposit(map.getBlock(52, 40), RessourceDeposit.AMBROSIA));
	        
	        for (RessourceDeposit d : allDeposits) {
	            d.setMaxWorkers(2); 
	            d.setCurrentWorkers(0);
	            this.ressourceDeposits.add(d);
	        }
	        
	        Unit w1 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1, cpu1.getFactionName(), ennemyHQ.getPosition());
	        Unit w2 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1,cpu1.getFactionName(), ennemyHQ.getPosition());
	        ((Worker) w1).setCurrentHQ((HQ) ennemyHQ);
	        ((Worker) w2).setCurrentHQ((HQ) ennemyHQ);
	        this.units.add(w1); 
	        cpu1.getCreatedUnits().add(w1);
	        this.units.add(w2); 
	        cpu1.getCreatedUnits().add(w2);
	        cpu1.setCurrentPopulation(2);
	
	        Unit w3 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1, player.getFactionName(), playerHQ.getPosition());
	        Unit w4 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1,player.getFactionName(), playerHQ.getPosition());
	        ((Worker) w3).setCurrentHQ((HQ) playerHQ);
	        ((Worker) w4).setCurrentHQ((HQ) playerHQ);
	        this.units.add(w3); 
	        player.getCreatedUnits().add(w3);
	        this.units.add(w4); 
	        player.getCreatedUnits().add(w4);
	        player.setCurrentPopulation(2);
	        
	        Unit w5 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1, cpu2.getFactionName(), ennemy2HQ.getPosition());
	        Unit w6 = UnitFactory.createUnit(UnitFactory.WORKER_UNIT, 1,cpu2.getFactionName(), ennemy2HQ.getPosition());
	        ((Worker) w5).setCurrentHQ((HQ) ennemy2HQ);
	        ((Worker) w6).setCurrentHQ((HQ) ennemy2HQ);
	        this.units.add(w5); 
	        cpu2.getCreatedUnits().add(w5);
	        this.units.add(w6); 
	        cpu2.getCreatedUnits().add(w6);
	        cpu2.setCurrentPopulation(2);
	        
	        this.buildings.add(playerHQ);
	        this.buildings.add(ennemyHQ);
	        this.buildings.add(ennemy2HQ);
	        System.out.println("Vision HQ: " + playerHQ.getVision());
    	}
    }
    
    public void motherload() {
    	/**
    	 * Set infinite ressources for the player
    	 */
    	player.setAmbroisieStock(999999);
    	player.setFaithStock(999999);
    	player.setMaxPopulation(999999);
    	setNotifText("Motherload activated", true);
    }
    public void cpuLoad() {
    	/**
    	 * Set infinite ressources for the player
    	 */
    	cpu1.setAmbroisieStock(999999);
    	cpu1.setFaithStock(999999);
    	if(mode==1) {
    		cpu2.setAmbroisieStock(999999);
        	cpu2.setFaithStock(999999);
    	}
    	setNotifText("cpuLoad activated", true);
    }
    
    public boolean ifBlockInGamePanel(Block block) {
    	if(block.getColumn()<=100 && block.getLine()>=7) {
    		return true;
    	}else {
    		return false;
    	}
    }
    /**
     * This method is used to count the number of instance of a class (used in militaryProductionManagement)
     * 
     * @param p the current player or cpu of the Game
     * @param clazz the instance of which class we want to count
     * @return
     */
    public int countBuildingType(Player p, Class<?> clazz) {
        int count = 0;
        synchronized(p.getBuiltBuilding()) { 
            for (Building b : p.getBuiltBuilding()) {
                if (clazz.isInstance(b)) {
                    count++;
                }
            }
        }
        return count;
    }
    public int countBuildingTypeByTier(Player p, Class<?> clazz, int tier) {
        int count = 0;
        synchronized(p.getBuiltBuilding()) {
            for (Building b : p.getBuiltBuilding()) {
                if (clazz.isInstance(b) && b.getTierLevel() == tier) {
                    count++;
                }
            }
        }
        return count;
    }
    //method for the communation between this class and BuildingManager
    
    public void addInBuildings(Building n) {
        synchronized(this.buildings) {
            this.buildings.add(n);
        }
    }

    public void addInUnits(Unit u) {
        synchronized(this.units) {
            this.units.add(u);
        }
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
    public void setUnitsInSelectedArea(ArrayList<Unit> units){
        this.unitsInSelectedArea=units;
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
    public int buildBuilding(Block position, int tier, String faction, Player p) {
        int result = buildingManager.buildBuilding(position, tier, faction, p); // p et non player
        boolean playerBuild = faction.equalsIgnoreCase(player.getFactionName());//the no
        if (playerBuild) {
            if (result == 1) {
                setNotifText("Batiment ajouté", true);
            } else if(result ==0){
                setNotifText("Ressources insuffisantes", false);
            }
            else {
            	setNotifText("Construction impossible, raison inconnue",false);
            }
        }
        return result;
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
    public void setSelectedArea(ArrayList<Block> selectedArea) {
        this.selectedArea=selectedArea;
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
		return cpu1;
	}

	public void setCpu(CPU cpu) {
		this.cpu1 = cpu;
	}

	public CPU getCpu2() {
		return cpu1;
	}
	
	public void setCpu2(CPU cpu) {
		this.cpu2 = cpu;
	}
	
	public CPUManager getCpuManager() {
		return cpuManager1;
	}

	public void setCpuManager(CPUManager cpuManager) {
		this.cpuManager1 = cpuManager;
	}
	
	public CPUManager getCpuManager2() {
		return cpuManager2;
	}

	public void setCpuManager2(CPUManager cpuManager) {
		this.cpuManager2 = cpuManager;
	}
	
	public void setNotifText(String info,boolean isgood) {
		if (info!=null) {
			this.notification =info;
			this.isNotificationGood=isgood;
		} else {
			this.notification=null;
			this.isNotificationGood=false;
		}
	}
	
	public String getNotification() {
		return this.notification;
	}
	
	public boolean isNotificationGood() {
		return this.isNotificationGood;
	}

	public boolean isGameStoped() {
		return isGameStoped;
	}

	public void setIsGameStoped(boolean stop) {
		this.isGameStoped = stop;
	}
}