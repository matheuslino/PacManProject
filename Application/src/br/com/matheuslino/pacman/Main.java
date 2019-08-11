/*
  ==========================================================
 |  PacMan Project
 | ---------------------------------------------------------
 |  Versão: 1.0
 |  Ano: 2019
 |  Developed by: Matheus Lino
 |  LinkedIn: https://br.linkedin.com/in/matheus-lino
 |  Site: https://matheuslino.com.br
 |  GitHub:   https://github.com/matheuslino/
  ==========================================================
*/

package br.com.matheuslino.pacman;

import br.com.matheuslino.pacman.game.GameEngine;
import br.com.matheuslino.pacman.game.TextEngine;

public class Main {

	private static void runGame(GameEngine gameEngine) {
		gameEngine.gameLoop();
	}
	
	public static void main(String [] args)  throws Exception {
		LabyrinthMap map = LabyrinthMapLoader.getInstance().menu(true);
		runGame(new TextEngine(map));		// Run the game textually
	}
}