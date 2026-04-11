package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import config.GameConfiguration;
import engine.process.GameUtility;
import engine.process.MenuInterface;

public class MenuStrategy {
	private final int windowWidth = GameConfiguration.WINDOW_WIDTH;
	private final int windowHeight = GameConfiguration.WINDOW_HEIGHT; 

	public void paintPauseMenu(Graphics graphics) {
		graphics.setColor(new Color(0,0,0,150));
		graphics.fillRect(0, 0, windowWidth, windowHeight);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(windowWidth/2-175,windowHeight/2-50, 350, 100);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("PAUSE", windowWidth/2-35, windowHeight/2-20);
		graphics.drawString("Appuyez sur Esc pour revenir au jeu", windowWidth/2-155, windowHeight/2+20);
	}
	
	public void paintMainMenu(Graphics graphics) {
		graphics.drawImage(GameUtility.readImage("src/gameData/images/FateOfOlympusTitle.png"),0,0,1280,720,null);
	}
	
	public void paintChooseMenu(MenuInterface menu,Graphics graphics) {
		graphics.drawImage(GameUtility.readImage("src/gameData/images/SelectMenu.png"),0,0,1280,720,null);
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
	
	public void paintEndMenu(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 40));
		graphics.drawString("Fin de partie", windowWidth/2-35, windowHeight/2-200);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("Inserer diagramme de fin ici", windowWidth/2-35, windowHeight/2+20);
	}
}
