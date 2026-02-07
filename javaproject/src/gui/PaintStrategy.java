package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.process.chrono.CyclicCounter;

/**
 * 
 * @author LE RAY Yann
 *
 */
public class PaintStrategy {
	
	public void paint(Map map, Graphics graphics) {
		int blockSize = GameConfiguration.BLOCK_SIZE;
		Block[][] blocks = map.getBlocks();

		
		
		
		//used for drawing the game grid
		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = blocks[lineIndex][columnIndex];
				
				if(lineIndex==3) {
					graphics.setColor(Color.BLACK);
					graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
				}
				
				else if(lineIndex>3 && columnIndex<GameConfiguration.COLUMN_COUNT-15) {
					if ((lineIndex + columnIndex) % 2 == 0) {
						graphics.setColor(Color.GRAY);
						graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
					}
				}
				
				else if(lineIndex>3 && columnIndex==GameConfiguration.COLUMN_COUNT-15) {
					graphics.setColor(Color.BLACK);
					graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
				}
			}
		}

	}
	
	public void paint(CyclicCounter hour, CyclicCounter minute, CyclicCounter second, Graphics graphics) {

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		graphics.drawString("Temps de jeu :"+hour.toString()+":"+minute.toString()+":"+second.toString(), 20, 28);
	}
	
	
	public void paint(Building building, Graphics graphics) {
        Block position = building.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int y = position.getLine();
        int x = position.getColumn();

     
        if (building instanceof UnitProducer) {
            graphics.setColor(Color.ORANGE); // Caserne en Orange
        }
        
        graphics.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
        
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x * blockSize, y * blockSize, blockSize, blockSize);
    }

	public void paint(Unit unit, Graphics graphics) {
        Block position = unit.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int y = position.getLine();
        int x = position.getColumn();

     
        if (unit instanceof Infantry) {
            graphics.setColor(Color.GREEN); // Greeeeeeeeeeen
        }
        
        graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);
        
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
    }
	
	public void paint(List<Block> selectedArea, Graphics graphics) {
		
		int blockSize = GameConfiguration.BLOCK_SIZE;
			graphics.setColor(Color.YELLOW); // Selected area in yellow
			if(selectedArea!=null) {
				for(Block block:selectedArea) {
						graphics.fillRect(block.getLine() * blockSize, block.getColumn() * blockSize, blockSize, blockSize);
					}
			}
	}
}