package engine.process;

import java.util.List;
import engine.map.Block;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * @author LE RAY Yann
 *
 */

public interface MobileInterface {

	void nextRound();
	
	public CyclicCounter getHour();
	
	public CyclicCounter getMinute();
	
	public CyclicCounter getSecond();
}