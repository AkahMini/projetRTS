package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.HQ;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Worker extends Unit{

	private int currentRessourceLoad;
	private String ressourceType;
	private int maxCargoCapacity;
	private RessourceDeposit currentDeposit;
	private HQ workerHQ;
	
	public Worker(Block position) {
		super(position);
	}
	public HQ getCurrentHQ() {
		return this.workerHQ;
	}
	public void setCurrentDeposit(RessourceDeposit deposit) {
		currentDeposit=deposit;
	}
	public RessourceDeposit getCurrentDeposit() {
		return currentDeposit;
	}
	public void setCurrentRessourceLoad(int addedLoad) {
		this.currentRessourceLoad+=addedLoad;
	}
	public int getRessourceLoad() {
		return currentRessourceLoad;
	}
	public int getMaxCargoCapacity() {
		return maxCargoCapacity;
	}
}
