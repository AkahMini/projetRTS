package engine.mobile;

import engine.map.Block;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class RessourceDeposit {
	// Const
    public static final String FAITH = "FAITH";
    public static final String AMBROISE = "AMBROISE";
	
	
	private Block position;
	private int amountRemaining;
	private int currentWorkers;
	private int maxWorkers;
	private String type;
	private float extractionSpeed;
	
	public RessourceDeposit(Block position,String type) {
		this.position=position;
		this.type=type;
	}
	
	public Block getPosition() {
		return this.position;
	}
	public int getAmountRemaining() {
		return this.amountRemaining;
	}
	
	public int getCurrentWorkers() {
		return this.currentWorkers;
	}
	public int getMaxWorkers() {
		return this.maxWorkers;
	}
	public String getType() {
		return this.type;
	}
	public float getExtractionSpeed() {
		return this.extractionSpeed;
	}
	
}
	

