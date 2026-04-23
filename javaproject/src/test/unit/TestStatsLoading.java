package test.unit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.mobile.building.BuildingStats;
import engine.mobile.building.BuildingStatsLoader;
import engine.mobile.unit.UnitStats;
import engine.mobile.unit.UnitsStatsLoader;
import engine.process.BuildingRepository;
import engine.process.UnitRepository;





public class TestStatsLoading {
	@Before
	public void prepareLoaders() {
		new BuildingStatsLoader(GameConfiguration.BUILDINGS_STATS);
		new UnitsStatsLoader(GameConfiguration.BUILDINGS_STATS);
	}
	
	@Test
	public void testBuildingStatsLoader() {
		/**
		 * Tests on "Baliste" stats
		 */
		BuildingStats buildingLoadedStats = BuildingRepository.getInstance().getStats("DEFENSETOWER_ZEUS_2");
		BuildingStats buildingstats = new BuildingStats();
		buildingstats.setId("Baliste");
		buildingstats.setBuildingType("DefenseTower");
		buildingstats.setFaction("Zeus");
		buildingstats.setTierLevel(2);
		buildingstats.setMaxHp(800);
		buildingstats.setAmbroisieCost(250);
		buildingstats.setFaithCost(250);
		buildingstats.setConstructionTime(20);
        buildingstats.setProductionSpeed(0);
        buildingstats.setTowerDamage(25);
        buildingstats.setTowerAttackSpeed(2);
        buildingstats.setTowerRange(6);
        buildingstats.setPopulationProvided(0);
        buildingstats.addTechnologieUnlocked("0");
        buildingstats.setVisionRange(7);
		
		assertNotNull(buildingLoadedStats);
		assertEquals(buildingstats.toString(), buildingLoadedStats.toString());
	}
	@Test
	public void testUnitStatsLoader() {
		/**
		 * Test on "Spartiates" stats
		 */
		UnitStats unitLoadedStats = UnitRepository.getInstance().getStats("WORKER_HADES_1");
		UnitStats unitStats = new UnitStats();
		unitStats.setId("Spartiates");
		unitStats.setUnitType("WORKER");
		unitStats.setFaction("HADES");
		unitStats.setTierLevel(1);
		unitStats.setMaxHp(60);
		unitStats.setPopulationCost(1);
		unitStats.setAmbroisieCost(0);
		unitStats.setFaithCost(0);
		unitStats.setAttackDamage(0);
		unitStats.setAttackSpeed(0);
		unitStats.setMovementSpeed(11);
		unitStats.setAttackRange(1);
		unitStats.setVisionRange(1);
		unitStats.setHpRegenRate(2);
		unitStats.setBlastRadius(0);
		unitStats.setThroughObstacles(false);
		unitStats.setChargeBonusDamage(0);
		unitStats.setChargeDistanceMax(0);
		unitStats.setChargeSpeed(0);
		unitStats.setMaxShield(0);
		
		assertNotNull(unitLoadedStats);
		assertEquals(unitStats.toString(), unitLoadedStats.toString());
	}
}
