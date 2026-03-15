package engine.mobile;

import java.util.ArrayList;

import engine.mobile.building.Building;

/**
 * Class representing a player in the game.
 * It stores the player's profile (pseudo, faction),resources and population
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class Player {

	private String pseudo;
	private String factionName;
	private int currentTier;
	private int faithStock;
	private int ambroisieStock;
	private int currentPopulation;
	private int maxPopulation;
	private ArrayList<String> technologies;
	private ArrayList<String> builtBuilding; //The id of the built buildings
	
	

	/**
	 * Constructor for Player.
	 * Initializes the player with a pseudo and a faction.
	 * Sets default values for resources (0), population (0/100), and tier (1).
	 * @param pseudo      The display name of the player.
	 * @param factionName The name of the faction chosen by the player.
	 */
	public Player(String pseudo, String factionName) {
		this.pseudo=pseudo;
		this.factionName=factionName;
		this.currentTier=1;
		this.faithStock=0;
		this.ambroisieStock=0;
		this.currentPopulation=0;
		this.maxPopulation=100;
		this.technologies= new ArrayList<String>();
		this.builtBuilding = new ArrayList<String>();
	}
	
	/**
	 * @return The player's username.
	 */
	public String getPseudo() {
		return pseudo;
	}
	
	/**
	 * @param pseudo The new username to set.
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	/**
	 * @return The name of the faction the player belongs to.
	 */
	public String getFactionName() {
		return factionName;
	}
	
	/**
	 * @param factionName The new faction name.
	 */
	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}
	
	/**
	 * @return The current technology tier of the player.
	 */
	public int getCurrentTier() {
		return currentTier;
	}
	
	/**
	 * @param currentTier The new technology tier.
	 */
	public void setCurrentTier(int currentTier) {
		this.currentTier = currentTier;
	}
	
	/**
	 * @return The current amount of Faith.
	 */
	public int getFaithStock() {
		return faithStock;
	}
	
	/**
	 * @param faithStock The new amount of Faith.
	 */
	public void setFaithStock(int faithStock) {
		this.faithStock = faithStock;
	}
	
	/**
	 * @return The current amount of Ambrosia.
	 */
	public int getAmbroisieStock() {
		return ambroisieStock;
	}
	
	/**
	 * @param ambroisieStock The new amount of Ambrosia.
	 */
	public void setAmbroisieStock(int ambroisieStock) {
		this.ambroisieStock = ambroisieStock;
	}
	
	/**
	 * @return The current population of the Player.
	 */
	public int getCurrentPopulation() {
		return currentPopulation;
	}
	
	/**
	 * @param currentPopulation The new population count.
	 */
	public void setCurrentPopulation(int currentPopulation) {
		this.currentPopulation = currentPopulation;
	}
	
	/**
	 * @return The maximum population cost the player can have.
	 */
	public int getMaxPopulation() {
		return maxPopulation;
	}
	
	/**
	 * @param maxPopulation The new maximum population limit.
	 */
	public void setMaxPopulation(int maxPopulation) {
		this.maxPopulation = maxPopulation;
	}

	public ArrayList<String> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(ArrayList<String> technologies) {
		this.technologies = technologies;
	}
	public void addTechnologieUnlocked(String technologieUnlocked) {
		this.technologies.add(technologieUnlocked);
	}
	public ArrayList<String> getBuiltBuilding() {
		return builtBuilding;
	}

	public void setBuiltBuilding(ArrayList<String> buildtBuilding) {
		this.builtBuilding = buildtBuilding;
	}
}
