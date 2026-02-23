package engine.process;

import config.DefaultGameSettings;
import engine.map.Block;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Infantry;
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
    public static final String WORKER_UNIT = "WORKER";//TEMP other types NEED to be added

    public static Unit createUnit(String type, int tier, String faction, Block position) {
        switch (type) {
            
            //Unit prod
            case INFANTRY_UNIT:
                Infantry infantry = new Infantry(position);
                
                // common value
                infantry.setTierLevel(tier);
                infantry.setUnitFaction(faction);
                infantry.setMoveCounter(0);

                
                
                //default setter for now :
                infantry.setPopCost(1);
                infantry.setACost(1);
                infantry.setFCost(1);
                infantry.setATK(1);
                infantry.setATKSpeed(1);
                infantry.setMovementSpeed(9);
                infantry.setATKRange(1);
                infantry.setVision(1);
                infantry.setHpRegen(1);
                infantry.setTarget(null);
                infantry.setHp(70);
                
                
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
                worker.setMovementSpeed(14);
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
	}
