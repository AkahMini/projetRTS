package gui;

import java.awt.Color;

/**
 * Class use to manage color and texture in a more efficient way than draw image do
 * only works for the current blocksize, 10x10 here
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
		case "GrassTexture":
			Color[] palette= {
					new Color(1,115,13),
					new Color(24,158,37),
					new Color(0,94,27),
			};
			return palette;
		default:
			Color[] placeHolder = {
					new Color(255,0,255),
					new Color(255,255,255)
			};
			return placeHolder;
		}
	}
	
	//10x10 grid is the norm
	public int[][] getPattern() {
		switch (currentTexture) {
		case "GrassTexture":
			int[][] grid= {
					{0,0,0,0,0,0,2,0,1,0},
					{0,2,0,1,0,1,0,2,1,0},
					{0,2,0,1,2,0,1,2,0,0},
					{0,1,0,2,0,0,1,0,0,1},
					{2,0,1,2,1,0,0,0,1,0},
					{0,2,1,0,0,1,0,2,1,0},
					{0,2,0,0,2,1,2,0,0,0},
					{0,1,0,1,2,0,2,1,2,0},
					{1,0,2,1,0,0,0,1,2,0},
					{1,0,2,0,0,0,0,0,2,0}
			};
			return grid;
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
