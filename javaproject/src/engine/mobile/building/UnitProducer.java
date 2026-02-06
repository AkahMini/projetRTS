package engine.mobile.building;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class UnitProducer extends MobileElement{

	private ArrayList<String> productionQueue;
	private float productionSpeed;
	
	public UnitProducer(Block position) {
		super(position);
	}
}
