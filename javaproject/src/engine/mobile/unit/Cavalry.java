package engine.mobile.unit;

import engine.map.Block;
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

	public boolean isThroughObstacles() {
		return throughObstacles;
	}

	public void setThroughObstacles(boolean throughObstacles) {
		this.throughObstacles = throughObstacles;
	}

	public int getChargeBonusDamage() {
		return chargeBonusDamage;
	}

	public void setChargeBonusDamage(int chargeBonusDamage) {
		this.chargeBonusDamage = chargeBonusDamage;
	}
}
