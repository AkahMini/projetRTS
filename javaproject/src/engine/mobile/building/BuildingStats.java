package engine.mobile.building;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class representing the stats of a building loaded from the CSV.
 */
public class BuildingStats {
    private String id;
    private String buildingType;
    private String faction;
    private int tierLevel;
    private int maxHp;
    private int ambroisieCost;
    private int faithCost;
    private int constructionTime;
    private int productionSpeed;
    private int towerDamage;
    private int towerAttackSpeed;
    private int towerRange;
    private int populationProvided;
    private ArrayList<String> technologieUnlocked = new ArrayList<>();

    public BuildingStats(String id, String buildingType, String faction, int tierLevel, int maxHp, int ambroisieCost, int faithCost, int constructionTime, int productionSpeed, int towerDamage, int towerAttackSpeed, int towerRange, int populationProvided,String technologie) {
        this.setId(id);
        this.setBuildingType(buildingType);
        this.setFaction(faction);
        this.setTierLevel(tierLevel);
        this.setMaxHp(maxHp);
        this.setAmbroisieCost(ambroisieCost);
        this.setFaithCost(faithCost);
        this.setConstructionTime(constructionTime);
        this.setProductionSpeed(productionSpeed);
        this.setTowerDamage(towerDamage);
        this.setTowerAttackSpeed(towerAttackSpeed);
        this.setTowerRange(towerRange);
        this.setPopulationProvided(populationProvided);
        this.addTechnologieUnlocked(technologie);
    }

	public int getPopulationProvided() {
		return populationProvided;
	}

	public void setPopulationProvided(int populationProvided) {
		this.populationProvided = populationProvided;
	}

	public int getTowerRange() {
		return towerRange;
	}

	public void setTowerRange(int towerRange) {
		this.towerRange = towerRange;
	}

	public int getTowerDamage() {
		return towerDamage;
	}

	public void setTowerDamage(int towerDamage) {
		this.towerDamage = towerDamage;
	}

	public int getProductionSpeed() {
		return productionSpeed;
	}

	public void setProductionSpeed(int productionSpeed) {
		this.productionSpeed = productionSpeed;
	}

	public int getConstructionTime() {
		return constructionTime;
	}

	public void setConstructionTime(int constructionTime) {
		this.constructionTime = constructionTime;
	}

	public int getFaithCost() {
		return faithCost;
	}

	public void setFaithCost(int faithCost) {
		this.faithCost = faithCost;
	}

	public int getAmbroisieCost() {
		return ambroisieCost;
	}

	public void setAmbroisieCost(int ambroisieCost) {
		this.ambroisieCost = ambroisieCost;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(int tierLevel) {
		this.tierLevel = tierLevel;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTowerAttackSpeed() {
		return towerAttackSpeed;
	}

	public void setTowerAttackSpeed(int towerAttackSpeed) {
		this.towerAttackSpeed = towerAttackSpeed;
	}
	public ArrayList<String> getTechnologieUnlocked() {
		return technologieUnlocked;
	}

	public void addTechnologieUnlocked(String technologieUnlocked) {
		this.technologieUnlocked.add(technologieUnlocked);
	}
	public String toString() {
        return "BuildingStats :" + "id='" + id +", buildingType='" + buildingType  + ", faction='" + faction +", tierLevel=" + tierLevel 
        		+", maxHp=" + maxHp +", ambroisieCost=" + ambroisieCost +", faithCost=" + faithCost + ", constructionTime=" + constructionTime +
                ", productionSpeed=" + productionSpeed +
                ", towerDamage=" + towerDamage +
                ", towerAttackSpeed=" + towerAttackSpeed +
                ", towerRange=" + towerRange +
                ", populationProvided=" + populationProvided +
                ", technologieUnlocked=" + technologieUnlocked ;
    }
}