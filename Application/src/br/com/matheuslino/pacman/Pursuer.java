package br.com.matheuslino.pacman;

import java.util.List;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class Pursuer extends Ghost {

	// Attributes
	private static final Pursuer instance = new Pursuer(0, 0);

	Pursuer(int x, int y) {
		super(x, y);
	}
	
	public static Pursuer getInstance() {
		return instance;
	}

	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}

}
