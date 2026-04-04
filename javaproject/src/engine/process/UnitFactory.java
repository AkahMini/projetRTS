package engine.process;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import engine.map.Block;
import engine.mobile.unit.Artillery;
import engine.mobile.unit.Cavalry;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.UnitStats;
import engine.mobile.unit.UnitsStatsLoader;
import engine.mobile.unit.Worker;

/**
 * 
 * Factory pattern class. Used for initialize Unit.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 * 
 */

public class UnitFactory { 
    
    public static final String INFANTRY_UNIT = "INFANTRY";
    public static final String ARTILLERY_UNIT = "ARTILLERY";
    public static final String WORKER_UNIT = "WORKER";
    public static final String CAVALRY_UNIT = "CAVALRY";
    

	public static Unit createUnit(String type, int tier, String faction, Block position) {
		String key = type.toUpperCase() + "_" + faction.toUpperCase() + "_" + tier;
		UnitStats stats = UnitRepository.getInstance().getStats(key);
		//System.out.println(stats);
		Unit unit = null;
		if(stats==null) {
			throw new IllegalArgumentException("Unknown key: " + key);
		}
		switch (type) {
		case INFANTRY_UNIT :
			Infantry infantry = new Infantry(position);
            infantry.setMaxShield(stats.getMaxShield());
            infantry.setShieldValue(stats.getMaxShield());
            unit=infantry;
            break;
		case ARTILLERY_UNIT:
            Artillery artillery = new Artillery(position);
            artillery.setBlastRadius(stats.getBlastRadius());
            unit = artillery;
            break;
		case CAVALRY_UNIT :
			Cavalry cavalry= new Cavalry(position);
			cavalry.setChargeBonusDamage(stats.getChargeBonusDamage());
			cavalry.setChargeDistanceMax(stats.getChargeDistanceMax());
			cavalry.setChargeDistanceValue(0);
			cavalry.setChargeSpeed(stats.getChargeSpeed());
			cavalry.setThroughObstacles(stats.isThroughObstacles());
			unit=cavalry;
            break;
		case WORKER_UNIT :
			Worker worker= new Worker(position);
			worker.setIsWorking(false);
			worker.setMaxCargoCapacity(100);
			worker.setCurrentRessourceLoad(0);
			unit = worker;
            break;
		default:
			throw new IllegalArgumentException("Unknown operation type : " + type);
		}
		unit.setUnitName(stats.getId()); 
		unit.setUnitFaction(stats.getFaction());
		unit.setTierLevel(stats.getTierLevel());

		unit.setMaxHp(stats.getMaxHp());
		unit.setHp(stats.getMaxHp());
		unit.setPopCost(stats.getPopulationCost());
		unit.setACost(stats.getAmbroisieCost());
		unit.setFCost(stats.getFaithCost());

		unit.setATK(stats.getAttackDamage());
		unit.setATKSpeed(stats.getAttackSpeed());
		unit.setMovementSpeed(stats.getMovementSpeed());
		unit.setATKRange(stats.getAttackRange());
		unit.setVision(stats.getVisionRange());
		unit.setHpRegen(stats.getHpRegenRate());

		// Default Values
		unit.setMoveCounter(0);
		unit.setAttackCounter(0.0);
		unit.setIsInCombat(false);
		unit.setTarget(null);
		unit.setDestination(null);
		
		return unit;
	}
}
