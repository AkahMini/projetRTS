package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
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
	
	public void paint(Player player, Graphics graphics) {
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.setColor(new Color(204,102,0));
		graphics.drawString("Population : "+player.getCurrentPopulation()+"/"+player.getMaxPopulation(), 1670,28);
		graphics.setColor(new Color(0,204,102));
		graphics.drawString("Ambroisie : "+player.getAmbroisieStock(), 1370,28);
		graphics.setColor(new Color(0,0,153));
		graphics.drawString("Foi : "+player.getFaithStock(), 1070,28);
	}
	
	
	public void paint(Building building, Graphics graphics) {
        Block position = building.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int y = position.getLine();
        int x = position.getColumn();
        if(building.getIsUnderConstruction()) {
        	graphics.setColor(Color.ORANGE); // Orange for building under Construction
        }
        else if(building instanceof HQ) {
        	graphics.setColor(Color.MAGENTA);
        }
        else if (building instanceof UnitProducer) {
            graphics.setColor(Color.BLUE); // blue for unitProdcing Building
            
        }
        
        graphics.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
        
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x * blockSize, y * blockSize, blockSize, blockSize);
        if (building instanceof UnitProducer && !building.getIsUnderConstruction()) {
            int queueSize = ((UnitProducer) building).getProductionQueue().size();
            graphics.setColor(Color.WHITE);
            int dotSize = blockSize / 5; 
            int gap = 2;

            for (int i = 0; i < queueSize; i++) { // Loop to draw the queue as a visual cue for the user
            	int drawX = (x * blockSize) + (i * (dotSize + gap)); 

            	int drawY = (y * blockSize) + (blockSize - dotSize - 2);

            	graphics.setColor(Color.WHITE);
            	graphics.fillRect(drawX, drawY, dotSize, dotSize);

            	graphics.setColor(Color.BLACK);
            	graphics.drawRect(drawX, drawY, dotSize, dotSize);
                graphics.setColor(Color.WHITE);
            }
        }
        
	}
    
	public void paint(RessourceDeposit deposit, Graphics graphics) {
		 Block position = deposit.getPosition();
	        int blockSize = GameConfiguration.BLOCK_SIZE;
	        int y = position.getLine();
	        int x = position.getColumn();
	        graphics.setColor(new Color(218, 165, 32));
	        graphics.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
	}

	public void paint(Unit unit, Graphics graphics) {
        Block position = unit.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int y = position.getLine();
        int x = position.getColumn();

     
        if (unit instanceof Infantry) {
        	if(unit.getUnitFaction().equals("Zeus")) {
                graphics.setColor(Color.GREEN); // Greeeeeeeeeeen
        	}
        	
        	else {
                graphics.setColor(Color.RED); // Ennemy=Red
        	}
        }
        if(unit instanceof Worker) {
        	graphics.setColor(Color.YELLOW);
        }
        
        graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);
        
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
    }
	
	
	//draw the selected area
	public void paint(List<Block> selectedArea, Graphics graphics) {
		//check if an area is selected first
		if(selectedArea!=null && !selectedArea.isEmpty()) {
			Block startPosition = selectedArea.get(0);
			int blockSize = GameConfiguration.BLOCK_SIZE;
			graphics.setColor(Color.BLACK);
			Iterator<Block> it = selectedArea.iterator();//iterator is used here but can be replaced
			Block endPosition=selectedArea.get(0);
			Block temp=null;
			
			//find the top-left most and the bottom-right most square of the selection, its not always selectedArea.get(0) !!
			while(it.hasNext()) {
				temp=it.next();
				if(endPosition.getLine()<temp.getLine()||endPosition.getColumn()<temp.getColumn()) {
					endPosition=temp;
				}else if(startPosition.getLine()>temp.getLine()||startPosition.getColumn()>temp.getColumn()) {
					startPosition=temp;
				}
			}
			
			//maybe all of this is not optimal but it work as wanted so its fair enough
			int firstLine = Math.min(startPosition.getLine(), endPosition.getLine());
			int lastLine = Math.max(startPosition.getLine(), endPosition.getLine());
			int firstColumn = Math.min(startPosition.getColumn(), endPosition.getColumn());
			int lastColumn = Math.max(startPosition.getColumn(), endPosition.getColumn());
				
			//get the distance between the start and the end
			int y=((lastLine-(firstLine))+1)*blockSize;
			int x=((lastColumn-(firstColumn))+1)*blockSize;
			// x & y are in pixel unit. this make the 1x1 block selection invisible at screen (mainly because you can only select 1 thing with this)
			if(x>20 && y>20) {
				graphics.drawRect(firstColumn*blockSize, firstLine*blockSize, x, y);
			}
		}
	}
	
	
	//display selected Units info
	public void paintUnitInfo(List<Unit> unitsInSelectedArea, Graphics graphics) {
		//int blockSize = GameConfiguration.BLOCK_SIZE;
		// These one are choses BECAUSE of the canva size, need to change to relative but good for now
		int x =1650;
		int y =100;
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("Unitées selectionées : "+unitsInSelectedArea.size(), x, y);
		graphics.setFont(new Font("Arial", Font.PLAIN, 16));
		y+=24;
		//this is for each unit in the selected area
		for(Unit unit : unitsInSelectedArea) {
			graphics.setColor(Color.BLACK);
			graphics.drawString(unit.getUnitName()+" hp :", x, y);
			y+=10;
			int percent = unit.getPercentHP();
			
			//only current hp is in green. The >=98 is for the eventual float to int conevrsion error
			if(percent>=98) {
				graphics.setColor(new Color(0,102,0));// dark greeeeeeen
				graphics.fillRect(x, y, 220, 6);
			}else {
				graphics.setColor(new Color(0,102,0));// dark greeeeeeen
				graphics.fillRect(x, y, (int)((percent*220.0/100)), 6);
				graphics.setColor(Color.RED);
				graphics.fillRect((int)((percent*220.0/100))+x, y, (int)(220-(percent*220.0/100)), 6);
			}
			y+=26;
		}
	}
}