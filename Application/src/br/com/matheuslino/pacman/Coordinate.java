package br.com.matheuslino.pacman;

public class Coordinate {
	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void changeCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isSameCoordinates(int x, int y) {
		if(this.x == x && this.y == y) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean equals(LabyrinthObject obj) {
		if(this.x == obj.getX() && this.y == obj.getY()) {
			return true;
		}else {
			return false;
		}
	}
}

