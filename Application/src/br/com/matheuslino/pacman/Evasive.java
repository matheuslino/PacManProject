package br.com.matheuslino.pacman;

import java.util.List;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class Evasive extends Ghost {
	private static final Evasive instance = new Evasive(0, 0);

	Evasive(int x, int y) {
		super(x, y);
	}

	public static Evasive getInstance() {
		return instance;
	}

	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}

}
