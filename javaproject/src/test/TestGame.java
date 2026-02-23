package test;

import config.DefaultGameSettings;
import gui.MainGUI;

/**
 * Test class used to initialize the game engine and and graphical component.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class TestGame {
	public static void main(String[] args) {

		MainGUI gameMainGUI = new MainGUI("RTS game");
		DefaultGameSettings gameSettings= new DefaultGameSettings();

		Thread gameThread = new Thread(gameMainGUI);
		gameThread.start();
	}
}
