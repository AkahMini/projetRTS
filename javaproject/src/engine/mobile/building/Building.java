package engine.mobile.building;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Building extends MobileElement{

	private int hp;
	private int constructionTime;
	private boolean isUnderContruction;
	private int tierLevel;
	
	public Building(Block position) {
		super(position);
	}
}
