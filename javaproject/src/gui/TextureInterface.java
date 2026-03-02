package gui;

import java.awt.Color;

public interface TextureInterface {
	Color[] getPalette();
	int[][] getPattern();
	
	String getCurrent();
	void setCurrent(String current);
}
