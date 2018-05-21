package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;

public class SpriteMap {
	
	private BufferedImage[] sprites;
	private int width;
	
	public SpriteMap(String name) throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		//System.out.println(classLoader.getResource("resources/" + name));
		BufferedImage tmp = ImageIO.read(classLoader.getResource("resources/" + name));
		width = tmp.getWidth();
		int rows = tmp.getHeight() / width;
		sprites = new BufferedImage[rows];
	    for (int i = 0; i < rows; i++)
	    {
	        sprites[i] = tmp.getSubimage(
	            0,
	            i * width,
	            width,
	            width
	        );
	    }
	}
	
	public SpriteMap(String name, int height) throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		BufferedImage tmp = ImageIO.read(classLoader.getResource("resources/" + name));
		width = tmp.getWidth();
		int rows = tmp.getHeight() / height;
		sprites = new BufferedImage[rows];
	    for (int i = 0; i < rows; i++)
	    {
	        sprites[i] = tmp.getSubimage(
	            0,
	            i * height,
	            width,
	            height
	        );
	    }
	}
	
	public BufferedImage get(int n){
		return sprites[n - 1];
	}
	
	public int getSpriteWidth(){
		return width;
	}
	public int getSpriteAmount(){
		return sprites.length;
	}
}
