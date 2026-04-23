package engine.mobile.unit;



/**
 * Data class representing the stats of a Unit loaded from the CSV.
 */
public class UnitStats {
    private String id;
	private String UnitType;
    private String faction;
    private int tierLevel;
    private int maxHp;
	private int populationCost;
	private int ambroisieCost;
	private int faithCost;
	private double attackDamage;
	private float attackSpeed;
	private double movementSpeed;
	private float attackRange;
	private float visionRange;
	private int hpRegenRate;
	
	private float maxShield;

	private float blastRadius;
	
	private boolean throughObstacles;
	private double chargeBonusDamage;
	private int chargeDistanceMax;
	private double chargeSpeed;
	

	/**
	 * @param id
	 * @param unitType
	 * @param faction
	 * @param maxHp
	 * @param populationCost
	 * @param ambroisieCost
	 * @param faithCost
	 * @param attackDamage
	 * @param attackSpeed
	 * @param movementSpeed
	 * @param attackRange
	 * @param visionRange
	 * @param hpRegenRate
	 * @param tierLevel
	 * @param blastRadius
	 * @param throughObstacles
	 * @param chargeBonusDamage
	 * @param chargeDistanceMax
	 * @param chargeSpeed
	 * @param maxShield
	 */
	public UnitStats(String id, String unitType, String faction, int maxHp, int populationCost, int ambroisieCost,
			int faithCost, float attackDamage, float attackSpeed, float movementSpeed, float attackRange,
			float visionRange, int hpRegenRate, int tierLevel, float blastRadius, boolean throughObstacles,
			float chargeBonusDamage, int chargeDistanceMax, float chargeSpeed, float maxShield) {
		this.id = id;
		this.UnitType = unitType;
		this.faction = faction;
		this.tierLevel = tierLevel;
		this.maxHp = maxHp;
		this.populationCost = populationCost;
		this.ambroisieCost = ambroisieCost;
		this.faithCost = faithCost;
		this.attackDamage = attackDamage;
		this.attackSpeed = (float)attackSpeed;
		this.movementSpeed = movementSpeed;
		this.attackRange = (float) attackRange;
		this.visionRange = (float) visionRange;
		this.hpRegenRate = hpRegenRate;
		
		this.maxShield = (float) maxShield;
		
		this.blastRadius = (float) blastRadius;
		
		this.throughObstacles = throughObstacles;
		this.chargeBonusDamage = chargeBonusDamage;
		this.chargeDistanceMax = chargeDistanceMax;
		this.chargeSpeed = chargeSpeed;
	}
	public UnitStats() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitType() {
		return UnitType;
	}

	public void setUnitType(String unitType) {
		UnitType = unitType;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getPopulationCost() {
		return populationCost;
	}

	public void setPopulationCost(int populationCost) {
		this.populationCost = populationCost;
	}

	public int getAmbroisieCost() {
		return ambroisieCost;
	}

	public void setAmbroisieCost(int ambroisieCost) {
		this.ambroisieCost = ambroisieCost;
	}

	public int getFaithCost() {
		return faithCost;
	}

	public void setFaithCost(int faithCost) {
		this.faithCost = faithCost;
	}

	public double getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(double attackDamage) {
		this.attackDamage = attackDamage;
	}

	public float getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public float getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}

	public float getVisionRange() {
		return visionRange;
	}

	public void setVisionRange(float visionRange) {
		this.visionRange = visionRange;
	}

	public int getHpRegenRate() {
		return hpRegenRate;
	}

	public void setHpRegenRate(int hpRegenRate) {
		this.hpRegenRate = hpRegenRate;
	}

	public int getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(int tierLevel) {
		this.tierLevel = tierLevel;
	}

	public float getBlastRadius() {
		return blastRadius;
	}

	public void setBlastRadius(float blastRadius) {
		this.blastRadius = blastRadius;
	}

	public boolean isThroughObstacles() {
		return throughObstacles;
	}

	public void setThroughObstacles(boolean throughObstacles) {
		this.throughObstacles = throughObstacles;
	}

	public double getChargeBonusDamage() {
		return chargeBonusDamage;
	}

	public void setChargeBonusDamage(double chargeBonusDamage) {
		this.chargeBonusDamage = chargeBonusDamage;
	}

	public int getChargeDistanceMax() {
		return chargeDistanceMax;
	}

	public void setChargeDistanceMax(int chargeDistanceMax) {
		this.chargeDistanceMax = chargeDistanceMax;
	}

	public double getChargeSpeed() {
		return chargeSpeed;
	}

	public void setChargeSpeed(double chargeSpeed) {
		this.chargeSpeed = chargeSpeed;
	}

	public float getMaxShield() {
		return maxShield;
	}

	public void setMaxShield(float maxShield) {
		this.maxShield = maxShield;
	}

	@Override
	public String toString() {
		return "UnitStats [id=" + id + ", UnitType=" + UnitType + ", faction=" + faction + ", tierLevel=" + tierLevel
				+ ", maxHp=" + maxHp + ", populationCost=" + populationCost + ", ambroisieCost=" + ambroisieCost
				+ ", faithCost=" + faithCost + ", attackDamage=" + attackDamage + ", attackSpeed=" + attackSpeed
				+ ", movementSpeed=" + movementSpeed + ", attackRange=" + attackRange + ", visionRange=" + visionRange
				+ ", hpRegenRate=" + hpRegenRate + ", blastRadius=" + blastRadius + ", throughObstacles="
				+ throughObstacles + ", chargeBonusDamage=" + chargeBonusDamage + ", chargeDistanceMax="
				+ chargeDistanceMax + ", chargeSpeed=" + chargeSpeed + ", maxShield=" + maxShield + "]";
	}
	
}
	