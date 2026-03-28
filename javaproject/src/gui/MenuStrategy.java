package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import config.GameConfiguration;
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
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 40));
		graphics.drawString("FATE OF OLYMPUS", windowWidth/2-35, windowHeight/2-200);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("Appuyez sur entrée pour lancer une partie", windowWidth/2-35, windowHeight/2-20);
		graphics.drawString("Appuyez sur Esc pour quitter le jeu", windowWidth/2-35, windowHeight/2+20);
	}
	
	public void paintChooseMenu(MenuInterface menu,Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 30));
		graphics.drawString(menu.getSelectedFaction()+" sélectionnée", windowWidth/2-35, windowHeight/2-200);
		int mode =menu.getSelectedMode();
		if(mode==0) {
			graphics.drawString("Mode 1V1", windowWidth/2-35, windowHeight/2-150);
		}else if(mode==1) {
			graphics.drawString("Mode 1V1V1", windowWidth/2-35, windowHeight/2-150);
		}
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("Appuyez sur s pour jouer en 1V1", windowWidth/2-35, windowHeight/2-100);
		graphics.drawString("Appuyez sur f pour jouer en 1v1v1", windowWidth/2-35, windowHeight/2-60);
		graphics.drawString("Appuyez sur 1 pour ZEUS", windowWidth/2-35, windowHeight/2-20);
		graphics.drawString("Appuyez sur 2 pour Hades", windowWidth/2-35, windowHeight/2+20);
		graphics.drawString("Appuyez sur 3 pour Poseidon", windowWidth/2-35, windowHeight/2+60);
		graphics.drawString("Appuyez sur entrée pour commencer", windowWidth/2-35, windowHeight/2+100);
		graphics.drawString("Appuyez sur Esc pour revenir a l'acceuil", windowWidth/2-35, windowHeight/2+150);
	}
	
	public void paintEndMenu(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 40));
		graphics.drawString("Fin de partie", windowWidth/2-35, windowHeight/2-200);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString("Inserer diagramme de fin ici", windowWidth/2-35, windowHeight/2+20);
	}
}
