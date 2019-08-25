import java.awt.Color;
import java.awt.Graphics;

public class Apple {

	private int width, height, length;
	private int[] loc;
	
	
	public Apple(int width, int height, int length) {
		this.width = width;
		this.height = height;
		this.length = length;
		loc = new int[2];
		randomLoc();
	}
	
	public void randomLoc() {
		loc[0] = (int)(Math.random()*length);
		loc[1] = (int)(Math.random()*length);
	}
	
	public int[] getLoc() {
		return loc;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int)((double)width/length*loc[0]), (int)((double)height/length*loc[1]), width/length, height/length);
	}
}
