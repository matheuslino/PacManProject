package br.com.matheuslino.pacman;

// Difficulty levels enum
public enum Level {
	EASY,
	MEDIUM,
	HARD;
	
	// Returns a random element
	public static Level getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
