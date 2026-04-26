package gui;

import java.awt.Color;

/**
 * Class use to manage color and texture in a more efficient way than draw image do
 * only works for the current blocksize, 10x10 here
 * 
 * Not used for now, can be usefull later
 * @see {GameConfiguration}
 */
public class TextureManager implements TextureInterface {

	private String currentTexture;
	
	public TextureManager() {
		this.currentTexture=null;
	}

	// can be 1 to 100 (min color to max pixel in a 10x10 square)
	public Color[] getPalette() {
		switch (currentTexture) {
		default:
			Color[] placeHolder = {
					new Color(255,0,255),
					new Color(255,255,255)
			};
			return placeHolder;
		}
	}
	
	//10x10 grid
	public int[][] getPattern() {
		switch (currentTexture) {
		default:
			int[][] placeHolder = {
					{0,1,0,1,0,1,0,1,0,1},
					{1,0,1,0,1,0,1,0,1,0},
					{0,1,0,1,0,1,0,1,0,1},
					{1,0,1,0,1,0,1,0,1,0},
					{0,1,0,1,0,1,0,1,0,1},
					{1,0,1,0,1,0,1,0,1,0},
					{0,1,0,1,0,1,0,1,0,1},
					{1,0,1,0,1,0,1,0,1,0},
					{0,1,0,1,0,1,0,1,0,1},
					{1,0,1,0,1,0,1,0,1,0}
			};
			return placeHolder;
		}
	}
	
	public String getCurrent() {
		return currentTexture;
	}

	public void setCurrent(String current) {
		this.currentTexture = current;
	}
	
}
