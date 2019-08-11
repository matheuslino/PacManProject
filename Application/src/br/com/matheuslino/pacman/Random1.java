package br.com.matheuslino.pacman;

import java.util.List;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class Random1 extends Ghost {
	private static final Random1 instance = new Random1(0, 0);
	
	Random1(int x, int y) {
		super(x, y);
		this.setCurrentDirection(Direction.getRandom());		// Assigns the first random direction to the Random Ghost
	}
	
	public static Random1 getInstance() {
		return instance;
	}

	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}

	void moveRandom(List<Wall> walls) {

		Direction  dir_random = null;			// Random ghost direction
		boolean 		  aux = false;			// checks if random direction is valid

		dir_random = getCurrentDirection();		// Current direction
		
		// Move in one direction until it collides with a wall
		while(!aux) {
			
			if(dir_random == Direction.DOWN) {
				for(Wall wall: walls) {
					if(this.getX() == wall.getX() && this.getY()+1 == wall.getY()) {
						aux = false;
						dir_random = Direction.getRandom();
						break;
					}
					aux = true;
				}
				
			}
			else if(dir_random == Direction.UP) {
				for(Wall wall: walls) {
					if(this.getX() == wall.getX() && this.getY()-1 == wall.getY()) {
						aux = false;
						dir_random = Direction.getRandom();
						break;
					}
					aux = true;
				}
			}
			else if(dir_random == Direction.LEFT) {
				for(Wall wall: walls) {
					if(this.getX()-1 == wall.getX() && this.getY() == wall.getY()) {
						aux = false;
						dir_random = Direction.getRandom();
						break;
					}
					aux = true;
				}
			}
			else if(dir_random == Direction.RIGHT) {
				for(Wall wall: walls) {
					if(this.getX()+1 == wall.getX() && this.getY() == wall.getY()) {
						aux = false;
						dir_random = Direction.getRandom();
						break;
					}
					aux = true;
				}
			}
		}
		this.setCurrentDirection(dir_random);					// update random ghost direction
		
		if(dir_random != null)	this.move(dir_random, walls);	// Move ghost
	}
}
