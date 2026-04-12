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
    
    //non static parameters
    private String playerFaction = DEFAULT_PLAYER_FACTION;
    private int effectiveGameSpeed =GameConfiguration.DEFAULT_GAME_SPEED;//10 by default, 1 for devMode
    private boolean fogOfWar=true;
    
	
	public String getPlayerFaction() {
    	return this.playerFaction;
    }
    public void setPlayerFaction(String faction){
    	if(faction==ZEUS||faction==HADES||faction==POSEIDON) {
    		this.playerFaction=faction;
    	}
    }
    public int getEffectiveGameSpeed() {
		return effectiveGameSpeed;
	}
	public void setEffectiveGameSpeed(int effectiveGameSpeed) {
		if(effectiveGameSpeed>=1)
		this.effectiveGameSpeed = effectiveGameSpeed;
	}
	public boolean isFogOfWar() {
		return fogOfWar;
	}
	public void setFogOfWar(boolean fogOfWar) {
		this.fogOfWar = fogOfWar;
	}
}
