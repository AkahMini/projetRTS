package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

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
	private int chargeBonusDamage;
	
	public Cavalry(Block position) {
		super(position);
	}
}
