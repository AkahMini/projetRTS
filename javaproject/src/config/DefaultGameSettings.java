package config;
public class DefaultGameSettings {
	/*
	 *Constants and variables related to the current game, not the program itself 
	 *
	 */
	
	public static final String ZEUS = "Zeus";
    public static final String HADES = "Hades";
    public static final String POSEIDON = "Poseidon";
    
    public static final String DEFAULT_PLAYER_FACTION = ZEUS;
    public static final int DEFAULT_POPULATION = 20;
    
    
    private String playerFaction = DEFAULT_PLAYER_FACTION;
    
    public String getPlayerFaction() {
    	return this.playerFaction;
    }
    public void setPlayerFaction(String faction){
    	if(faction==ZEUS||faction==HADES||faction==POSEIDON) {
    		this.playerFaction=faction;
    	}
    }
}
