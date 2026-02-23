package config;

import engine.process.UnitFactory;

public class DefaultGameSettings {
	/*
	 *Constants and variables related to the current game, not the program itself 
	 *
	 */
	
	public static final String ZEUS = "Zeus";
    public static final String HADES = "Hades";
    public static final String POSEIDON = "Poseidon";
    
    public static final String defaultPlayerFaction = ZEUS;
    
    
    
    private String playerFaction = defaultPlayerFaction;
    
    public String getPlayerFaction() {
    	return this.playerFaction;
    }
    public void setPlayerFaction(String faction){
    	if(faction==ZEUS||faction==HADES||faction==POSEIDON) {
    		this.playerFaction=faction;
    	}
    }
}
