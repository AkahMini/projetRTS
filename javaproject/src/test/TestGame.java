package test;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.mobile.building.BuildingStatsLoader;
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
		BuildingStatsLoader statLoader=new BuildingStatsLoader(GameConfiguration.BUILDINGS_STATS);
		Thread gameThread = new Thread(gameMainGUI);
		gameThread.start();
	}
}
