package engine.process;

import java.util.ArrayList;
import java.util.HashMap;

import config.DefaultGameSettings;
import engine.map.Block;
import engine.mobile.unit.Unit;
import engine.mobile.unit.UnitStats;
import engine.mobile.unit.Infantry;
import engine.mobile.building.Building;
import engine.mobile.building.BuildingStats;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.building.PopulationBuilding;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Artillery;
import engine.mobile.unit.Cavalry;
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
    
    
    /*
    public static Unit createUnit(String unit_ID,Block position, HashMap<String,ArrayList<Float>> stats){
    	Infantry unknownUnit = new Infantry(position);
    	ArrayList<Float> unitData = stats.get(unit_ID);
    	
    	if(unitData==null) {
    		throw new IllegalArgumentException("Clé inexistante : " + unit_ID);
 
    	}
    	
    	int unitType=unitData.get(1).intValue();
    	System.out.println(unitType);
    	switch(unitType) {

    		
    	case(2)://Cavalry
    		
    	
    	case(3)://Artillery
    		Artillery artillery = new Artillery(position);
	    	
    	default:
    		return unknownUnit;
    	}
    	
    }
    */
    
    public static Infantry createInfantry(String unitID,Block position, HashMap<String,ArrayList<Float>> stats) {
    	Infantry infantry = new Infantry(position);
    	ArrayList<Float> unitData = stats.get(unitID);
    	
    	if(unitData==null) {
    		throw new IllegalArgumentException("Clé inexistante : " + unitID);
    	}
    	if(unitData.get(0)!=1) {
    		throw new IllegalArgumentException("Unit "+ unitID + " is not an Infantry");
    	}
    	infantry.setUnitName(unitID);
		infantry.setUnitFaction(decodeFaction(unitData.get(1).intValue()));
		infantry.setTierLevel(unitData.get(2).intValue());;
		infantry.setMaxHp(unitData.get(3).intValue());
		infantry.setPopCost(unitData.get(4).intValue());
		infantry.setACost(unitData.get(5).intValue());
		infantry.setFCost(unitData.get(6).intValue());
		infantry.setATK(unitData.get(7).intValue());
		infantry.setATKSpeed(unitData.get(8));
		infantry.setMovementSpeed(unitData.get(9));
		infantry.setATKRange(unitData.get(10));
		infantry.setVision(unitData.get(11));
		infantry.setHpRegen(unitData.get(12).intValue());
		
		infantry.setPosition(position);
		infantry.setHp(infantry.getMaxHp());
    	
    	return infantry;
    }
    
    public static Cavalry createCavalry(String unitID,Block position, HashMap<String,ArrayList<Float>> stats) {
    	Cavalry cavalry = new Cavalry(position);
    	ArrayList<Float> unitData = stats.get(unitID);
    	
    	if(unitData==null) {
    		throw new IllegalArgumentException("Clé inexistante : " + unitID);
    	}
    	if(unitData.get(0)!=2) {
    		throw new IllegalArgumentException("Unit "+ unitID + " is not an Infantry");
    	}
    	cavalry.setUnitName(unitID);
		cavalry.setUnitFaction(decodeFaction(unitData.get(1).intValue()));
		cavalry.setTierLevel(unitData.get(2).intValue());;
		cavalry.setMaxHp(unitData.get(3).intValue());
		cavalry.setPopCost(unitData.get(4).intValue());
		cavalry.setACost(unitData.get(5).intValue());
		cavalry.setFCost(unitData.get(6).intValue());
		cavalry.setATK(unitData.get(7).intValue());
		cavalry.setATKSpeed(unitData.get(8));
		cavalry.setMovementSpeed(unitData.get(9));
		cavalry.setATKRange(unitData.get(10));
		cavalry.setVision(unitData.get(11));
		cavalry.setHpRegen(unitData.get(12).intValue());
		
		cavalry.setPosition(position);
		cavalry.setHp(cavalry.getMaxHp());
		return cavalry;
    }
    
    public static Artillery createArtillery(String unitID,Block position, HashMap<String,ArrayList<Float>> stats) {
    	Artillery artillery = new Artillery(position);
    	ArrayList<Float> unitData = stats.get(unitID);
    	
    	if(unitData==null) {
    		throw new IllegalArgumentException("Clé inexistante : " + unitID);
    	}
    	if(unitData.get(0)!=3) {
    		throw new IllegalArgumentException("Unit "+ unitID + " is not an Artillery");
    	}
    	artillery.setUnitName(unitID);
    	artillery.setUnitFaction(decodeFaction(unitData.get(1).intValue()));
    	artillery.setTierLevel(unitData.get(2).intValue());;
    	artillery.setMaxHp(unitData.get(3).intValue());
    	artillery.setPopCost(unitData.get(4).intValue());
    	artillery.setACost(unitData.get(5).intValue());
    	artillery.setFCost(unitData.get(6).intValue());
    	artillery.setATK(unitData.get(7).intValue());
    	artillery.setATKSpeed(unitData.get(8));
    	artillery.setMovementSpeed(unitData.get(9));
    	artillery.setATKRange(unitData.get(10));
    	artillery.setVision(unitData.get(11));
		artillery.setHpRegen(unitData.get(12).intValue());
		
		artillery.setPosition(position);
		artillery.setHp(artillery.getMaxHp());
		return artillery;
    	
    }
    
    public static Worker createWorker(String unitID,Block position, HashMap<String,ArrayList<Float>> stats) {
    	
    	ArrayList<Float> unitData = stats.get(unitID);
    	if(unitData==null) {
    		throw new IllegalArgumentException("Clé inexistante : " + unitID);
 
    	}
    	if(unitData.get(0)!=0) {
    		throw new IllegalArgumentException("Unit "+ unitID + " is not a Worker");
    	}
    	Worker worker = new Worker(position);
		worker.setUnitFaction(decodeFaction(unitData.get(1).intValue()));
		worker.setTierLevel(unitData.get(2).intValue());;
		worker.setMaxHp(unitData.get(3).intValue());
		worker.setPopCost(unitData.get(4).intValue());
		worker.setACost(unitData.get(5).intValue());
		worker.setFCost(unitData.get(6).intValue());
		worker.setATK(unitData.get(7).intValue());
		worker.setATKSpeed(unitData.get(8));
		worker.setMovementSpeed(unitData.get(9));
		worker.setATKRange(unitData.get(10));
		worker.setVision(unitData.get(11));
		worker.setHpRegen(unitData.get(12).intValue());
		
		worker.setPosition(position);
		worker.setHp(worker.getMaxHp());
		worker.setIsWorking(false);
		return worker;
		
    }
    
    
    
    public static final String INFANTRY_UNIT = "INFANTRY";
    public static final String ARTILLERY_UNIT = "ARTILLERY";
    public static final String WORKER_UNIT = "WORKER";
    public static final String CAVALRY_UNIT = "CAVALRY";
    

	public static Unit createUnit(String type, int tier, String faction, Block position) {
		String key = type.toUpperCase() + "_" + faction.toUpperCase() + "_" + tier;
		UnitStats stats = UnitRepository.getInstance().getStats(key);
		System.out.println(stats);
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
			cavalry.setChargeDistanceValue(stats.getChargeDistanceMax());
			cavalry.setChargeSpeed(stats.getChargeSpeed());
			cavalry.setThroughObstacles(stats.isThroughObstacles());
			unit=cavalry;
            break;
		case WORKER_UNIT :
			Worker worker= new Worker(position);
			worker.setIsWorking(false);
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
    private static String decodeFaction(int n) {
    	/*
    	 * Converts the encoded value of the unit's faction into the corresponding faction
    	 */
    	switch(n) {
    	case(1): return DefaultGameSettings.ZEUS;
    	case(2): return DefaultGameSettings.HADES;
    	case(3): return DefaultGameSettings.POSEIDON;
    	default: return "UNKNWON_FACTION";
    	}
    }
    private static Unit setDataUnit(Unit u, ArrayList<Float> unitData) {
    	u.setUnitFaction(decodeFaction(unitData.get(2).intValue()));
		u.setTierLevel(unitData.get(3).intValue());;
		u.setMaxHp(unitData.get(4).intValue());
		u.setPopCost(unitData.get(5).intValue());
		u.setACost(unitData.get(6).intValue());
		u.setFCost(unitData.get(7).intValue());
		u.setATK(unitData.get(8).intValue());
		u.setATKSpeed(unitData.get(9));
		u.setMovementSpeed(unitData.get(10));
		u.setATKRange(unitData.get(11));
		u.setVision(unitData.get(12));
		u.setHpRegen(unitData.get(13).intValue());
    	return u;
    }
	}
