package engine.mobile.building;

import engine.map.Block;
import engine.mobile.unit.Unit;

/**
 * Class representing a defensive structure.
 * A DefenseTower is a building capable of attacking enemy units within a certain range.
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class DefenseTower extends Building{
	private static final int ATTACK_TIME = 200;
	
	private int attackCounter=0; //The active counter between attacks	
	private Unit target;
	
	private int towerDamage;
	private int towerAttackSpeed;
	private int towerRange;
	private int isAttacking;
	
	
	

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
	
	public Unit getTarget() {
		return this.target;
	}
	
	public void setTarget(Unit target) {
		this.target=target;
	}
	
	public int getTowerAttackSpeed() {
		return this.towerAttackSpeed;
	}
	public void setTowerAttackSpeed(int towerAttackSpeed) {
		this.towerAttackSpeed=towerAttackSpeed;
	}
	
	public int getAttackCounter() {
		return this.attackCounter;
	}
	public void setAttackCounter(int counter) {
		this.attackCounter=counter;
	}
	
	public void resetAttackCounter() {
		this.attackCounter=this.towerAttackSpeed;
	}
	public static int getAttackTime() {
		return ATTACK_TIME;
	}
	public int getIsAttacking() {
		return isAttacking;
	}
	public void setIsAttacking(int isAttacking) {
		this.isAttacking = isAttacking;
	}
}