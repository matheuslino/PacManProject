package br.com.matheuslino.pacman;

import java.util.List;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public abstract class Player extends LabyrinthObject {
	
	// Attributes
	private Direction currentDirection;
	private Coordinate initialCoordinate = new Coordinate(0,0); 

	Player(int x, int y) {
		super(x, y);
		this.initialCoordinate.changeCoordinates(x, y);
	}

	// Methods
	public Coordinate getInitialCoordinate() {
		return initialCoordinate;
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	@Override
	public abstract void accept(LabyrinthObjectVisitor visitor);

	
}
