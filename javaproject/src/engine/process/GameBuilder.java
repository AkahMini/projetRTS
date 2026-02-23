package engine.process;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;


/**
 * 
 * Builder pattern class. Use to launch the game by building the object map and initialize the main manager. 
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class GameBuilder {

	public static Map buildMap() {
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}

	public static MobileInterface buildInitMobile(Map map, DefaultGameSettings gameSettings) {
		MobileInterface manager = new MobileElementManager(map, gameSettings);
				
		return manager;
	}

}
