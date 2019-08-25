import java.awt.Color;
import java.awt.Graphics;

public class Map {
	
	private int length;
	private int width;
	private int height;
	int[][] grid;
	
	public Map(int width, int height, int length) {
		this.length = length;
		this.width = width;
		this.height = height;
		grid = new int[length][length];
		populateGrid();
	}
	
	public void populateGrid() {
		for(int i = 0; i<grid.length; i++) {
			for(int j = 0; j<grid[0].length; j++) {
				grid[i][j] = 0;
			}
		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.WHITE);
		for(int i = 0; i<length+1; i++) {
			g.drawLine(0, (int)(i*(double)height/length), width, (int)(i*(double)height/length));
			g.drawLine((int)(i*(double)width/length), 0, (int)(i*(double)width/length), height);
		}
	}
}
