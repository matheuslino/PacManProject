package br.com.matheuslino.pacman;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class Checkpoint extends LabyrinthObject {
	private boolean conquered;
	private boolean special;
	
	Checkpoint(int x, int y) {
		super(x, y);
		this.conquered = false;			// Estado do checkpoint (conquistado ou nao)
		this.special = false;			// Tipo do checkpoint (especial ou comum)
	}
	
	@Override
	public void accept(LabyrinthObjectVisitor visitor) {
		visitor.visit(this);
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public boolean isConquered() {
		return conquered;	
	}
	
	public void setConquered(boolean conquered) {
		this.conquered = conquered;
	}
	
	// Mtodo com visibilidade de pacote (Default)
	void conquer() {
		this.setConquered(true);
	}
}
