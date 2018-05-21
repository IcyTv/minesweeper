package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Face {

	private int x, y, size;
	private SpriteMap sprites;
	private int currentSprite;
	private boolean locked;
	
	public Face(int x, int y, int size) throws IOException{
		sprites = new SpriteMap("faces.png");
		this.x = x - size/2;
		this.y = y - size/2;
		this.size = size;
		currentSprite = 5;
		locked = false;
	}

	public int current(){
		return currentSprite;
	}
	
	public void changeFace(int face){
		if(!locked){
			currentSprite = face;
		}
	}
	
	public BufferedImage getSprite(){
		return sprites.get(currentSprite);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}
	
	public void locked(boolean lock){
		locked = lock;
	}
	
}
