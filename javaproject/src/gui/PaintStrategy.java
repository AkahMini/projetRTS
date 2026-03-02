package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.MobileElement;
import engine.mobile.Player;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Artillery;
import engine.mobile.unit.Cavalry;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.chrono.CyclicCounter;
import engine.process.GameUtility;

/**
 * 
 * Class that contains all the method used to paint element on the game window.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class PaintStrategy {
	private final int windowWidth = GameConfiguration.WINDOW_WIDTH;
	private final int windowHeight = GameConfiguration.WINDOW_HEIGHT; 
	private TextureInterface textureManager=new TextureManager();
	
	
	public void paint(Map map, Graphics graphics) {
		int blockSize = GameConfiguration.BLOCK_SIZE;
		Block[][] blocks = map.getBlocks();

		
		
		
		//used for drawing the game grid HARD IMPLEMENTED, NOT RELATIVE
		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = blocks[lineIndex][columnIndex];
				
				if(lineIndex==6) {
					graphics.setColor(Color.BLACK);
					graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
				}
				
				else if(lineIndex==7 && columnIndex==0) {
					graphics.drawImage(GameUtility.readImage("gameData/images/grassTiled.png"),0,7*blockSize,1000,650,null);
				}
				
				//this part is too laggy but keep it if this strategy have to be used
				/*
				else if(lineIndex>6 && columnIndex<GameConfiguration.COLUMN_COUNT-28) {
					textureManager.setCurrent("GrassTexture");
					Color[] palette=textureManager.getPalette();
					int[][] grid=textureManager.getPattern();
					for (int row = 0; row < grid.length; row++) {
			            for (int col = 0; col < grid[row].length; col++) {
			                graphics.setColor(palette[grid[row][col]]);
			                graphics.fillRect(columnIndex* blockSize + col,lineIndex* blockSize + row,1,1);
			            }
			        }
				}
				*/
				else if(lineIndex>6 && columnIndex==GameConfiguration.COLUMN_COUNT-28) {
					graphics.setColor(Color.BLACK);
					graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
				}
			}
			graphics.setColor(Color.BLACK);
			graphics.fillRect(100*blockSize, 27*blockSize, GameConfiguration.COLUMN_COUNT*blockSize, blockSize);
			graphics.fillRect(100*blockSize, 48*blockSize, GameConfiguration.COLUMN_COUNT*blockSize, blockSize);
			//this is temporary foh suh
			graphics.setFont(new Font("Arial", Font.PLAIN, 24));
			graphics.drawString("Inserez diagramme ici", 1020, 380);
		}

	}
	
	public void paint(CyclicCounter hour, CyclicCounter minute, CyclicCounter second, Graphics graphics) {

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		graphics.drawString("Temps de jeu :"+hour.toString()+":"+minute.toString()+":"+second.toString(), windowWidth/80,windowHeight/20);
	}
	
	public void paint(Player player, Graphics graphics) {
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.setColor(new Color(204,102,0));
		graphics.drawString("Population : "+player.getCurrentPopulation()+"/"+player.getMaxPopulation(), 5*windowWidth/6,windowHeight/20);
		graphics.setColor(new Color(0,204,102));
		graphics.drawString("Ambroisie : "+player.getAmbroisieStock(), windowWidth/2,windowHeight/20);
		graphics.setColor(new Color(0,0,153));
		graphics.drawString("Foi : "+player.getFaithStock(), 2*windowWidth/3,windowHeight/20);
	}
	
	
	public void paint(Building building, Graphics graphics) {
        Block position = building.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;
        int buildingSize = blockSize *2;

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
        
        graphics.fillRect(x * blockSize, y * blockSize, buildingSize, buildingSize);
        
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x * blockSize, y * blockSize, buildingSize, buildingSize);
        if (building instanceof UnitProducer && !building.getIsUnderConstruction()) {
            int queueSize = ((UnitProducer) building).getProductionQueue().size();
            graphics.setColor(Color.WHITE);
            int dotSize = buildingSize / 5; 
            int gap = 2;

            for (int i = 0; i < queueSize; i++) { // Loop to draw the queue as a visual cue for the user
            	int drawX = (x * blockSize) + (i * (dotSize + gap)); 

            	int drawY = (y * blockSize) + (buildingSize - dotSize - 2);

            	graphics.setColor(Color.WHITE);
            	graphics.fillRect(drawX, drawY, dotSize, dotSize);

            	graphics.setColor(Color.BLACK);
            	graphics.drawRect(drawX, drawY, dotSize, dotSize);
                graphics.setColor(Color.WHITE);
            }
        }
        
        if (building instanceof HQ && !building.getIsUnderConstruction()) {
            int queueSize = ((HQ) building).getWorkerProducer().getProductionQueue().size();
            graphics.setColor(Color.WHITE);
            int dotSize = buildingSize / 5; 
            int gap = 2;

            for (int i = 0; i < queueSize; i++) { // Loop to draw the queue as a visual cue for the user
            	int drawX = (x * blockSize) + (i * (dotSize + gap)); 

            	int drawY = (y * blockSize) + (buildingSize - dotSize - 2);

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
	        int buildingSize = blockSize *2;
	        int y = position.getLine();
	        int x = position.getColumn();
	        graphics.setColor(new Color(218, 165, 32));
	        graphics.fillRect(x * blockSize, y * blockSize, buildingSize, buildingSize);
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
        if(unit instanceof Artillery) {
        	graphics.setColor(Color.RED.darker());
        }
        if(unit instanceof Cavalry) {
            Cavalry cav = (Cavalry) unit;
            if (cav.getChargeDistanceValue() > 0) {
                graphics.setColor(Color.CYAN);
            } else {
                graphics.setColor(Color.BLUE.darker());
            }
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
			if(x>10 && y>10) {
				graphics.drawRect(firstColumn*blockSize, firstLine*blockSize, x, y);
			}
		}
	}
	
	
	//display selected Units info
	public void paintUnitInfo(List<Unit> unitsInSelectedArea, Graphics graphics) {

		int x =windowWidth-windowWidth/5;
		int y =windowHeight/8;
		final int maxUnitDisplayed =5;

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));
		graphics.drawString("Unitées selectionées : "+unitsInSelectedArea.size(), x, y);
		graphics.setFont(new Font("Arial", Font.PLAIN, 14));
		y+=18;
		//this is for each unit in the selected area
		int i=0; //counter
		for(Unit unit : unitsInSelectedArea) {
			graphics.setColor(Color.BLACK);
			graphics.drawString(unit.getUnitName()+" hp :", x, y);
			y+=8;
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
			if (unit instanceof Infantry) {
			    int percentShield = ((Infantry) unit).getPercentShield();
			    
			    if (percentShield > 0) {
			        int shieldWidth = (int)((percentShield *50) / 100.0);
			        graphics.setColor(Color.BLUE);
			        graphics.fillRect(x + (int)((percent*220.0/100)), y, shieldWidth, 6);
			    }
			}
			y+=22;
			i++;
			if(i==maxUnitDisplayed) break;
		}
		// this is for the +nbOfunitNotDisplayed at the end
		if(unitsInSelectedArea.size()>maxUnitDisplayed) {
			y+=4;//tweak because of the weird way drawString works
			graphics.setFont(new Font("Arial", Font.PLAIN, 18));
			graphics.setColor(Color.BLACK);
			int unitNotDisplayed=unitsInSelectedArea.size()-maxUnitDisplayed;
			graphics.drawString("+"+unitNotDisplayed, x, y);
		}
	}
	
	//only the info+button of the first selected building is displayed
	public void paintBuildingInfo(Building build, Graphics graphics) {
		int x =windowWidth-windowWidth/5;
		int y =510;//~13*windowHeight/18 but meh
		
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));
		graphics.drawString(build.getBuildingName()+" Info :", x, y);
		graphics.setFont(new Font("Arial", Font.PLAIN, 14));
		y+=18;
		graphics.drawString("hp :", x, y);
		y+=8;
		int percent = build.getPercentHP();
		
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
		
		
		//button to be defined
		if(build.getBuildingName().equals("Camp Olympique")) {
			graphics.drawImage(GameUtility.readImage("gameData/images/miner.png"),1020,560,60,60,null);
		}
		//template for visual use only
		//graphics.drawRect(1020, 560, 240, 140);
		//Draw img max 6 or 9 for the different button related to the building 
		//-> one image per building per button (capacity/research/unit)
		
	}
	
	public void paintAttack(Unit unit, Graphics graphics) {
		MobileElement enemy = unit.getTarget();
		int blockSize = GameConfiguration.BLOCK_SIZE;
		graphics.setColor(Color.YELLOW);
		graphics.drawLine(unit.getPosition().getColumn()*blockSize+5,unit.getPosition().getLine()*blockSize+5,enemy.getPosition().getColumn()*blockSize+5,enemy.getPosition().getLine()*blockSize+5);
	}
}