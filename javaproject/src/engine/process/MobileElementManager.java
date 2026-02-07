package engine.process;

import java.util.ArrayList;
import java.util.List;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;


/**
 * 
 * @author LE RAY Yann
 *
 */
public class MobileElementManager implements MobileInterface {
	private Map map;
	private Chronometer chronometer = new Chronometer();

	public MobileElementManager(Map map) {
		this.map = map;
		chronometer.init();
	}

	public void nextRound() {
		chronometer.increment();
	}
	
	public CyclicCounter getHour() {
		return chronometer.getHour();
	}
		
	public CyclicCounter getMinute() {
		return chronometer.getMinute();
	}
	
	public CyclicCounter getSecond() {
		return chronometer.getSecond();
	}
	

	private static int getRandomNumber(int min, int max) {
		return (int) (Math.random() * (max + 1 - min)) + min;
	}
}