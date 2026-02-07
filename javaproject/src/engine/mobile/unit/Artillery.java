package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Artillery extends Unit{

	private float blastRadius;
	private float attackRange;
	
	public Artillery(Block position) {
		super(position);
	}
}
