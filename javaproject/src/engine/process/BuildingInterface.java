package engine.process;

import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;


/**
 * Interface used for the BuildingManager class.
 *
 *
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 * 
 * 
 */


public interface BuildingInterface {
	 void selectBuilding(String type);
	 
	 void buildBuilding(Block position);
	 
	 void buildingsInSelectedArea();
	 
	 void reduceConstructionTime(Building building);
	 
	 void addQueue(UnitProducer building, Block position, String unitType);
	 
	 void removeQueue(UnitProducer building);
}
