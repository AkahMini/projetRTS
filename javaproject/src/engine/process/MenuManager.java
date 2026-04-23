package engine.process;

import config.DefaultGameSettings;
import engine.map.Map;

/**
 * This class is used to manage every windows excepting the main game 
 * @see MainGUI
 * @see MobileElementManager
 */
public class MenuManager implements MenuInterface {
	private DefaultGameSettings gameSettings;
	
	private String currentState="MENU";
	// 0=1V1
	// 1=1V1V1
	private int selectedMode=0;
	
	private String selectedFaction;
	
	private String winnerFac;
	
	public MenuManager(DefaultGameSettings gameSettings) {
		this.gameSettings=gameSettings;
		this.selectedFaction= this.gameSettings.getPlayerFaction();
	}
	
	//use for the logic before the painting 
	public void updateMenu(String currentState) {
		this.currentState=currentState;
	}
	
	public void setselectedMode(int mode) {
		this.selectedMode=mode;
	}
	public int getSelectedMode() {
		return selectedMode;
	}
	
	public void setSelectedFaction(String faction) {
		this.selectedFaction=faction;
	}
	public String getSelectedFaction() {
		return selectedFaction;
	}
	
	public String getCurrentState() {
		return currentState;
	}

	public String getWinnerFac() {
		return winnerFac;
	}

	public void setWinnerFac(String winnerFac) {
		this.winnerFac = winnerFac;
	}
}