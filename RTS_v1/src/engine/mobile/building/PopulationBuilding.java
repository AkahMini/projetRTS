package engine.mobile.building;

import engine.map.Block;

/**
 * Class representing a building that provides population capacity.
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class PopulationBuilding extends Building{

	private int populationProvided;
	
	public PopulationBuilding(Block position) {
		super(position);
	}

	public int getPopulationProvided() {
		return populationProvided;
	}

	public void setPopulationProvided(int populationProvided) {
		this.populationProvided = populationProvided;
	}
}
