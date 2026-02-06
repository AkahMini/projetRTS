package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public abstract class Unit extends MobileElement{

	private String faction;
	private int hp;
	private int maxHp;
	private int populationCost;
	private int ambroisieCost;
	private int faithCost;
	private int attackDamage;
	private float attackSpeed;
	private float movementSpeed;
	private int attackRange;
	private int visionRange;
	private float hpRegenRate;
	private int tierLevel;
	private MobileElement target;
	
	public Unit(Block position) {
		super(position);
	}
}
