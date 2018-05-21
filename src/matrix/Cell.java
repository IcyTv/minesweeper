package matrix;

public class Cell {

	private int x, y, bombs;
	private boolean bomb, markedBomb, markedQues, clicked;
	private boolean oBomb, wrong;
	
	public Cell(int x, int y, boolean bomb){
		this.x = x;
		this.y = y;
		this.bomb = bomb;
		markedBomb = markedQues = clicked = false;
		oBomb = false;
	}
	

	public boolean isWrong() {
		return wrong;
	}


	public void setWrong(boolean wrong) {
		this.wrong = wrong;
	}


	public boolean isoBomb() {
		return oBomb;
	}


	public void setoBomb(boolean oBomb) {
		this.oBomb = oBomb;
	}


	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}

	public boolean hasBomb() {
		return bomb;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getBombs(){
		return bombs;
	}
	public void setBombs(int bombs){
		this.bombs = bombs;
	}
	
	
	public boolean isClicked() {
		return clicked;
	}


	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}


	public boolean isMarkedBomb() {
		return markedBomb;
	}


	public void setMarkedBomb(boolean markedBomb) {
		this.markedBomb = markedBomb;
	}


	public boolean isMarkedQues() {
		return markedQues;
	}


	public void setMarkedQues(boolean markedQues) {
		this.markedQues = markedQues;
	}
	
	
	
}
