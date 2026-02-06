package engine.mobile.building;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class HQ extends MobileElement{

	private ArrayList<String> productionQueue;
	private int defenseDamage;
	private int defenseRange;
	
	public HQ(Block position) {
		super(position);
	}
}
