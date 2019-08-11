package br.com.matheuslino.pacman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LabyrinthMapLoader {
	
	// Attribute - Instance of LabyrinthMap Class (dependency relationship)
	private static LabyrinthMap map;
	
	// Only instance allowed for the LabyrinthMapLoader class (Singleton pattern)
	private static final LabyrinthMapLoader instance = new LabyrinthMapLoader(map);

	// Private constructor due to Singleton pattern
	private LabyrinthMapLoader(LabyrinthMap map) {
		LabyrinthMapLoader.map = map;
	}

	// Returns the "instance" object instance
	public static LabyrinthMapLoader getInstance() {
		return instance;
	}
	
	// Random map creation - size proportional to difficulty
	public LabyrinthMap randomMap() {
		
		List<Checkpoint> check = new ArrayList<>();		// Checkpoints list
		List<Wall> 		  wall = new ArrayList<>();		// Walls list
		List<Ghost> 	 ghost = new ArrayList<>();		// ghosts list
		Level 			 level = Level.getRandom();		// Difficulty
		Random 	  			 r = new Random();			// Generate random number
		int 			number = 0;						// r.nextInt()
		int    			  size = 11;					// Matrix size
		int 			   aux = 0;						// Increase in the matrix size
		int 			centro = 0;						// Center point of the map
		char[][]		mapAux = null;					// Stores generated "tray" to be traversed by other methods
		char[][]		mapAtual = null;				// Stores newest map version
		
		// Defines matrix size
		switch (level) {
			case EASY: aux = 0;			// EASY: maps: 11, 15
					   break;
	
			case MEDIUM: aux = 8;		// MEDIUM: maps: 19 e 23
					   break;
	
			case HARD: aux = 16;		// HARD: maps: 27 e 31
					   break;
		   default:
				break;
		}
		
		number = (r.nextInt(5) < 0) ? r.nextInt(5)*-1 : r.nextInt(5);		// Positive random number
		
		while(number != 0 && number != 4) {									// Ensures the it'll be 0 or 4 
			number = (r.nextInt(5) < 0) ? r.nextInt(54)*-1 : r.nextInt(5);
		}
		
		size += number+aux;									// Update map size according to difficulties
		mapAux = new char[size][size];						// Initializes auxiliary matrix (map)
		mapAtual = new char[size][size];					// Initializes current matrix (map)
		centro = (int) Math.floor((size-2)/2);				// Rounds to lowest in case of decimal number
		
		PacMan p = new PacMan(size-4,size-2);				// PacMan begins at the bottom right (project decision)
		
		// Add ghosts to the map center point
		Ghost  gRandom = new Random1(centro+1, centro+1);
		Ghost gEvasive = new Evasive(centro+1, centro+1);
		Ghost  gJumper = new Jumper (centro+1, centro+1);
		Ghost gPursuer = new Pursuer(centro+1, centro+1);
		ghost.add(gRandom);
		ghost.add(gEvasive);
		ghost.add(gJumper);
		ghost.add(gPursuer);
		
		// Add walls at the borders (business rule)
		for(int i=0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				
				mapAux[i][j]   = ' ';		// Fills auxiliary matrix with 'empty char'
				mapAtual[i][j] = ' ';		// Fills current map with 'empty char'
				
				if(i == 0 || i == (size-1) || j == 0 || j == (size-1)) {
					
					mapAux[i][j] = 'x';
					mapAtual[i][j] = 'x';
					
					Wall w = new Wall(j,i);
					wall.add(w);
				}
			}
		}
		
		Wall w;		// instance of Wall
		
		// Reads matrix to place walls
		for(int i = 1; i<size-1; i++) {
			for(int j = 1; j<size-1; j++) {
				
				// Add interspersed walls (board)
				if((i%2 == 0 || j%2 == 0)) {
					
					// Certifies that it won't change the central region (birth of ghosts)
					if(!((i==centro   && j==centro) || (i==centro   && j==centro+1) || (i==centro   && j==centro+2) ||
						 (i==centro+1 && j==centro) || (i==centro+1 && j==centro+1) || (i==centro+1 && j==centro+2) ||
						 (i==centro+2 && j==centro) || (i==centro+2 && j==centro+1) || (i==centro+2 && j==centro+2))) {
							mapAux[i][j] = 'x';
							mapAtual[i][j] = 'x';
							w = new Wall(j,i);
							wall.add(w);
					}
				}
			}
		}

		// Reads matrix to free paths
		for(int i = 1; i<size-1; i++) {
			for(int j = 1; j<size-1; j++) {
				
				if(mapAux[i-1][j-1] == 'x' || mapAux[i-1][j+1] == 'x') {	// There is a diagonal wall, so remove the intermediate
					
					// Certifies that it won't change the central region (birth of ghosts)
					if(!((i==centro   && j==centro) || (i==centro   && j==centro+1) || (i==centro   && j==centro+2) ||
						 (i==centro+1 && j==centro) || (i==centro+1 && j==centro+1) || (i==centro+1 && j==centro+2) ||
						 (i==centro+2 && j==centro) || (i==centro+2 && j==centro+1) || (i==centro+2 && j==centro+2))) {
							for(int k=0; k<wall.size(); k++) {
								Wall obj = wall.get(k);
								if(obj.getY() == i && obj.getX()==j) {
									mapAtual[i][j] = ' ';
									wall.remove(obj);
									break;	
								}
							}
					}
				}
			}
		}

		// Reads matrix to complete random positions (longer walls)
		for(int i = 1; i<size-1; i++) {
			for(int j = 1; j<size-1; j++) {
				if((i%2 == 0 || j%2 == 0)) {
					if(mapAtual[i][j] != 'x' && i!= 1 && j!=1 &&	// It doesn't check first rows and columns (project decision)
					   i!=size-2 && j!=size-2) {					
						
						if(!((i==centro   && j+1==centro)||			// It doesn't fill left of central region
						    (i==centro+1 && j+1==centro) ||			// It doesn't fill left of central region
						    (i==centro+2 && j+1==centro) ||			// It doesn't fill left of central region
						    (i==centro   && j==centro+3) ||			// It doesn't fill left of central region
							(i==centro+1 && j==centro+3) ||			// It doesn't fill left of central region
							(i==centro+2 && j==centro+3) ||			// It doesn't fill left of central region
							(i==centro-1 && j==centro)   ||			// It doesn't fill above of central region
							(i==centro-1 && j==centro+1) ||			// It doesn't fill above of central region
							(i==centro-1 && j==centro+2) ||			// It doesn't fill above of central region
							(i==centro+3 && j==centro)   ||			// It doesn't fill above of central region
							(i==centro+3 && j==centro+1) ||			// It doesn't fill above of central region
							(i==centro+3 && j==centro+2) ||			// It doesn't fill above of central region
							(i==centro   && j==centro)   ||			// Certifies that it won't change the central region (birth of ghosts)
							(i==centro   && j==centro+1) || 
							(i==centro   && j==centro+2) ||
							(i==centro+1 && j==centro)   ||
							(i==centro+1 && j==centro+1) ||
							(i==centro+1 && j==centro+2) ||
							(i==centro+2 && j==centro)   ||
							(i==centro+2 && j==centro+1) ||
							(i==centro+2 && j==centro+2)))
						{
							if(r.nextInt()%4==0) {
								mapAtual[i][j] = 'x';
								w = new Wall(j,i);
								wall.add(w);
							}
						}
					}
				}
			}
		}
		
		// Correcting fenced positions
		for(int i = 1; i<size-1; i++) {
			for(int j = 1; j<size-1; j++) {
				if(mapAtual[i][j] != 'x' && i!= 1 && j!=1 && i!=size-2 && j!=size-2) {		// It doesn't check first rows and columns (project decision)
					if(mapAtual[i][j-1]=='x' && mapAtual[i-1][j]=='x' &&
					   mapAtual[i][j+1]=='x' && mapAtual[i+1][j]=='x') {
						Direction direcao = Direction.getRandom();
						if(direcao == Direction.UP) {
							mapAtual[i-1][j]=' ';
							for(int k=0; k<wall.size(); k++) {
								Wall obj = wall.get(k);
								if(obj.getY() == i-1 && obj.getX()==j) {
									wall.remove(obj);
								}
							}
						}
						else if(direcao == Direction.RIGHT) {
							mapAtual[i][j+1]=' ';
							for(int k=0; k<wall.size(); k++) {
								Wall obj = wall.get(k);
								if(obj.getY() == i && obj.getX()==j+1) {
									wall.remove(obj);
								}
							}
						}
						else if(direcao == Direction.LEFT) {
							mapAtual[i][j-1]=' ';
							for(int k=0; k<wall.size(); k++) {
								Wall obj = wall.get(k);
								if(obj.getY() == i && obj.getX()==j-1) {
									wall.remove(obj);
								}
							}
						}
						else if(direcao == Direction.DOWN) {
							mapAtual[i+1][j]=' ';
							for(int k=0; k<wall.size(); k++) {
								Wall obj = wall.get(k);
								if(obj.getY() == i+1 && obj.getX()==j) {
									wall.remove(obj);
								}
							}
						}
						
					}
				}
			}
		}
		
		// Add walls in central region (business rule)
		w = new Wall(centro,centro);
		wall.add(w);
		w = new Wall(centro,centro+1);
		wall.add(w);
		w = new Wall(centro,centro+2);
		wall.add(w);
		w = new Wall(centro+1,centro+2);
		wall.add(w);
		w = new Wall(centro+2,centro);
		wall.add(w);
		w = new Wall(centro+2,centro+1);
		wall.add(w);
		w = new Wall(centro+2,centro+2);
		wall.add(w);
		
		// Update current map
		for(Wall w1:wall) {
			mapAtual[w1.getY()][w1.getX()] = 'x';
		}

		// Add checkpoints to empty positions (except in the PacMan and ghosts origin location)
		for(int i = 1; i<size-1; i++) {
			for(int j = 1; j<size-1; j++) {
				if(mapAtual[i][j] != 'x') {
					if(!(i==p.getY() && j==p.getX()) &&
					   !(i==ghost.get(0).getY() && j==ghost.get(0).getX())){
							Checkpoint c = new Checkpoint(j, i);
							check.add(c);
					}
				}
			}
		}
		
		// Initialize map
		LabyrinthMap lab = new LabyrinthMap(p, check, wall, ghost, size, size, level);
		return lab;
	}
	
	// Method responsible for reading a map from a file on disk
	public LabyrinthMap loadMapFromFile(String path) {

		List<Checkpoint> check = new ArrayList<>();
		List<Wall> 		  wall = new ArrayList<>();
		List<Ghost> 	 ghost = new ArrayList<>();
		PacMan 				 p = null;
		Level   		 level = null;
		int 			height = 0;
		int 		     width = 0;

		try {
			FileReader 		  file = new FileReader(path);
			BufferedReader buffer  = new BufferedReader(file);
			String 		  	  line = buffer.readLine();
			String 	  	    info[] = null;
			boolean	   	 flagLevel = false;		// Control variable (reading first line of the text document)
			int c = 0;							
			int l = -1;							// Starts at -1 for disregarding first line of document

			// Read the file while there is data
			while (line != null) {

				if(flagLevel == false) {
	
					info = line.split(":");			// Stores the level in a temporary vector
					
						if(info[1].equals("EASY"))
							level = Level.EASY;
						else if(info[1].equals("MEDIUM"))
							level = Level.MEDIUM;
						else if(info[1].equals("HARD"))
							level = Level.HARD;
						else
							level = Level.EASY; 	// Default - prevents erros if spelling is incorrect

				flagLevel = true;					// Ensures that the first line has been performed
				}
				else {
					for(int i=0; i<line.length(); i++) {

						// If the character read is different from space, create map objects and update indexes
						if(!line.substring(i, i+1).equals(" ")) {
						
							if(line.substring(i, i+1).equals("x") || line.substring(i, i+1).equals("X")) {
								Wall w = new Wall(c, l);
								wall.add(w);
							}
							else if(line.substring(i, i+1).equals("*")) {
								Checkpoint cp = new Checkpoint(c, l);
								check.add(cp);
							}
							else if(line.substring(i, i+1).equals("p") || line.substring(i, i+1).equals("P")) {
								p = new PacMan(c, l);
							}
							else if(line.substring(i, i+1).equals("G") || line.substring(i, i+1).equals("G")) {
								Ghost gRandom = new Random1(c, l);
								Ghost gEvasive = new Evasive(c, l);
								Ghost gJumper = new Jumper(c, l);
								Ghost gPursuer = new Pursuer(c, l);
		
								ghost.add(gRandom);
								ghost.add(gEvasive);
								ghost.add(gJumper);
								ghost.add(gPursuer);
							}
							
							c++;					// Update column index
						}
					}
					width = c;
					c = 0; 							//Update column index
				}
				line = buffer.readLine();			// Read from second to last line
				l++;								// Update line index
				height = l;
			}
			file.close();							// Close file
			LabyrinthMap lab = new LabyrinthMap(p, check, wall, ghost, width, height, level);
			return lab;
			} catch (IOException e) {				// Handle exception
				System.err.printf("Falha ao abrir o arquivo: %s.\n", e.getMessage());
			}
		return null;
	}
	
	// Menu de carregamento
	public LabyrinthMap menu(boolean flag) throws InterruptedException{
		
		Scanner scanner = new Scanner(System.in);
		String 	 option = null;						// Stores the keyboard return
		boolean  aux    = false;					// Control the input text

		if(flag == true) {							// Controls the menu options printing
		
			System.out.println();
			System.out.println(" .==========================================.");
			System.out.println(" | Pacman Game                              |");
			System.out.println(" |==========================================|");
			System.out.println(" | Opcoes:                                  |");
			System.out.println(" |..........................................|");
			System.out.println(" | (a)   Carregar arquivo do mapa(txt)      |");
			System.out.println(" |..........................................|");
			System.out.println(" | (b)   Modo aleatorio                     |");
			System.out.println(" |..........................................|");
			System.out.println(" | (v)   Voltar                             |");
			System.out.println(" |..........................................|");
			System.out.println(" | (q)   Encerrar app                       |");
			System.out.println(" *==========================================*");
		}
		
		do {
			if(aux == false)
				System.out.print("\n  Digite sua escolha: ");
			else
				System.out.print("\n  Opcao invalida! Digite sua escolha: ");

			try {
				option = scanner.next();	// Stores the entered value
				aux = true;					// Changes the message displayed to the player
				
			} catch (InputMismatchException e) {
	            System.out.print("O valor inserido e invalido!");
	        }
		} while(!option.equals("a") && !option.equals("b") && !option.equals("q"));


		// Manages methods call after receive a menu option
		if(option.equals("a")) {
			File f;
			do {
				System.out.print("\n     ----> Digite o caminho completo mapa: ");
				option = scanner.next();											// Stores the entered value
				 f = new File(option);												// Creates object of type File
			}while(!option.equals("q") && !option.equals("q") &&
				   !option.equals("v") && !f.isFile());								// repeat loop while input is incorrect

			if(option.equals("v")) {
				return this.menu(false);
			}
			else if(option.equals("q")) {
				System.out.println("\n -------------------------------------------");
				System.out.println("    Programa finalizado com sucesso!");
				System.out.println(" -------------------------------------------");
				System.exit(0);
			}
			else {
				return loadMapFromFile(option);										// Load map from the disk
			}
		}
		else if(option.equals("b")){
			// Random mode
			return randomMap();
		}
		else if(option.equals("q")){
			System.out.println("\n -------------------------------------------");
			System.out.println("    Programa finalizado com sucesso!");
			System.out.println(" -------------------------------------------");
			System.exit(0);
		}
		scanner.close();
		return null;
	}
}