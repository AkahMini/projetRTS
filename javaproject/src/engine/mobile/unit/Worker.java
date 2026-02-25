package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.HQ;

/**
 * 
 * Class representing a worker unit. For the version 1.0 worker cannot attack but they
 * are initialized in the same class as other unit, UnitFactory. 
 * May be changed in the future
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
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
	public void setCurrentHQ(HQ hq) {
		this.workerHQ=hq;
	}
	
	public void setCurrentDeposit(RessourceDeposit deposit) {
		currentDeposit=deposit;
	}
	public RessourceDeposit getCurrentDeposit() {
		return currentDeposit;
	}
	public void setCurrentRessourceLoad(int addedLoad) {
		this.currentRessourceLoad=addedLoad;
	}
	public int getRessourceLoad() {
		return currentRessourceLoad;
	}
	public int getMaxCargoCapacity() {
		return maxCargoCapacity;
	}
	public void setMaxCargoCapacity(int max) {
		this.maxCargoCapacity=max;
	}
	public String getRessourceType() {
		return this.ressourceType;
	}
	public void setRessourceType(String type) {
		this.ressourceType=type;
	}
}
