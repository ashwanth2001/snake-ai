import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.LinkedList;

public class Snake {

	private int width, height, length;
	private int size, dir;
	private LinkedList<int[]> body;
	private boolean add;
	private boolean isAlive;
	private int lifetime;
	
	public Snake(int width, int height, int length, int size) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.size = size;
		dir = (int)(Math.random()*4);
		dir = 3;
		lifetime = 0;
		add = false;
		isAlive = true;
		body = new LinkedList<int[]>();
		initialize();
	}

	private void initialize() {
		int[] add;
		int xs = length/2;
		int ys = length/2;
		for(int i = 0; i<size; i++) {
			add = new int[2];
			add[0] = xs;
			add[1] = ys+i;
			body.addFirst(add);
		}
	}
	
	public void move(int ndir) {
		if(ndir>-1 && ndir%2 != dir%2 && ndir<=3) {
			dir = ndir;
		}
		int[] head = body.getFirst();
		int x = head[0] - (dir-1)*((dir+1)%2);
		int y = head[1] + (dir-2)*(dir%2);
		int[] nhead = {x, y};
		body.addFirst(nhead);
		
		if(add) {
			size++;
			add = false;
		}
		else {
			body.removeLast();
		}
	}
	
	public void turn(int ndir) {
		dir+=ndir+4;
		dir%=4;
		int[] head = body.getFirst();
		move(-1);
	}
	
	public boolean appleCollision(int[] apple) {
		int[] sect = body.getFirst();
		if(apple[0]==sect[0] && apple[1]==sect[1]) {
			return true;
		}
		return false;
	}

	public boolean edgeCollision() {
		int[] sect = body.getFirst();
		if(sect[0]>=length || sect[0]<0 || sect[1]>=length || sect[1]<0) {
			return true;
		}
		return false;
	}
	
	public boolean selfCollision() {
		int[] a = body.getFirst();
		for(int i = 1; i<size; i++) {
			int[] b = body.get(i);
			if(a[0]==b[0] && a[1]==b[1]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean intersect(int[] loc) {
		for(int i = 0; i<size; i++) {
			if(loc[0] == body.get(i)[0] && loc[1] == body.get(i)[1]) {
				return true;
			}
		}
		return false;
	}
	
	public float calcFitness() {
		if(size<10) {
			return (float) (lifetime*lifetime*Math.pow(2, size));
		}
		return (float) (1000*1000*Math.pow(2, 10)*(size-9));
	}
	
	public void addsect() {
		add = true;
	}
	
	public void addLife() {
		lifetime++;
	}
	
	public int[] getHead() {
		int[] head = body.get(0);
		return head;
	}
	
	public int getDir() {
		return dir;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void die() {
		isAlive = false;
	}
	
	public void draw(Graphics g, int c) {
		for(int i = 0; i<body.size(); i++) {
			int[] sect = body.get(i);
			g.setColor(new Color(c, c, c));
			g.fillRect((int)((double)width/length*sect[0]), (int)((double)height/length*sect[1]), width/length, height/length);
			g.setColor(Color.BLACK);
			g.drawRect((int)((double)width/length*sect[0]), (int)((double)height/length*sect[1]), width/length, height/length);
		}
	}
}
