package engine.mobile.building;

import java.util.ArrayList;

import engine.map.Block;

/**
 * Class representing the Headquarters (HQ) of a player.
 * The HQ is the main building that can produce  workers and has defensive capabilities for hades only.
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class HQ extends Building{

	private ArrayList<String> productionQueue;
	private float productionSpeed;
	private int currentProduction;
	private int defenseDamage;
	private int defenseRange;
	
	public HQ(Block position) {
		super(position);
	}

	public ArrayList<String> getProductionQueue() {
		return productionQueue;
	}

	public void setProductionQueue(ArrayList<String> productionQueue) {
		this.productionQueue = productionQueue;
	}

	public float getProductionSpeed() {
		return productionSpeed;
	}

	public void setProductionSpeed(float productionSpeed) {
		this.productionSpeed = productionSpeed;
	}

	public int getCurrentProduction() {
		return currentProduction;
	}

	public void setCurrentProduction(int currentProduction) {
		this.currentProduction = currentProduction;
	}

	public int getDefenseDamage() {
		return defenseDamage;
	}

	public void setDefenseDamage(int defenseDamage) {
		this.defenseDamage = defenseDamage;
	}

	public int getDefenseRange() {
		return defenseRange;
	}

	public void setDefenseRange(int defenseRange) {
		this.defenseRange = defenseRange;
	}
}
