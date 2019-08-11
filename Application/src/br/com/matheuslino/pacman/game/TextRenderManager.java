package br.com.matheuslino.pacman.game;

import br.com.matheuslino.pacman.Checkpoint;
import br.com.matheuslino.pacman.Evasive;
import br.com.matheuslino.pacman.Jumper;
import br.com.matheuslino.pacman.LabyrinthMap;
import br.com.matheuslino.pacman.LabyrinthObject;
import br.com.matheuslino.pacman.Level;
import br.com.matheuslino.pacman.PacMan;
import br.com.matheuslino.pacman.Pursuer;
import br.com.matheuslino.pacman.Random1;
import br.com.matheuslino.pacman.Wall;
import java.lang.System;

// Class responsible by printing the labyrinth
class TextRenderManager implements LabyrinthObjectVisitor {
	private char charMap [][];
	private int info[] = new int[2];			// info[0]: life, info[1]: score
	private Level level;						// Difficulty level
	
	// Constructor with package visibility - Default
	TextRenderManager (int mapWidth , int mapHeight ) {
		this.charMap = new char[mapHeight][mapWidth];
	}
	// Method responsible by managing map printing
	public void render(LabyrinthMap map){
		level = map.getLevel();
		clearMap();
		map.accept(this);
		printMap();
	}
	
	public int[] getInfo() {
		return info;
	}
	
	public Level getLevel() {
		return level;
	}
	
	// Method responsible by cleaning the labyrinth
	private void clearMap(){
		for(int i=0; i<charMap.length; i++){
			for (int j=0; j<charMap[0].length; j++){
				charMap[i][j] = ' ';
			}
		}
	}
	
	// Print the map
	private void printMap(){
		
		// Alternative to "clean up" eclipse console
		for(int i=0; i<50; i++) {
			System.out.println("\n");
		}
		
		// Print game info (life and score)
		System.out.println("Life: " + getInfo()[0] + "\t   Score: " + getInfo()[1] +  "\tLevel: "+getLevel()+"\n");
		
		// Print the matrix
		for(int i=0; i<charMap.length; i++){
			for (int j=0; j<charMap[0].length; j++){
				System.out.print(charMap[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	// Set - charMap's atribute
	private void setSymbol(LabyrinthObject obj, char character){
		charMap[obj.getY()][obj.getX()] = character;
	}
	
	@Override
	public void visit(Wall parede){
		setSymbol(parede, 'X');					// Character chosen for Walls
	}

	@Override
	public void visit(Checkpoint checkpoint){
		if(checkpoint.isConquered()){
			setSymbol(checkpoint, ' ');			// Character chosen for the visited checkpoint
		} else if(checkpoint.isSpecial()) {
			setSymbol(checkpoint,'•');			// Character chosen for the special checkpoint
		} else{
			setSymbol(checkpoint,'.');			// Character chosen for the unvisited checkpoint
		}
	}
	
	@Override
	public void visit(PacMan pacman){
		setSymbol(pacman,'©');					// Character chosen for the Pacman
		info[0] = pacman.getLife();
		info[1] = pacman.getScore();	
	}

	@Override
	public void visit(Random1 random){
		setSymbol(random, 'R');					// Character chosen for the random ghost
	}
	
	@Override
	public void visit(Pursuer pursuer){
		setSymbol(pursuer, 'U');				// Character chosen for the pursuer ghost
	}
	
	@Override
	public void visit(Jumper jumper){
		setSymbol(jumper, 'J');					// Character chosen for the jumper ghost
	}
	
	@Override
	public void visit(Evasive evasive){
		setSymbol(evasive, 'E');				//  Character chosen for the evasive ghost
	}
}