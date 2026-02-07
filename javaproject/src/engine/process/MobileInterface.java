package engine.process;

import java.util.List;
import engine.map.Block;
import engine.mobile.building.Building;

/**
 * 
 * @author LE RAY Yann
 *
 */

public interface MobileInterface {

	void nextRound();
	public void selectBuilding(String type);
	void buildBuilding(Block position);
	List<Building> getBuildings();
}