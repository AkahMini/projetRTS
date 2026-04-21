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
		BuildingStats buildingstats = new BuildingStats("Baliste","DefenseTower","Zeus",2,800,250,250,20,0,15,2,8,0,"0",(float)3.0);
		BuildingStats buildingLoadedStats = BuildingRepository.getInstance().getStats("DEFENSETOWER_ZEUS_2");
		assertNotNull(buildingLoadedStats);
		assertEquals(buildingstats.toString(), buildingLoadedStats.toString());
	}
	@Test
	public void testUnitStatsLoader() {
		//new UnitStats(id, UnitType, faction, maxHp, populationCost, ambroisieCost, faithCost, AttackDamage, attackSpeed, movementsSpeed, attackRange, visionRange, hpRegenRate, tierLevel, BlastRadius_ARTILLERY, ThroughObstacle_CAVALRY, ChageBonusDamage, chargeDistanceMax, chargeSpeed, maxShield));
		UnitStats unitLoadedStats = UnitRepository.getInstance().getStats("WORKER_HADES_1");
		UnitStats unitStats = new UnitStats("Spartiates", "WORKER", "HADES", 1, 60, 1, 0, 0, 0.0f, 0.0f, 11.0f, 1.0f, 1, 2, 0.0f,false, 0.0f, 0,0.0f, 0.0f);
		assertNotNull(unitLoadedStats);
		assertEquals(unitStats.toString(), unitLoadedStats.toString());
	}
}
