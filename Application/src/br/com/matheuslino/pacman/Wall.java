package br.com.matheuslino.pacman;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class Wall extends LabyrinthObject {
	
	Wall(int x, int y) {
		super(x, y);
	}

	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}
}
