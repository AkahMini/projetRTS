package engine.process;

import engine.map.Block;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Infantry;

public class UnitFactory {

    public static final String INFANTRY_UNIT = "INFANTRY";//TEMP other types NEED to be added
    
    private static final String ZEUS = "Zeus";
    private static final String HADES = "Hades";
    private static final String POSEIDON = "Poseidon";

    public static Unit createUnit(String type, int tier, String faction, Block position) {
        
        switch (type) {
            
            //Unit prod
            case INFANTRY_UNIT:
                Infantry infantry = new Infantry(position);
                
                // common value
                infantry.setTierLevel(tier);
                infantry.setUnitFaction(faction);
                
                
                //default setter for now :
                infantry.setPopCost(1);
                infantry.setACost(1);
                infantry.setFCost(1);
                infantry.setATK(1);
                infantry.setATKSpeed(1);
                infantry.setMS(1);
                infantry.setATKRange(1);
                infantry.setVision(1);
                infantry.setHpRegen(1);
                infantry.setTarget(null);
                infantry.setHp(70);
                
                
                //this is an exemple and would be upgraded
                //TIER 
                if (tier == 1) {
                	infantry.setMaxHp(100);

                    if (faction.equals(ZEUS)) {
                    	//infantry=null;
                    	//temp for the test part
                    	infantry.setUnitName("Temp");
                    } else if (faction.equals(HADES)) {
                    	infantry.setUnitName("Hoplite");
                    } else if (faction.equals(POSEIDON)) {
                    	infantry.setUnitName("Rétiaire");
                    }
                }
                else if (tier == 2) {
                	infantry.setMaxHp(150);  

                    if (faction.equals(ZEUS)) {
                    	infantry.setUnitName("Cyclope");
                    } else if (faction.equals(HADES)) {
                    	infantry=null;
                    } else if (faction.equals(POSEIDON)) {
                    	infantry=null;
                    }
                }
                else if (tier == 3) {
                	infantry.setMaxHp(200);

                    if (faction.equals(ZEUS)) {
                    	infantry.setUnitName("Hydre");
                    } else if (faction.equals(HADES)) {
                    	infantry=null;
                    } else if (faction.equals(POSEIDON)) {
                    	infantry.setUnitName("Kraken fantôme");
                    }
                }
                //ABSOLUTE NEED TO MANAGE THE RETURN NULL IF A CASE OF POSIBLE NULL INCOME CAN HAPPENDS
                return infantry;
		default:
			throw new IllegalArgumentException("Unknown operation type : " + type);
			}
		}
	}
