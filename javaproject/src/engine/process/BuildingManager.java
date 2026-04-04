package engine.process;

import java.util.ArrayList;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.mobile.Player;
import engine.mobile.building.Building;
import engine.mobile.building.BuildingStats;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.UnitStats;
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


	public void selectBuilding(String type) {
		this.selectedBuilding = type;
	}

	public int buildBuilding(Block position,int tier,String faction, Player p) {
		/**
		 * Build a building, returns 0 if it fails
		 */
		if (selectedBuilding == null){
			return 0;
		}
		if (position.getLine() >= 7 && position.getColumn() < GameConfiguration.COLUMN_COUNT - 28) {
			Building newBuilding = BuildingFactory.createBuilding(selectedBuilding, tier, faction, position);

			if (newBuilding != null) {
				if(newBuilding.getAmbroisieCost()>p.getAmbroisieStock()||newBuilding.getFaithCost()>p.getFaithStock()) {
					//if the player don't have the funds to build
					return 0;
				}
				
				manager.addInBuildings(newBuilding);
				p.getBuiltBuilding().add(newBuilding);
				p.setAmbroisieStock(p.getAmbroisieStock()-newBuilding.getAmbroisieCost());
				p.setFaithStock(p.getFaithStock()-newBuilding.getFaithCost());
			}
			selectedBuilding = null;
			return 1;
		}
		return 0;
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
			//System.out.println("nb build : "+nbOfBuildings);

			for(int buildingIndex=0; buildingIndex<nbOfBuildings; buildingIndex++) {
				Block buildingPosition = buildings.get(buildingIndex).getPosition();
				for(int blockIndex = 0; blockIndex<nbOfBlocksInSelectedArea;blockIndex++) {
					if(selectedArea.get(blockIndex).equals(buildingPosition)) {
						buildingsInSelectedArea.add(buildings.get(buildingIndex));
						//System.out.println(buildings.get(buildingIndex).getBuildingName());
						break;
					}
				}
			}

			if (!buildingsInSelectedArea.isEmpty()) {
				manager.setSelectedBuild(buildingsInSelectedArea.get(0));
				manager.setSelectedWorker(null);
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
	/*
	public void addQueue(UnitProducer building, Block position,String unitType,Player p) {
		/**
		 * Adds to UnitProducer's queue the next unit to produce
		 */
	/*
		
		if (building.getProductionQueue().size() < 3) { //max 3 unit in queue
			Unit newUnit=null;
			if(building.getProductionQueue().isEmpty()) {
				building.setCurrentProduction(building.getProductionSpeed());
			}
			if (building.getTierLevel() >= 1) {
				if(DefaultGameSettings.ZEUS.equalsIgnoreCase(building.getFaction())) {
					newUnit = UnitFactory.createUnit(unitType, 1, "Zeus", position);
					if(p.getFactionName().equals(DefaultGameSettings.DEFAULT_PLAYER_FACTION)) {
						p.setCurrentPopulation(p.getCurrentPopulation()+newUnit.getPopCost());
					}
				}else if(DefaultGameSettings.HADES.equalsIgnoreCase(building.getFaction())) {
					newUnit = UnitFactory.createUnit(unitType, 1, "Hades", position);
					if(p.getFactionName().equalsIgnoreCase("Hades")) {
						p.setCurrentPopulation(p.getCurrentPopulation()+newUnit.getPopCost());
					}
				}else if(DefaultGameSettings.POSEIDON.equalsIgnoreCase(building.getFaction())) {
					newUnit = UnitFactory.createUnit(unitType, 1, "Poseidon", position);
					if(p.getFactionName().equalsIgnoreCase("Poseidon")) {
						p.setCurrentPopulation(p.getCurrentPopulation()+newUnit.getPopCost());
					}
				}
				((UnitProducer) building).getProductionQueue().add(newUnit);
				p.getCreatedUnits().add(newUnit);
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
	*/
	public void addQueue(UnitProducer building, Block position,String unitType,Player p) {
		/**
		 * Adds to UnitProducer's queue its next unit to produce
		 */
		if(building.getIsUnderConstruction()==false&&building.getProductionQueue().size()<3) {
			String key = unitType+ "_" + building.getFaction().toUpperCase() + "_" + building.getTierLevel();
			UnitStats stats = UnitRepository.getInstance().getStats(key);
			System.out.println(key);
			System.out.println(stats);
			if(stats!=null) {
				if(building.getProductionQueue().isEmpty()) {
					building.setCurrentProduction(building.getProductionSpeed());
				}
				Unit newUnit = UnitFactory.createUnit(unitType, building.getTierLevel(), p.getFactionName(), position);
				if (newUnit instanceof Worker) {
					//if it's a worker, he needs to be affiliated with the building (his HQ)
					for (Building b : manager.getBuildings()) {
						if (b instanceof HQ && b.getPosition()==newUnit.getPosition()) {
							((Worker) newUnit).setCurrentHQ((HQ) b);
							break; 
						}
					}
				}
				((UnitProducer) building).getProductionQueue().add(newUnit);
				p.getCreatedUnits().add(newUnit);
				building.setCurrentProduction(building.getCurrentProduction()+1);
			}
		}
		
	}
	public void removeQueue(UnitProducer building) {
		/**
		 * reduce the timer for the creation of new units,
		 * creates the corresponding unit when the timer hits 0
		 */
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
	public void action(String button, Player p) {
		/*
		 * Take the corresponding action for each button in bottom right part of the screen (adding unit to unitProducers)
		 */
		//System.out.println("Bouton"+ button+"cliqué pour : " + manager.getSelectedBuild().getBuildingName());
		switch (button) {
		case "button1":			
			//first button for unitProducers
			if(manager.getSelectedBuild() instanceof HQ) {
				//UnitProducer hq = (UnitProducer) ((HQ) manager.getSelectedBuild()).getWorkerProducer();
					UnitProducer hq = (UnitProducer) ((HQ) manager.getSelectedBuild()).getWorkerProducer();
					manager.addQueue(hq, manager.getSelectedBuild().getPosition(), "WORKER",p);
			}
			if(manager.getSelectedBuild() instanceof UnitProducer) {
				UnitProducer producer = (UnitProducer) manager.getSelectedBuild();
				switch(producer.getBuildingName()) {
				case("Colisée d'Atlantide"):
				case("Camp spartiate"):
				case("prytanée"):
				case("Fosse sous marine"):
				case("Autel de la sagesse"):
					manager.addQueue(producer, manager.getSelectedBuild().getPosition(), "INFANTRY", p);
					break;
				case("Camp Olympique"):
				case("Puit d'invocation"):
				case("Cascade"):
				case("Portail vers les champs Élysées"):
					manager.addQueue(producer, manager.getSelectedBuild().getPosition(), "ARTILLERY", p);
					break;
				}
			}
			
			
			/*
			 * switch(name) {
				case("Camp spartiate"):
					image1=imageRepertory+"Infantry.png";
					break;
				case("Colisée d'Atlantide"):
					image1=imageRepertory+"Infantry.png";
					break;
				case("Camp Olympique"):
					image1=imageRepertory+"Artillery.png";
					break;
				case("Puit d'invocation"):
					image1=imageRepertory+"Artillery.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Cascade"):
					image1=imageRepertory+"Artillery.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("prytanée"):
					image1=imageRepertory+"Infantry.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Portail vers les champs Élysées"):
					image1=imageRepertory+"Artillery.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Fosse sous marine"):
					image1=imageRepertory+"Infantry.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Autel de la sagesse"):
					image1=imageRepertory+"Infantry.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				}
			 */
				
			
			
			
			if (manager.getSelectedBuild() instanceof ResearchBuilding && !p.getTechnologies().contains("attackDamage_1.25") ) {
				if(!manager.getSelectedBuild().getIsUnderConstruction()) {
					p.addTechnologieUnlocked("attackDamage_1.25");
					String id = null;
					if(p.getFactionName().equalsIgnoreCase("ZEUS")) { // We only upgrade the unit of tier 1
						id = "ARTILLERY";
					} else{
						id = "INFANTRY";
					}
					String key = id+ "_" + manager.getSelectedBuild().getFaction().toUpperCase() + "_" + 1;
					//System.out.println(key);
					UnitStats stats = UnitRepository.getInstance().getStats(key);
					stats.setAttackDamage(stats.getAttackDamage()*1.25);
					//System.out.println("Attack damage of "+id+" of tier increased by 1.25 times");
					manager.setNotifText("Atk des "+id+" multiplié par 1.25",true);
					for (Unit u :manager.getUnits()) {
						if(!(u instanceof Worker) && (u.getUnitFaction().equals(p.getFactionName()) && u.getTierLevel()==1)) {
							u.setATK(u.getATK()*1.25);	
						}
					}

				}
			}
			break;
		case "button2":
			if (manager.getSelectedBuild() instanceof ResearchBuilding && !p.getTechnologies().contains("productionSpeed_2") ) {
				if(!manager.getSelectedBuild().getIsUnderConstruction()) {
					p.addTechnologieUnlocked("productionSpeed_2");
					String key = "Producer".toUpperCase() + "_" + manager.getSelectedBuild().getFaction().toUpperCase() + "_" + 1;
					BuildingStats stats = BuildingRepository.getInstance().getStats(key);
					stats.setProductionSpeed(stats.getProductionSpeed()/2);
					System.out.println("amelioration de la production speed effectué");
					manager.setNotifText("Vitesse de prod amélioré",true);
					for (Building b :manager.getBuildings()) {
						if(b instanceof UnitProducer && b.getFaction().equals(p.getFactionName())) {
							UnitProducer unitProducer= (UnitProducer) b;
							unitProducer.setProductionSpeed(unitProducer.getProductionSpeed()/2);
						}
					}
						
				}
			}
			if(manager.getSelectedBuild() instanceof UnitProducer) {
				//if its the second button of a unit producer
				UnitProducer producer = (UnitProducer) manager.getSelectedBuild();	
				manager.addQueue(producer, manager.getSelectedBuild().getPosition(), "CAVALRY", p);//the second button always create a cavalery unit
				
				}
			
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

	/*
    public Unit closestEnnemy(DefenseTower tower) {
        Unit nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Unit unit : manager.getUnits()) {
        	double dist = manager.getDistance(tower.getPosition(), unit.getPosition());

        	if (dist < minDistance&&unit.getUnitFaction()!=tower.getFaction()) {

            minDistance = dist;
            nearest = unit;
        	}
        }
        return nearest;
    }
	 */
	public void setTowerTarget(DefenseTower tower) {
		if(tower.getIsUnderConstruction()==false) {
			Unit target = null;
			double minDistance = Double.MAX_VALUE;

			for (Unit unit : manager.getUnits()) {
				double dist = manager.getDistance(tower.getPosition(), unit.getPosition());

				if (dist < minDistance&&!(unit.getUnitFaction().equalsIgnoreCase(tower.getFaction()))) {
					minDistance = dist;
					target = unit;
				}
			}
			tower.setTarget(target);
		}
	}

	public void singleTowerAttackSystem(DefenseTower tower) {
		/**
		 * Seek for a potential target and attacks if possible
		 */
		if(tower.getIsUnderConstruction()==false) { //first check, for performance purpose
			Unit target = tower.getTarget();
			if(tower.getAttackCounter()<tower.getAttackTime()) {
				tower.setIsAttacking(0);
				tower.setAttackCounter(tower.getAttackCounter()+tower.getTowerAttackSpeed());
			}

			if (target!=null&&tower.getAttackTime()<=tower.getAttackCounter()) {
				tower.setIsAttacking(1);	
				tower.resetAttackCounter();
				if(manager.getDistance(tower.getPosition(),target.getPosition())<tower.getTowerRange()) {
					//if the closestEnnemy is in range, the tower attacks
					double attack=tower.getTowerDamage();

					if (target instanceof Infantry) {
						Infantry infantryTarget = (Infantry) target;
						double shield = infantryTarget.getShieldValue();
						if (shield > 0) {
							if (shield >= attack) {
								infantryTarget.setShieldValue(shield - attack);
								attack = 0;
							} else {
								infantryTarget.setShieldValue(0);
								attack -= shield;
							}
						}
					}
					if (attack > 0) {
						int newHp = (int) Math.max(0, target.getHp() - attack);
						target.setHp(newHp);
					}

					if (target.getHp() <= 0) {
						tower.setTarget(null);
					}

				}

			}
		}
	}


	public void allTowerAttack(ArrayList<Building> buildings) {
		for(Building building:buildings) {
			if(building instanceof DefenseTower) {
				singleTowerAttackSystem((DefenseTower) building);
			}
		}
	}


}
