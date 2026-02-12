package engine.map;

/**
 * Class used to initialize the grid of the map with the right numbers of line and column 
 * and an array to store the blocks 
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */

public class Map {

	private Block[][] blocks;
	private int lineCount;
	private int columnCount;
	/**
	 * constructor of the class Map
	 * 
	 * @param lineCount   Number of lines.
	 * @param columnCount   Number of columns.
     *
     * The constructor instantiate the blocks in the right index in the array of the fields blocks
	 */
	public Map(int lineCount, int columnCount) {
		init(lineCount, columnCount);

		for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				blocks[lineIndex][columnIndex] = new Block(lineIndex, columnIndex);
			}
		}
	}
	
	/**
	 * Initialize the fields lineCount and columnCount and the array with the right size
	 * 
	 * @param lineCount   Number of lines.
	 * @param columnCount   Number of columns.
     *
	 */
	private void init(int lineCount, int columnCount) {
		this.lineCount = lineCount;	
		this.columnCount = columnCount;

		//blocks = new Block[54][96];
		blocks = new Block[lineCount][columnCount];
		
	
	}
	/**
     * @return The 2D array containing all the blocks of the map.
     */
	public Block[][] getBlocks() {
		return blocks;
	}
	/**
     * @return The total number of lines.
     */
	public int getLineCount() {
		return lineCount;
	}
	/**
     * @return The total number of columns.
     */
	public int getColumnCount() {
		return columnCount;
	}
	/**
     * Retrieves a specific block from the grid.
     * @param line   The line index.
     * @param column The column index.
     * @return The Block object at the specified coordinates.
     */
	public Block getBlock(int line, int column) {
		return blocks[line][column];
	}

}
