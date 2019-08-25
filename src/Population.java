import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Population {

	Map m;
	private Snake[] snakes;
	private Apple[] appleStore;
	private int[] apples;
	private NeuralNetwork[] brains;
	
	private int[] netStats = {24, 16, 4, 2};
	
	private int bestScore = 0;
	private int gen = 0;
	private float mutationRate = (float) 0.5;

	private int size;
	private int[] vals;

	private float[] fitness;
	private int fitnessSum = 0;
	private float bestFitness = 0;
	private NeuralNetwork bestBrain;

	private int moves = 0;
	private int maxMoves = 100;

	public Population(int n, int[] vals) {
		m = new Map(vals[0], vals[1], vals[2]);
		size = n;
		this.vals = vals;

		snakes = new Snake[n];
		appleStore = new Apple[100];
		apples = new int[n];
		brains = new NeuralNetwork[n];
		fitness = new float[n];

		//bestBrain = new NeuralNetwork(netStats[0], netStats[1], netStats[2], netStats[3]);
		bestBrain = readSnake();
		gen++;

		instantiate();
	}

	public void instantiate() {
		for (int i = 0; i < size; i++) {
			snakes[i] = new Snake(vals[0], vals[1], vals[2], vals[3]);
			brains[i] = new NeuralNetwork(netStats[0], netStats[1], netStats[2], netStats[3]);
			apples[i] = 0;
		}
		
		brains[0] = bestBrain.clone();
		
		for(int i = 0; i<100; i++) {
			appleStore[i] = new Apple(vals[0], vals[1], vals[2]);
		}
	}
	
	public NeuralNetwork readSnake() {
		NeuralNetwork ret = new NeuralNetwork(netStats[0], netStats[1], netStats[2], netStats[3]);
		
		int[][] sizes = new int[netStats[3]+2][2];
		sizes[0][0] = netStats[1];
		sizes[0][1] = netStats[0] + 1;
		for(int i = 1; i<netStats[3]; i++) {
			sizes[i][0] = netStats[1];
			sizes[i][1] = netStats[1] + 1;
		}
		sizes[sizes.length-2][0] = netStats[2];
		sizes[sizes.length-2][1] = netStats[1]+1;
		
		
		Matrix[] weights = new Matrix[netStats[3]+1];
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/load.txt"));
			String st = br.readLine();
			if(st==null) {
				return ret;
			}
			
			int ind1 = 0;
			int ind2 = 0;
			int max = sizes[ind1][0]*sizes[ind1][1];
			float[][] m = new float[sizes[ind1][0]][sizes[ind1][1]];
			
			while(st!=null) {
				while(!st.equals("")) {
					m[ind2/m[0].length][ind2%m[0].length] = Float.parseFloat(st.substring(0, st.indexOf(" ")));
					st = st.substring(st.indexOf(" ") + 1);
					ind2++;
				}
				if(ind2==max) {
					weights[ind1] = new Matrix(m).clone();
					ind1++;
					ind2 = 0;
					m = new float[sizes[ind1][0]][sizes[ind1][1]];
					max = sizes[ind1][0]*sizes[ind1][1];
				}
				st = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("loaded");
		ret.load(weights);
		return ret;
	}

	public void run() {
		move();
		collisions();
		checkEnd();
	}

	public void move() {
		moves++;
		for (int i = 0; i < size; i++) {
			if (snakes[i].isAlive()) {
				float[] inputs = getInput(i);
				snakes[i].move(brains[i].output(inputs));
			}
		}
	}

	public float getMaxInd(float[] input) {
		double max = -1;
		int ind = 0;
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
				ind = i;
			}
		}
		return ind;
	}

	public float[] getInput(int ind) {
		int[] step = { 0, 0 };
		float[] input = new float[24];

		for (int i = 0; i < 8; i++) {
			step[0] = 1 - ((i + 1) % 4) / 3 - (i % 6 / 3) * 2;
			step[1] = -1 + (i + 3) % 4 / 3 + ((i - 2) % 6 / 3) * 2;
			float[] vis = vision(ind, step);

			input[3 * i] = vis[0];
			input[3 * i + 1] = vis[1];
			input[3 * i + 2] = vis[2];
		}
		return input;
	}

	public float[] vision(int ind, int[] step) {
		int[] loc = snakes[ind].getHead().clone();
		int[] aLoc = appleStore[apples[ind]].getLoc().clone();
		int dist = 0;

		float[] ret = new float[3];

		while (loc[0] < vals[2] && loc[0] >= 0 && loc[1] < vals[2] && loc[1] >= 0) {
			loc[0] += step[0];
			loc[1] += step[1];
			dist++;

			if (snakes[ind].intersect(loc)) {
				ret[1] = (float) (1.0 / dist);
			}

			if (loc[0] == aLoc[0] && loc[1] == aLoc[1]) {
				ret[0] = 1;
			}
		}
		ret[2] = (float) (1.0 / dist);

		return ret;
	}

	public void collisions() {
		for (int i = 0; i < size; i++) {
			Snake s = snakes[i];
			if (snakes[i].isAlive()) {
				if (s.selfCollision() || s.edgeCollision()) {
					s.die();
				}
				if (s.appleCollision(appleStore[apples[i]].getLoc())) {
					s.addsect();
					apples[i]++;
				}
				s.addLife();
			}
		}
	}

	public void checkEnd() {
		boolean next = true;
		for (int i = 0; i < size; i++) {
			if ((snakes[i].getSize() - vals[3] + 1) * maxMoves < moves) {
				snakes[i].die();
			}
			if (snakes[i].isAlive()) {
				next = false;
				break;
			}
		}

		if (next) {
			System.out.println(bestScore);
			nextGen();
		}
	}

	public void nextGen() {
		calcFitness();
		naturalSelection();
	}

	public void calcFitness() {
		fitnessSum = 0;
		for (int i = 0; i < size; i++) {
			fitness[i] = snakes[i].calcFitness();
			fitnessSum += fitness[i];
		}
	}

	public void naturalSelection() {
		NeuralNetwork[] newBrains = new NeuralNetwork[brains.length];

		setBestBrain();
		newBrains[0] = bestBrain.clone();

		for (int i = 1; i < brains.length; i++) {
			NeuralNetwork child = selectParent().crossover(selectParent());
			child.mutate(mutationRate);
			newBrains[i] = child;
		}

		brains = newBrains.clone();
		gen++;
		moves = 0;

		resetSnakes();
	}

	public void setBestBrain() {
		float max = 0;
		int maxInd = 0;
		for (int i = 0; i < snakes.length; i++) {
			if (fitness[i] > max) {
				max = fitness[i];
				maxInd = i;
			}
		}
		if (max > bestFitness) {
			bestFitness = max;
			bestBrain = brains[maxInd].clone();
			bestScore = snakes[maxInd].getSize();
		}
		System.out.println(bestFitness);
	}

	public NeuralNetwork selectParent() {
		float rand = (float) (Math.random() * fitnessSum);
		float sum = 0;
		for (int i = 0; i < brains.length; i++) {
			sum += fitness[i];
			if (sum > rand) {
				return brains[i];
			}
		}
		return brains[0];
	}

	public void resetSnakes() {
		snakes = new Snake[snakes.length];
		apples = new int[apples.length];
		for (int i = 0; i < size; i++) {
			snakes[i] = new Snake(vals[0], vals[1], vals[2], vals[3]);
		}
		for(int i = 0; i<100; i++) {
			//appleStore[i] = new Apple(vals[0], vals[1], vals[2]);
		}
	}
	
	public void save() {
		System.out.println("ok");
		String str = bestBrain.toString();
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/save.txt", true));
			bw.write(str + "\n");
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Snake[] getSnakes() {
		return snakes;
	}

	public NeuralNetwork[] getBrains() {
		return brains;
	}

	public void draw(Graphics g) {
		m.draw(g);
		int c = 198;
		
		for (int i = 1; i < 2000; i++) {
			c += 1;
			if(snakes[i].isAlive())
				snakes[i].draw(g, 125);
		}
		snakes[0].draw(g, 255);
		appleStore[apples[0]].draw(g);
	}

}
