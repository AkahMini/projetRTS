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
		// 1. Si on compare l'objet avec lui-même, c'est forcément vrai
		if (this == obj) {
			return true;
		}
		
		// 2. Si l'autre objet n'existe pas (null) ou n'est pas de la classe Block, c'est faux
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		// 3. On est sûr que c'est un Block et qu'il n'est pas null, on peut le "convertir" (cast)
		Block b = (Block) obj;
		
		// 4. On compare enfin les coordonnées
		return this.column == b.column && this.line == b.line;
	}

	// Toujours redéfinir hashCode quand on redéfinit equals !
	@Override
	public int hashCode() {
		return Objects.hash(line, column);
	}
}
