package engine.process;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.mobile.CPU;
import engine.mobile.Player;
import engine.mobile.building.Building;
import engine.mobile.building.BuildingStats;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.building.PopulationBuilding;
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
import log.LoggerUtility;



public class BuildingManager implements BuildingInterface{
	private static Logger logger = LoggerUtility.getLogger(BuildingManager.class, "html");
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
		 * Build a building, 
		 * returns 0 if not enough founds
		 * returns -1 if no building was selected by the building
		 * 
		 */
		if (p.getBuildingToBuildID()==null){
			return -1;
		}
		if (position.getLine() >= 7 && position.getColumn() < GameConfiguration.COLUMN_COUNT - 28) {
			Building newBuilding = BuildingFactory.createBuilding(p.getBuildingToBuildID(), tier, faction, position);

			if (newBuilding != null) {
				if(newBuilding.getAmbroisieCost()>p.getAmbroisieStock()||newBuilding.getFaithCost()>p.getFaithStock()) {
					//if the player don't have the funds to build
					
					return 0;
				}
				
				manager.addInBuildings(newBuilding);
				p.getBuiltBuilding().add(newBuilding);
				if(newBuilding instanceof ResearchBuilding) {
					p.getResearchBuildings().add((ResearchBuilding) newBuilding);
				}
				if (newBuilding instanceof PopulationBuilding) {
			        PopulationBuilding popBuild = (PopulationBuilding) newBuilding;
			        p.setMaxPopulation(p.getMaxPopulation() + popBuild.getPopulationProvided());
			    }
				p.setAmbroisieStock(p.getAmbroisieStock()-newBuilding.getAmbroisieCost());
				p.setFaithStock(p.getFaithStock()-newBuilding.getFaithCost());
			}
			p.setBuildingToBuildID(null);;
			return 1;
		}
		return -1;
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
	public void addQueue(UnitProducer building, Block position, String unitType, Player p) {
	    /**
	     * Adds to UnitProducer's queue its next unit to produce
	     */
	    if (building.getIsUnderConstruction() == false && building.getProductionQueue().size() < 3) {
	        String key = unitType + "_" + building.getFaction().toUpperCase() + "_" + building.getTierLevel();
	        UnitStats stats = UnitRepository.getInstance().getStats(key);
	        
	        if (stats != null) {
	            Unit newUnit = UnitFactory.createUnit(unitType, building.getTierLevel(), p.getFactionName(), position);
	            
	            if (newUnit != null) {
	            	if (p.getCurrentPopulation() + newUnit.getPopCost() > p.getMaxPopulation()) {
	                    return;
	                }
	            }
	                if (p.getAmbroisieStock() >= newUnit.getACost() && p.getFaithStock() >= newUnit.getFCost()) {
	                    
	                    p.setAmbroisieStock(p.getAmbroisieStock() - newUnit.getACost());
	                    p.setFaithStock(p.getFaithStock() - newUnit.getFCost());

	                    if (building.getProductionQueue().isEmpty()) {
	                        building.setCurrentProduction(building.getProductionSpeed());
	                    }
	                    
	                    if (newUnit instanceof Worker) {
	                        for (Building b : manager.getBuildings()) {
	                            if (b instanceof HQ && b.getPosition().equals(newUnit.getPosition())) {
	                                ((Worker) newUnit).setCurrentHQ((HQ) b);
	                                break; 
	                            }
	                        }
	                    }
	                    
	                    ((UnitProducer) building).getProductionQueue().add(newUnit);
	                    p.getCreatedUnits().add(newUnit);
	                    p.setCurrentPopulation(p.getCurrentPopulation()+newUnit.getPopCost());
	                    
	                    if (p instanceof CPU) {
	                        System.out.println("[CPU] Unité ajoutée : " + unitType + newUnit.getTierLevel() 
	                            + " (Cout Pop: " + newUnit.getPopCost() + ")"
	                            + " | Population totale : " + p.getCurrentPopulation() 
	                            + "/" + p.getMaxPopulation());
	                    }
	                    
	                    building.setCurrentProduction(building.getCurrentProduction() + 1);
	                } else {
	                    System.out.println("Fonds insuffisants pour créer : " + unitType);
	                }
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
				//System.out.println(producer.getBuildingName());
				switch(producer.getBuildingName()) {
				case("Colisée d’Atlantide"):
				case("Camp Spartiate"):
				case("prytanée"):
				case("Fosse sous marine"):
				case("Autel de la sagesse"):
					manager.addQueue(producer, manager.getSelectedBuild().getPosition(), "INFANTRY", p);
					break;
				case("Camp Olympique"):
				case("Puit d’invocation"):
				case("Cascade"):
				case("Portail vers les champ Élysées"):
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
					ResearchBuilding research = (ResearchBuilding) manager.getSelectedBuild();
					if(research.getResearch2() <= ResearchBuilding.getResearchTime()) {
						research.setResearchActive2(true);
						System.out.println("Recherche d'attaque start");
						manager.setNotifText("Recherche d'attaque en cours", true);
					}
				}
			}
			break;
		case "button2":
			if (manager.getSelectedBuild() instanceof ResearchBuilding && (!p.getTechnologies().contains("productionSpeed_2") || !p.getTechnologies().contains("productionSpeed_3")) ) {
				if(!manager.getSelectedBuild().getIsUnderConstruction()) {
					ResearchBuilding research =(ResearchBuilding) manager.getSelectedBuild();
					if(research.getResearch1()<=ResearchBuilding.getResearchTime()) {
						research.setResearchActive1(true);
						System.out.println("Recherche d'amelioration production start");
						manager.setNotifText("Amélioration de la Production en cours", true);
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

	public void researchTime(Player p) {
		for (ResearchBuilding r : p.getResearchBuildings()) {
				if (r.isResearchActive1()) {
					if (r.getResearch1() < ResearchBuilding.getResearchTime()) {
						r.setResearch1(r.getResearch1() + 1);
					} else {
						r.setResearchActive1(false);

						String techName = r.getTechnologiesUnlocked().get(0);

						if (!p.getTechnologies().contains(techName)) {
							p.addTechnologieUnlocked(techName);

							String key = "PRODUCER_" + p.getFactionName().toUpperCase() + "_1";
							BuildingStats stats = BuildingRepository.getInstance().getStats(key);

							if (stats != null) {
								int value = Integer.valueOf(techName.split("_")[1]);
								stats.setProductionSpeed(stats.getProductionSpeed() / value);

								System.out.println("Vitesse de prod : fois" + value);

								if (!(p instanceof CPU)) {
									manager.setNotifText("Vitesse de prod améliorée", true);
								}

								for (Building prodBuilding : manager.getBuildings()) {
									if (prodBuilding instanceof UnitProducer && prodBuilding.getFaction().equals(p.getFactionName())) {
										UnitProducer unitProducer = (UnitProducer) prodBuilding;
										unitProducer.setProductionSpeed(unitProducer.getProductionSpeed() / value);
									}
								}
							}
						}
					}
				}

				if (r.isResearchActive2()) {
					if (r.getResearch2() < ResearchBuilding.getResearchTime()) {
						r.setResearch2(r.getResearch2() + 1);
					} else {
						r.setResearchActive2(false);
						
						String techName = "attackDamage_1.25";
						
						if (!p.getTechnologies().contains(techName)) {
							p.addTechnologieUnlocked(techName);
							
							String id = (p.getFactionName().equalsIgnoreCase("ZEUS")) ? "ARTILLERY" : "INFANTRY";
							String key = id + "_" + p.getFactionName().toUpperCase() + "_1";
							
							UnitStats stats = UnitRepository.getInstance().getStats(key);
							
							if(stats != null) {
								stats.setAttackDamage(stats.getAttackDamage() * 1.25);
								System.out.println("Attaque des " + id + "augménté");
								
								if (!(p instanceof CPU)) {
									manager.setNotifText("Atk des " + id + " améliorée", true);
								}
								
								for (Unit u : manager.getUnits()) {
									if (!(u instanceof Worker) && u.getUnitFaction().equals(p.getFactionName()) && u.getTierLevel() == 1) {
										u.setATK(u.getATK() * 1.25);	
									}
								}
							}
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
