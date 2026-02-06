package engine.mobile.building;

import java.util.List;

import engine.map.Block;
import engine.mobile.MobileElement;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class ResearchBuilding extends MobileElement{

	private List<String> technologiesUnlocked;
	
	public ResearchBuilding(Block position) {
		super(position);
	}
}
