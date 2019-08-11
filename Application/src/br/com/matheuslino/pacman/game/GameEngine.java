package br.com.matheuslino.pacman.game;

import br.com.matheuslino.pacman.LabyrinthMap;

public abstract class GameEngine {
	private LabyrinthMap labyrinthMap;

	// Construtor
	public GameEngine(LabyrinthMap labyrinthMap) {
		this.labyrinthMap = labyrinthMap;
	}

	protected LabyrinthMap getLabyrinthMap() {
		return labyrinthMap;
	}

	// Principal Loop
	public abstract void gameLoop();
	
}
