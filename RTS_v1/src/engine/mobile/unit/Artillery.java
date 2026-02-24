package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * Class representing an artillery Unit
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */

public class Artillery extends Unit{

	private float blastRadius;
	private float attackRange;
	
	public Artillery(Block position) {
		super(position);
	}
}
