package engine.mobile;

import engine.map.Block;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class RessourceDeposit {
	private Block position;
	private int amountRemaing;
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
}

