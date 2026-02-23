package config;


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
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;

	public static final int BLOCK_SIZE = 10;//Default 10

	public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
	public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;
	
	public static final int GAME_SPEED =10;//Default 10
	

}