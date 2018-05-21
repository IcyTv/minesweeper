package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;

import javax.swing.*;

import matrix.Cell;
import matrix.Matrix;
import requests.Request;
import requests.Status;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable, MouseListener, KeyListener, WindowListener{

	private static final char[] cheatCode = new char[]{'x', 'y', 'z', 'Z'};
	int currCheat;
	
	private int width, height;
	
	private int bombs;
	private int flags;
	
	private Window ctrl;
	
	private SpriteMap fieldSprites;
	private SpriteMap numberSprites;
	
	private Face face;
	
	private Boundary playingBoundary;
	
	private boolean lost;
	private boolean timer;
	private boolean cheat;
	
	private boolean myTurn;
	private boolean multiplayer;
	private int code;
	private String usernm;
	private String resetReq;
	
	private int lostTime;
	
	private Matrix playingField;
	
	private long startTime;
	

	public Game(Window ctrl, int sx, int sy, int bs){
		this(ctrl,sx,sy,bs,false, 0, "");
	}
	
	public Game(Window ctrl, int sx, int sy, int bs, boolean multiplayer, int code, String nm){
		this.ctrl = ctrl;
		lost = false;
		timer = false;
		lostTime = 0;
		currCheat = 0;
		cheat = false;
		myTurn = false;
		bombs = bs;
		flags = bombs;
		this.multiplayer = multiplayer;
		this.code = code;
		usernm = nm;
		resetReq = "";
		
		startTime = System.currentTimeMillis();

		this.addMouseListener(this);
		ctrl.addWindowListener(this);
		
		try {
			fieldSprites = new SpriteMap("field.png");
			numberSprites = new SpriteMap("nums.png", 276/12);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		playingField = new Matrix(sx, sy);
		playingBoundary = new Boundary(0, 40, new int[]{playingField.rows()*fieldSprites.getSpriteWidth(), playingField.columns()*fieldSprites.getSpriteWidth()});
		
		playingField.reset(bombs);
		//  + 16         + 39
		//ctrl.setBounds(500, 300, playingBoundary.getX2() + 6, playingBoundary.getY2() + 29);
		ctrl.cBounds(500, 300, playingBoundary.getX2(), playingBoundary.getY2());
		ctrl.setResizable(false);
		
		width = 9 * fieldSprites.getSpriteWidth();
		height = 9 * fieldSprites.getSpriteWidth();
		if(multiplayer){
			initMultiplayer();
		} else {
			System.out.println("No multipl");
		}
		
		try {
			face = new Face(playingBoundary.getSizeX() / 2, playingBoundary.getY() / 2, 24);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.addKeyListener(this);
		
		new Thread(this).start();
		
	}
	
	@Override
	public void run(){
		try{
			while(true){
				Thread.sleep(5);
				repaint();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g){
		Status status = new Status();
		if(multiplayer){
			try {
				status = Request.status(code, usernm);
				myTurn = status.isTurn();
				if(!myTurn && !lost) {
					lost = playingField.input(Request.getMap(code, usernm));
					if(lost){
						System.out.println("Lost");
						face.changeFace(3);
						face.locked(true);
					}
				}
//				if(status.isLost()) {
//					System.out.println("Lost");
//					lost = true;
//					face.changeFace(3);
//					face.locked(true);
//				} else {
//					//System.out.println(status);
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Something very bad just happened!");
				ctrl.toMenu();
			}
		}
		
		Graphics2D g2 = (Graphics2D) g;
		g2.clearRect(0, 0, width, height);
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		
		int xOff = playingBoundary.getX();
		int yOff = playingBoundary.getY();
		
		int mCount = 0;
		int dCount = 0;
		
		for(int i = 0; i < playingField.rows(); i++){
			for(int n = 0; n < playingField.columns(); n++){
				int swidth = fieldSprites.getSpriteWidth();
				Cell c = playingField.get(i, n);
				BufferedImage img = fieldSprites.get(1);
				if(c.isClicked()) {
					img = c.hasBomb() ? c.isoBomb() ? fieldSprites.get(4) : fieldSprites.get(6) : fieldSprites.get(16 - c.getBombs());
				} else {
					if(c.hasBomb()){
						dCount++;
					} else {
						dCount--;
					}
				}
				if(c.isMarkedBomb()) {
					img = fieldSprites.get(2);
					if(c.hasBomb()){
						mCount++;
					}
				} else if(c.isMarkedQues()) {
					img = fieldSprites.get(3);
				}
				if(c.isWrong()) {
					img = fieldSprites.get(5);
				}
				if(c.hasBomb() && (cheat == true || currCheat >= cheatCode.length)){
					img = fieldSprites.get(6);
				}
				
				g2.drawImage(img , i * swidth + xOff, n * swidth + yOff, null);
				
				if(mCount == bombs || dCount == bombs){
					if(mCount < bombs){
						for(int b = 0; b < playingField.rows(); b++){
							for(int d = 0; d < playingField.columns(); d++){
								if(playingField.get(b, d).hasBomb()){
									playingField.get(b, d).setMarkedBomb(true);
								}
							}
						}
					}
					face.changeFace(2);
					if(!lost){
						lost = true;
						lostTime = (int) ((System.currentTimeMillis() - startTime)/1000);
					}
				}
			}
		}
		
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(xOff, 0, playingBoundary.getX2(), yOff);
		
		g2.drawImage(face.getSprite(), face.getX(), face.getY(), face.getSize(), face.getSize(), null);
		
		int tmpFlags = flags;
		boolean minus = false;
		
		if(flags < 0){
			tmpFlags = Math.abs(tmpFlags);
			minus = true;
		}
		
		String flagss = String.format("%03d", tmpFlags);
		g2.drawImage(numberSprites.get(minus ? 1 : 12-Integer.parseInt("" + flagss.charAt(0))), playingBoundary.getSizeX() / 5 - numberSprites.getSpriteWidth(), playingBoundary.getY() / 2 - numberSprites.getSpriteWidth(), null);
		g2.drawImage(numberSprites.get(12-Integer.parseInt("" + flagss.charAt(1))), playingBoundary.getSizeX() / 5, playingBoundary.getY() / 2 - numberSprites.getSpriteWidth(), null);
		g2.drawImage(numberSprites.get(12-Integer.parseInt("" + flagss.charAt(2))), playingBoundary.getSizeX() / 5 + numberSprites.getSpriteWidth(), playingBoundary.getY() / 2 - numberSprites.getSpriteWidth(), null);
		
		if(!timer){
			startTime = System.currentTimeMillis();
		}
		
		String currentt = String.format("%03d", lost ? lostTime : (System.currentTimeMillis() - startTime) / 1000);
		g2.drawImage(numberSprites.get(12-Integer.parseInt("" + currentt.charAt(0))), 4 * playingBoundary.getSizeX() / 5 - numberSprites.getSpriteWidth(), playingBoundary.getY() / 2 - numberSprites.getSpriteWidth(), null);
		g2.drawImage(numberSprites.get(12-Integer.parseInt("" + currentt.charAt(1))), 4 * playingBoundary.getSizeX() / 5, playingBoundary.getY() / 2 - numberSprites.getSpriteWidth(), null);
		g2.drawImage(numberSprites.get(12-Integer.parseInt("" + currentt.charAt(2))), 4 * playingBoundary.getSizeX() / 5 + numberSprites.getSpriteWidth(), playingBoundary.getY() / 2 - numberSprites.getSpriteWidth(), null);
		if(multiplayer){
			if(status.isRequestRestart()){
				resetReq = "Opponent requested restart";
			}
			
			g2.setColor(Color.BLACK);
			g2.drawString("Playing against " + status.getPlayer(), 3 * playingBoundary.getSizeX() / 5, playingBoundary.getY() / 2 - g2.getFontMetrics().getHeight() / 2);
			g2.drawString(resetReq, 2 * playingBoundary.getSizeX() / 5 - g2.getFontMetrics().stringWidth(resetReq) / 2, playingBoundary.getY() / 2 - g2.getFontMetrics().getHeight() / 2);
		}
	}

	private void setClicked(Cell c){
		if(c.getBombs() == 0 && !c.isClicked()){
			c.setClicked(true);
			c.setMarkedQues(false);
			for(Cell n : playingField.neighbors(c.getX(), c.getY())){
				if(!n.hasBomb() && !n.isMarkedBomb()){
					setClicked(n);
				}
			}
		} else if(!c.isClicked()) {
			c.setClicked(true);
			c.setMarkedQues(false);
		} 
	}

	private void lost(){
		for(int i = 0; i < playingField.rows(); i++){
			for(int n = 0; n < playingField.columns(); n++){
				if(playingField.get(i, n).hasBomb()){
					playingField.get(i,  n).setClicked(true);
				} else if(playingField.get(i, n).isMarkedBomb()) {
					playingField.get(i, n).setWrong(true);
				}
			}
		}
		if(multiplayer) {
			try {
				if(!Request.died(code, usernm)) {
					throw new Exception("Terribly wrong!");
				} else {
					//playingField.input(Request.getMap(code));
					Request.sendMap(code, usernm, playingField.export());
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.err.println("Sth went wrong, wich prevented you from dying");
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		face.changeFace(4);
		int xOff = playingBoundary.getX();
		int yOff = playingBoundary.getY();
		
		int boundX = playingBoundary.getX2();
		int boundY = playingBoundary.getY2();
		int x = (e.getX() - xOff);
		int y = (e.getY() - yOff);
		
		if(x < 0 || x > boundX || y < 0 || y > boundY){
			if(e.getX() > face.getX() && e.getX() < face.getX() + face.getSize() && e.getY() > face.getY() && e.getY() < face.getY() + face.getSize()){
				if(!multiplayer){
					//FACE
					face.locked(false);
					face.changeFace(1);
					//timer = false;
					lost = false;
					flags = bombs;
					playingField.reset(bombs);
				} else {
					face.locked(false);
					face.changeFace(1);
					int ret = 0;
					try {
						ret = Request.restart(code, usernm);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("Reset return value: " + ret);
					if(ret == 1){
						resetReq = "Reset requested";
						new Thread(){
							@Override
							public void run(){
								Status status = new Status();
								try {
									status = Request.status(code, usernm);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								while(!status.isRestart()){
									try {
										status = Request.status(code, usernm);
										Thread.sleep(5);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
								playingField.reset(bombs);
								System.out.println("Sending map at: " + new Date());
								try {
									Request.sendMap(code, usernm, playingField.export());
									//playingField.input(Request.getMap(code, usernm));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("Resetting");
								resetReq = "Reset";
								new Timer(3000, new AbstractAction(){

									@Override
									public void actionPerformed(ActionEvent e) {
									resetReq = "";
										
									}
									
								}).start();
								lost = false;
								face.locked(false);
							}
						}.start();
					} else if(ret == 2){
						System.out.println("resetting");
						try {
							playingField.reset(0);
							new Thread(){
								@Override
								public void run(){
									try {
										Thread.sleep(1000);
										System.out.println("Inputting map at "+ new Date());
										lost = playingField.input(Request.getMap(code, usernm));
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									resetReq = "Reset";
									new Timer(3000, new AbstractAction(){

										@Override
										public void actionPerformed(ActionEvent e) {
										resetReq = "";
											
										}
										
									}).start();
									if(lost){
										System.err.println("Something went wrong while resetting");
									}
									face.locked(false);
								}
							}.start();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
			}
			return;
		}
		
		if(lost) {
			return;
		}
		

		if(!timer){
			timer = true;
		}
		
		x /= fieldSprites.getSpriteWidth();
		y /= fieldSprites.getSpriteWidth();
		
		Cell c = playingField.get(x, y);
		boolean wasClicked = c.isClicked();
		if(e.getButton() == MouseEvent.BUTTON1 && (myTurn || !multiplayer)) {
			if(!c.isMarkedBomb()) {	
				if(c.hasBomb()){
					c.setoBomb(true);
					lost = true;
					timer = false;
					lostTime = (int) ((System.currentTimeMillis() - startTime)/1000);
					face.changeFace(3);
					face.locked(true);
					lost();
				} else if(c.getBombs() == 0){
					setClicked(c);
				}
				c.setClicked(true);
			}
			
		} else if(e.getButton() == MouseEvent.BUTTON3 && !c.isClicked()) {
			if(!c.isMarkedBomb() && !c.isMarkedQues()) {
				if(multiplayer) {
					if(myTurn) {
						System.out.println("Marked bomb");
						c.setMarkedBomb(true);
						flags--;
					}
				} else {
					c.setMarkedBomb(true);
					flags--;
				}
			} else if(c.isMarkedBomb()) {
				c.setMarkedQues(true);
				c.setMarkedBomb(false);
				flags++;
			} else if(c.isMarkedQues()) {
				c.setMarkedQues(false);
			}
		}
		
		if(myTurn && !wasClicked) {
			try {
				System.out.println("Exporting Map");
				Request.sendMap(code, usernm, this.playingField.export());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(wasClicked){
			System.out.println("Not sending, because clicked");
		}
	}
	
	
	private void initMultiplayer(){
		if(multiplayer) {
			try {
				lost = true;
				if(Request.connect(code, usernm)) {
					//ctrl.toMenu();
				} else {
					System.out.println("Else");
					Game tmp = this;
					Thread reg = new Thread() {
						@Override
						public void run() {
							System.out.println("Running");
							Status status = new Status();
							try {
								status = Request.status(code, usernm);
								if(!status.isFull()){
									Request.sendMap(code, usernm, playingField.export());
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							while (!status.isFull()) {
								try {
									Thread.sleep(5);
									status = Request.status(code, usernm);
									//System.out.println(status);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							System.out.println("2nd player joined");
							tmp.lost = false;
							try {
								status = Request.status(code, usernm);
								System.out.println(status);
								tmp.myTurn = status.isTurn();
								lost = playingField.input(Request.getMap(code, usernm));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(tmp.myTurn ? "My turn": "Not my turn");
//							try {
//								if(tmp.myTurn){
//									Request.sendMap(code, usernm, tmp.playingField.export());
//								} else {
//									tmp.playingField.input(Request.getMap(code));
//								}
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
						
					};
					reg.setDaemon(true);
					reg.start();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Test");
				e.printStackTrace();
				ctrl.toMenu();
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!lost || face.current() == 1){
			face.changeFace(5);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(multiplayer){
			return;
		}
		if(e.getKeyChar() == 'r'){
			cheat = false;
			currCheat = 0;
		}
		if((e.getKeyCode() >= 65 && e.getKeyCode() <= 95) || (e.getKeyCode() >= 97 && e.getKeyCode() <= 122)){
			if(currCheat >= cheatCode.length){
				cheat = true;
			} else {
				if(cheatCode[currCheat] == e.getKeyChar()){
					//System.out.println("Next char");
					currCheat++;
				} else {
					currCheat = 0;
					//System.out.println("fail");
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {

		if(!multiplayer) {
			System.out.println("Nothing to clean up");
			return;
		}
		
		System.out.println("Triggered");
		try {
			if(Request.close(code, usernm)) {
				System.out.println("Closed correctly");
			} else {
				System.err.println("Something went wrong");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Somehing went wrong x 2");
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
