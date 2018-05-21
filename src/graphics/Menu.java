package graphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import javax.swing.*;

public class Menu extends JPanel{

	private static final Map<String, int[]> difficulties = new HashMap<String, int[]>();
	private static final List<String> converter = new ArrayList<String>();
	
	private int width, height;
	
	private Window ctrl;
	private JButton[] buttons;
	
	public Menu(int w, int h, Window ctrl){
		width = w;
		height = h;
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		
		addDiff("Beginner", 9, 9, 10);
		addDiff("Intermediate", 16, 16, 40);
		addDiff("Expert", 30, 16, 99);
		addDiff("Stephen", 10, 10, 99);
		addDiff("Multiplayer", 50, 20, 200);

		this.ctrl = ctrl;
		
		buttons = new JButton[difficulties.keySet().size()];
		
		for(int i = 0; i < buttons.length; i++){
			int[] tmp = difficulties.get(converter.get(i));
			
			buttons[i] = new JButton(new AbstractAction(){
	
				@Override
				public void actionPerformed(ActionEvent e) {
					if(((JButton)e.getSource()).getText().equals("Multiplayer")) {
						ctrl.toKeypad(tmp);
					} else {
						ctrl.toGame(tmp);
					}
				}
				
			});
			buttons[i].setText(converter.get(i));
			buttons[i].setToolTipText(String.format("%dx%d, %d bombs", tmp[0], tmp[1], tmp[2]));
			buttons[i].setEnabled(true);
			buttons[i].setPreferredSize(new Dimension(200, 50));
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.CENTER;
			gbc.gridy = i;
			layout.setConstraints(buttons[i], gbc);
			this.add(buttons[i]);
		}
			
		ctrl.cBounds(500, 300, 200, 50 * buttons.length);
	}
	
	public void addDiff(String name, int sx, int sy, int bombs){
		difficulties.put(name, new int[]{sx, sy, bombs});
		converter.add(name);
	}
	
}
