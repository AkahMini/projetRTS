package engine.mobile.building;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Building extends MobileElement{

	private String BuildingName;
	private String faction;
	private int hp;
	private int maxHp;
	private int constructionTime;
	private boolean isUnderConstruction;
	private int tierLevel;
	
	public Building(Block position) {
		super(position);
		}
	public void setBuildingName(String name) {
		this.BuildingName=name;
	}
	public void setHp(int hp) {
		this.hp=hp;
	}
	public void setConstructionTime(int time) {
		this.constructionTime=time;
	}
	public void setUnderConstruction(boolean underConstruction) {
		this.isUnderConstruction=underConstruction;
	}
	public void setTierLevel(int tier) {
		this.tierLevel=tier;
	}
	public void setFaction(String faction) {
		this.faction=faction;
	}
	public String getBuildingName() {
		return this.BuildingName;
	}
	public int getHp() {
		return this.hp;
	}
	public int getConstructionTime() {
		return this.constructionTime;
	}
	public boolean getIsUnderConstruction() {
		return this.isUnderConstruction;
	}
	public int getTierLevel() {
		return this.tierLevel;
	}
	public String getFaction() {
		return this.faction;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp=maxHp;
	}
	public int getMaxHp() {
		return this.maxHp;
	}
	
	//get the remaing health point as a percentage
	public int getPercentHP() {
		return (int)(this.hp*100.0/this.maxHp);	
	}
}
