package test;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.mobile.building.BuildingStatsLoader;
import engine.mobile.unit.UnitsStatsLoader;
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
		
		DefaultGameSettings gameSettings= new DefaultGameSettings();
		BuildingStatsLoader buildingStatLoader=new BuildingStatsLoader(GameConfiguration.BUILDINGS_STATS);
		UnitsStatsLoader unitsStatLoader=new UnitsStatsLoader(GameConfiguration.BUILDINGS_STATS);
		MainGUI gameMainGUI = new MainGUI("RTS game");
		
		Thread gameThread = new Thread(gameMainGUI);
		gameThread.start();
	}
}
