/**
 * A maze creator and solver
 * James Garry
 * Started on the 16/10/21
 */
import java.io.IOException;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class Maze
{   
    // Global variables for use in generating and solving the maze
    public static Maze_piece[][] maze;
    public static int[] start;
    public static int[] end;
    public static int pause_time;
    public static int size;
    
    // For keyboard input
    public static String direction;
    
    // A graphical view of the simulation.
    public static Maze_view MazeView; 
    
    /**
     * Runs maze method based upon user input
     */
    public static void main(String[] args) {
        // When using Maze_options the Maze_view does not get updates until the method has finished
        //Maze_options optionsObject = new Maze_options();
        //optionsObject.mazeOptions();
                
        // Options for maze methods determined by parameter given to main
        // If no parameters given generate and solve maze from text file
        if (args.length == 0){
            mazeGenTextFile();
        }
        // If first parameter is 0 generate and solve maze from text file
        else if (args[0].equals("0")){
            mazeGenTextFile();
        }
        
        // If first parameter is 1 generate a random maze until one is solveable
        else if (args[0].equals("1")){
            // If only one parameter is given generate mazes of size 10
            if (args.length == 1){
                mazeGenRandom(true,10);
            }
            // If a second parameter is given generate mazes of that size
            else {
                mazeGenRandom(true,Integer.valueOf(args[1]));
            }
        }
        
        // If first parameter is 2 generate a random maze until one is solveable
        // Then allow the user to solve it
        else if (args[0].equals("2")){
            // If only one parameter is given generate mazes of size 10
            if (args.length == 1){
                mazeGenRandomToSolve(10);
            }
            // If a second parameter is given generate mazes of that size
            else {
                mazeGenRandomToSolve(Integer.valueOf(args[1]));
            }
        }
    }
    
    /**
     * Generates a maze from the text file a visually displays the attempt at solving it
     */
    public static void mazeGenTextFile()
    {
        // Creates the view of the maze with different colours for different classes (Maze_pieces)
        MazeView = createMazeView();
        
        // Generating the maze from the text file using a dummy Maze_generateFromTxtFile object
        // in order to use the generateMazeFromTxtFile method
        Maze_generate generateMazeObject = new Maze_generate();
        try {
            maze = generateMazeObject.generateMazeFromTxtFile("maze_blueprint.txt");
        } catch (IOException e){
        }
        
        // Start solving the maze using a dummy solveMazeObject object
        // in order to use the solveMaze method
        Maze_solve solveMazeObject = new Maze_solve();
        solveMazeObject.solveMaze(true);
    }    
    
    /**
     * Generates a random maze until one is solveable
     * Display determines if the flood solving algorithm is displayed
     */
    public static Maze_piece[][] mazeGenRandom(boolean display, int Size)
    {
        // Creates two dummy object in order to use methods from theyre classes
        Maze_generate generateMazeObject = new Maze_generate();
        Maze_solve solveMazeObject = new Maze_solve();
        
        // Creates the view of the maze with different colours for different classes (Maze_pieces)
        MazeView = createMazeView();
        
        // Repetively generate and attempt to solve the random maze until one is solveable
        boolean solved = false;
        while (!solved){
            // Generate a random maze
            maze = generateMazeObject.generateRandomMaze(Size,0);
            // Start solving the maze
            solved = solveMazeObject.solveMaze(display);
        }
        
        // Returns solveable random maze
        return maze;
    }
    
    /**
     * Makes use of the above method and then lets the user to solve it
     */
    public static void mazeGenRandomToSolve(int Size)
    {
        // Retuns a solveable maze which has not visually been solved
        maze = mazeGenRandom(false,Size);
        Maze_generate generateMazeObject = new Maze_generate();
        Maze_solve solveMazeObject = new Maze_solve();
        
        // Clear maze of solvers
        for (int row = 1; row<(size-1); row++){
            for (int col = 1; col<(size-1); col++){
                if (maze[row][col] instanceof Maze_solver || maze[row][col] instanceof Maze_staticSolver){
                    generateMazeObject.generatePiece("0",maze,row,col);
                }
            }
        }
        
        // Add player to the maze
        generateMazeObject.generatePiece("X",maze,start[0],1);
        MazeView.updateMazeView(maze,"Player solving...",1,true);
        
        // Intialises useful variables
        boolean Solved = false;
        direction = " ";
        int steps = 1;
        int currentPos[] = {start[0],1};
        int newPos[] = {start[0],1};
        String solvingState = "Player solving...";
        
        // Loops until the maze has been solved
        while (!Solved){
            // Sets the current position of player to be empty (Corridor)
            generateMazeObject.generatePiece("0",maze,currentPos[0],currentPos[1]);
            
            // Set new possible positions to start of as current position
            // So if the player doesn't need to be moved the player can still be positioned at newPos
            newPos[0] = currentPos[0];
            newPos[1] = currentPos[1];
            
            // Changes the new position for the respective directions
            if (direction.equals("N")){
                newPos[0] = currentPos[0] - 1;
            }
            else if (direction.equals("S")){
                newPos[0] = currentPos[0] + 1;                
            }
            else if (direction.equals("W")){
                newPos[1] = currentPos[1] - 1;
            }
            else if (direction.equals("E")){
                newPos[1] = currentPos[1] + 1;
            }            
            
            // Only if the intended move isn't into a wall or the start is the current position updated
            if (!(maze[newPos[0]][newPos[1]] instanceof Maze_wall) && !(maze[newPos[0]][newPos[1]] instanceof Maze_start)){
                // Only increments steps if new position is differnet to current position
                if ((currentPos[0] != newPos[0]) || (currentPos[1] != newPos[1])){                                    
                    steps++;
                }
                // Updates current position
                currentPos[0] = newPos[0];
                currentPos[1] = newPos[1];
            }
            // If the player has reached the end update maze view one last time before exiting the loop
            if (maze[currentPos[0]][currentPos[1]] instanceof Maze_end){
                Solved = true;
                solvingState = "Maze solved!";
            }
            
            // Generate a player in the new position (Might not have been updated)
            generateMazeObject.generatePiece("X",maze,currentPos[0],currentPos[1]);
            // Update maze view
            MazeView.updateMazeView(maze,solvingState,steps,true);
            // Adds a delay so one key press by the user doesn't last mutliple loops
            // and cause mutliple movements
            solveMazeObject.delay(200);
        }
    }
    
    /**
     * Determines which key has been pressed and if the direction
     * of the player piece needs to be updated
     */
    public void keyPressed(KeyEvent e)
    {
        // Gets the key code for which key caused the keyPressed event
        int key = e.getKeyCode();
        
        // Sets the direction based upon which key was pressed
        if (key == KeyEvent.VK_LEFT) {
            direction = "W";
        }
        else if (key == KeyEvent.VK_RIGHT) {
            direction = "E";
        }
        else if (key == KeyEvent.VK_UP) {
            direction = "N";
        }
        else if (key == KeyEvent.VK_DOWN) {
            direction = "S";
        }
    }
    
    /**
     * Determines which key has been released and if the direction
     * of the player piece needs to be updated
     */
    public void keyReleased(KeyEvent e)
    {
        // Gets the key code for which key caused the keyReleased event
        int key = e.getKeyCode();
        
        // Resets the direction to be " " if one of the direction keys has been realsed
        if (key == KeyEvent.VK_LEFT) {
            direction = " ";
        }
        else if (key == KeyEvent.VK_RIGHT) {
            direction = " ";
        }
        else if (key == KeyEvent.VK_UP) {
            direction = " ";
        }
        else if (key == KeyEvent.VK_DOWN) {
            direction = " ";
        }
    }
    /**
     * Creates and returns a maze view object
     */
    public static Maze_view createMazeView()
    {
        // Creates the view of the maze with different colours for different classes (Maze_pieces)
        MazeView = new Maze_view(100, 100);
        MazeView.setColour(Maze_wall.class, Color.BLACK);
        MazeView.setColour(Maze_corridor.class, Color.WHITE);
        MazeView.setColour(Maze_start.class, Color.GREEN);
        MazeView.setColour(Maze_end.class, Color.RED);
        MazeView.setColour(Maze_solver.class, Color.BLUE);
        MazeView.setColour(Maze_staticSolver.class, Color.CYAN);
        MazeView.setColour(Maze_player.class, Color.ORANGE);
        
        // Returns the maze view object
        return MazeView;
    }
}