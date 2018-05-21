package graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Keypad extends JPanel{
	
	private static final int SIZE = 50;
	
	private Window ctrl;
	private JButton[] buttons;
	private JLabel code;
	
	public Keypad(Window ctrl) {
		this(ctrl, null);
	}
	
	public Keypad(Window ctrl, int[] sizes){
		this.ctrl = ctrl;
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		
		code = new JLabel();
		code.setPreferredSize(new Dimension(SIZE * 3, SIZE));
		code.setFont(new Font(null, Font.TRUETYPE_FONT, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		layout.setConstraints(code, gbc);
		add(code);
		
		buttons = new JButton[13];
		for(int i = 0; i < 12; i++) {
			JButton b = new JButton(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JButton d = ((JButton)arg0.getSource());
					if(d.getText() == "B" && code.getText().length() > 0) {
						code.setText(code.getText().substring(0, code.getText().length() - 1));
					} else if(d.getText() == "D" && code.getText().length() >= 4) {
						ctrl.toName(sizes, Integer.parseInt(code.getText()));
					} else if(d.getText() != "D"){
						code.setText(code.getText() + d.getText());
					}
				}
				
			});
			
			b.setText("" + i);
			
			if(i > 9) {
				if(i == 10) {
					b.setText("B");
				} else {
					b.setText("D");
				}
			}
			
			b.setPreferredSize(new Dimension(SIZE, SIZE));
			
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.CENTER;
			gbc.gridy = (i / 3) + 1;
			gbc.gridx = i % 3;
			layout.setConstraints(b, gbc);
			
			add(b);
			buttons[i] = b;
		}
		ctrl.setBounds(500, 300, SIZE * 3 + 10, SIZE * 5 + 10);
	}
	
}