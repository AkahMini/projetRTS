package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

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
}
