package engine.process;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.building.BuildingStats;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.building.PopulationBuilding;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;

/**
 * 
 * Factory pattern class. Used to initialize building in the engine.
 * 
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 * 
 * 
 */

public class BuildingFactory {

	public static final String PRODUCER_BUILDING = "Producer";
	public static final String HQ_BUILDING = "HQ";
	public static final String DEFENSE_BUILDING = "DefenseTower";
	public static final String POPULATION_BUILDING = "PopulationBuilding";
	public static final String RESEARCH_BUILDING = "ResearchBuilding";

	public static Building createBuilding(String type, int tier, String faction, Block position) {
		String key = type.toUpperCase() + "_" + faction.toUpperCase() + "_" + tier;
		BuildingStats stats = BuildingRepository.getInstance().getStats(key);
		System.out.println("CLÉ RECHERCHÉE : [" + key + "]"); // <-- AJOUTE CECI
		Building building = null;
		
		switch (type) {
		case PRODUCER_BUILDING :
			UnitProducer producer = new UnitProducer(position);
            producer.setProductionQueue(new ArrayList<Unit>());
            producer.setProductionSpeed(stats.getProductionSpeed());
            building = producer;
            break;
		case HQ_BUILDING:
            HQ hq = new HQ(position);
            hq.setWorkerProducer(new UnitProducer(position));
            hq.getWorkerProducer().setProductionQueue(new ArrayList<Unit>());
            hq.getWorkerProducer().setProductionSpeed(stats.getProductionSpeed());
            hq.getWorkerProducer().setTierLevel(tier);
            hq.getWorkerProducer().setFaction(faction);
            
            hq.setDefenseDamage(stats.getTowerDamage());
            hq.setDefenseRange(stats.getTowerRange());
            hq.setDefenseAttackSpeed(stats.getTowerAttackSpeed());
            building = hq;
            break;
		case DEFENSE_BUILDING :
			DefenseTower defense= new DefenseTower(position);
			defense.setTowerDamage(stats.getTowerDamage());
			defense.setTowerAttackSpeed(stats.getTowerAttackSpeed());
			defense.setTowerRange(stats.getTowerRange());
			building = defense;
            break;
		case POPULATION_BUILDING :
			PopulationBuilding population= new PopulationBuilding(position);
			population.setPopulationProvided(stats.getPopulationProvided());
			building = population;
            break;
		case RESEARCH_BUILDING :
			ResearchBuilding research= new ResearchBuilding(position);
			research.setTechnologiesUnlocked(stats.getTechnologieUnlocked());
			building = research;
            break;
		default:
			throw new IllegalArgumentException("Unknown operation type : " + type);
		}
		building.setBuildingName(stats.getId()); 
        building.setFaction(faction);
        building.setTierLevel(tier);
        building.setMaxHp(stats.getMaxHp());
        building.setHp(stats.getMaxHp());
        building.setAmbroisieCost(stats.getAmbroisieCost());
        building.setFaithCost(stats.getFaithCost());
        building.setConstructionTime(stats.getConstructionTime());
        building.setUnderConstruction(true);
		
		return building;
	}
	/*
        switch (type) {

            //Unit prod
            case PRODUCER_BUILDING:
                UnitProducer producer = new UnitProducer(position);

                // common value
                producer.setTierLevel(tier);
                producer.setUnderConstruction(true); // The building is under construction
                ArrayList<Unit> queue = new ArrayList<Unit>();
                producer.setProductionQueue(queue); // currently empty
                producer.setProductionSpeed(10);
                producer.setMaxHp(1000);

                //TIER 
                if (tier == 1) {
                    producer.setMaxHp(1000);
                    producer.setConstructionTime(5);
                    producer.setFaction(faction);

                    if (faction.equals(ZEUS)) {
                        producer.setBuildingName("Camp Olympique");
                    } else if (faction.equals(HADES)) {
                        producer.setBuildingName("Camp Spartiate");
                    } else if (faction.equals(POSEIDON)) {
                        producer.setBuildingName("Colisée d'Atlantide");
                    }
                }
                else if (tier == 2) {
                    producer.setMaxHp(1500); // More resistant
                    producer.setConstructionTime(200); // Much more construction time

                    if (faction.equals(ZEUS)) {
                        producer.setBuildingName("Prytanée");
                    } else if (faction.equals(HADES)) {
                        producer.setBuildingName("Puits d'invocation");
                    } else if (faction.equals(POSEIDON)) {
                        producer.setBuildingName("Cascade");
                    }
                }
                else if (tier == 3) {
                    producer.setMaxHp(2000);
                    producer.setConstructionTime(300);

                    if (faction.equals(ZEUS)) {
                        producer.setBuildingName("Autel de la sagesse");
                    } else if (faction.equals(HADES)) {
                        producer.setBuildingName("Portail vers les Champs Élysées");
                    } else if (faction.equals(POSEIDON)) {
                        producer.setBuildingName("Fosse sous-marine");
                    }
                }

                return producer;
            case HQ_BUILDING:
                HQ HQ = new HQ(position);

                // common value
                HQ.setTierLevel(tier);
                HQ.setWorkerProducer(new UnitProducer(position));
                HQ.setUnderConstruction(true); // The building is under construction
                ArrayList<Unit> WorkerQueue = new ArrayList<Unit>();
                HQ.getWorkerProducer().setProductionQueue(WorkerQueue);
                HQ.getWorkerProducer().setProductionSpeed(4);
                HQ.setMaxHp(1000);
                HQ.getWorkerProducer().setTierLevel(tier);

                //TIER 
                if (tier == 1) {
                	HQ.setMaxHp(1000);
                	HQ.setConstructionTime(5);
                	HQ.setFaction(faction);
                    HQ.getWorkerProducer().setFaction(faction);

                    if (faction.equals(ZEUS)) {
                    	HQ.setBuildingName("Camp Olympique");
                    } else if (faction.equals(HADES)) {
                    	HQ.setBuildingName("Camp Spartiate");
                    } else if (faction.equals(POSEIDON)) {
                    	HQ.setBuildingName("Colisée d'Atlantide");
                    }
                }
                else if (tier == 2) {
                	HQ.setMaxHp(1500); // More resistant
                	HQ.setConstructionTime(200); // Much more construction time

                    if (faction.equals(ZEUS)) {
                    	HQ.setBuildingName("Prytanée");
                    } else if (faction.equals(HADES)) {
                    	HQ.setBuildingName("Puits d'invocation");
                    } else if (faction.equals(POSEIDON)) {
                    	HQ.setBuildingName("Cascade");
                    }
                }
                else if (tier == 3) {
                	HQ.setMaxHp(2000);
                	HQ.setConstructionTime(300);

                    if (faction.equals(ZEUS)) {
                    	HQ.setBuildingName("Autel de la sagesse");
                    } else if (faction.equals(HADES)) {
                    	HQ.setBuildingName("Portail vers les Champs Élysées");
                    } else if (faction.equals(POSEIDON)) {
                    	HQ.setBuildingName("Fosse sous-marine");
                    }
                }

                return HQ;
		default:
			throw new IllegalArgumentException("Unknown operation type : " + type);
			}
	 */
}
