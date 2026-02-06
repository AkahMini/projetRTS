package engine.process;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;


/**
 * 
 * @author LE RAY Yann
 *
 */
public class GameBuilder {

	public static Map buildMap() {
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}

	public static MobileInterface buildInitMobile(Map map) {
		MobileInterface manager = new MobileElementManager(map);
				
		return manager;
	}

}
