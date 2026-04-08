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
		BuildingStats buildingstats = new BuildingStats("Baliste","DefenseTower","Zeus",2,400,250,250,20,0,15,2,8,0,"0",(float)3.0);
		BuildingStats buildingLoadedStats = BuildingRepository.getInstance().getStats("DEFENSETOWER_ZEUS_2");
		
		//System.out.println(buildingstats);
		//System.out.println(buildingLoadedStats);
		assertNotNull(buildingLoadedStats);
		assertEquals(buildingstats.toString(), buildingLoadedStats.toString());
	}
	@Test
	public void testUnitStatsLoader() {
		BuildingStats buildingstats = new BuildingStats("Baliste","DefenseTower","Zeus",2,400,250,250,20,0,15,2,8,0,"0",(float)3.0);
		BuildingStats buildingLoadedStats = BuildingRepository.getInstance().getStats("DEFENSETOWER_ZEUS_2");
		
		//System.out.println(buildingstats);
		//System.out.println(buildingLoadedStats);
		assertNotNull(buildingLoadedStats);
		assertEquals(buildingstats.toString(), buildingLoadedStats.toString());
	}
}
