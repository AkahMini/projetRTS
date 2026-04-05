package engine.mobile.building;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import config.GameConfiguration;
import engine.process.BuildingRepository;


public class BuildingStatsLoader {
	
	private BuildingRepository buildingRepository = BuildingRepository.getInstance();
	
	public BuildingStatsLoader (String buildingsStats) {
        String line;
        try {
			BufferedReader br = new BufferedReader(new FileReader(GameConfiguration.BUILDINGS_STATS));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");        	
				String id = data[0].trim();
                String buildingType = data[1].trim();
                String faction = data[2].trim();
                int tierLevel = Integer.valueOf(data[3].trim());
                int maxHp = Integer.valueOf(data[4].trim());
                int ambroisieCost = Integer.valueOf(data[5].trim());
                int faithCost = Integer.valueOf(data[6].trim());
                int constructionTime = Integer.valueOf(data[7].trim());
                int productionSpeed = Integer.valueOf(data[8].trim());
                int towerDamage = Integer.valueOf(data[9].trim());
                int towerAttackSpeed = Integer.valueOf(data[10].trim());
                int towerRange = Integer.valueOf(data[11].trim());
                int populationProvided = Integer.valueOf(data[12].trim());
                String technologies=data[13].trim();
                String[] upgrade= technologies.split("\\|");
                int visionRange = Integer.valueOf(data[14].trim());
                if (upgrade.length>1) {
                    BuildingStats stats = new BuildingStats(id, buildingType, faction, tierLevel, maxHp, ambroisieCost, faithCost, constructionTime, productionSpeed, towerDamage,towerAttackSpeed, towerRange, populationProvided,upgrade[0],visionRange);
                    int i;
                    for(i=0;i<upgrade.length;i++) {
                    	stats.addTechnologieUnlocked(upgrade[i]);
                        System.out.println(upgrade[i]);

                    }
                    //the key is this and not the name cause we use these three arguments in the factory so it make more sense
                    String key = buildingType.toUpperCase() + "_" + faction.toUpperCase() + "_" + tierLevel;
                    buildingRepository.register(key, stats);
                    //System.out.println("Added building key: "+key);
                }else {
                    BuildingStats stats = new BuildingStats(id, buildingType, faction, tierLevel, maxHp, ambroisieCost, faithCost, constructionTime, productionSpeed, towerDamage,towerAttackSpeed, towerRange, populationProvided,technologies,visionRange);
                    //the key is this and not the name cause we use these three arguments in the factory so it make more sense
                    String key = buildingType.toUpperCase() + "_" + faction.toUpperCase() + "_" + tierLevel;
                    buildingRepository.register(key, stats);
                    //System.out.println("Added building key: "+key);
                }
            }
			
            br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
