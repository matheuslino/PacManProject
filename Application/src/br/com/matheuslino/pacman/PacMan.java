package br.com.matheuslino.pacman;

import java.util.List;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class PacMan extends Player {
	private int life;
	private int score;

	PacMan(int x, int y) {
		super(x, y);
		this.life = 3;
		this.score = 0;
	}

	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}

	// Move method
	void move(Direction direction, List<Wall> walls, List<Ghost> ghosts) {
		switch (direction) {
			case DOWN:
				for(Wall wall: walls) {
					if(this.getX() == wall.getX() && this.getY()+1 == wall.getY()) {
						return;
					}
				}
				for(Ghost ghost: ghosts) {
					if(this.getX() == ghost.getX() && this.getY()+1 == ghost.getY()) {
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
				for(Ghost ghost: ghosts) {
					if(this.getX() == ghost.getX() && this.getY()-1 == ghost.getY()) {
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
				for(Ghost ghost: ghosts) {
					if(this.getX()-1 == ghost.getX() && this.getY() == ghost.getY()) {
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
				for(Ghost ghost: ghosts) {
					if(this.getX()+1 == ghost.getX() && this.getY() == ghost.getY()) {
						return;
					}
				}
				getCoordinate().changeCoordinates(getX()+1, getY());
				break;
	
			default:
				break;
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}	
}
