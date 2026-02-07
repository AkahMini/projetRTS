package engine.mobile.building;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class DefenseTower extends Building{

	private int towerDamage;
	private int towerRange;
	
	public DefenseTower(Block position) {
		super(position);
	}
}