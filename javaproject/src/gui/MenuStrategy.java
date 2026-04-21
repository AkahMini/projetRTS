package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.jfree.chart.JFreeChart;

import config.GameConfiguration;
import engine.process.GameUtility;
import engine.process.MenuInterface;
import engine.process.MobileInterface;
import gui.instrument.ChartManager;

public class MenuStrategy {
	private final int windowWidth = GameConfiguration.WINDOW_WIDTH;
	private final int windowHeight = GameConfiguration.WINDOW_HEIGHT; 

	public void paintPauseMenu(Graphics graphics, JFreeChart chart,ChartManager chartManager) {
		graphics.setColor(new Color(0,0,0,150));
		graphics.fillRect(0, 0, windowWidth, windowHeight);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(windowWidth/2-200,windowHeight/2-100, 400, 250);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("PAUSE", windowWidth/2-35, windowHeight/2-60);
		graphics.drawString("Appuyez sur Esc pour revenir au jeu", windowWidth/2-155, windowHeight/2-20);
		graphics.drawString("Appuyez sur a pour abandonner la partie", windowWidth/2-165, windowHeight/2+20);
		graphics.drawString("Appuyez sur q pour quitter le jeu", windowWidth/2-145, windowHeight/2+60);
		graphics.drawString("Appuyez sur g pour changer le thème", windowWidth/2-155, windowHeight/2+100);
		
		/*
		 * Why doing this ???? -_-
		 * 
		//to paint the endscreen directly on the pause menu, test purpose
		if (chart != null) {
			chartManager.refreshDataset(); //we refresh the data here in the graphic thread because otherwise there are conflicts
		    int chartWidth = 261;
		    int chartHeight = 196;
		    int chartX=500;
		    int chartY = 285;
		    BufferedImage chartImage = chart.createBufferedImage(chartWidth, chartHeight);
		    graphics.drawImage(chartImage, chartX, chartY, null);
		}
		*/
		
	}
	
	public void paintMainMenu(Graphics graphics) {
		graphics.drawImage(GameUtility.readImage("/gameData/images/FateOfOlympusTitle.png"),0,0,1280,720,null);
	}
	
	public void paintChooseMenu(MenuInterface menu,Graphics graphics) {
		graphics.drawImage(GameUtility.readImage("/gameData/images/SelectMenu.png"),0,0,1280,720,null);
		String faction =menu.getSelectedFaction();
		graphics.setColor(new Color(0,0,0,150));
		if(faction.equals("Zeus")) {
			graphics.fillRect(426, 0,856 ,538 );
		}else if(faction.equals("Hades")) {
			graphics.fillRect(0, 0,426,538 );
			graphics.fillRect(856, 0,426,538 );
		}else if(faction.equals("Poseidon")) {
			graphics.fillRect(0, 0,856,538 );
		}
		int mode =menu.getSelectedMode();
		graphics.setColor(Color.ORANGE);
		if(mode==0) {
			graphics.drawRect(200, 580, 400, 104);
			graphics.drawRect(201, 581, 398, 102);
			graphics.drawRect(202, 582, 396, 100);
			graphics.drawRect(203, 583, 394, 98);
		}else if(mode==1) {
			graphics.drawRect(640, 580, 400, 104);
			graphics.drawRect(641, 581, 398, 102);
			graphics.drawRect(642, 582, 396, 100);
			graphics.drawRect(643, 583, 394, 98);
		}
	}
	
	public void paintEndMenu(Graphics graphics, MobileInterface manager, JFreeChart chart,ChartManager chartManager) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 40));
		graphics.drawString("Fin de partie", windowWidth/2-100, windowHeight/2-200);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		String gameWiner =manager.winningFaction();
		if(gameWiner!=null) {
			gameWiner="Défaite par abandon !";
		}
		graphics.drawString(gameWiner, windowWidth/2, windowHeight/10);
		graphics.drawString("Appuyez sur entrée pour retourner au menu principal", windowWidth/2-250, windowHeight/8);
		graphics.drawString("Nombre d'unités crées: "+String.valueOf(manager.getPlayer().getNumberOfCreatedUnit()), windowWidth/2-150,windowHeight/2+30);
		graphics.drawString("Nombre de bâtiments crées: "+String.valueOf(manager.getPlayer().getNumberOfBuiltBuilding()), windowWidth/2-150, windowHeight/2+60);
		graphics.drawString("Ambroisie collectée: "+String.valueOf(manager.getPlayer().getTotalAmbroisieGathered()), windowWidth/2-150, windowHeight/2+90);
		graphics.drawString("Foi collectée: "+String.valueOf(manager.getPlayer().getTotalFaithGathered()), windowWidth/2-150, windowHeight/2+120);
		graphics.drawString("Unités tuées: "+String.valueOf(manager.getPlayer().getTotalKilledUnit()), windowWidth/2-150, windowHeight/2+150);
		/**
		 * private int numberOfCreatedUnit;//check
			private int numberOfBuildBuilding;//check
			private int totalAmbroisieGathered;//check
			private int totalFaithGathered;//check
			private int totalKilledUnit;//check??
		 */
		
		if (chart != null) {
			chartManager.refreshDataset(); //we refresh the data here in the graphic thread because otherwise there are conflicts
		    int chartWidth = 261;
		    int chartHeight = 196;
		    int chartX=(windowWidth-chartWidth)/2;
		    int chartY = (windowHeight-chartHeight)/3;
		    BufferedImage chartImage = chart.createBufferedImage(chartWidth, chartHeight);
		    graphics.drawImage(chartImage, chartX, chartY, null);
		}
		
	}
}
