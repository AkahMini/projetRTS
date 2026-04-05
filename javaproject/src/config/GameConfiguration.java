package config;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class containing the constant relevant to the game configuration like the game speed or the number of line
 * and column of the grid
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class GameConfiguration {
	public static final int WINDOW_WIDTH = 1280;
	public static final int WINDOW_HEIGHT = 720;

	public static final int BLOCK_SIZE = 10;//Default 10

	public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
	public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;
	
	public static final int GAME_SPEED =1;//Default 10
	
	public static final String UNITS_STATS = "src/gameData/units_Stats.csv";
	public static final String BUILDINGS_STATS = "src/gameData/buildings_Stats.csv";
	
	public static final int SHORT_CLICK_TIME_DURATION = 120;

	public static final ArrayList<String> GAMESTATE= new ArrayList<String>(Arrays.asList("MENU","CHOOSE","PLAYING","END"));
}