package engine.process;

import java.util.ArrayList;

import config.GameConfiguration;
import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
/*
 * Manager pattern class. Used to manage building object.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 * 
 */



public class BuildingManager implements BuildingInterface{

	private String selectedBuilding = null;
    private MobileInterface manager;
	
	public BuildingManager(MobileInterface manager) {
		this.manager=manager;
	}
	
	
    //temporaty method for testing
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
        if (position.getLine() >= 7 && position.getColumn() < GameConfiguration.COLUMN_COUNT - 28) {
            Building nouveauBatiment = BuildingFactory.createBuilding(selectedBuilding, tier, faction, position);
    
            if (nouveauBatiment != null) {
                manager.addInBuildings(nouveauBatiment);
                System.out.println("Bâtiment posé en : " + position.getLine() + ", " + position.getColumn());
            }
            selectedBuilding = null;
        }
    }
    
    public void buildingsInSelectedArea() {
    	ArrayList<Block> selectedArea = manager.getSelectedArea();
        ArrayList<Building> buildings = manager.getBuildings();
        ArrayList<Building> buildingsInSelectedArea = manager.getBuildingsInSelectedArea();
        if(buildingsInSelectedArea != null) {
        	buildingsInSelectedArea.clear();//empty the list for the new selection
        }
        if(selectedArea !=null) {
        	int nbOfBlocksInSelectedArea = selectedArea.size();
            int nbOfBuildings = buildings.size();
            
            for(int buildingIndex=0; buildingIndex<nbOfBuildings; buildingIndex++) {
            	Block buildingPosition = buildings.get(buildingIndex).getPosition();
            	for(int blockIndex = 0; blockIndex<nbOfBlocksInSelectedArea;blockIndex++) {
            		if(selectedArea.get(blockIndex).equals(buildingPosition)) {
            			buildingsInSelectedArea.add(buildings.get(buildingIndex));
            			if(buildingIndex==0) {
            				manager.setSelectedBuild(buildings.get(buildingIndex));
            			}
            			break;//if 2 buildings are in the same position (not possible but in case of + it run faster :p)
            		}
            	}
            }
        } else {
        	buildingsInSelectedArea=null;
        }
        //System.out.println("Number of builds in selected Area:" + manager.getBuildingsInSelectedArea().size());
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

    public void addQueue(UnitProducer building, Block position,String unitType) {
        if (building.getProductionQueue().size() < 3) {
        	if(building.getProductionQueue().isEmpty()) {
                building.setCurrentProduction(building.getProductionSpeed());
        	}
            if (building.getTierLevel() == 1) {
                if("Zeus".equals(building.getFaction())) {
                    Unit newUnit = UnitFactory.createUnit(unitType, 1, "Zeus", position);
                    ((UnitProducer) building).getProductionQueue().add(newUnit);
                    if (newUnit instanceof Worker) {
                        for (Building b : manager.getBuildings()) {
                            if (b instanceof HQ && b.getPosition()==newUnit.getPosition()) {
                                ((Worker) newUnit).setCurrentHQ((HQ) b);
                                break; 
                            }
                        }
                    }
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
                Unit unit=queue.remove(0);
                int line = building.getPosition().getLine()-1;
                int column = building.getPosition().getColumn() + 1;
                
                if(column < manager.getMap().getColumnCount()) {
                    Block spawnBlock = manager.getMap().getBlock(line, column);
                    unit.setPosition(spawnBlock);
                    manager.addInUnits(unit);
                    if(!queue.isEmpty()){
                        building.setCurrentProduction(building.getProductionSpeed());
                    }
                }
             }
        }
    }
    
    //put here in each case the action wanted for your building
    public void action(String button) {
    	switch (button) {
    	case "button1":
    		if (manager.getSelectedBuild().getBuildingName().equals("Camp Olympique")) {
    			if(!manager.getSelectedBuild().getIsUnderConstruction()) {
    				UnitProducer hq = (UnitProducer) ((HQ) manager.getSelectedBuild()).getWorkerProducer();// forced cast not optimal
    				manager.addQueue(hq, manager.getSelectedBuild().getPosition(), "WORKER");
    			}
    		}
    		
    		
    		break;
    	case "button2":
    		break;
    	case "button3":
    		break;
    	case "button4":
    		break;
    	case "button5":
    		break;
    	case "button6":
    		break;
    	}
    }
}
