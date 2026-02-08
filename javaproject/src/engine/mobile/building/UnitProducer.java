package engine.mobile.building;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.MobileElement;
import engine.mobile.unit.Unit;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class UnitProducer extends Building{

	private ArrayList<Unit> productionQueue;
	private float productionSpeed;
	private float currentProduction;
	
	public UnitProducer(Block position) {
		super(position);
	}
	public void setProductionQueue(ArrayList<Unit> queue) {
		this.productionQueue=queue;
	}
	public void setProductionSpeed(float speed) {
		this.productionSpeed=speed;
	}
	public void setCurrentProduction(float currentProduction) {
		this.currentProduction=currentProduction;
	}
	public ArrayList<Unit> getProductionQueue() {
		return this.productionQueue;
	}
	public float getProductionSpeed() {
		return this.productionSpeed;
	}
	public float getCurrentProduction() {
		return this.currentProduction;
	}
}
