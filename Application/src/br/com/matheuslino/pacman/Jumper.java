package br.com.matheuslino.pacman;

import java.util.List;
import java.util.Random;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class Jumper extends Ghost {
	private static final Jumper instance = new Jumper(0, 0);
	private int teleportation;									// teleportation counter

	Jumper(int x, int y) {
		super(x, y);
		this.setCurrentDirection(Direction.getRandom());		// Set the first random direction of the jumper ghost
		setTeleportation(0); 									// Example of using GET and SET methods privately
	}
	
	public static Jumper getInstance() {
		return instance;
	}

	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}
	
	private int getTeleportation() {
		return teleportation;
	}

	private void setTeleportation(int teleportation) {
		this.teleportation = teleportation;
	}

	void moveJumper(List<Wall> walls, List<Checkpoint> checkpoints) {

		Direction  dir_jumper = null;			// Jumper ghost direction 
		boolean 		  aux = false;			// Checks if the random direction is valid
		Random 				r = new Random();	// Used in methods that require random data
		int 		 position = 0;				// Position to be teleported (in checkpoints lists)
		
		dir_jumper = getCurrentDirection();		// Initial direction
		
		// Teleport the jumper ghost after 20 moves (project decision)
		if(getTeleportation() < 20) {
			
			// Move in one direction until it collids with a wall
			while(!aux) {
				
				if(dir_jumper == Direction.DOWN) {
					for(Wall wall: walls) {
						if(this.getX() == wall.getX() && this.getY()+1 == wall.getY()) {
							aux = false;
							dir_jumper = Direction.getRandom();
							break;
						}
						aux = true;
					}
					
				}
				else if(dir_jumper == Direction.UP) {
					for(Wall wall: walls) {
						if(this.getX() == wall.getX() && this.getY()-1 == wall.getY()) {
							aux = false;
							dir_jumper = Direction.getRandom();
							break;
						}
						aux = true;
					}
				}
				else if(dir_jumper == Direction.LEFT) {
					for(Wall wall: walls) {
						if(this.getX()-1 == wall.getX() && this.getY() == wall.getY()) {
							aux = false;
							dir_jumper = Direction.getRandom();
							break;
						}
						aux = true;
					}
				}
				else if(dir_jumper == Direction.RIGHT) {
					for(Wall wall: walls) {
						if(this.getX()+1 == wall.getX() && this.getY() == wall.getY()) {
							aux = false;
							dir_jumper = Direction.getRandom();
							break;
						}
						aux = true;
					}
				}
			}
			this.setCurrentDirection(dir_jumper);					// Update jumper ghost direction
			this.setTeleportation(getTeleportation()+1);			// Increment counter (teleportation)
			
			if(dir_jumper != null)	this.move(dir_jumper, walls);	// Move ghost
		}
		else {
			this.setTeleportation(0); 								// Reset counter (teleportation)
			position = r.nextInt(checkpoints.size());
			if(position < 0) position *= -1;
			
			// Teleports the jumper
			getCoordinate().changeCoordinates(checkpoints.get(position).getX(),
											  checkpoints.get(position).getY());
		}
		
	}

}
