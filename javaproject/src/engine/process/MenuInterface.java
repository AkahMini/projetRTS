package engine.process;

public interface MenuInterface {
	void updateMenu(String currentState);
	String getCurrentState();
	
	void setSelectedFaction(String faction);
	String getSelectedFaction();
	void setselectedMode(int mode);
	int getSelectedMode();
	
	String getWinnerFac();
	void setWinnerFac(String winnerFac);
}
