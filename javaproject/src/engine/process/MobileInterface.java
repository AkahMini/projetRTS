package engine.process;

import java.util.ArrayList;

import config.DefaultGameSettings;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.CPU;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;
import gui.instrument.ChartManager;

/**
 * 
 * Interface used for the MobileElementManager class.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

//ok maybe this is too much
public interface MobileInterface {
	void firstRound();
	void nextRound();
	String winningFaction();
	
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
    void setUnitsInSelectedArea(ArrayList<Unit> units);
    void addUnitsInSelectedArea(Unit unit);
    ArrayList<Building> getBuildingsInSelectedArea();
	CyclicCounter getHour();
	CyclicCounter getMinute();
	CyclicCounter getSecond();
	Block getMousePosition(int y, int x);
	
	void setIsGameStoped(boolean stop);
	boolean isGameStoped();
	
	void areaButtonPressed(int x, int y);
	boolean ifBlockInGamePanel(Block block);
	Worker getSelectedWorker();
	void setSelectedWorker(Worker selectedWorker);
	Building getSelectedBuild();
	void setSelectedBuild(Building selectedBuild);
	void selectBuilding(String string);
	int buildBuilding(Block position,int tier,String faction,Player p);
	void addQueue(UnitProducer building, Block position, String unitType, Player p);

	void selectUnit(String string);
	void unitMoveOrder(Block firstBlock);
	
	public void setNotifText(String info,boolean isgood);
	String getNotification();
	boolean isNotificationGood();
	
	int getSelectedTier();
	void setSelectedTier(int selectedTier);
	void setSelectedArea(ArrayList<Block> selectedArea);
	String getTypeSelection();
	void setTypeSelection(String typeSelection);
	void spawnUnit(Block position, String faction);
	CPU getCpu();
	
	void motherload();
	void cpuLoad();
	int countBuildingType(Player p, Class<?> class1);
	DefaultGameSettings getGameSettings();
	Chronometer getChronometer();
	void setChartManager(ChartManager chartManager);
	int getMode();
	
	boolean isAltGui();
	void setAltGui(boolean isAltGui);
}