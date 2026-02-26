package engine.process;

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
           
           cavalry.setChargeDistanceMax(140);
           cavalry.setChargeDistanceValue(0);
           cavalry.setChargeSpeed(28);
           
           
           //this is an exemple and would be upgraded
           //TIER 
           if (tier == 1) {
        	  cavalry.setMaxHp(100);

               if (faction.equals(DefaultGameSettings.ZEUS)) {
               	//infantry=null;
               	//temp for the test part
            	   cavalry.setUnitName("Temp");
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
                infantry.setMovementSpeed(9);
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
	}
