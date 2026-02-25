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
	
	public Infantry(Block position) {
		super(position);
	}

	public int getShieldValue() {
		return shieldValue;
	}

	public void setShieldValue(int shieldValue) {
		this.shieldValue = shieldValue;
	}
}
