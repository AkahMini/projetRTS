package engine.process;

import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;

public class BuildingFactory {
	// Constantes pour éviter les fautes de frappe
    public static final String PRODUCER_BUILDING = "PRODUCER";
    
    // Noms des factions (pour éviter d'écrire "Zeus" partout en dur)
    private static final String ZEUS = "Zeus";
    private static final String HADES = "Hades";
    private static final String POSEIDON = "Poseidon";

    public static Building createBuilding(String type, int tier, String faction, Block position) {
        
        switch (type) {
            
            //Production d'unités
            case PRODUCER_BUILDING:
                UnitProducer producer = new UnitProducer(position);
                
                // Valeurs commune
                producer.setTierLevel(tier);
                producer.setUnderConstruction(true); // Le bâtiment commence en construction
                
                //TIER 1 (Infanterie de base)
                if (tier == 1) {
                    producer.setHp(1000);
                    producer.setConstructionTime(100);

                    if (faction.equals(ZEUS)) {
                        producer.setBuildingName("Camp Olympique");
                    } else if (faction.equals(HADES)) {
                        producer.setBuildingName("Camp Spartiate");
                    } else if (faction.equals(POSEIDON)) {
                        producer.setBuildingName("Colisée d'Atlantide");
                    }
                }
                else if (tier == 2) {
                    producer.setHp(1500); // Plus résistant
                    producer.setConstructionTime(200); // Plus long à construire

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
