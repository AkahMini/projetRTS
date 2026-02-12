package engine.mobile.building;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * Data class of a Building, it will be extended by all the specific type of building.
 * Its fields are the name of the building, the faction, the hp and maxhp and the constructionTime 
 * as well as, if it is under construction and the tier level.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */
public class Building extends MobileElement{

	private String BuildingName;
	private String faction;
	private int hp;
	private int maxHp;
	private int constructionTime;
	private boolean isUnderConstruction;
	private int tierLevel;
	
	/**
	 * Constructor for the Building.
	 * Initializes the building at a specific position on the map.
	 * @param position The block where the building is located.
	 */
	public Building(Block position) {
		super(position);
		}
	/**
	 * @param name The name of the building to set.
	 */
	public void setBuildingName(String name) {
		this.BuildingName=name;
	}
	/**
	 * @param hp The new current health points.
	 */
	public void setHp(int hp) {
		this.hp=hp;
	}
	
	/**
	 * Sets the time required for construction.
	 * @param time  The number of seconds to build the building.
	 */
	public void setConstructionTime(int time) {
		this.constructionTime=time;
	}
	
	/**
	 * Updates the construction status of the building.
	 * @param underConstruction True if the building is still being built, false if not.
	 */
	public void setUnderConstruction(boolean underConstruction) {
		this.isUnderConstruction=underConstruction;
	}
	
	/**
	 * @param tier The technology tier level of the building.
	 */
	public void setTierLevel(int tier) {
		this.tierLevel=tier;
	}
	
	/**
	 * @param faction The name of the faction owning the building.
	 */
	public void setFaction(String faction) {
		this.faction=faction;
	}
	
	/**
	 * @return The name of the building.
	 */
	public String getBuildingName() {
		return this.BuildingName;
	}
	
	/**
	 * @return The current health points of the building.
	 */
	public int getHp() {
		return this.hp;
	}
	
	/**
	 * @return The remaining time before construction is finished.
	 */
	public int getConstructionTime() {
		return this.constructionTime;
	}
	
	/**
	 * @return True if the building is currently under construction, false if not.
	 */
	public boolean getIsUnderConstruction() {
		return this.isUnderConstruction;
	}
	
	/**
	 * @return The technology tier level.
	 */
	public int getTierLevel() {
		return this.tierLevel;
	}
	
	/**
	 * @return The name of the faction.
	 */
	public String getFaction() {
		return this.faction;
	}
	
	/**
	 * @param maxHp The maximum health points capacity of the building.
	 */
	public void setMaxHp(int maxHp) {
		this.maxHp=maxHp;
	}
	
	/**
	 * @return The maximum health points of the building.
	 */
	public int getMaxHp() {
		return this.maxHp;
	}
	
	/**
	 * Calculates the remaining health points as a percentage.
	 * @return An integer between 0 and 100 representing the health percentage.
	 */
	public int getPercentHP() {
		return (int)(this.hp*100.0/this.maxHp);	
	}
}
