package engine.mobile.building;

import java.util.List;

import engine.map.Block;

/**
 * Class representing a building used for researching technologies.
 * Allows unlocking new upgrades or units for the player.
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class ResearchBuilding extends Building{

	private List<String> technologiesUnlocked;
	
	public ResearchBuilding(Block position) {
		super(position);
	}

	public List<String> getTechnologiesUnlocked() {
		return technologiesUnlocked;
	}

	public void setTechnologiesUnlocked(List<String> technologiesUnlocked) {
		this.technologiesUnlocked = technologiesUnlocked;
	}
}
