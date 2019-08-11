package br.com.matheuslino.pacman;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public abstract class LabyrinthObject {
	private Coordinate coordinate = new Coordinate(0,0);

	LabyrinthObject(int x, int y) {
		coordinate.changeCoordinates(x, y);
	}
	
	// "Set" Method - Changes coordinate
	public void changeCoordinates(int x, int y) {
		coordinate.changeCoordinates(x, y);
	}
	
	public int getX() {
		return coordinate.getX();
	}

	public int getY() {
		return coordinate.getY();
	}
	
	// return the coordinate object
	protected Coordinate getCoordinate() {
		return coordinate;
	}
	
	public boolean isSameCoordinates(int x, int y) {
		return coordinate.isSameCoordinates(x, y);
	}
	
	public boolean isSameCoordinates(LabyrinthObject obj) {
		return coordinate.equals(obj);
	}
	
	public abstract void accept ( LabyrinthObjectVisitor visitor ) ;
}
