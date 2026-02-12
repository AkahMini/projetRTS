package engine.mobile.building;

import engine.map.Block;

/**
 * Class representing a defensive structure.
 * A DefenseTower is a building capable of attacking enemy units within a certain range.
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class DefenseTower extends Building{

	private int towerDamage;
	private int towerRange;
	
	/**
	 * Constructor for DefenseTower.
	 * Initializes the tower at a specific position.
	 * @param position The block where the tower is built.
	 */
	public DefenseTower(Block position) {
		super(position);
	}
	
	/**
	 * @return The attack range of the tower in blocks.
	 */
	public int getTowerRange() {
		return towerRange;
	}
	
	/**
	 * @param towerRange The new range value for the tower.
	 */
	public void setTowerRange(int towerRange) {
		this.towerRange = towerRange;
	}
	
	/**
	 * @return The damage inflicted by the tower per attack.
	 */
	public int getTowerDamage() {
		return towerDamage;
	}
	
	/**
	 * @param towerDamage The new damage value for the tower.
	 */
	public void setTowerDamage(int towerDamage) {
		this.towerDamage = towerDamage;
	}
}