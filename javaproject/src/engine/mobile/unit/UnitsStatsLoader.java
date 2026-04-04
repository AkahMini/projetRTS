package engine.mobile.unit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//import org.apache.log4j.Logger;

import config.GameConfiguration;
import engine.process.UnitRepository;

public class UnitsStatsLoader {
	private UnitRepository unitsRepository = UnitRepository.getInstance();
	//private static final Logger logger = Logger.getLogger(UnitsStatsLoader.class);
	public UnitsStatsLoader (String unitsStats) {
		String line;
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(GameConfiguration.UNITS_STATS));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");        	
				String id = data[0].trim();
				String UnitType = data[1].trim();
				String faction = data[2].trim();
				int tierLevel = Integer.valueOf(data[3].trim());
				int maxHp = Integer.valueOf(data[4].trim());
				int populationCost = Integer.valueOf(data[5].trim());
				int ambroisieCost = Integer.valueOf(data[6].trim());
				int faithCost = Integer.valueOf(data[7].trim());
				Double AttackDamage = Double.valueOf(data[8].trim());
				Float attackSpeed = Float.valueOf(data[9].trim());
				int movementsSpeed = Integer.valueOf(data[10].trim());
				int attackRange = Integer.valueOf(data[11].trim());
				int visionRange = Integer.valueOf(data[12].trim());
				int hpRegenRate = Integer.valueOf(data[13].trim());
				float maxShield = Float.valueOf(data[14].trim());
				Boolean ThroughObstacle_CAVALRY = Boolean.valueOf(data[15].trim());
				Double ChageBonusDamage = Double.valueOf(data[16].trim());
				int chargeDistanceMax = Integer.valueOf(data[17].trim());
				Double chargeSpeed = Double.valueOf(data[18].trim());
				int BlastRadius_ARTILLERY = Integer.valueOf(data[19].trim());
				UnitStats stats = new UnitStats(id, UnitType, faction, maxHp, populationCost, ambroisieCost, faithCost, AttackDamage, attackSpeed, movementsSpeed, attackRange, visionRange, hpRegenRate, tierLevel, BlastRadius_ARTILLERY, ThroughObstacle_CAVALRY, ChageBonusDamage, chargeDistanceMax, chargeSpeed, maxShield);
				String key = UnitType.toUpperCase() + "_" + faction.toUpperCase() + "_" + tierLevel;
				unitsRepository.register(key, stats);
				System.out.println("Added Unit key: "+key);
				//logger.info("UnitStats créé → clé={"+key+"} valeur={"+stats+"}");
			}br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
