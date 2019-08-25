import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Main implements ActionListener {

	JFrame frame;
	GamePanel panel;
	int width = 600;
	int height = 600;
	Timer t;
	int repeat = 15;

	public static void main(String args[]) {
		Main main = new Main();
		main.Jsetup();
	}

	private void Jsetup() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocation(0, 0);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panel.save();
			}
		});
		frame.setVisible(true);
		panel.start();
		t.start();
	}

	public Main() {
		t = new Timer(repeat, this);
		frame = new JFrame();
		panel = new GamePanel(width, height);
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.addMouseListener(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
