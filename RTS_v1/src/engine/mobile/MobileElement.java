package engine.mobile;

import engine.map.Block;

/**
 * Abstract class of a MobileElement.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public abstract class MobileElement {

	private Block position;
	/**
     * Constructor for MobileElement.
     * @param position   The block where the mobileElement is at the moment of instantiation.
     */
	public MobileElement(Block position) {
		this.position = position;
	}
	/**
     * @return The position(the block) of the MobileElement.
     */
	public Block getPosition() {
		return position;
	}
	/**
     * @param position 	The new position(the block) of the MobileElement.
     */
	public void setPosition(Block position) {
		this.position = position;
	}

}
