package engine.process;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.Player;
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
	 
	 void buildBuilding(Block position,int tier,String faction);
	 
	 void buildingsInSelectedArea();
	 
	 void reduceConstructionTime(Building building);
	 
	 void addQueue(UnitProducer building, Block position,String unitType,Player p);
	 
	 void removeQueue(UnitProducer building);
	 
	 void action(String button, Player p);

	 void allBuildingsAttack(ArrayList<Building> buildings);
}
