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
	private int hp;
	private int constructionTime;
	private boolean isUnderContruction;
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
		this.isUnderContruction=true;
	}
	public void setTierLevel(int tier) {
		this.tierLevel=tier;
	}
}
