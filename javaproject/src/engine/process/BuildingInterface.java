package engine.process;

import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;

public interface BuildingInterface {
	 void selectBuilding(String type);
	 
	 void buildBuilding(Block position);
	 
	 void reduceConstructionTime(Building building);
	 
	 void addQueue(UnitProducer building, Block position);
	 
	 void removeQueue(UnitProducer building);
}
