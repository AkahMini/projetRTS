package engine.process;

import java.util.ArrayList;
import java.util.List;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.building.Building;


/**
 * 
 * @author LE RAY Yann
 *
 */
public class MobileElementManager implements MobileInterface {
	private Map map;
	private String selectedBuilding = null;
	
	private List<Building> buildings = new ArrayList<Building>();
	
	public MobileElementManager(Map map) {
		this.map = map;
	}

	public void nextRound() {
		System.out.println("tick");
	}
		public void selectBuilding(String type) {
		    this.selectedBuilding = type;
		    System.out.println("Mode construction : " + type);
		}

		public void buildBuilding(Block position) {
		    if (selectedBuilding == null) {
		        return;
		    }

		    String faction = "Zeus"; 
		    int tier = 1;

		    Building nouveauBatiment = BuildingFactory.createBuilding(selectedBuilding, tier, faction, position);

		    if (nouveauBatiment != null) {
		    	buildings.add(nouveauBatiment);
		        System.out.println("Bâtiment posé en : " + position.getLine() + ", " + position.getColumn());
		    }
		    selectedBuilding = null; 
		}

	private static int getRandomNumber(int min, int max) {
		return (int) (Math.random() * (max + 1 - min)) + min;
	}	
	public List<Building> getBuildings() {
        return buildings;
    }
}