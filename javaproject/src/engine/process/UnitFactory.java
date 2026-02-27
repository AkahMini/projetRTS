package engine.process;

import java.util.ArrayList;
import java.util.HashMap;

import config.DefaultGameSettings;
import engine.map.Block;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Infantry;
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

    public static final String INFANTRY_UNIT = "INFANTRY";//TEMP other types NEED to be added
    public static final String ARTILLERY_UNIT = "ARTILLERY";//TEMP other types NEED to be added
    public static final String WORKER_UNIT = "WORKER";//TEMP other types NEED to be added
    public static final String CAVALRY_UNIT = "CAVALRY";//TEMP other types NEED to be added
    
    
    
    public static Unit createUnit(String unit_ID,Block position, HashMap<String,ArrayList<Float>> stats){
    	Infantry unknownUnit = new Infantry(position);
    	ArrayList<Float> unitData = stats.get(unit_ID);
    	
    	if(unitData==null) {
    		throw new IllegalArgumentException("Clé inexistante : " + unit_ID);
 
    	}
    	
    	int unitType=unitData.get(1).intValue();
    	switch(unitType) {
    	case(0)://Worker
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
			return worker;
    		
    	case(1)://Infantry
    		Infantry infantry= new Infantry(position);
    		infantry.setUnitName(unit_ID);
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
    		
    	case(2)://Cavalry
    		Cavalry cavalry = new Cavalry(position);
    		cavalry.setUnitName(unit_ID);
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
    		return unknownUnit;
    	
    	case(3)://Artillery
    		Artillery artillery = new Artillery(position);
	    	artillery.setUnitName(unit_ID);
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
    	default:
    		return unknownUnit;
    	}
    	
    }
    
    public static Unit createUnit(String type, int tier, String faction, Block position) {
        switch (type) {
      //Unit prod
        case CAVALRY_UNIT:
            Cavalry cavalry = new Cavalry(position);
           
           // common value
           cavalry.setTierLevel(tier);
           cavalry.setUnitFaction(faction);
           cavalry.setMoveCounter(0);
           cavalry.setAttackCounter(0);

           
           
           //default setter for now :
           cavalry.setPopCost(1);
           cavalry.setACost(1);
           cavalry.setFCost(1);
           cavalry.setATK(10);
           cavalry.setATKSpeed(2);
           cavalry.setMovementSpeed(14);
           cavalry.setATKRange(1);
           cavalry.setVision(8);
           cavalry.setHpRegen(1);
           cavalry.setTarget(null);
           cavalry.setHp(70);
           
           cavalry.setChargeDistanceMax(200);
           cavalry.setChargeDistanceValue(0);
           cavalry.setChargeSpeed(1.5);
           cavalry.setChargeBonusDamage(1.5);
           
           
           //this is an exemple and would be upgraded
           //TIER 
           if (tier == 1) {
        	  cavalry.setMaxHp(100);

               if (faction.equals(DefaultGameSettings.ZEUS)) {
               	//infantry=null;
               	//temp for the test part
            	   cavalry.setUnitName("cavalier1");
               } else if (faction.equals(DefaultGameSettings.HADES)) {
            	   cavalry.setUnitName("Hoplite");
               } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
            	   cavalry.setUnitName("Rétiaire");
               }
           }
           else if (tier == 2) {
        	   cavalry.setMaxHp(150);  

               if (faction.equals(DefaultGameSettings.ZEUS)) {
            	   cavalry.setUnitName("Cyclope");
               } else if (faction.equals(DefaultGameSettings.HADES)) {
            	   cavalry=null;
               } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
            	   cavalry=null;
               }
           }
           else if (tier == 3) {
        	   cavalry.setMaxHp(200);

               if (faction.equals(DefaultGameSettings.ZEUS)) {
            	   cavalry.setUnitName("Hydre");
               } else if (faction.equals(DefaultGameSettings.HADES)) {
            	   cavalry=null;
               } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
            	   cavalry.setUnitName("Kraken fantôme");
               }
           }
           return cavalry;
        case ARTILLERY_UNIT:
             Artillery artillery = new Artillery(position);
            
            // common value
            artillery.setTierLevel(tier);
            artillery.setUnitFaction(faction);
            artillery.setMoveCounter(0);
            artillery.setAttackCounter(0);

            
            
            //default setter for now :
            artillery.setPopCost(1);
            artillery.setACost(1);
            artillery.setFCost(1);
            artillery.setATK(6);
            artillery.setATKSpeed(2);
            artillery.setMovementSpeed(9);
            artillery.setATKRange(2);
            artillery.setVision(8);
            artillery.setHpRegen(1);
            artillery.setTarget(null);
            artillery.setHp(70);
            
            
            //this is an exemple and would be upgraded
            //TIER 
            if (tier == 1) {
            	artillery.setMaxHp(100);

                if (faction.equals(DefaultGameSettings.ZEUS)) {
                	//infantry=null;
                	//temp for the test part
                	artillery.setUnitName("Temp");
                } else if (faction.equals(DefaultGameSettings.HADES)) {
                	artillery.setUnitName("Hoplite");
                } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                	artillery.setUnitName("Rétiaire");
                }
            }
            else if (tier == 2) {
            	artillery.setMaxHp(150);  

                if (faction.equals(DefaultGameSettings.ZEUS)) {
                	artillery.setUnitName("Cyclope");
                } else if (faction.equals(DefaultGameSettings.HADES)) {
                	artillery=null;
                } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                	artillery=null;
                }
            }
            else if (tier == 3) {
            	artillery.setMaxHp(200);

                if (faction.equals(DefaultGameSettings.ZEUS)) {
                	artillery.setUnitName("Hydre");
                } else if (faction.equals(DefaultGameSettings.HADES)) {
                	artillery=null;
                } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                	artillery.setUnitName("Kraken fantôme");
                }
            }
            return artillery;
            //Unit prod
            case INFANTRY_UNIT:
                Infantry infantry = new Infantry(position);
                
                // common value
                infantry.setTierLevel(tier);
                infantry.setUnitFaction(faction);
                infantry.setMoveCounter(0);
                infantry.setAttackCounter(0);

                
                
                //default setter for now :
                infantry.setPopCost(1);
                infantry.setACost(1);
                infantry.setFCost(1);
                infantry.setATK(15);
                infantry.setATKSpeed(1);
                infantry.setMovementSpeed(8);
                infantry.setATKRange(1);
                infantry.setVision(4);
                infantry.setHpRegen(1);
                infantry.setTarget(null);
                infantry.setHp(100);
                
                infantry.setShieldValue(30);
                infantry.setMaxShield(60);
                
                
                //this is an exemple and would be upgraded
                //TIER 
                if (tier == 1) {
                	infantry.setMaxHp(100);

                    if (faction.equals(DefaultGameSettings.ZEUS)) {
                    	//infantry=null;
                    	//temp for the test part
                    	infantry.setUnitName("Temp");
                    } else if (faction.equals(DefaultGameSettings.HADES)) {
                    	infantry.setUnitName("Hoplite");
                    } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                    	infantry.setUnitName("Rétiaire");
                    }
                }
                else if (tier == 2) {
                	infantry.setMaxHp(150);  

                    if (faction.equals(DefaultGameSettings.ZEUS)) {
                    	infantry.setUnitName("Cyclope");
                    } else if (faction.equals(DefaultGameSettings.HADES)) {
                    	infantry=null;
                    } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                    	infantry=null;
                    }
                }
                else if (tier == 3) {
                	infantry.setMaxHp(200);

                    if (faction.equals(DefaultGameSettings.ZEUS)) {
                    	infantry.setUnitName("Hydre");
                    } else if (faction.equals(DefaultGameSettings.HADES)) {
                    	infantry=null;
                    } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                    	infantry.setUnitName("Kraken fantôme");
                    }
                }
                return infantry;
            case WORKER_UNIT:
                Worker worker = new Worker(position);
                
                // common value
                worker.setTierLevel(tier);
                worker.setUnitFaction(faction);
                worker.setMoveCounter(0);
                worker.setCurrentRessourceLoad(0);

                
                
                //default setter for now :
                worker.setPopCost(1);
                worker.setACost(1);
                worker.setFCost(1);
                worker.setATK(0);
                worker.setATKSpeed(0);
                worker.setMovementSpeed(11);
                worker.setATKRange(0);
                worker.setVision(1);
                worker.setHpRegen(1);
                worker.setTarget(null);
                worker.setHp(50);
                
            	worker.setMaxCargoCapacity(100);
                	if (faction.equals(DefaultGameSettings.ZEUS)) {
                    	worker.setUnitName("Athéniens");
                    } else if (faction.equals(DefaultGameSettings.HADES)) {
                    	worker.setUnitName("Spartiates");
                    } else if (faction.equals(DefaultGameSettings.POSEIDON)) {
                    	worker.setUnitName("Atlan ");
                    }
                return worker;
		default:
			throw new IllegalArgumentException("Unknown operation type : " + type);
			}
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
