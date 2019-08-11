package br.com.matheuslino.pacman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.matheuslino.pacman.game.LabyrinthObjectVisitor;

public class LabyrinthMap {
	
	// Attributes
	private PacMan pacman;
	private Level level;											// Difficulty level
	private List<Checkpoint> checkpoints = new ArrayList<>();
	private List<Wall> walls = new ArrayList<>();
	private List<Ghost> ghosts = new ArrayList<>();
	private int width;												// Map width
	private int height;												// Map height
	private int[][] grafo;											// Adjacency matrix containing graph's elements
	private int timerCheckSpecial;									// "Timer" - Special checkpoint duration
	private int pos[] =  new int[3];								// Stores the graph's vertices corresponding to evasive,
																	// pursuer and PacMan, respectively

	// Construtor (protected visibility)
	protected LabyrinthMap(PacMan pacman, List<Checkpoint> checkpoints, List<Wall> walls, List<Ghost> ghosts, int width,
						   int height, Level level) {
		
		// Local variable, just to generate a random number
		Random r = new Random();

		// Project decision: 10% of special checkpoints
		for(int i=0; i<Math.ceil(checkpoints.size()*0.1); i++) {
			
			int n = r.nextInt(checkpoints.size());
			
			// Ensures positive random numbers
			if(n<0)
				n = n*(-1);
			
			// Set special checkpoints
			if(!checkpoints.get(n).isSpecial()) {
				checkpoints.get(n).setSpecial(true);
			}else {
				if(i>0) {
					i--;
				}
			}	
		}
		
		this.pacman = pacman;
		this.checkpoints = checkpoints;
		this.walls = walls;
		this.ghosts = ghosts;
		this.width = width;
		this.height = height;
		this.level = level;
		this.grafo = new int[checkpoints.size()+2][checkpoints.size()+2];		// free positions for movement: checkpoints +
																				// PacMan and Ghost start positions
	}
		
	// Return Difficulty level
	public Level getLevel() {
		return level;
	}
	
	// Call the accept method for each map element, passing a visitor as argument
	public void accept(LabyrinthObjectVisitor visitor) {
		for(Wall wall : walls) {
			wall.accept(visitor);
		}
		for(Checkpoint checkpoint : checkpoints) {
			checkpoint.accept(visitor);
		}
		for(Ghost ghost : ghosts) {
			ghost.accept(visitor);
		}
		pacman.accept(visitor);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	// Manage the ghosts movement
	private void moveGhosts(List<Wall> walls) {
		
		// Definitions of local use variables (only)
		Random1	 	  random  = (Random1) ghosts.get(0);	// Returns the random ghost
		Evasive 	 evasive  = (Evasive) ghosts.get(1);	// Returns the evasive ghost
		Jumper 	 	  jumper  = (Jumper)  ghosts.get(2);	// Returns the jumper ghost
		Pursuer 	 pursuer  = (Pursuer) ghosts.get(3);	// Returns the pursuer ghost
		Direction dir_evasive = null;						// Evasive ghost direction
		Direction dir_pursuer = null;						// Pursuer ghost direction
		int[] 	coord_evasive = null;						// coord[x][y] - evasive ghost coordinates
		int[] 	 next_evasive = null;						// next [x][y] - coordinates of the next vertex
		int[] 	coord_pursuer = null;						// coord[x][y] - pursuer ghost coordinates
		int[] 	 next_pursuer = null;						// next [x][y] - coordinates of the next vertex
		int 	nextElement_e = -1;							// Vertex of the next element to be visited (evasive)
		int     nextElement_p = -1;							// Vertex of the next element to be visited (pursuer)
		
		// evasive ghost direction - Dijkstra Algorithm
		nextElement_e = Dijkstra.shortWay(grafo,pos[0],pointToVertex(midpoint().getX(), midpoint().getY()));
		next_evasive = (nextElement_e >= 0) ? vertexToPoint(nextElement_e) : null;
		coord_evasive = vertexToPoint(pos[0]);
		
		if(next_evasive != null && coord_evasive != null) {
			if(coord_evasive[1] == next_evasive[1] && next_evasive[0] > coord_evasive[0]) {				// Checks if it should move right
				dir_evasive = Direction.RIGHT;
			}
			else if(coord_evasive[1] == next_evasive[1] && next_evasive[0] < coord_evasive[0]) {		// Checks if it should move left
				dir_evasive = Direction.LEFT;
			}
			else if(coord_evasive[0] == next_evasive[0] && next_evasive[1] < coord_evasive[1]) {		// Checks if it should move up
				dir_evasive = Direction.UP;
			}
			else if(coord_evasive[0] == next_evasive[0] && next_evasive[1] > coord_evasive[1]) {		// Checks if it should move down
				dir_evasive = Direction.DOWN;
			}
		}

		// Pursuer ghost direction - Dijkstra Algorithm
		nextElement_p = Dijkstra.shortWay(grafo,pos[1],pos[2]);
		next_pursuer  = (nextElement_p >=0) ? vertexToPoint(nextElement_p) : null;
		coord_pursuer = vertexToPoint(pos[1]);
		
		if(next_pursuer != null && coord_pursuer != null) {
			if(coord_pursuer[1] == next_pursuer[1] && next_pursuer[0] > coord_pursuer[0]) {				// Checks if it should move right
				dir_pursuer = Direction.RIGHT;
			}
			else if(coord_pursuer[1] == next_pursuer[1] && next_pursuer[0] < coord_pursuer[0]) {		// Checks if it should move left
				dir_pursuer = Direction.LEFT;
			}
			else if(coord_pursuer[0] == next_pursuer[0] && next_pursuer[1] < coord_pursuer[1]) {		// Checks if it should move up
				dir_pursuer = Direction.UP;
			}
			else if(coord_pursuer[0] == next_pursuer[0] && next_pursuer[1] > coord_pursuer[1]) {		// Checks if it should move down
				dir_pursuer = Direction.DOWN;
			}
		}
		
		// Move ghosts
		random.moveRandom(walls);
		jumper.moveJumper(walls, checkpoints);
		if(dir_evasive != null)	evasive.move(dir_evasive, walls);
		if(dir_pursuer != null)	pursuer.move(dir_pursuer, walls);
	}
	
	// Checks collision between ghosts and PacMan
	private void ghostCollision(){
		for(Ghost ghost : ghosts) {
			if(pacman.isSameCoordinates(ghost)) {									// Checks collision between a ghost and PacMan
				if(timerCheckSpecial>0) {											// Ensures PacMan ate a special checkpoint
					pacman.setScore(pacman.getScore()+100);							// Increases 100 points by killing a ghost
					ghost.changeCoordinates(ghost.getInitialCoordinate().getX(),	// Moves dead ghost to origin
											ghost.getInitialCoordinate().getY());
				}else {
					pacman.setLife(pacman.getLife()-1);								// Decreases the PacMan life
					pacman.changeCoordinates(pacman.getInitialCoordinate().getX(),	// Moves PacMan to origin
											 pacman.getInitialCoordinate().getY());
					for(Ghost g : ghosts) {
						g.changeCoordinates(g.getInitialCoordinate().getX(),		// Moves ghosts to origin
							g.getInitialCoordinate().getY());
					}
				}
				break;
			}
		}
	}

	private void checkpointUpdate() {
		for(Checkpoint checkpoint : checkpoints) {					// visits each list element
			if(pacman.isSameCoordinates(checkpoint)) {				// Checks if PacMan has earned a checkpoint
				if(!checkpoint.isConquered()) {
					checkpoint.conquer();
					if(checkpoint.isSpecial()) {
						timerCheckSpecial = 15;
						pacman.setScore(pacman.getScore()+10);		// Increases 10 points per special checkpoint
					}else {
						pacman.setScore(pacman.getScore()+1);		// Increases 1 point per commom checkpoint
					}
					if((pacman.getScore()%10000)==0) {				// Increases 1 life by earning 10000 points
						pacman.setLife(pacman.getLife()+1);
					}
				}
			}
		}
	}
	
	// Checks if the map is done (all checkpoints earned)
	public boolean isDone() {
		for(Checkpoint checkpoint : checkpoints) {
			if(!checkpoint.isConquered()) {
				return false;
			}
		}
		return true;
	}

	// Adjacency matrix used to create the graph with route map
	private void adjacenceMatrice(){
		
		char[][] aux = new char[getHeight()][getWidth()];	// temporary map
		int cont = -1;										// local counter
		
		for (int i = 0; i < aux.length; i++) {
			for (int j = 0; j < aux[0].length; j++) {
				aux[i][j] = ' ';							// Fills matrix with 'empty char'
			}
		}
			
		for(Wall wall : walls) {							// Fills positions with wall
			aux[wall.getY()][wall.getX()] = 'x';
		}
		
		for (int i = 1; i < aux.length-1; i++) {			// Creation algorithm. Walls must be placed at the edges (project decision)
			for (int j = 1; j < aux[0].length-1; j++) {
				
				if(aux[i][j] != 'x') {						// Ensures that reading will be performed in positions without a wall
					
					cont++;											// Increases adjacency table row (vertex)
				
					if(ghosts.get(1).isSameCoordinates(j, i)) {		// Checks if it's reading the evasive ghost position
						pos[0] = cont;								// Stores the vertex in pos vector
					}
					if(ghosts.get(3).isSameCoordinates(j, i)) {		// Checks if it's reading the pursuer ghost position
						pos[1] = cont;								// Stores the vertex in pos vector
					}					
					if(pacman.isSameCoordinates(j, i)) {			// Checks if it's reading the PacMan position
						pos[2] = cont;								// Stores the vertex in pos vector
					}
					
					if(aux[i-1][j]!='x') {							// Checks if there is a wall above of read position
						
						int x = 0;									// Calculates the number of walls between the elements I[i-1][j] e I[i][j] 
						int elements = 0;							// Calculates the number of positions between the elements I[i-1][j] e I[i][j]
						
						for(int k = i-1; k < i; k++) {
							for(int l = j; l < aux[0].length-1; l++) {
								elements += 1;
								if(aux[k][l] == 'x')
									x += 1;
							}
						}
						
						if(j > 1) {
							for(int k = i; k <= i; k++) {
								for(int l = 1; l < j; l++) {
									elements += 1;
									if(aux[k][l] == 'x')
										x += 1;
								}
							}
						}
						grafo[cont][cont-(elements-x)] = 1;
					}
					
					if(aux[i+1][j]!='x') {							// Checks if there is a wall below of read position
						
						int x = 0;									// Calculates the number of walls between the elements I[i][j] e I[i+1][j] 
						int elements = 0;							// Calculates the number of positions between the elements I[i][j] e I[i+1][j]
						
						for(int k = i; k <= i; k++) {
							for(int l = j; l < aux[0].length-1; l++) {
								elements += 1;
								if(aux[k][l] == 'x')
									x += 1;
							}
						}
						
						if(j > 1) {
							for(int k = i+1; k <= i+1; k++) {
								for(int l = 1; l < j; l++) {
									elements += 1;
									if(aux[k][l] == 'x')
										x += 1;
								}
							}
						}
						grafo[cont][cont+(elements-x)] = 1;
					}
					if(aux[i][j-1]!='x') {							// Checks if there is a wall to the left of read position
						grafo[cont][cont-1] = 1;
					}
					if(aux[i][j+1]!='x') {							//  Checks if there is a wall to the right of read position
						grafo[cont][cont+1] = 1;
					}
				}
			}
		}
	}

	// Returns the point corresponding to a given adjacency matrix vertex
		private int[] vertexToPoint(int v){

			int 	 cont = -1;										// vertices counter
			char[][]  aux = new char[getHeight()][getWidth()];		// auxiliary map (temporary)
			int[] 	point = new int[2];								// Stores x and y coordinates to be returned (p[x][y])

			// Aux fill
			for (int i = 0; i < aux.length; i++) {
				for (int j = 0; j < aux[0].length; j++) {
					 if(pacman.getX()==j && pacman.getY()==i) {
						aux[i][j] = 'P';
					}else {
					aux[i][j] = ' ';
					}
				}
			}

			// Aux fill
			for(Wall wall : walls) {
				aux[wall.getY()][wall.getX()] = 'X';
			}

			// Verification Logic
			for (int i = 1; i < aux.length-1; i++) {
				for (int j = 1; j < aux[0].length-1; j++) {
					if(aux[i][j]!='X') {
						cont++;
					}
					if(cont == v) {
						point[0] = j;
						point[1] = i;
						return point;
					}
				}
			}
			return null;
		}

		// Returns the vertex corresponding to a given map point (except borders, according to defined business rule)
		private int pointToVertex(int x, int y) {

			int 	cont =- 1;										// vertices counter
			char[][] aux = new char[getHeight()][getWidth()];		// Auxiliary map (temporary)

			// Aux fill
			for (int i = 0; i < aux.length; i++) {
				for (int j = 0; j < aux[0].length; j++) {				
					aux[i][j] = ' ';
				}
			}

			// Aux fill
			for(Wall wall : walls) {
				aux[wall.getY()][wall.getX()] = 'X';
			}

			// Verification Logic
			for (int i = 1; i < aux.length-1; i++) {
				for (int j = 1; j < aux[0].length-1; j++) {
					if(aux[i][j]!='X') {
						cont++;
					}
					if(x==j && y==i) {
						return cont;
					}
				}
			}
			return -1;
		}
		
		private Coordinate midpoint(){
			Coordinate aux = new Coordinate(0, 0);			// Baricenter of the triangle (biggest distance between the gosts)
															// Formula: G[(xa+xb+xc)/3][(ya+yb+yc)/3]
			Wall 	  aux2 = new Wall(0, 0);
			
			aux.changeCoordinates(Math.round((ghosts.get(0).getX()+ghosts.get(2).getX()+ghosts.get(3).getX())/3),
								  Math.round((ghosts.get(0).getY()+ghosts.get(2).getY()+ghosts.get(3).getY())/3));
			
			aux2.changeCoordinates(aux.getX(), aux.getY());;			// Checks if there is a wall at that coordinate
			if(!walls.contains(aux2)) {
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX()+1,aux.getY());			// Checks if there is a wall to the right
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX()-1,aux.getY());			// Checks if there is a wall to the left
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX(),aux.getY()+1);			// Checks if there is a wall below
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX(),aux.getY()-1);			// Checks if there is a wall above
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX()+2,aux.getY());			// Checks if there is a wall to the right (2 units forward)
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX()-2,aux.getY());			// Checks if there is a wall to the left (2 units backward)
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX(),aux.getY()+2);			// Checks if there is a wall bellow (2 units forward)
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			aux2.changeCoordinates(aux.getX(),aux.getY()-2);			// Checks if there is a wall above (2 units backward)
			if(!walls.contains(aux2)){
				aux.changeCoordinates(aux2.getX(), aux2.getY());
				return aux;
			}
			return aux;
		}

		// Method responsible by updating and map control
		public void updateMap(Direction direction) {

			if(direction != null) {

				// Assemble adjacency matrix (Graph)
				adjacenceMatrice();

				// Special CheckPoint duration control
				if(timerCheckSpecial>0) {
					timerCheckSpecial--;
				}

				pacman.move(direction, walls, ghosts);		// Moves the PacMan
				moveGhosts(walls);							// Moves the ghosts
				ghostCollision();							// Checks collision between ghosts and PacMan
				checkpointUpdate();							// Achieve checkpoints
				
			}
		}
}