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
		//System.out.println(stats);
		Building building = null;
		if(stats==null) {
			throw new IllegalArgumentException("Unknown key: " + key);
		}
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
		building.setVision(stats.getVisionRange());
        
		return building;
	}
}
