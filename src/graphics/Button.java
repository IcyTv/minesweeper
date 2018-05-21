package graphics;

class Button {
	private int[] pos;
	private int[] dim;
	private String text;
	private String secTxt;
	
	public Button(int x, int y, int w, int h, String text_, String secTxt_) {
		pos = new int[] {x, y};
		dim = new int[] {w, h};
		text = text_;
		secTxt = secTxt_;
	}
	public Button(int x, int y, int w, int h, String text_){
		this(x, y, w, h, text_, "");
	}
	public boolean clicked(int x, int y) {
		return (x >= pos[0] && x <= pos[0] + dim[0]) && (y >= pos[1] && y < pos[1] + dim[1]);
	}
	public int getX() {
		return pos[0];
	}
	public int getY() {
		return pos[1];
	}
	public int getW() {
		return dim[0];
	}
	public int getH() {
		return dim[1];
	}
	public String getTxt() {
		return text;
	}
	public String getSecTxt(){
		return secTxt;
	}
	public void setSecTxt(String newS){
		secTxt = newS;
	}
}