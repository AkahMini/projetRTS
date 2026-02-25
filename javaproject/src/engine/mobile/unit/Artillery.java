package engine.mobile.unit;

import engine.map.Block;
/**
 * Class representing an artillery Unit
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */

public class Artillery extends Unit{

	private float blastRadius;
	private float attackRange;
	
	public Artillery(Block position) {
		super(position);
	}

	public float getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}

	public float getBlastRadius() {
		return blastRadius;
	}

	public void setBlastRadius(float blastRadius) {
		this.blastRadius = blastRadius;
	}
}
