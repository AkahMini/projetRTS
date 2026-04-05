package engine.map;

import java.util.Objects;

/**
 * Data class of a block which is a area determined by its fields line and column
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class Block {
	
	private int line;
	private int column;
	/**
     * Constructor for Block.
     * @param line   The line index (y-coordinate).
     * @param column The column index (x-coordinate).
     */
	public Block(int line, int column) {
		this.line = line;
		this.column = column;
	}
	/**
     * @return The line index of this block.
     */
	public int getLine() {
		return line;
	}
	/**
     * @return The column index of this block.
     */
	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "Block [line=" + line + ", column=" + column + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		Block b = (Block) obj;
		
		return this.column == b.column && this.line == b.line;
	}

	@Override
	public int hashCode() {
		return Objects.hash(line, column);
	}
}
