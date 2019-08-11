package br.com.matheuslino.pacman.game;

import br.com.matheuslino.pacman.Checkpoint;
import br.com.matheuslino.pacman.Evasive;
import br.com.matheuslino.pacman.Jumper;
import br.com.matheuslino.pacman.PacMan;
import br.com.matheuslino.pacman.Pursuer;
import br.com.matheuslino.pacman.Random1;
import br.com.matheuslino.pacman.Wall;

// Visitor (according to Visitor Design Pattern)
public interface LabyrinthObjectVisitor {

	void visit(Checkpoint checkpoint);
	void visit(Wall wall);
	void visit(PacMan pacMan);
	void visit(Random1 random);
	void visit(Pursuer pursuer);
	void visit(Jumper jumper);
	void visit(Evasive evasive);
}