import java.awt.Graphics;

public class ObjectManager {

	private int width, height;
	private int mapSize = 20;
	
	private Map map;
	private Snake snake;
	private Apple apple;
	
	public ObjectManager(int width, int height) {
		this.width = width;
		this.height = height;
		
		snake = new Snake(width, height, mapSize, 5);
		apple = new Apple(width, height, mapSize);
		
		map = new Map(width, height, mapSize);
	}
	
	public void fpCollisions() {
		int c = collisions(snake, apple);
		if(c==0) {
			snake = new Snake(width, height, mapSize, 5);
		}
		if(c==1) {
			apple.randomLoc();
			snake.addsect();
		}
	}
	
	public int collisions(Snake s, Apple a) {
		if(s.selfCollision()||s.edgeCollision()) {
			return 0;
		}
		if(s.appleCollision(a.getLoc())) {
			return 1;
		}
		return -1;
	}
	
	public void draw(Graphics g) {
		map.draw(g);
		apple.draw(g);
		snake.draw(g, 255);
	}

	public void moveSnake(int dir) {
		snake.move(dir);
	}
}
