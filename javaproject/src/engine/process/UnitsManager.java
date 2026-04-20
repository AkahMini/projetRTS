package engine.process;

import java.util.ArrayList;
import java.util.Iterator;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.MobileElement;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.unit.Cavalry;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;

/**
 * 
 * Manager Pattern class. Used to manage the different units in the engine.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class UnitsManager implements UnitsInterface{
	
    private String selectedUnit = null;
    private MobileInterface manager;
    private DefaultGameSettings gameSettings;	
   	public UnitsManager(MobileInterface manager, DefaultGameSettings gameSettings) {
   		this.manager = manager;
   		this.gameSettings=gameSettings;
   	}
   	
	
    public void selectUnit(String type) {
        this.selectedUnit = type;
        System.out.println("Mode spawn : " + type);
    }
    
    public void spawnUnit(Block position, String faction) {
        if (selectedUnit == null) {
            return;
        }
        int tier = manager.getSelectedTier();
        if (position.getLine() >= 7 && position.getColumn() < GameConfiguration.COLUMN_COUNT - 28) {
            Unit newUnit = UnitFactory.createUnit(selectedUnit, tier, faction, position);
    
            if (newUnit != null) {
                manager.addInUnits(newUnit);
                manager.getCpu().getCreatedUnits().add(newUnit);
                //System.out.println("Unité posée en : " + position.getLine() + ", " + position.getColumn() + " | Faction: " + faction + " | Tier: " + tier);
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
            	displacedUnit.setDestination(null);
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
                
                if(newLine > 6 && newLine < manager.getMap().getLineCount() && newColomn > 0 && newColomn < manager.getMap().getColumnCount() - 28) {
                    Block newPosition = manager.getMap().getBlock(newLine, newColomn);
                    if(manager.isBlockCollider(newPosition) == 0||displacedUnit instanceof Worker) {//if there is no collider
                        displacedUnit.setPosition(newPosition);
                    }
                }
            }
        }
    }
    public static void unitTime(Unit unit) {
    	unit.setMoveCounter((int)(unit.getMoveCounter()+unit.getMovementSpeed()));
	}
    
    
    public void workerMouvement(Worker displacedWorker,Player p) {
    	/**
    	 * how the worker collect its ressources
    	 */
    	unitMovement((Unit)displacedWorker); //Moves like a normal unit
    	
    	//if the ai is busy elsewere, we don't do anything else
    	if (displacedWorker.getIsWorking() && displacedWorker.getCurrentDeposit() == null) {
            return; 
        }

    	if(displacedWorker.getCurrentDeposit()==null)  {
    		//if no deposit assigned, check for a deposit nearby
    		for(RessourceDeposit deposit: manager.getRessourceDeposit()) {
    			if(manager.getDistance(displacedWorker.getPosition(),deposit.getPosition())<=displacedWorker.getVision()) {
    				//if a deposit is in range
    					displacedWorker.setCurrentDeposit(deposit);
    					displacedWorker.setRessourceType(deposit.getType());
    			}
    			
    			
    		}
    	}
    	
    	else if((displacedWorker.getRessourceLoad()<displacedWorker.getMaxCargoCapacity())&&manager.getDistance(displacedWorker.getPosition(),displacedWorker.getCurrentDeposit().getPosition())<=displacedWorker.getVision()) {
    		//if the worker can carry more ressources and a deposit is in worker's range
    		displacedWorker.setCurrentRessourceLoad(displacedWorker.getRessourceLoad()+1);
    		displacedWorker.setIsWorking(true);
    	}
    	
    	else if(displacedWorker.getRessourceLoad()>=displacedWorker.getMaxCargoCapacity()) {
    		displacedWorker.setIsWorking(false);
    		//if he has ressources, he comes back
    		displacedWorker.setDestination(displacedWorker.getCurrentHQ().getPosition());
    	}

    	else if(displacedWorker.getDestination() == null || displacedWorker.getDestination().equals(displacedWorker.getPosition())) {
    		//if the worker is stationnary, we can check for new deposit
    		for(RessourceDeposit deposit: manager.getRessourceDeposit()) {
    			if(manager.getDistance(displacedWorker.getPosition(),deposit.getPosition())<displacedWorker.getVision()) {
    				//if a deposit is in range
    					displacedWorker.setCurrentDeposit(deposit);
    					displacedWorker.setRessourceType(deposit.getType());
    			}
    			
    			
    		}
    	}
    	
    	workerRessourceDeposit(displacedWorker,p);
    }
    
    public void workerRessourceDeposit(Worker worker, Player player) {
        if (worker.getCurrentHQ() != null && worker.getRessourceLoad() > 0) {
            if (manager.getDistance(worker.getPosition(), worker.getCurrentHQ().getPosition()) <= worker.getVision()) {
                
                if (worker.getRessourceType() == RessourceDeposit.FAITH) {
                    player.setFaithStock(player.getFaithStock() + worker.getRessourceLoad());
                    player.setTotalFaithGathered(player.getTotalFaithGathered()+worker.getRessourceLoad());
                } else if (worker.getRessourceType() == RessourceDeposit.AMBROSIA) {
                    player.setAmbroisieStock(player.getAmbroisieStock() + worker.getRessourceLoad());
                    player.setTotalAmbroisieGathered(player.getTotalAmbroisieGathered()+worker.getRessourceLoad());
                }
                
                worker.setCurrentRessourceLoad(0);
                
                if (worker.getCurrentDeposit() != null) {
                    worker.setDestination(worker.getCurrentDeposit().getPosition());
                }
            }
        }
    }
    
    public MobileElement scanForEnemy(Unit unit) {
        /*
         * Returns the nearest ennemy of the unit
         */
    	MobileElement nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Unit otherUnit : manager.getUnits()) {
            if (otherUnit != unit && !otherUnit.getUnitFaction().equals(unit.getUnitFaction())) {
                double dist = manager.getDistance(unit.getPosition(), otherUnit.getPosition());
                
                if (dist <= unit.getVision() && dist < minDistance) {
                    minDistance = dist;
                    nearest = otherUnit;
                }
            }
            for (Building b : manager.getBuildings()) {
                if (!b.getFaction().equalsIgnoreCase(unit.getUnitFaction())) {
                    double dist = manager.getDistance(unit.getPosition(), b.getPosition());
                    
                    if (dist <= unit.getVision() && dist < minDistance) {
                        minDistance = dist;
                        nearest = b;
                    }
                }
            }
        }
        return nearest;
    }
    
    public void setCombatState(Unit unit1, MobileElement target) {
    	/*
    	 * Tells the unit and the target they are fighting
    	 */
    	if (target instanceof Unit) {
    		Unit unit2 = (Unit) target;
    		if(unit1.getIsInCombat() == false && unit2.getIsInCombat() == false) {
    			unit1.setIsInCombat(true);
    			unit2.setIsInCombat(true);
    			unit2.setTarget(unit1);
    			unit1.setTarget(unit2);
    			if(unit1 instanceof Cavalry) {
    				Cavalry cavalry = (Cavalry) unit1;
    				cavalry.setMovementSpeed(cavalry.getMovementSpeed()*cavalry.getChargeSpeed());
    				cavalry.setChargeDistanceValue(cavalry.getChargeDistanceMax());
    			}
    			if(unit2 instanceof Cavalry) {
    				Cavalry cavalry = (Cavalry) unit2;
    				cavalry.setMovementSpeed(cavalry.getChargeSpeed());
    				cavalry.setChargeDistanceValue(cavalry.getChargeDistanceMax());
    			}
    		} else if(unit1.getIsInCombat() == false && unit2.getIsInCombat() == true) {
    			unit1.setIsInCombat(true);
    			unit1.setTarget(unit2);
    		} else if(unit2.getIsInCombat() == false && unit1.getIsInCombat() == true) {
    			unit2.setIsInCombat(true);
    			unit2.setTarget(unit1);
    		}
    	}else if(target instanceof Building && unit1.getIsInCombat() == false) {
    		unit1.setTarget(target);
    		unit1.setIsInCombat(true);
    	} 
    }
    
    public String damageCalculation(Unit unit) {
    	/**
    	 * Compute the damage inflicted by the unit in parameter
    	 * the returned string is the name of the faction attributed to the potential kill
    	 * the attackCounter increments by each call of the function by the attackSpeedValue of the unit, and 
    	 * the units attacks when it's counter reaches its AttackTime
    	 */
    	
    	MobileElement target = unit.getTarget();
    	
    	
        if (target != null) {
        	if (target.getHp() <= 0) {
                //if the targer is already dead, we ignore it
        		unit.setTarget(null);
                unit.setIsInCombat(false);
                return null;
            }
            double attackCounter = unit.getAttackCounter();
            double attack = unit.getATK();
            unit.setAttackCounter(attackCounter + unit.getATKSpeed());
            double dist = manager.getDistance(unit.getPosition(), target.getPosition());
            if (attackCounter >= Unit.getAttackTime() && dist <= unit.getATKRange()) {
                if (unit instanceof Cavalry) {
                    Cavalry cavalry = (Cavalry) unit;
                    if (cavalry.getChargeDistanceValue() > 0) {
                        attack = (float) (attack * cavalry.getChargeBonusDamage());
                        System.out.println("degats charge : " + attack);
                        cavalry.setChargeDistanceValue(0);
                        cavalry.setMovementSpeed((int) (cavalry.getMovementSpeed() / cavalry.getChargeSpeed()));
                    }
                }
                double remainingDamage = attack;
                if (target instanceof Infantry) {
                    Infantry infantryTarget = (Infantry) target;
                    double shield = infantryTarget.getShieldValue();
                    if (shield > 0) {
                        if (shield >= attack) {
                            infantryTarget.setShieldValue(shield - attack);
                            remainingDamage = 0;
                        } else {
                            infantryTarget.setShieldValue(0);
                            remainingDamage = attack - shield;
                        }
                    }
                }
                if (remainingDamage > 0) {
                    int newHp = (int) Math.max(0, target.getHp() - remainingDamage);
                    target.setHp(newHp);
                }
                unit.setAttackCounter(unit.getAttackCounter() - Unit.getAttackTime());
                if (target.getHp() <= 0) {
                    //if we just killed the target, the killcount increments
            		unit.setTarget(null);
                    unit.setIsInCombat(false);
                    
                    return unit.getUnitFaction();
                }
                
            }
        }
        return null;
    }
    
    
    
    public void unitsInSelectedArea() {
    	ArrayList<Block> selectedArea = manager.getSelectedArea();
        ArrayList<Unit> units = manager.getUnits();  
        ArrayList<Unit> unitsInSelectedArea = manager.getUnitsInSelectedArea(); 
        Worker foundWorker = null;
        
        if(unitsInSelectedArea != null) {
            unitsInSelectedArea.clear();// empty the list so that we don't select the same units multiple times
        }
        if(selectedArea != null){
            int nbOfBlocksInSelectedArea = selectedArea.size();
            int nbOfUnits = units.size();
            
            for(int unitIndex = 0; unitIndex < nbOfUnits; unitIndex++){
                Block unitPosition = units.get(unitIndex).getPosition();
                for(int blockIndex = 0; blockIndex < nbOfBlocksInSelectedArea; blockIndex++) {
                    if(selectedArea.get(blockIndex).equals(unitPosition)) {
                        unitsInSelectedArea.add(units.get(unitIndex));
                        //used to set selected worker once
                        if (foundWorker==null && units.get(unitIndex) instanceof Worker){
                        	foundWorker=(Worker) units.get(unitIndex);
                        	
                        }
                        break;//in case the same block is present multiple time in the selection
                    }
                }
            }
            if(foundWorker!=null) {
            	manager.setSelectedWorker(foundWorker);
            	manager.setSelectedBuild(null);
            }
        } else {
        	unitsInSelectedArea=null;
        }
        //System.out.println("Number of units in selected Area:" + manager.getUnitsInSelectedArea().size());
    }
    
    public void unitMoveOrder(Block destination, Map map){
        int nbUnits = manager.getUnitsInSelectedArea().size();
        ArrayList<Block> groupDestination = new ArrayList<Block>();
        int sideOfGroupDestination=0; //the lenght of the square allocated for the troups
        while((sideOfGroupDestination*sideOfGroupDestination)<nbUnits) {
        	//We take the square root of the smallest perfect square n such as n>=nbUnits
        	sideOfGroupDestination++;
        }
        
        //Allocating the area for the moved army
        for(int i=0;i<sideOfGroupDestination;i++) {
        	for(int j=0; j<sideOfGroupDestination;j++) {
        		int line = destination.getLine();
        		int colomn = destination.getColumn();
        		groupDestination.add(map.getBlock(line+i,colomn+j));
        	}
        }
        
        int movedUnitCounter=0;
        for(int unitIndex = 0; unitIndex < nbUnits; unitIndex++) {
            Unit unit = manager.getUnitsInSelectedArea().get(unitIndex);
            if(unit.getUnitFaction().equalsIgnoreCase(manager.getPlayer().getFactionName())) {    
            	unit.setDestination(groupDestination.get(movedUnitCounter));
            	movedUnitCounter+=1;
            	unit.setTarget(null);
                unit.setIsInCombat(false);
            }
        }
    }

    public void moveAllUnits(Player p) {
        int size = p.getCreatedUnits().size();
        for(int i = 0; i < size; i++) {
            Unit unit = p.getCreatedUnits().get(i);
            unitTime(unit);
        	if(unit.getMoveCounter()>=Unit.getMoveTime()) {
        		if(unit instanceof Worker) {
        			workerMouvement((Worker)unit,p);
        		}else {
        			if(unit instanceof Cavalry) {
        				Cavalry cavalry=(Cavalry) unit;
        				if(cavalry.getChargeDistanceValue()>0 && cavalry.getDestination() != null) {
        					cavalry.setChargeDistanceValue((int) Math.max(0, cavalry.getChargeDistanceValue()-cavalry.getMovementSpeed()));
        					if(cavalry.getChargeDistanceValue()==0) {
        						cavalry.setMovementSpeed((int) (cavalry.getMovementSpeed()/cavalry.getChargeSpeed()));
        					}
        				}
        			}
        			unitMovement(unit);
        		}
        		unit.setMoveCounter(unit.getMoveCounter() - Unit.getMoveTime());
        	}        
        }
    }
    /*
    public void killUnit(Unit unit, ArrayList<Unit> units) {
    	if(unit.getHp()<=0) {
    		Iterator<Unit> it = units.iterator();
    		while (it.hasNext()) {
    		    Unit u= it.next();
    		    if (u.equals(unit)) {
    		        it.remove();
    		    }
    		}
    		//units.remove(unit);
    	}
    }
    */
    //manage the button part
    public void workerConstruction(String button, Player p, Worker worker) {
    	int currentButtonTier = manager.getSelectedTier();
    	int playerTier = manager.getPlayer().getCurrentTier();
    	//manager.setTypeSelection("build");
    	
    	switch (button) {
    	case "button1":
    		if(currentButtonTier==0) {
    			manager.setSelectedTier(1);
    			break;
    		}else if(currentButtonTier==1) {
    			manager.selectBuilding("HQ");
    			p.setBuildingToBuildID("HQ");
    			manager.setNotifText("QG sélectioné",true);
    			//System.out.println("selectetd");
    			break;
    		}else if(currentButtonTier==2) {
    			manager.selectBuilding("ResearchBuilding");
    			p.setBuildingToBuildID("ResearchBuilding");
    			manager.setNotifText("Labo de recherche",true);
    			break;
    		}else if(currentButtonTier==3) {
    			manager.selectBuilding("Producer");
    			p.setBuildingToBuildID("Producer");
    			manager.setNotifText("Bat de prod tier 3 sélectioné",true);
    			break;
    		}
    		break;
    	case "button2":
    		
    		if(currentButtonTier==0&&playerTier>=2) { //if the player chose the tier2 button and is tier 2 or higher
    			manager.setSelectedTier(2);
    			break;
    		}else if(currentButtonTier==1) {
    			manager.selectBuilding("Producer");
    			p.setBuildingToBuildID("Producer");
    			manager.setNotifText("Bat de prod tier 1 sélectioné",true);
    			manager.setTypeSelection("build");
    			break;
    		}else if(currentButtonTier==2) {
    			manager.selectBuilding("Producer");
    			p.setBuildingToBuildID("Producer");
    			manager.setNotifText("Bat de prod tier 2 sélectioné",true);
    			break;
    		}
    		break;
    	case "button3":
    		if(currentButtonTier==0&&playerTier>=3) { //if the player chose the tier3 button and is tier 3 or higher
    			manager.setSelectedTier(3);
    			break;
    		}else if(currentButtonTier==1) {
    			manager.selectBuilding("PopulationBuilding");
    			p.setBuildingToBuildID("PopulationBuilding");
    			manager.setNotifText("Bat de population sélectioné",true);
    			break;
    		}else if(currentButtonTier==2) {
    			p.setBuildingToBuildID("DefenseTower");
    			manager.selectBuilding("DefenseTower");
    			manager.setNotifText("Tour de défense sélectioné",true);
    			break;
    		}
    		break;
    	case "button4":
    		break;
    	case "button5":
    		break;
    	case "button6":
    		if(currentButtonTier!=0) {
    			manager.setSelectedTier(0);
    			break;
    		}
    		break;
    	}
    }
}
