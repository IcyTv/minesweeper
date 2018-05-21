package graphics;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NameEnterField extends JPanel{
	
	public NameEnterField(int[] size, int code, Window ctrl) {
		JTextField jt = new JTextField();
		jt.setPreferredSize(new Dimension(100, 20));
		JButton submit = new JButton("Submit");
		Action subm = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ctrl.toGame(size, code, jt.getText());
			}
			
		};
		jt.addActionListener(subm);
		submit.addActionListener(subm);
		add(jt);
		add(submit);
	}
}
