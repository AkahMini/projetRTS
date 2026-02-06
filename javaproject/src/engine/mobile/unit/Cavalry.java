package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Cavalry extends MobileElement{

	private boolean throughObstacles;
	private int chargeBonusDamage;
	
	public Cavalry(Block position) {
		super(position);
	}
}
