package test;

import gui.MainGUI;

/**
 * 
 * @author LE RAY Yann
 *
 */
public class TestGame {
	public static void main(String[] args) {

		MainGUI gameMainGUI = new MainGUI("RTS game");

		Thread gameThread = new Thread(gameMainGUI);
		gameThread.start();
	}
}
