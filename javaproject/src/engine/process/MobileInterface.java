package engine.process;

import java.util.List;
import engine.map.Block;
import engine.mobile.building.Building;
import engine.mobile.unit.Unit;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * @author LE RAY Yann
 *
 */

public interface MobileInterface {

	void nextRound();
	
	public void selectBuilding(String type);
	public void buildBuilding(Block position);
	public List<Building> getBuildings();
	
	public void selectUnit(String type);
	public void spawnUnit(Block position);
	public List<Unit> getUnits();
	
	public CyclicCounter getHour();
	
	public CyclicCounter getMinute();
	
	public CyclicCounter getSecond();
}