package graphics;

public class Boundary {

	public int x,y,sx,sy;
	
	public Boundary(int x, int y, int x2, int y2){
		this.x = x;
		this.y = y;
		sx = x2 - x;
		sy = y2 - y;
	}
	
	public Boundary(int x, int y, int[] size){
		this.x = x;
		this.y = y;
		sx = size[0];
		sy = size[1];
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getSizeX(){
		return sx;
	}
	
	public int getSizeY(){
		return sy;
	}
	
	public int getX2(){
		return x + sx;
	}
	
	public int getY2(){
		return y + sy;
	}
	
	
}
