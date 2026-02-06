package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;
import engine.mobile.RessourceDeposit;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Worker extends MobileElement{

	private int currentRessourceLoad;
	private String ressourceType;
	private int maxCargoCapacity;
	private RessourceDeposit currentDeposit;
	
	public Worker(Block position) {
		super(position);
	}
}
