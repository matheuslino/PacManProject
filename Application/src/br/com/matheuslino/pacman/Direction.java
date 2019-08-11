package br.com.matheuslino.pacman;

public enum Direction {
	UP,
	DOWN,
	RIGHT,
	LEFT;
	
	// Retorna um elemento aleatorio
	public static Direction getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}