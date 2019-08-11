package br.com.matheuslino.pacman.game;

import java.util.Scanner;

import br.com.matheuslino.pacman.Direction;
import br.com.matheuslino.pacman.LabyrinthMap;

public class TextEngine extends GameEngine {
	private TextRenderManager renderManager =  new TextRenderManager(getLabyrinthMap().getWidth(), getLabyrinthMap().getHeight());

	public TextEngine(LabyrinthMap labyrinthMap) {
		super(labyrinthMap);
	}
	
	// Method responsible by reading the keyboard
	public Direction readCommandFromKeyboard(Scanner scanner) {		
		
		String read = scanner.next();

		if(read.equals("w") || read.equals("W")) {
			return Direction.UP;
		}		
			
		else if(read.equals("a") || read.equals("A")) {
			return Direction.LEFT;
		}
			
		else if(read.equals("d") || read.equals("D")) {
			return Direction.RIGHT;	
		}
			
		else if(read.equals("s") || read.equals("S")) {
			return Direction.DOWN;
		}
		return null;
	}
	
	@Override
	public void gameLoop() {
		
		Direction newDirection;							// Direction read by keyboard (W, A, S, D)
		LabyrinthMap map = getLabyrinthMap();			// Returns the map
		boolean 	load = true;						// Auxiliary variable - First load
		Scanner	 scanner = new Scanner(System.in);
		
		while(!map.isDone()){
			
			if(load == true) {
				renderManager.render(map);				// Print map on screen
				load = false;
			}
			
			if(renderManager.getInfo()[0] == 0) { 		// Checks if player has lost all lives
				System.out.println("\nGame Over");
				return;
			}

			// Read the keyboard
			System.out.print("\nEntre com um movimento (w, a, s, d): ");
        	newDirection = readCommandFromKeyboard(scanner);
        	map.updateMap(newDirection);
        	renderManager.render(map);
		}
		scanner.close();

		System.out.println("\nParabens! Voce venceu. =D");
	}
}
