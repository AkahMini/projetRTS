package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import config.GameConfiguration;

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
}
