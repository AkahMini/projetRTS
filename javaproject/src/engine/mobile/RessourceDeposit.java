package engine.mobile;

import engine.map.Block;

/**
 * Data class of a RessourceDeposit, the deposit can be of Faith or Ambrosia and has a limited amount.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class RessourceDeposit {
	// Constant
    public static final String FAITH = "FAITH";
    public static final String AMBROSIA = "AMBROSIA";
	
	
	private Block position;
	private int amountRemaining;
	private int currentWorkers;
	private int maxWorkers;
	private String type;
	private float extractionSpeed;
	
	/**
	 * Constructor for RessourceDeposit.
	 * Initializes the deposit at a specific location with a defined type.
	 * @param position The block on the map where the deposit is located.
	 * @param type     The type of resource (Use constants FAITH or AMBROSIA).
	 */
	public RessourceDeposit(Block position,String type) {
		this.position=position;
		this.type=type;
	}
	
	/**
	 * @return The position of the deposit on the map.
	 */
	public Block getPosition() {
		return this.position;
	}
	
	/**
	 * @return The amount of resources currently remaining in the deposit.
	 */
	public int getAmountRemaining() {
		return this.amountRemaining;
	}
	
	/**
	 * @return The number of workers currently extracting resources from this deposit.
	 */
	public int getCurrentWorkers() {
		return this.currentWorkers;
	}
	
	/**
	 * @return The maximum number of workers allowed to extract simultaneously.
	 */
	public int getMaxWorkers() {
		return this.maxWorkers;
	}
	
	/**
	 * @return The type of the resource.
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * @return The speed at which resources are extracted from this deposit.
	 */
	public float getExtractionSpeed() {
		return this.extractionSpeed;
	}

	public void setPosition(Block position) {
		this.position = position;
	}

	public void setAmountRemaining(int amountRemaining) {
		this.amountRemaining = amountRemaining;
	}

	public void setCurrentWorkers(int currentWorkers) {
		this.currentWorkers = currentWorkers;
	}

	public void setMaxWorkers(int maxWorkers) {
		this.maxWorkers = maxWorkers;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExtractionSpeed(float extractionSpeed) {
		this.extractionSpeed = extractionSpeed;
	}
	
}
	

