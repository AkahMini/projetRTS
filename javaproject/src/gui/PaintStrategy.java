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
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.building.PopulationBuilding;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.Artillery;
import engine.mobile.unit.Cavalry;
import engine.mobile.unit.Infantry;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.process.MobileInterface;
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
	//private TextureInterface textureManager=new TextureManager();
	// do NOT remove the line above pls
	
	private void drawBoldLine(int boldness,int x1, int y1, int x2, int y2, Graphics g) {
		/*
		 *Draw lines next to each other to create a thick line
		 */
		for (int i = 0; i < boldness; i++) {
		    g.drawLine(x1, y1 + i, x2, y2 + i);
		}
	}

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
					graphics.drawImage(GameUtility.readImage("src/gameData/images/grassTiled.png"),0,7*blockSize,1000,650,null);
				}
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
	
	//used to paint dark tiles
	public void paint(int x, int y, Graphics graphics){
		x*=10;
		y*=10;
		graphics.setColor(new Color(0,0,0,150));
		graphics.fillRect(x, y, 10,10);
	}

	public void paint(CyclicCounter hour, CyclicCounter minute, CyclicCounter second, Graphics graphics) {

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		graphics.drawString("Temps de jeu :"+hour.toString()+":"+minute.toString()+":"+second.toString(), windowWidth/80,windowHeight/20);
	}

	public void paint(Player player, Graphics graphics) {
		/**
		 * paints the stats of the player
		 */
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.setColor(new Color(204,102,0));
		graphics.drawString("Population : "+player.getCurrentPopulation()+"/"+player.getMaxPopulation(), 5*windowWidth/6,windowHeight/20);
		graphics.setColor(new Color(0,204,102));
		graphics.drawString("Ambroisie : "+player.getAmbroisieStock(), windowWidth/2,windowHeight/20);
		graphics.setColor(new Color(0,0,153));
		graphics.drawString("Foi : "+player.getFaithStock(), 2*windowWidth/3,windowHeight/20);
		graphics.setColor(new Color(0,0,0));
		graphics.drawString("Tier "+player.getCurrentTier(), 7*windowWidth/9, windowHeight/20);
	}

	public void paint(String notif, boolean isgood, Graphics graphics) {
		/**
		 * game notification paiting
		 */
		graphics.setFont(new Font("Arial", Font.BOLD, 16));
		if(isgood) {
			graphics.setColor(Color.GREEN.darker());
		}else {
			graphics.setColor(Color.RED.darker());
		}
		graphics.drawString(notif, 300, 34);
	}

	//Need to upgrade
	public void paint(Building building, Graphics graphics) {
		Block position = building.getPosition();
		int blockSize = GameConfiguration.BLOCK_SIZE;
		int buildingSize = blockSize *2;

		int y = position.getLine();
		int x = position.getColumn();
		if(building.getIsUnderConstruction()) {
			if (building instanceof PopulationBuilding || building instanceof DefenseTower) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/hourglassVariation.png"),x * blockSize,y * blockSize,10,10,null);
			}else {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/hourglass.png"),x * blockSize,y * blockSize,20,20,null);
			}
		}
		else if(building instanceof HQ) {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/HQ"+building.getFaction()+".png"),x * blockSize,y * blockSize,20,20,null);
		}
		else if (building instanceof UnitProducer) {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/Camp"+building.getTierLevel()+building.getFaction()+".png"),x * blockSize,y * blockSize,20,20,null);
		}
		else if (building instanceof DefenseTower) {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/Tower"+building.getTierLevel()+building.getFaction()+".png"),x * blockSize,y * blockSize,10,10,null);  
		}
		else if (building instanceof PopulationBuilding) {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/Pop"+building.getFaction()+".png"),x * blockSize,y * blockSize,10,10,null);
		}
		else if (building instanceof ResearchBuilding) {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/Research"+building.getFaction()+".png"),x * blockSize,y * blockSize,20,20,null);
		}

		//graphics.fillRect(x * blockSize, y * blockSize, buildingSize, buildingSize);

		graphics.setColor(Color.BLACK);
		//graphics.drawRect(x * blockSize, y * blockSize, buildingSize, buildingSize);
		if (building instanceof UnitProducer && !building.getIsUnderConstruction()) {
			int queueSize = ((UnitProducer) building).getProductionQueue().size();
			graphics.setColor(Color.MAGENTA);
			int dotSize = buildingSize / 5; 
			int gap = 2;

			for (int i = 0; i < queueSize; i++) { // Loop to draw the queue as a visual cue for the user
				int drawX = (x * blockSize) + (i * (dotSize + gap)); 

				int drawY = (y * blockSize) + (buildingSize - dotSize - 2);

				graphics.setColor(Color.MAGENTA);
				graphics.fillRect(drawX, drawY, dotSize, dotSize);

				graphics.setColor(Color.BLACK);
				graphics.drawRect(drawX, drawY, dotSize, dotSize);
				graphics.setColor(Color.MAGENTA);
			}
		}

		if (building instanceof HQ && !building.getIsUnderConstruction()) {
			int queueSize = ((HQ) building).getWorkerProducer().getProductionQueue().size();
			graphics.setColor(Color.MAGENTA);
			int dotSize = buildingSize / 5; 
			int gap = 2;

			for (int i = 0; i < queueSize; i++) { // Loop to draw the queue as a visual cue for the user
				int drawX = (x * blockSize) + (i * (dotSize + gap)); 

				int drawY = (y * blockSize) + (buildingSize - dotSize - 2);

				graphics.setColor(Color.MAGENTA);
				graphics.fillRect(drawX, drawY, dotSize, dotSize);

				graphics.setColor(Color.BLACK);
				graphics.drawRect(drawX, drawY, dotSize, dotSize);
				graphics.setColor(Color.MAGENTA);
			}
		}

	}

	public void paint(RessourceDeposit deposit, Graphics graphics) {
		Block position = deposit.getPosition();
		int blockSize = GameConfiguration.BLOCK_SIZE;
		int buildingSize = blockSize *2;
		int y = position.getLine();
		int x = position.getColumn();
		if(deposit.getType().equals("FAITH")) {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/faith.png"),x * blockSize,y * blockSize,20,20,null);
		}else {
			graphics.drawImage(GameUtility.readImage("src/gameData/images/ambroisie.png"),x * blockSize,y * blockSize,20,20,null);
		}
	}

	
	
	public void paint(Unit unit, Graphics graphics) {
		Block position = unit.getPosition();
	    int blockSize = GameConfiguration.BLOCK_SIZE;

	    int y = position.getLine();
	    int x = position.getColumn();
	    int px = x * blockSize;
	    int py = y * blockSize;

	    //we draw with the good color
	    Color baseColor = Color.GREEN; //worker or if the color isn't defined
	    if(!(unit instanceof Worker)) {
		    int tierUnit=unit.getTierLevel();
		    if (unit.getUnitFaction().equalsIgnoreCase("Zeus")) {
		        if(tierUnit==1) {
		        	baseColor = new Color(255, 255, 148);
		        }else if(tierUnit==2) {
		        	baseColor = Color.YELLOW;
		        }else if(tierUnit==3) {
		        	baseColor = Color.YELLOW.darker();
		        }
		    } else if (unit.getUnitFaction().equalsIgnoreCase("Hades")) {
		    	if(tierUnit==1) {
		        	baseColor = new Color(255, 148, 148);
		        }else if(tierUnit==2) {
		        	baseColor = Color.RED;
		        }else if(tierUnit==3) {
		        	baseColor = Color.RED.darker();
		        }
		    } else if (unit.getUnitFaction().equalsIgnoreCase("Poseidon")) {
		    	if(tierUnit==1) {
		        	baseColor = new Color(148, 148, 255);
		        }else if(tierUnit==2) {
		        	baseColor = Color.BLUE;
		        }else if(tierUnit==3) {
		        	baseColor = Color.BLUE.darker();
		        }
		    }
	    graphics.setColor(baseColor);
	    graphics.fillOval(px, py, blockSize, blockSize);

	    //we do the variation
	    int centerX = px + blockSize / 2;
	    int centerY = py + blockSize / 2;

	    if (unit instanceof Infantry) {
	        graphics.setColor(Color.BLACK);
	        graphics.drawLine(centerX - 5, centerY, centerX + 5, centerY);
	        graphics.drawLine(centerX, centerY - 5, centerX, centerY + 5);
	    }

	    else if (unit instanceof Artillery) {
	        graphics.setColor(Color.BLACK);
	        graphics.drawOval(px + blockSize/4, py + blockSize/4, blockSize/2, blockSize/2);
	    }

	    else if (unit instanceof Cavalry) {
	        Cavalry cav = (Cavalry) unit;

	        if (cav.getChargeDistanceValue() > 0) {
	            graphics.setColor(Color.CYAN);
	            graphics.drawOval(px - 2, py - 2, blockSize + 4, blockSize + 4);
	        }
	        graphics.setColor(Color.BLACK);
	        graphics.drawLine(centerX - 3, centerY + 3, centerX, centerY - 3);
	        graphics.drawLine(centerX, centerY - 3, centerX + 3, centerY + 3);
	    }
	    
	    graphics.setColor(Color.BLACK);
		graphics.drawOval(px, py, blockSize, blockSize);
	    }
	    //wprker part
	    else {
	    	graphics.setColor(baseColor);
		    graphics.fillOval(px, py, blockSize, blockSize);
		    Color contour = Color.BLACK;
		    if (unit.getUnitFaction().equalsIgnoreCase("Zeus")) {
		    	contour = Color.YELLOW;
		    } else if (unit.getUnitFaction().equalsIgnoreCase("Hades")) {
		    	contour = Color.RED;
		    } else if (unit.getUnitFaction().equalsIgnoreCase("Poseidon")) {
		    	contour = Color.BLUE;
		    }
		    
		    graphics.setColor(contour);
			graphics.drawOval(px, py, blockSize, blockSize);
			graphics.drawOval(px, py, blockSize, blockSize);
	    }
	}
	
	
	public void paintSelectedUnit(Unit selectedUnit, Graphics graphics) {
		Block position = selectedUnit.getPosition();
		int blockSize = GameConfiguration.BLOCK_SIZE;
		int y = position.getLine();
		int x = position.getColumn();
		graphics.setColor(Color.ORANGE.darker());
		graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
		
	}
	public void paintWorkingWorker(Worker worker, Graphics graphics) {
		if(worker.getIsWorking()==true) {
			Block position = worker.getPosition();
			int blockSize = GameConfiguration.BLOCK_SIZE;
			int y = position.getLine();
			int x = position.getColumn();
			graphics.setColor(Color.BLACK);
			graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
		}
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

	
	
	//cant be cut in 2 method because of the call order conflict
	//only the info+button of the first selected building is displayed
	public void paintSelectedInfo(MobileInterface manager, Graphics graphics) {
		int x =windowWidth-windowWidth/5;
		int y =510;//~13*windowHeight/18 but meh
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));
		
		//if a worker is selected then if a build is selected
		//(same things for each element, with button graphical changes)
		if (manager.getSelectedWorker()!=null) {
			Worker worker =manager.getSelectedWorker();
			graphics.drawString(worker.getUnitName()+" :", x, y);
			graphics.setFont(new Font("Arial", Font.PLAIN, 14));
			y+=18;
			graphics.drawString("hp :", x, y);
			y+=8;
			int percent = worker.getPercentHP();
			
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
			
			int currentTier = manager.getSelectedTier();
			//button to be defined, go see just under
			/*
			 * button1
			 * button2
			 * button3
			 * button4
			 * button5
			 * button6
			 */
			if(currentTier==0) {
				//tier 1,2,3 button
				graphics.drawImage(GameUtility.readImage("src/gameData/images/tier1.png"),1020,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/tier2.png"),1100,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/tier3.png"),1180,560,60,60,null);
			}else if(currentTier==1) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/HQ.png"),1020,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Camp.png"),1100,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Pop.png"),1180,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/cancel.png"),1180,640,60,60,null);
			}else if(currentTier==2) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Lab.png"),1020,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Camp.png"),1100,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Tower.png"),1180,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/cancel.png"),1180,640,60,60,null);
			}else if(currentTier==3) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Camp.png"),1020,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/cancel.png"),1180,640,60,60,null);
			}
			
		}else if (manager.getSelectedBuild()!=null){
			Building build =manager.getSelectedBuild();
			graphics.drawString(build.getBuildingName()+" :", x, y);
			graphics.setFont(new Font("Arial", Font.PLAIN, 14));
			y+=18;
			graphics.drawString("hp :", x, y);
			y+=8;
			int percent = build.getPercentHP();
	
			if(percent>=98) {
				graphics.setColor(new Color(0,102,0));
				graphics.fillRect(x, y, 220, 6);
			}else {
				graphics.setColor(new Color(0,102,0));
				graphics.fillRect(x, y, (int)((percent*220.0/100)), 6);
				graphics.setColor(Color.RED);
				graphics.fillRect((int)((percent*220.0/100))+x, y, (int)(220-(percent*220.0/100)), 6);
			}
	
	
			/*
			if(build.getBuildingName().equals("Temple de Zeus")) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/miner.png"),1020,560,60,60,null);
			}
			if(build.getBuildingName().equals("École des Pythagoricien")) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/dmgUp.png"),1020,560,60,60,null);
				graphics.drawImage(GameUtility.readImage("src/gameData/images/prodUnitUp.png"),1100,560,60,60,null);
			}
			if(build.getBuildingName().equals("Camp Olympique")) {
				graphics.drawImage(GameUtility.readImage("src/gameData/images/Artillery.png"),1020,560,60,60,null);
			}
			*/
			
			String imageRepertory = "src/gameData/images/";
			String image1=imageRepertory+"emptyButton.png";
			String image2=imageRepertory+"emptyButton.png";
			String image3=imageRepertory+"emptyButton.png";
			
			
			String name =build.getBuildingName();
			
			if(name.equals("Temple de Zeus")||name.equals("Gouffre du Tartare")||name.equals("Forum aquatique")) {
				image1=imageRepertory+"miner.png";
			}
			if(name.equals("Bibliothèque d'Alexandrie")||name.equals("École des Pythagoricien")||name.equals("Centre d'étude Atlan")) {
				image1=imageRepertory+"dmgUp.png";
				image2=imageRepertory+"prodUnitUp.png";
			}
			else {
				switch(name) {
				case("Camp spartiate"):
					image1=imageRepertory+"Infantry.png";
					break;
				case("Colisée d'Atlantide"):
					image1=imageRepertory+"Infantry.png";
					break;
				case("Camp Olympique"):
					image1=imageRepertory+"Artillery.png";
					break;
				case("Puit d'invocation"):
					image1=imageRepertory+"Artillery.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Cascade"):
					image1=imageRepertory+"Artillery.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("prytanée"):
					image1=imageRepertory+"Infantry.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Portail vers les champs Élysées"):
					image1=imageRepertory+"Artillery.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Fosse sous marine"):
					image1=imageRepertory+"Infantry.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				case("Autel de la sagesse"):
					image1=imageRepertory+"Infantry.png";
					image2=imageRepertory+"Cavalry.png";
					break;
				}
			}
			
			
			
			graphics.drawImage(GameUtility.readImage(image1),1020,560,60,60,null);//first button
			if(image2.equals(imageRepertory+"emptyButton.png")==false) {
				graphics.drawImage(GameUtility.readImage(image2),1100,560,60,60,null);//second button
			}
			if(image3.equals(imageRepertory+"emptyButton.png")==false) {
				graphics.drawImage(GameUtility.readImage(image3),1180,560,60,60,null);//third button
			}
			
			/**
			 * Camp spartiate: hoplites, infanterie
			 * Colisée d'Atlantide: poseidon 1, Rétiaire, infantry
			 * Camp Olympique: lanceurs de disque, artillery
			 * Puit d'invocation: hades 2, Archers du Styx, Gêolière du tartare, cavalry
			 * Cascade: poseidon 2, Élémentaire d'eau, artillery,Harpie, cavalerie
			 * prytanée: aigle du caucase, cavalery, Cyclope, infantry
			 * Portail vers les champs Élysées: hades 3, Chevalier sans tête, Méduses, artillery
			 * Fosse sous marine: poseidon 3,Kraken fantôme infanterie, Hippocampe de guerre cavalry
			 * Autel de la sagesse:zeus 3,Centaures, cavalery, Hydre infanterie
			 * 
			 * 
			 */
			
			//template for visual use only
			//graphics.drawRect(1020, 560, 240, 140);
			//Draw img max 6 from the entry point
			//img are 60x60 and 20 pixels between each
			//-> one image per building per button (capacity/research/unit)
			
		}
	}

	public void paintGrid(Map map, Graphics graphics) {
		//dont work and cant figure it out....
		/*
		int blockSize = GameConfiguration.BLOCK_SIZE;
		for (int columnIndex = 0; columnIndex < map.getColumnCount()-4; columnIndex+=blockSize) {
			graphics.setColor(Color.BLACK);
			graphics.drawLine(columnIndex, 70, columnIndex,1280);
		}
		*/
	}
	
	public void paintAttack(Unit unit, Graphics graphics) {
		MobileElement enemy = unit.getTarget();
		int blockSize = GameConfiguration.BLOCK_SIZE;
		graphics.setColor(Color.YELLOW);
		graphics.drawLine(unit.getPosition().getColumn()*blockSize+5,unit.getPosition().getLine()*blockSize+5,enemy.getPosition().getColumn()*blockSize+5,enemy.getPosition().getLine()*blockSize+5);
	}
	
	public void paintAttack(DefenseTower tower, Graphics graphics) {
		MobileElement target = tower.getTarget();
		if(tower.getIsAttacking()==1&&tower.getTarget()!=null){
			int dx = Math.abs(tower.getPosition().getColumn() - target.getPosition().getColumn());
	        int dy = Math.abs(tower.getPosition().getLine() - target.getPosition().getLine());
	        int dist = Math.max(dx, dy);
			if(dist<tower.getTowerRange()) {
	        	int blockSize = GameConfiguration.BLOCK_SIZE;
				graphics.setColor(Color.RED);
				drawBoldLine(8,tower.getPosition().getColumn()*blockSize+5,tower.getPosition().getLine()*blockSize+5,target.getPosition().getColumn()*blockSize+5,target.getPosition().getLine()*blockSize+5,graphics);
			}
		}
	}
	
	public void paintMouse(Block position, Graphics graphics){
		/**
		 * draw an image to the coordonate of the mouse
		 */
		
		//not used for now
		
		int x1=position.getLine()*GameConfiguration.BLOCK_SIZE;
		int y1=position.getColumn()*GameConfiguration.BLOCK_SIZE;
		int x2=(position.getLine()+1)*GameConfiguration.BLOCK_SIZE;
		int y2=(position.getColumn()+1)*GameConfiguration.BLOCK_SIZE;
		
		graphics.drawImage(GameUtility.readImage("src/gameData/images/Camp.png"),x1,y1,x2,y2,null);
	}
	
	
}