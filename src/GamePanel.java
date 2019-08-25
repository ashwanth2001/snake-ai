import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	
	private Timer timer;
	private int width, height;
	private int dir = -1;
	long time;
	
	private ObjectManager manager;
	private Population pop;
	private test t;

	public GamePanel(int width, int height) {
		this.width = width;
		this.height = height;
		timer = new Timer(20, this);
		manager = new ObjectManager(width, height);
		
		int[] vals = {width, height, 30, 3};
		pop = new Population(2000, vals);
		t = new test(vals);
	}
	
	public void start() {
		time = System.currentTimeMillis();
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//move();
		pop.run();
		repaint();
	}
	
	private void move() {
		if(System.currentTimeMillis()-50>time) {
			manager.fpCollisions();
			manager.moveSnake(dir);
			dir = -1;
			time = System.currentTimeMillis();
		}
	}
	
	public void save() {
		pop.save();
	}

	public void paint(Graphics g) {
		//manager.draw(g);
		pop.draw(g);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_W) {
			dir = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			dir = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			dir = 3;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			dir = 0;
		}
		//System.out.println(dir);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
