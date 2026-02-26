package engine.mobile.unit;

import engine.map.Block;
/**
 * Class representing an infantry Unit
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */

public class Infantry extends Unit{

	private int shieldValue;
	private int maxShield;
	
	public Infantry(Block position) {
		super(position);
	}

	public int getShieldValue() {
		return shieldValue;
	}
	public int getPercentShield() {
		return (int)(this.shieldValue*100.0/this.maxShield);	
	}

	public void setShieldValue(int shieldValue) {
		this.shieldValue = shieldValue;
	}

	public int getMaxShield() {
		return maxShield;
	}

	public void setMaxShield(int maxShield) {
		this.maxShield = maxShield;
	}
}
