package engine.mobile.building;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.unit.Unit;

/**
 * Class representing the Headquarters (HQ) of a player.
 * The HQ is the main building that can produce  workers and has defensive capabilities for hades only.
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class HQ extends Building{

	private UnitProducer workerProducer;
	private int defenseDamage;
	private int defenseAttackSpeed;
	private int defenseRange;
	
	public HQ(Block position) {
		super(position);
	}

	public int getDefenseDamage() {
		return defenseDamage;
	}

	public void setDefenseDamage(int defenseDamage) {
		this.defenseDamage = defenseDamage;
	}

	public int getDefenseRange() {
		return defenseRange;
	}

	public void setDefenseRange(int defenseRange) {
		this.defenseRange = defenseRange;
	}

	public UnitProducer getWorkerProducer() {
		return workerProducer;
	}

	public void setWorkerProducer(UnitProducer workerProducer) {
		this.workerProducer = workerProducer;
	}

	public int getDefenseAttackSpeed() {
		return defenseAttackSpeed;
	}

	public void setDefenseAttackSpeed(int defenseAttackSpeed) {
		this.defenseAttackSpeed = defenseAttackSpeed;
	}
}
