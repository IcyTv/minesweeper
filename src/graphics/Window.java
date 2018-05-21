package graphics;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame{
	
	private int width, height;
	
	private Game game;
	private Menu menu;
	private Keypad keypad;
	private NameEnterField nm;
	
	public Window(int sx, int sy){
		width = sx;
		height = sy;
		
		setSize(width, height);
		//setResizable(false);
		setBackground(Color.BLACK);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game = new Game(this, 1, 1, 1);
		//menu = new Menu(width, height, this);
		//keypad = new Keypad(this);
		//nm = new NameEnterField(null ,0, this);
	}
	
	public void toKeypad(int[] arg0){
		keypad = new Keypad(this, arg0);
		((Component)keypad).setFocusable(true);
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().add(keypad);
		getContentPane().revalidate();
		keypad.grabFocus();
		repaint();
	}
	
	public void toMenu(){
		menu = new Menu(width, height, this);
		((Component)menu).setFocusable(true);
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().add(menu);
		getContentPane().revalidate();
		menu.grabFocus();
		repaint();
	}
	
	public void toName(int[] size, int code){
		nm = new NameEnterField(size , code, this);
		((Component)nm).setFocusable(true);
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().add(nm);
		getContentPane().revalidate();
		nm.grabFocus();
		repaint();
	}
	
	
	public void toGame(int[] args, int code, String nm){
		toGame(args[0], args[1], args[2], code, nm);
	}
	public void toGame(int sx, int sy, int bombs, int code, String nm){
		game = new Game(this, sx, sy, bombs, true, code, nm);
		((Component)game).setFocusable(true);
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().add(game);
		getContentPane().revalidate();
		game.grabFocus();
		repaint();
	}
	
	public void toGame(int[] arg0){
		game = new Game(this, arg0[0], arg0[1], arg0[2]);
		((Component)game).setFocusable(true);
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().add(game);
		getContentPane().revalidate();
		game.grabFocus();
		repaint();
	}
	
	
	public void cBounds(int x, int y, int sx, int sy) {
		setBounds(x, y, sx + getInsets().left + getInsets().right, sy + getInsets().top + getInsets().bottom);
	}
}
