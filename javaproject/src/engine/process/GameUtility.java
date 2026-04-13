package engine.process;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Utility class used for the game (only used for images for now)
 * 
 * 
 * 
 */

//Some very general function can be added here
//Used for I/O uses likes read Images
//Otehr I/O method can be put here
public class GameUtility {

	/**
	 * Reads a image from an image file.
	 * 
	 * @param filePath the path (from "src") of the image file
	 * @return the read file
	 */
	public static Image readImage(String path) {
	    try {
	        URL url = GameUtility.class.getResource(path);
	        if (url == null) {
	            System.err.println("Image introuvable : " + path);
	            return null;
	        }
	        return ImageIO.read(url);
	    } catch (IOException e) {
	        System.err.println("Impossible de lire l'image !");
	        return null;
	    }
	}
}
