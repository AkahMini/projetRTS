package engine.process;

import java.util.ArrayList;

import config.DefaultGameSettings;

import engine.map.Block;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
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
    
    public void spawnUnit(Block position) {
        if (selectedUnit == null) {
            return;
        }

        String faction = "Zeus";
        int tier = 1;
        if (position.getLine() >= 3 && position.getColumn() > 28) {
            Unit newUnit = UnitFactory.createUnit(selectedUnit, tier, faction, position);
    
            if (newUnit != null) {
            	manager.addInUnits(newUnit);
                System.out.println("Unité posée en : " + position.getLine() + ", " + position.getColumn());
            }
            selectedUnit = null;
        }
    }

    
    //this is temporary for testing
    public void spawnUnitEnnemy(Block position) {
        if (selectedUnit == null) {
            return;
        }

        String faction = "Hades";
        int tier = 1;
        if (position.getLine() >= 6 && position.getColumn() > 28) {
            Unit newUnit = UnitFactory.createUnit(selectedUnit, tier, faction, position);
    
            if (newUnit != null) {
            	manager.addInUnits(newUnit);
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
                
                if(newLine > 6 && newLine < manager.getMap().getLineCount() && newColomn > 0 && newColomn < manager.getMap().getColumnCount() - 28) {
                    Block newPosition = manager.getMap().getBlock(newLine, newColomn);
                    if(manager.isBlockCollider(newPosition) == 0) {
                        displacedUnit.setPosition(newPosition);
                    }
                }
            }
        }
    }
    public static void unitTime(Unit unit) {
    	unit.setMoveCounter((int)(unit.getMoveCounter()+unit.getMovementSpeed()));
	}
    
    public void workerMouvement(Worker displacedWorker) {
    	unitMovement((Unit)displacedWorker); //Moves like a normal unit
    	if(displacedWorker.getCurrentDeposit()==null)  {
    		for(RessourceDeposit deposit: manager.getRessourceDeposit()) {
    			if(manager.getDistance(displacedWorker.getPosition(),deposit.getPosition())<=displacedWorker.getVision()) {
    				//if a deposit is in range
    					displacedWorker.setCurrentDeposit(deposit);
    					displacedWorker.setRessourceType(deposit.getType());
    			}
    			
    			
    		}
    	}
    	
    	else if(manager.getDistance(displacedWorker.getPosition(),displacedWorker.getCurrentDeposit().getPosition())<=displacedWorker.getVision()) {
    		//if a deposit is in worker's range	
    		displacedWorker.setCurrentRessourceLoad(displacedWorker.getRessourceLoad()+1);
    		}
    	
    	if(displacedWorker.getRessourceLoad()>=displacedWorker.getMaxCargoCapacity()) {
    		//if he has ressources, he comes back
    		displacedWorker.setDestination(displacedWorker.getCurrentHQ().getPosition());
    	}

    	if(displacedWorker.getDestination().equals(displacedWorker.getPosition())) {
    		//if the worker is stationnary, we can check for new deposit
    		for(RessourceDeposit deposit: manager.getRessourceDeposit()) {
    			if(manager.getDistance(displacedWorker.getPosition(),deposit.getPosition())<displacedWorker.getVision()) {
    				//if a deposit is in range
    					displacedWorker.setCurrentDeposit(deposit);
    					displacedWorker.setRessourceType(deposit.getType());
    			}
    			
    			
    		}
    	}
    	
    	workerRessourceDeposit(displacedWorker);
    }
    
    public void workerRessourceDeposit(Worker worker){
    	Player player=manager.getPlayer();
    	if(manager.getDistance(worker.getPosition(),worker.getCurrentHQ().getPosition())<=worker.getVision()) {
    		//if he is the HQ's range
    		if(worker.getRessourceType()==RessourceDeposit.FAITH) {
    			player.setFaithStock(player.getFaithStock()+worker.getRessourceLoad());
    		}
    		if(worker.getRessourceType()==RessourceDeposit.AMBROSIA) {
    			player.setAmbroisieStock(player.getAmbroisieStock()+worker.getRessourceLoad());
    		}
    		worker.setCurrentRessourceLoad(0);
    		if(worker.getCurrentDeposit()!=null) {
    			worker.setDestination(worker.getCurrentDeposit().getPosition());
    		}
    	}
    }
    
    public Unit scanForEnemy(Unit unit) {
        Unit nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Unit otherUnit : manager.getUnits()) {
            if (otherUnit != unit && !otherUnit.getUnitFaction().equals(unit.getUnitFaction())) {
                double dist = manager.getDistance(unit.getPosition(), otherUnit.getPosition());
                
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
            if(unit1 instanceof Cavalry) {
                Cavalry cavalry = (Cavalry) unit1;
                cavalry.setMovementSpeed(cavalry.getChargeSpeed());
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
    }
    
    
    public void calculDegats(Unit unit) {
        Unit target = (Unit) unit.getTarget();
        if (target != null) {
            double attackCounter = unit.getAttackCounter();
            int attack = unit.getATK();
            unit.setAttackCounter(attackCounter + unit.getATKSpeed());
            double dist = manager.getDistance(unit.getPosition(), target.getPosition());
            if (attackCounter >= Unit.getAttackTime() && dist <= unit.getATKRange()) {
                int remainingDamage = attack;
                if (target instanceof Infantry) {
                    Infantry infantryTarget = (Infantry) target;
                    int shield = infantryTarget.getShieldValue();
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
                    int newHp = Math.max(0, target.getHp() - remainingDamage);
                    target.setHp(newHp);
                }
                unit.setAttackCounter(unit.getAttackCounter() - Unit.getAttackTime());
                
                if (target.getHp() <= 0) {
                    unit.setTarget(null);
                    unit.setIsInCombat(false);
                }
            }
        }
    }
    
    public void unitsInSelectedArea() {
    	ArrayList<Block> selectedArea = manager.getSelectedArea();
        ArrayList<Unit> units = manager.getUnits();  
        ArrayList<Unit> unitsInSelectedArea = manager.getUnitsInSelectedArea(); 
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
                        break;//in case the same block is present multiple time in the selection
                    }
                }
            }
        } else {
        	unitsInSelectedArea=null;
        }
        //System.out.println("Number of units in selected Area:" + manager.getUnitsInSelectedArea().size());
    }
    
    public void unitMoveOrder(Block destination){
        int nbUnits = manager.getUnitsInSelectedArea().size();
        for(int unitIndex = 0; unitIndex < nbUnits; unitIndex++) {
            Unit unit = manager.getUnitsInSelectedArea().get(unitIndex);
            if(unit.getUnitFaction().equals(this.gameSettings.getPlayerFaction())) {
            	unit.setDestination(destination);
            	unit.setTarget(null);
                unit.setIsInCombat(false);
            }
        }
    }

    public void moveAllUnits() {
        int size = manager.getUnits().size();
        for(int i = 0; i < size; i++) {
            Unit unit = manager.getUnits().get(i);
            unitTime(unit);
        	if(unit.getMoveCounter()>=Unit.getMoveTime()) {
        		if(unit instanceof Worker) {
        			workerMouvement((Worker)unit);
        		}else {
        			if(unit instanceof Cavalry) {
        				Cavalry cavalry=(Cavalry) unit;
        				if(cavalry.getChargeDistanceValue()>0) {
        					cavalry.setChargeDistanceValue(Math.max(0, cavalry.getChargeDistanceValue()-cavalry.getChargeSpeed()));
        					if(cavalry.getChargeDistanceValue()==0) {
        						cavalry.setMovementSpeed((int) (cavalry.getMovementSpeed()/1.5));
        					}
        				}
        			}
        			unitMovement(unit);
        		}
        		unit.setMoveCounter(unit.getMoveCounter() - Unit.getMoveTime());
        	}        
        }
    }
}
