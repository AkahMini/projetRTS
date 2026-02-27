package engine.mobile.unit;

import engine.map.Block;
/**
 * Class representing an cavalry Unit
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class Cavalry extends Unit{

	private boolean throughObstacles;
	private double chargeBonusDamage;
	private int chargeDistanceMax;
	private int chargeDistanceValue;
	private double chargeSpeed;
	
	public Cavalry(Block position) {
		super(position);
	}

	public boolean isThroughObstacles() {
		return throughObstacles;
	}

	public void setThroughObstacles(boolean throughObstacles) {
		this.throughObstacles = throughObstacles;
	}

	public double getChargeBonusDamage() {
		return chargeBonusDamage;
	}

	public void setChargeBonusDamage(double d) {
		this.chargeBonusDamage = d;
	}

	public int getChargeDistanceMax() {
		return chargeDistanceMax;
	}

	public void setChargeDistanceMax(int chargeDistanceMax) {
		this.chargeDistanceMax = chargeDistanceMax;
	}

	public int getChargeDistanceValue() {
		return chargeDistanceValue;
	}

	public void setChargeDistanceValue(int chargeDistanceValue) {
		this.chargeDistanceValue = chargeDistanceValue;
	}

	public double getChargeSpeed() {
		return chargeSpeed;
	}

	public void setChargeSpeed(double d) {
		this.chargeSpeed = d;
	}
}
