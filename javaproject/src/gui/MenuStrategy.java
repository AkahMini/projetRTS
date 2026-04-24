package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
		graphics.setColor(new Color(0, 0, 0, 150));
		graphics.fillRect(0, 0, windowWidth, windowHeight);

		int panelWidth = 400;
		int panelHeight = 250;
		int panelX = (windowWidth - panelWidth) / 2;
		int panelY = (windowHeight - panelHeight) / 2;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(panelX, panelY, panelWidth, panelHeight);

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		int y = panelY + 40;
		int spacing = 40;
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		drawCenteredString(graphics, "PAUSE", windowWidth, y);

		y += spacing;
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));
		drawCenteredString(graphics, "Appuyez sur Esc pour revenir au jeu", windowWidth, y);
		y += spacing;
		drawCenteredString(graphics, "Appuyez sur A pour abandonner la partie", windowWidth, y);
		y += spacing;
		drawCenteredString(graphics, "Appuyez sur Q pour quitter le jeu", windowWidth, y);
		y += spacing;
		drawCenteredString(graphics, "Appuyez sur G pour changer le thème", windowWidth, y);
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
	
	public void paintEndMenu(Graphics graphics, MobileInterface manager, MenuInterface menu, JFreeChart chart,ChartManager chartManager) {
		graphics.setColor(new Color(230, 230, 230));
	    graphics.fillRect(0, 0, windowWidth, windowHeight);

	    Color factionColor = Color.GRAY;
	    String faction = manager.getPlayer().getFactionName();
	    if (faction.equalsIgnoreCase("Zeus")) {
	        factionColor = new Color(230, 200, 40); // jaune
	    } else if (faction.equalsIgnoreCase("Hades")) {
	        factionColor = new Color(200, 60, 60); // rouge
	    } else if (faction.equalsIgnoreCase("Poseidon")) {
	        factionColor = new Color(60, 140, 220); // bleu
	    }
	    int panelWidth = 700;
	    int panelHeight = 550;
	    int panelX = (windowWidth - panelWidth) / 2;
	    int panelY = (windowHeight - panelHeight) / 2;

	    graphics.setColor(Color.WHITE);
	    graphics.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
	    for (int i = 0; i < 4; i++) {
	        graphics.setColor(new Color(factionColor.getRed(),factionColor.getGreen(),factionColor.getBlue(),100 - i * 20));
	        graphics.drawRoundRect(panelX - i,panelY - i,panelWidth + (i * 2),panelHeight + (i * 2),20,20);
	    }
	    graphics.setColor(new Color(200, 200, 200));
	    graphics.drawRoundRect(panelX + 2, panelY + 2, panelWidth - 4, panelHeight - 4, 18, 18);

	    int y = panelY + 60;
	    graphics.setFont(new Font("Arial", Font.BOLD, 40));
	    graphics.setColor(new Color(50, 50, 50));
	    drawCenteredString(graphics, "Fin de partie", windowWidth, y);

	    y += 50;
	    String winner = menu.getWinnerFac();
	    if (winner == null) {
	        winner = "Défaite par abandon";
	        graphics.setColor(new Color(200, 60, 60));
	    } else {
	        graphics.setColor(new Color(60, 160, 90));
	    }
	    graphics.setFont(new Font("Arial", Font.BOLD, 24));
	    drawCenteredString(graphics, winner, windowWidth, y);

	    y += 70;
	    graphics.setFont(new Font("Arial", Font.PLAIN, 20));
	    graphics.setColor(new Color(70, 70, 70));
	    int leftX = panelX + 80;
	    int rightX = panelX + panelWidth - 80;
	    int spacing = 35;
	    drawStat(graphics, "Unités créées :", manager.getPlayer().getNumberOfCreatedUnit(), leftX, rightX, y); y += spacing;
	    drawStat(graphics, "Bâtiments construits :", manager.getPlayer().getNumberOfBuiltBuilding(), leftX, rightX, y); y += spacing;
	    drawStat(graphics, "Ambroisie collectée :", manager.getPlayer().getTotalAmbroisieGathered(), leftX, rightX, y); y += spacing;
	    drawStat(graphics, "Foi collectée :", manager.getPlayer().getTotalFaithGathered(), leftX, rightX, y); y += spacing;
	    drawStat(graphics, "Unités tuées :", manager.getPlayer().getTotalKilledUnit(), leftX, rightX, y);

	    y += 50;
	    if (chart != null) {
	        chartManager.refreshDataset();
	        int chartWidth = 350;
	        int chartHeight = 180;
	        int chartX = (windowWidth - chartWidth) / 2;
	        int chartY = y;
	        BufferedImage chartImage = chart.createBufferedImage(chartWidth, chartHeight);
	        graphics.drawImage(chartImage, chartX, chartY, null);
	        graphics.setColor(factionColor);
	        graphics.drawRect(chartX, chartY, chartWidth, chartHeight);
	        y += chartHeight;
	    }

	    y += 30;
	    graphics.setFont(new Font("Arial", Font.ITALIC, 16));
	    graphics.setColor(new Color(120, 120, 120));
	    drawCenteredString(graphics, "Appuyez sur Entrée pour retourner au menu", windowWidth, y);
	}
	
	private void drawCenteredString(Graphics g, String text, int width, int y) {
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
	    int x = (width - metrics.stringWidth(text)) / 2;
	    g.drawString(text, x, y);
	}
	
	private void drawStat(Graphics g, String label, int value, int leftX, int rightX, int y) {
	    g.drawString(label, leftX, y);

	    String val = String.valueOf(value);
	    FontMetrics fm = g.getFontMetrics();
	    int valWidth = fm.stringWidth(val);

	    g.drawString(val, rightX - valWidth, y);
	}
}
