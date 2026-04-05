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
	
	private static final int RESEARCH_TIME = 20;
	
	private int research1=0;
	private int research2=0;
	
	private boolean researchActive1=false;
	private boolean researchActive2=false;

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

	public int getResearch1() {
		return research1;
	}

	public void setResearch1(int research1) {
		this.research1 = research1;
	}

	public int getResearch2() {
		return research2;
	}

	public void setResearch2(int research2) {
		this.research2 = research2;
	}

	public static int getResearchTime() {
		return RESEARCH_TIME;
	}

	public boolean isResearchActive1() {
		return researchActive1;
	}

	public void setResearchActive1(boolean researchActive1) {
		this.researchActive1 = researchActive1;
	}

	public boolean isResearchActive2() {
		return researchActive2;
	}

	public void setResearchActive2(boolean researchActive2) {
		this.researchActive2 = researchActive2;
	}
}
