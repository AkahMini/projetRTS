package engine.mobile.unit;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public abstract class Unit extends MobileElement{

	private String unitName;
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
	private Block destination;
	
	public Unit(Block position) {
		super(position);
	}
	
	public int getTierLevel() {
		return this.tierLevel;
	}
	public void setTierLevel(int tier) {
		this.tierLevel=tier;
	}
	public int getHp() {
		return this.hp;
	}
	public void setHp(int hp) {
		this.hp=hp;
	}
	public int getMaxHp() {
		return this.maxHp;
	}
	public void setMaxHp(int maxhp) {
		this.maxHp=maxhp;
	}
	public int getPopCost() {
		return this.populationCost;
	}
	public void setPopCost(int pc) {
		this.populationCost=pc;
	}
	public int getACost() {
		return this.ambroisieCost;
	}
	public void setACost(int ac) {
		this.ambroisieCost=ac;
	}
	public int getFCost() {
		return this.faithCost;
	}
	public void setFCost(int fc) {
		this.faithCost=fc;
	}
	public int getATK() {
		return this.attackDamage;
	}
	public void setATK(int atk) {
		this.attackDamage=atk;
	}
	public float getATKSpeed() {
		return this.attackSpeed;
	}
	public void setATKSpeed(float atks) {
		this.attackSpeed=atks;
	}
	public float getMS() {
		return this.movementSpeed;
	}
	public void setMS(float ms) {
		this.movementSpeed=ms;
	}
	public int getATKRange() {
		return this.attackRange;
	}
	public void setATKRange(int atkr) {
		this.attackRange=atkr;
	}
	public int getVision() {
		return this.visionRange;
	}
	public void setVision(int v) {
		this.visionRange=v;
	}
	public float getHpRegen(){
		return this.hpRegenRate;
	}
	public void setHpRegen(float hpRegen) {
		this.hpRegenRate=hpRegen;
	}
	public MobileElement getTarget() {
		return this.target;
	}
	public void setTarget(MobileElement tg) {
		this.target=tg;
	}
	public String getUnitName() {
		return this.unitName;
	}
	public void setUnitName(String name) {
		this.unitName=name;
	}
	public String getUnitFaction() {
		return this.faction;
	}
	public void setUnitFaction(String faction) {
		this.faction=faction;
	}
	public Block getDestination() {
		return this.destination;
	}
	public void setDestination(Block destination) {
		this.destination=destination;
	}
}
