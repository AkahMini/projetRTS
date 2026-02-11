package engine.process;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.RessourceDeposit;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;

public class UnitsManager implements UnitsInterface{
	
    private String selectedUnit = null;
	
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

    
    //this is temporary for testing
    public void spawnUnitEnnemy(Block position) {
        if (selectedUnit == null) {
            return;
        }

        String faction = "Hades";
        int tier = 1;
        if (position.getLine() >= 6 && position.getColumn() > 30) {
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
                
                if(newLine > 6 && newLine < map.getLineCount() && newColomn > 0 && newColomn < map.getColumnCount() - 30) {
                    Block newPosition = map.getBlock(newLine, newColomn);
                    if(isBlockCollider(newPosition) == 0) {
                        displacedUnit.setPosition(newPosition);
                    }
                }
            }
        }
    }
    public static void unitTime(Unit unit) {
    	if(unit.getMoveCounter()<Unit.MOVE_TIME + unit.getMovementSpeed()) {
    		unit.setMoveCounter(unit.getMoveCounter()+unit.getMovementSpeed());
    	}else {
    		unit.setMoveCounter((unit.getMoveCounter()+unit.getMovementSpeed())-Unit.MOVE_TIME);

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
            unitTime(unit);
        	if(unit.getMoveCounter()>=Unit.MOVE_TIME) {
        		if(unit instanceof Worker) {
        			workerMouvement((Worker)unit);
        		}else {
                	unitMovement(unit);
        		}
        	}        
        }
    }
}
