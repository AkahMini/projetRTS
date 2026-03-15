package engine.process;

import java.util.ArrayList;

import engine.map.Block;
import engine.map.Map;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * Interface used for the MobileElementManager class.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public interface MobileInterface {
	void firstRound();
	void nextRound();
	
	double getDistance(Block b1, Block b2);
	void initSelectedArea(Block firstBlock);
	void calculateSelectedArea(Block lastBlock);
	int isBlockCollider(Block block);
	
    void addInBuildings(Building n);
    void addInUnits(Unit newUnit);
    
    Map getMap();
    Player getPlayer();
    ArrayList<RessourceDeposit> getRessourceDeposit();
    ArrayList<Unit> getUnits();
    ArrayList<Building> getBuildings();
    ArrayList<Block> getSelectedArea();
    ArrayList<Unit> getUnitsInSelectedArea();
    ArrayList<Building> getBuildingsInSelectedArea();
	CyclicCounter getHour();
	CyclicCounter getMinute();
	CyclicCounter getSecond();
	Block getMousePosition(int y, int x);
	
	void areaButtonPressed(int x, int y);
	boolean ifBlockInGamePanel(Block block);
	Worker getSelectedWorker();
	void setSelectedWorker(Worker selectedWorker);
	Building getSelectedBuild();
	void setSelectedBuild(Building selectedBuild);
	void selectBuilding(String string);
	void buildBuilding(Block position,int tier,String faction,Player p);
	void addQueue(UnitProducer building, Block position, String unitType, Player p);
	
	void spawnUnit(Block spawnBlock);
	void spawnUnitEnnemy(Block position);
	void selectUnit(String string);
	void unitMoveOrder(Block firstBlock);
	
	int getSelectedTier();
	void setSelectedTier(int selectedTier);
	String getTypeSelection();
	void setTypeSelection(String typeSelection);
}