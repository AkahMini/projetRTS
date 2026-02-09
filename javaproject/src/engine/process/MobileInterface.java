package engine.process;

import java.util.List;
import engine.map.Block;
import engine.mobile.Player;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * @author LE RAY Yann
 *
 */

public interface MobileInterface {
	void firstRound();
	void nextRound();
	
	public void selectBuilding(String type);
	public void buildBuilding(Block position);
	public List<Building> getBuildings();
	
	public void selectUnit(String type);
	public void spawnUnit(Block position);
	public List<Unit> getUnits();
	
	public void initSelectedArea(Block firstBlock);
	public void calculateSelectedArea(Block lastBlock);
	
	public void unitsInSelectedArea();
	public void unitMoveOrder(Block destination);
	public void moveAllUnits();
	
	public int isBlockCollider(Block block);
	
	public CyclicCounter getHour();
	
	public CyclicCounter getMinute();
	
	public CyclicCounter getSecond();
	
	public Player getPlayer();
	
	public Block getMousePosition(int x, int y);
	
	public List<Block> getSelectedArea();
	
	public List<Unit> getUnitsInSelectedArea();

	void addQueue(UnitProducer building, Block position);

	void spawnUnitEnnemy(Block position);
	
}