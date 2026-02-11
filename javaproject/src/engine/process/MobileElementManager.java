package engine.process;

import java.util.ArrayList;
import java.util.List;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;

/**
 * * @author LE RAY Yann
 *
 */
public class MobileElementManager implements MobileInterface {
    private Map map;
    
    private String selectedBuilding = null;
    private List<Building> buildings = new ArrayList<Building>();
    private List<Unit> unitsInSelectedArea = new ArrayList<Unit>();
    private List<RessourceDeposit> ressourceDeposits = new ArrayList<RessourceDeposit>();
    
    private String selectedUnit = null;
    private List<Unit> units = new ArrayList<Unit>();
    
    private List<Block> selectedArea;
    
    private Chronometer chronometer = new Chronometer();
    private CyclicCounter timetweaker = new CyclicCounter(0,100,0);

    private Player player;
    
    public MobileElementManager(Map map) {
        this.map = map;
        this.player = new Player("Jhon Doe", "Zeus");
        chronometer.init();
    }

    public void firstRound() {
    	//We add player's HQ, ressource deposits
    	Block playerHQposition = map.getBlock(10, 10); //TMP player's HQ
    	HQ playerHQ = new HQ(playerHQposition);
    	
    	Worker playerWorker = new Worker(playerHQposition);  //TMP player's worker
    	playerWorker.setUnitName("Initial worker");
    	playerWorker.setHp(1);//TMP Because he keeps getting slimed
    	playerWorker.setUnitFaction("Zeus");
    	playerWorker.setVision(1);
    	playerWorker.setCurrentHQ(playerHQ);
    	playerWorker.setMaxCargoCapacity(100);
    	playerWorker.setDestination(playerWorker.getPosition());
    	
    	Block faithDepositLocation = map.getBlock(30, 20);//TMP
    	RessourceDeposit deposit1 = new RessourceDeposit(faithDepositLocation,RessourceDeposit.FAITH);
    	
    	Block ambroiseDepositLocation = map.getBlock(10, 35);
    	RessourceDeposit deposit2= new RessourceDeposit(ambroiseDepositLocation,RessourceDeposit.AMBROISE);
    	
    	this.buildings.add(playerHQ);
    	this.units.add(playerWorker);
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
                if (unit.getTarget() == null) {
                    Unit enemy = scanForEnemy(unit);
                    if (enemy != null) {
                        combatSystem(unit, enemy);
                    }
                }

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
                            calculDegats(unit);
                        }
                    }
                }
            }
            
            // Buildings management
            for(Building building : buildings) {
                reduceConstructionTime(building);
                if(building instanceof UnitProducer) {
                    UnitProducer producer = (UnitProducer) building;
                    removeQueue(producer);
                }
            }
        }
        moveAllUnits();
    }
    
    // --- Build part ---
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
        if (position.getLine() >= 3 && position.getColumn() > 15) {
            Building nouveauBatiment = BuildingFactory.createBuilding(selectedBuilding, tier, faction, position);
    
            if (nouveauBatiment != null) {
                buildings.add(nouveauBatiment);
                System.out.println("Bâtiment posé en : " + position.getLine() + ", " + position.getColumn());
            }
            selectedBuilding = null;
        }
    }
    
    public void reduceConstructionTime(Building building) {
        if (building.getIsUnderConstruction()) {
            // reduce remaining building time
            building.setConstructionTime(building.getConstructionTime() - 1);
            if (building.getConstructionTime() == 0) {
                building.setUnderConstruction(false);
            }
        }
    }

    public void addQueue(UnitProducer building, Block position) {
        if (building.getProductionQueue().size() < 3) {
            building.setCurrentProduction(building.getProductionSpeed());
            if (building.getTierLevel() == 1) {
                if("Zeus".equals(building.getFaction())) {
                    Unit newUnit = UnitFactory.createUnit("INFANTRY", 1, "ZEUS", position);
                    ((UnitProducer) building).getProductionQueue().add(newUnit);
                }
            }
        }
    }

    public void removeQueue(UnitProducer building) {
        ArrayList<Unit> queue = building.getProductionQueue();
        if (building.getCurrentProduction() != 0) {
            // reduce remaining spawning time
            building.setCurrentProduction(building.getCurrentProduction() - 1);
            if (building.getCurrentProduction() == 0) {
                queue.removeFirst();
                int line = building.getPosition().getLine();
                int column = building.getPosition().getColumn() + 1;
                
                if(column < map.getColumnCount()) {
                    Block spawnBlock = map.getBlock(line, column);
                    selectedUnit = "INFANTRY";
                    spawnUnit(spawnBlock);
                    if(!queue.isEmpty()){
                        building.setCurrentProduction(building.getProductionSpeed());
                    }
                }
             }
        }
    }
        
    // --- Unit part ---
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
        if (position.getLine() >= 3 && position.getColumn() > 15) {
            Unit newUnit = UnitFactory.createUnit(selectedUnit, tier, faction, position);
    
            if (newUnit != null) {
                units.add(newUnit);
                System.out.println("Unité posée en : " + position.getLine() + ", " + position.getColumn());
            }
            selectedUnit = null;
        }
    }

    public void spawnUnitEnnemy(Block position) {
        if (selectedUnit == null) {
            return;
        }

        String faction = "Hades";
        int tier = 1;
        if (position.getLine() >= 3 && position.getColumn() > 15) {
            Unit newUnit = UnitFactory.createUnit(selectedUnit, tier, faction, position);
    
            if (newUnit != null) {
                units.add(newUnit);
                System.out.println("Unité ennemie posée en : " + position.getLine() + ", " + position.getColumn());
            }
            selectedUnit = null;
        }
    }
    
    public void unitMovement(Unit displacedUnit) {
        if(displacedUnit.getDestination() != null) {
            Block position = displacedUnit.getPosition();
            Block destination = displacedUnit.getDestination();
            int xDisplacement = 1;
            int yDisplacement = 1;
            
            int x1 = position.getColumn();
            int x2 = destination.getColumn();
            int y1 = position.getLine();
            int y2 = destination.getLine();
            
            if(x1 == x2 && y1 == y2) {
                // Arrived
            } else {
                int newLine = y1; 
                int newColomn = x1;
                
                if(x1 < x2 && y1 < y2) { // SE
                    newLine += yDisplacement; newColomn += xDisplacement;
                } else if(x1 < x2 && y1 == y2) { // E
                    newColomn += xDisplacement;
                } else if(x1 < x2 && y1 > y2) { // NE
                    newLine -= yDisplacement; newColomn += xDisplacement;
                } else if(x1 == x2 && y1 < y2) { // S
                    newLine += yDisplacement;
                } else if(x1 == x2 && y1 > y2) { // N
                    newLine -= yDisplacement;
                } else if(x1 > x2 && y1 < y2) { // SW
                    newLine += yDisplacement; newColomn -= xDisplacement;
                } else if(x1 > x2 && y1 == y2) { // W
                    newColomn -= xDisplacement;
                } else if(x1 > x2 && y1 > y2) { // NW
                    newLine -= yDisplacement; newColomn -= xDisplacement;
                }
                
                if(newLine > 3 && newLine < map.getLineCount() && newColomn > 0 && newColomn < map.getColumnCount() - 15) {
                    Block newPosition = map.getBlock(newLine, newColomn);
                    if(isBlockCollider(newPosition) == 0) {
                        displacedUnit.setPosition(newPosition);
                    }
                }
            }
        }
    }
    
    public void workerMouvement(Worker displacedWorker) {
    	unitMovement((Unit)displacedWorker); //Moves like a normal unit
    	if(displacedWorker.getCurrentDeposit()==null)  {
    		for(RessourceDeposit deposit: this.ressourceDeposits) {
    			if(getDistance(displacedWorker.getPosition(),deposit.getPosition())<displacedWorker.getVision()) {
    				//if a deposit is in range
    					displacedWorker.setCurrentDeposit(deposit);
    					displacedWorker.setRessourceType(deposit.getType());
    			}
    			
    			
    		}
    	}
    	
    	else if(getDistance(displacedWorker.getPosition(),displacedWorker.getCurrentDeposit().getPosition())<=displacedWorker.getVision()) {
    		//if a deposit is in worker's range	
    		displacedWorker.setCurrentRessourceLoad(displacedWorker.getRessourceLoad()+1);
    		}
    	
    	if(displacedWorker.getRessourceLoad()>=displacedWorker.getMaxCargoCapacity()) {
    		//if he has ressources, he comes back
    		displacedWorker.setDestination(displacedWorker.getCurrentHQ().getPosition());
    	}

    	if(displacedWorker.getDestination().equals(displacedWorker.getPosition())) {
    		//if the worker is stationnary, we can check for new deposit
    		for(RessourceDeposit deposit: this.ressourceDeposits) {
    			if(getDistance(displacedWorker.getPosition(),deposit.getPosition())<displacedWorker.getVision()) {
    				//if a deposit is in range
    					displacedWorker.setCurrentDeposit(deposit);
    					displacedWorker.setRessourceType(deposit.getType());
    			}
    			
    			
    		}
    	}
    	
    	workerRessourceDeposit(displacedWorker);
    }
    
    public void workerRessourceDeposit(Worker worker){
    	if(getDistance(worker.getPosition(),worker.getCurrentHQ().getPosition())<=worker.getVision()) {
    		//if he is the HQ's range
    		if(worker.getRessourceType()==RessourceDeposit.FAITH) {
    			player.setFaithStock(player.getFaithStock()+worker.getRessourceLoad());
    		}
    		if(worker.getRessourceType()==RessourceDeposit.AMBROISE) {
    			player.setAmbroisieStock(player.getAmbroisieStock()+worker.getRessourceLoad());
    		}
    		worker.setCurrentRessourceLoad(0);
    		if(worker.getCurrentDeposit()!=null) {
    			worker.setDestination(worker.getCurrentDeposit().getPosition());
    		}
    	}
    }
    
    
    
    private double getDistance(Block b1, Block b2) {
        int dx = b1.getColumn() - b2.getColumn();
        int dy = b1.getLine() - b2.getLine();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Unit scanForEnemy(Unit unit) {
        Unit nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Unit otherUnit : units) {
            if (otherUnit != unit && !otherUnit.getUnitFaction().equals(unit.getUnitFaction())) {
                double dist = getDistance(unit.getPosition(), otherUnit.getPosition());
                
                if (dist <= unit.getVision() && dist < minDistance) {
                    minDistance = dist;
                    nearest = otherUnit;
                }
            }
        }
        return nearest;
    }
    
    public void combatSystem(Unit unit1, Unit unit2) {
        if(unit1.getIsInCombat() == false && unit2.getIsInCombat() == false) {
            unit1.setIsInCombat(true);
            unit2.setIsInCombat(true);
            unit2.setTarget(unit1);
            unit1.setTarget(unit2);
        } else if(unit1.getIsInCombat() == false && unit2.getIsInCombat() == true) {
            unit1.setIsInCombat(true);
            unit1.setTarget(unit2);
        } else if(unit2.getIsInCombat() == false && unit1.getIsInCombat() == true) {
            unit2.setIsInCombat(true);
            unit2.setTarget(unit1);
        }
    }
    
    public void calculDegats(Unit unit) {
        Unit target = (Unit) unit.getTarget();
        
        if (target != null) {
            int damage = (int) (unit.getATK() * unit.getATKSpeed());
            
            int newHp = Math.max(0, target.getHp() - damage);
            target.setHp(newHp);
            
            if (target.getHp() <= 0) {
                unit.setTarget(null);
                unit.setIsInCombat(false);
            }
        }
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
    }
    
    public void unitsInSelectedArea() {
        this.unitsInSelectedArea = new ArrayList<Unit>();
        
        if(this.selectedArea != null){
            int nbOfBlocksInSelectedArea = this.selectedArea.size();
            int nbOfUnits = this.units.size();
            
            for(int unitIndex = 0; unitIndex < nbOfUnits; unitIndex++){
                Block unitPosition = units.get(unitIndex).getPosition();
                for(int blockIndex = 0; blockIndex < nbOfBlocksInSelectedArea; blockIndex++) {
                    if(selectedArea.get(blockIndex).equals(unitPosition)) {
                        this.unitsInSelectedArea.add(units.get(unitIndex));
                        break;//in case the same block is present multiple time in the selection
                    }
                }
            }
        } else {
            this.unitsInSelectedArea = null;
        }
        System.out.println("Number of units in selected Area:" + this.unitsInSelectedArea.size());
    }
    
    public void unitMoveOrder(Block destination){
        int nbUnits = this.unitsInSelectedArea.size();
        for(int unitIndex = 0; unitIndex < nbUnits; unitIndex++) {
            Unit unit = this.unitsInSelectedArea.get(unitIndex);
            unit.setDestination(destination);
        }
    }

    public void moveAllUnits() {
        int size = this.units.size();
        for(int i = 0; i < size; i++) {
            Unit unit = this.units.get(i);
        	if(unit instanceof Worker) {
        		workerMouvement((Worker) unit); //We cast the type Worker for using the correct method
        	}
        	unitMovement(this.units.get(i));
        }
    }
    
    public int isBlockCollider(Block block) {
        int nbOfUnits = this.units.size();
        for(int i = 0; i < nbOfUnits; i++) {
            if(this.units.get(i).getPosition() == block){
                return 1;
            }
        }
        return 0;
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
    
    private static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }   
    
    public List<Building> getBuildings() {
        return buildings;
    }
    
    public List<RessourceDeposit> getRessourceDeposit(){
    	return this.ressourceDeposits;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Unit> getUnitsInSelectedArea(){
        return unitsInSelectedArea;
    }
    
    public Block getMousePosition(int x, int y) {
        int line = x / GameConfiguration.BLOCK_SIZE;
        int column = y / GameConfiguration.BLOCK_SIZE;
        return map.getBlock(line, column);
    }
    
    public List<Block> getSelectedArea() {
        return selectedArea;
    }
    
    public Player getPlayer() {
        return this.player;
    }
}