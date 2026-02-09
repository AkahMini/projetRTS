package engine.mobile.building;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class HQ extends Building{

	private ArrayList<String> productionQueue;
	private float productionSpeed;
	private int currentProduction;
	private int defenseDamage;
	private int defenseRange;
	
	public HQ(Block position) {
		super(position);
	}
}
