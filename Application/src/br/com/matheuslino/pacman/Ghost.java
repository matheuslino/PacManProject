package br.com.matheuslino.pacman;


import java.util.List;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public abstract class Ghost extends Player {

	Ghost(int x, int y) {
		super(x, y);
	}

	@Override
	public abstract void accept(LabyrinthObjectVisitor visitor);		// Metodo a ser implementado nas classes filhas

	void move(Direction direction, List<Wall> walls) {
		switch (direction) {
			case DOWN:
				for(Wall wall: walls) {
					if(this.getX() == wall.getX() && this.getY()+1 == wall.getY()) {
						return;
					}
				}
				getCoordinate().changeCoordinates(getX(), getY()+1);
				break;
			case UP:
				for(Wall wall: walls) {
					if(this.getX() == wall.getX() && this.getY()-1 == wall.getY()) {
						return;
					}
				}
				getCoordinate().changeCoordinates(getX(), getY()-1);
				break;
			case LEFT:
				for(Wall wall: walls) {
					if(this.getX()-1 == wall.getX() && this.getY() == wall.getY()) {
						return;
					}
				}
				getCoordinate().changeCoordinates(getX()-1, getY());
				break;
			case RIGHT:
				for(Wall wall: walls) {
					if(this.getX()+1 == wall.getX() && this.getY() == wall.getY()) {
						return;
					}
				}
				getCoordinate().changeCoordinates(getX()+1, getY());
				break;
	
			default:
				break;
		}
	}

}
