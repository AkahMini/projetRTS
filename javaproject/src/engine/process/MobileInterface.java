package engine.process;

import java.util.List;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
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
	
	double getDistance(Block b1, Block b2);
	void initSelectedArea(Block firstBlock);
	void calculateSelectedArea(Block lastBlock);
	int isBlockCollider(Block block);
	
	void addInBuildings(Building n);
	 Map getMap();
}