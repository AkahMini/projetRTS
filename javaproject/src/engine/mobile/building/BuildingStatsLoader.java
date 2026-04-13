package engine.mobile.building;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import engine.process.BuildingRepository;
import engine.process.GameUtility;
import log.LoggerUtility;



public class BuildingStatsLoader {
	
	private BuildingRepository buildingRepository = BuildingRepository.getInstance();
	private static Logger logger = LoggerUtility.getLogger(BuildingStatsLoader.class, "html");
	
	public BuildingStatsLoader (String buildingsStats) {
        String line;
        try {
        	BufferedReader br = getReader(GameConfiguration.BUILDINGS_STATS);
			logger.info("Reading \""+GameConfiguration.BUILDINGS_STATS+"\"");
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
                        logger.info("upgrade "+ upgrade[i]);

                    }
                    //the key is this and not the name cause we use these three arguments in the factory so it make more sense
                    String key = buildingType.toUpperCase() + "_" + faction.toUpperCase() + "_" + tierLevel;
                    buildingRepository.register(key, stats);
                    logger.info("Added building key: " + key+", id: "+id);
                    //System.out.println("Added building key: "+key);
                }else {
                    BuildingStats stats = new BuildingStats(id, buildingType, faction, tierLevel, maxHp, ambroisieCost, faithCost, constructionTime, productionSpeed, towerDamage,towerAttackSpeed, towerRange, populationProvided,technologies,visionRange);
                    //the key is this and not the name cause we use these three arguments in the factory so it make more sense
                    String key = buildingType.toUpperCase() + "_" + faction.toUpperCase() + "_" + tierLevel;
                    buildingRepository.register(key, stats);
                    logger.info("Added building key : " + key+", id: "+id);
                    //System.out.println("Added building key: "+key);
                }
            }
			
            br.close();
            logger.info("Reading of \""+GameConfiguration.BUILDINGS_STATS+"\" completed");
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal(e);
		}
	}
	
	public static BufferedReader getReader(String path) {
	    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	    
	    if (is == null) {
	        throw new RuntimeException("Fichier introuvable : " + path);
	    }

	    return new BufferedReader(new InputStreamReader(is));
	}
}
