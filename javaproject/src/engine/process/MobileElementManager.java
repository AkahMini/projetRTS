package engine.process;

import java.util.ArrayList;
import java.util.List;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;


/**
 * 
 * @author LE RAY Yann
 *
 */
public class MobileElementManager implements MobileInterface {
	private Map map;

	public MobileElementManager(Map map) {
		this.map = map;
	}

	public void nextRound() {
		System.out.println("next Round");
	}

	private static int getRandomNumber(int min, int max) {
		return (int) (Math.random() * (max + 1 - min)) + min;
	}
}