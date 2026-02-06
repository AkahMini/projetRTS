package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;

/**
 * 
 * @author LE RAY Yann
 *
 */
public class PaintStrategy {
	public void paint(Map map, Graphics graphics) {
		int blockSize = GameConfiguration.BLOCK_SIZE;
		Block[][] blocks = map.getBlocks();

		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = blocks[lineIndex][columnIndex];

				if ((lineIndex + columnIndex) % 2 == 0) {
					graphics.setColor(Color.GRAY);
					graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
				}
			}
		}
	}

}
