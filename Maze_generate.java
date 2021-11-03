import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Generates and returns the maze from the text file
 */
public class Maze_generate extends Maze
{
    /**
     * A dummy constructor to allow the call of its method from its parent class (Maze)
     */
    public Maze_generate(){
    }
    
    /**
     * Generates and returns the maze from the text file
     */
    public Maze_piece[][] generateMazeFromTxtFile(String mazeblueprint) throws IOException {
        // Temporary intialises size and maze
        size = 0;
        maze = new Maze_piece[1][1];
        
        // Pause time
        pause_time = 0;
        
        // Stores start and end positions
        start = new int[]{0,0};
        end = new int[2];
        
        // Load maze blueprint from text file and start to generate the maze pieces from the blueprint
        // Scans through the text file
        try (Scanner blueprint = new Scanner(new File(mazeblueprint))){
            // Intilize size and maze
            if (blueprint.hasNext()){
                size = Integer.valueOf(blueprint.next());
                maze = new Maze_piece[size][size];
            }
            
            // Set pause_time if choosen to have a delay
            if (blueprint.hasNext() && blueprint.next().equals("1")){
                pause_time = 250;
            }
            
            // Loop through text file and generate maze pieces based on found string
            for (int row=0; row<size; row++){
                for (int col=0; col<size; col++){
                    if (blueprint.hasNext()){
                        generatePiece(blueprint.next(),maze,row,col);
                    }
                    else {
                        // If the file is not full fills rest with corridors
                        generatePiece("0",maze,row,col);
                    }
                }
            }
        }
        
        // Return generated maze
        return maze;
    }
    
    /**
     * Generates a random maze with a couple restrictions
     */
    public Maze_piece[][] generateRandomMaze(int dimensions, int wait_time)
    {
        // Intialise blank maze
        maze = new Maze_piece[dimensions][dimensions];
        size = dimensions;
        
        // Pause time
        pause_time = wait_time;
        
        // Generate walls on all edges
        for (int row=0; row<size; row++){ // Left edge
            generatePiece("1",maze,row,0);
        }
        for (int row=0; row<size; row++){ // Right edge
            generatePiece("1",maze,row,size-1);
        }
        for (int col=1; col<(size-1); col++){ // Top edge
            generatePiece("1",maze,0,col);
        }
        for (int col=1; col<(size-1); col++){ // bottom edge
            generatePiece("1",maze,size-1,col);
        }
        
        // Store and generate random vertical position of start and end
        start = new int[]{(int) Math.round(Math.random()*(size-3))+1 ,0}; // Random row not 0 or size-1
        generatePiece("@",maze,start[0],start[1]);
        end = new int[]{(int) Math.round(Math.random()*(size-3))+1,size-1}; // Random row not 0 or size-1
        generatePiece("=",maze,end[0],end[1]);
        
        // Generate the centre of the maze
        for (int row=1; row<(size-1); row++){
            for (int col=1; col<(size-1); col++){               
                // Generate a corridor for adjecent location to the start or end
                if (startEndAdjacent(row,col)){
                    generatePiece("0",maze,row,col); // Corridor
                }
                // Randomly decide to generate a corridor or a wall based on a factor
                else if (Math.random() < 0.5){
                    generatePiece("0",maze,row,col); // Corridor
                }
                else {
                    generatePiece("1",maze,row,col); // Wall
                }
            }
        }

        // Return generated maze
        return maze;
    }
    
    /**
     * Generate a new piece based on given string in position maze[row][col]
     */
    public void generatePiece(String piece, Maze_piece[][] maze, int row, int col)
    {
        if (piece.equals("0")){ // Creates new instance of a corridor
            int[] temp = {row,col};
            maze[row][col] = new Maze_corridor(temp);
        }
        else if (piece.equals("1")){ // Creates new instance of a wall
            int[] temp = {row,col};
            maze[row][col] = new Maze_wall(temp);
        }
        else if (piece.equals("@")){ // Creates new instance of a start
            int[] temp = {row,col};
            maze[row][col] = new Maze_start(temp);
            start[0] = row; start[1] = col; // Storing start postion
        }
        else if (piece.equals("=")){ // Creates new instance of an end
            int[] temp = {row,col};
            maze[row][col] = new Maze_end(temp);
            end[0] = row; end[1] = col; // Storing end postion
        }
        else if (piece.equals("X")){ // Creates new instance of a player
            int[] temp = {row,col};
            maze[row][col] = new Maze_player(temp);
        }
            
    }
    
    /**
     * Determines if a location is near to start or end
     */
    public boolean startEndAdjacent(int row, int col)
    {
        // If location near to start
        //   1
        // @ 1 1
        //   1
        if (maze[row][col-1] instanceof Maze_start){
            return true;
        }
        if (row<(size-1) && maze[row+1][col-1] instanceof Maze_start){
            return true;
        }
        if (row>1 && maze[row-1][col-1] instanceof Maze_start ){
            return true;
        }
        if (col>1 && maze[row][col-2] instanceof Maze_start){
            return true;
        }
        
        // If location near to end
        //   1
        // = 1 1
        //   1
        if (maze[row][col+1] instanceof Maze_end){
            return true;
        }
        if (row<(size-1) && maze[row+1][col+1] instanceof Maze_end){
            return true;
        }
        if (row>1 && maze[row-1][col+1] instanceof Maze_end ){
            return true;
        }
        if (col<(size-2) && maze[row][col+2] instanceof Maze_end){
            return true;
        }
        
        // If not near to either
        return false;
    }
}