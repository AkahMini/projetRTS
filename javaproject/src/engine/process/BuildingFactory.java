package engine.process;

import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;

public class BuildingFactory {
	// Const
    public static final String PRODUCER_BUILDING = "PRODUCER";
    
    // Name of the faction
    private static final String ZEUS = "Zeus";
    private static final String HADES = "Hades";
    private static final String POSEIDON = "Poseidon";

    public static Building createBuilding(String type, int tier, String faction, Block position) {
        
        switch (type) {
            
            //Unit prod
            case PRODUCER_BUILDING:
                UnitProducer producer = new UnitProducer(position);
                
                // common value
                producer.setTierLevel(tier);
                producer.setUnderConstruction(true); // The building is under construction
                
                //TIER 
                if (tier == 1) {
                    producer.setHp(1000);
                    producer.setConstructionTime(5);

                    if (faction.equals(ZEUS)) {
                        producer.setBuildingName("Camp Olympique");
                    } else if (faction.equals(HADES)) {
                        producer.setBuildingName("Camp Spartiate");
                    } else if (faction.equals(POSEIDON)) {
                        producer.setBuildingName("Colisée d'Atlantide");
                    }
                }
                else if (tier == 2) {
                    producer.setHp(1500); // More resistant
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
                    producer.setHp(2000);
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
		default:
			throw new IllegalArgumentException("Unknown operation type : " + type);
			}
		}
	}
