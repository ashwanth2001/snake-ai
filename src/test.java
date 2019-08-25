import java.awt.Graphics;
import java.util.Arrays;

public class test {
	int[] vals;
	Snake s;
	Snake[] snakes;
	Apple a;
	NeuralNetwork brain;
	
	public test(int[] vals) {
		this.vals = vals;
		instantiate();
	}
	
	public void instantiate() {
		s = new Snake(vals[0], vals[1], vals[2], vals[3]);
		a = new Apple(vals[0], vals[1], vals[2]);
		int[] nodes = {18, 18};
		brain = new NeuralNetwork(24, 16, 4, 2);
	}
	
	public void run() {
		move();
		collision();
		end();
	}
	
	public void move() {
		s.move(brain.output(getInput()));
	}
	
	public int maxInd(double[] input) {
		double max = -1;
		int ind = 0;
		for(int i = 0; i<input.length; i++) {
			if(input[i]>max) {
				max = input[i];
				ind = i;
			}
		}
		return ind;
	}
	
	public float[] getInput() {
		int[] step = {0, 0};
		float[] input = new float[24];
		int dir = 0;
		for(int i = 0; i<8; i++) {
			step[0] = 1 - ((i+1)%4)/3 - (i%6/3)*2;
			step[1] = -1 + (i+3)%4/3 + ((i-2)%6/3)*2;
			float[] vis = vision(step);
			
			input[3*i] = vis[0];
			input[3*i+1] = vis[1];
			input[3*i+2] = vis[2];
		}
		return input;
	}
	
	public float[] vision(int[] step) {
		int[] loc = s.getHead().clone();
		int[] aLoc = a.getLoc().clone();
		int dist = 0;
		
		float[] ret = new float[3];
		
		while(loc[0]<vals[2] && loc[0]>=0 && loc[1]<vals[2] && loc[1]>=0) {
			loc[0]+=step[0];
			loc[1]+=step[1];
			dist++;
			
			if(s.intersect(loc)) {
				ret[1] = (float) (1.0/dist);
			}
			
			if(loc[0]==aLoc[0] && loc[1]==aLoc[1]) {
				ret[0] = 1;
			}
		}
		ret[2] = (float) (1.0/dist);
		
		return ret;
	}
	
	public void collision() {
		if(s.selfCollision()||s.edgeCollision()) {
			s.die();
		}
		if(s.appleCollision(a.getLoc())) {
			s.addsect();
			a.randomLoc();
		}
		s.addLife();
	}
	
	public void end() {
		if(!s.isAlive()) {
			instantiate();
		}
	}
	
	public void draw(Graphics g) {
		s.draw(g, 255);
		a.draw(g);
	}
}
