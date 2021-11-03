import java.util.*;
import java.io.IOException;

/**
 * Solves the maze defined its parent class (maze) using the flood solving algorithm
 */
public class Maze_solve extends Maze
{
    /**
     * A dummy constructor to allow the call of its method from its parent class (Maze)
     */
    public Maze_solve(){
    }
    
    /**
     * Solves the maze defined its parent class (maze) using the flood solving algorithm
     */
    public boolean solveMaze(boolean display){
        // Initialising first maze solver(s) at start
        // Pass in intial maze solver with position of start, steps left being the max possible for a maze of size size
        newSolvers(maze,size,new Maze_solver(start, size*size));
        
        // Solving/updating loop
        boolean solved = false;
        boolean stillSolving = true; // If there are any solvers left
        
        // Array to contain all the currently alive solvers (Can hold whole maze of solvers)
        Maze_solver[] solvers = new Maze_solver[size*size];
        int nextIndex;
        
        // Displays first maze visulisation
        MazeView.updateMazeView(maze,"Solving...",1,true);
        
        // Add slight delay so the maze view has time to load and be seen before starting to solve the maze
        delay(500);
        
        // Intilaise steps done for use in the solving loop 
        int stepsDone = 0;
        
        // Continue to update all solvers until thhe solvers have found the exit or all been removed
        while (!solved && stillSolving){
            // Choosen delay
            delay(pause_time);
            
            // Reset variables
            stillSolving = false;
            Arrays.fill(solvers,null); // Clearing array from last update
            nextIndex = 0;
            
            // Collecting all current alive solvers to the array
            for (int row=0; row<size; row++){
                for (int col=0; col<size; col++){
                    // If we find a solver we add it to the solvers array
                    if (maze[row][col] instanceof Maze_solver){
                        // Checks if the current sovler has made it to the end
                        if ((row == end[0]) && (col == end[1])){
                            solved = true;
                            // Reset end position to be of class Maze_end
                            Maze_generate generatePieceObject = new Maze_generate();
                            generatePieceObject.generatePiece("=",maze,end[0],end[1]);
                        }
                        else {
                            solvers[nextIndex] = (Maze_solver) maze[row][col];
                        }
                        nextIndex++;
                        stillSolving = true;
                    }
                }
            }
            
            // Dont update solvers if the maze has been solved or not solveable
            if(!solved && stillSolving){
                // Updating all current alive solvers
                for (int solver_index=0; solver_index<nextIndex; solver_index++){
                    // Gets postion for current solver
                    Maze_solver tempSolver = solvers[solver_index];
                    
                    // Creates new solvers based upon its current position
                    newSolvers(maze, size, tempSolver);
                    
                    // Replaces current location with static solver (In effect removing itself) to prevent backtracking
                    int[] pos = tempSolver.getLocation();
                    maze[pos[0]][pos[1]] = new Maze_staticSolver(pos);
                }
            }
            // Updates the maze display
            String solvingState = "Solving...";
            if (solved){
                solvingState = "Maze solved!";
            }
            else if (!stillSolving){
                solvingState = "Maze not solveable";
            }
            else if (stillSolving){
                stepsDone = (int) Math.abs(solvers[0].getStepsLeft() - Math.pow(size,2));
            }

            MazeView.updateMazeView(maze, solvingState,stepsDone, display);
        }
        // Returns if the maze is solveable or not
        return solved;
    }
    
    /**
     * Intilaises new maze solvers based on current positon and possible positions for the new solvers
     * And excludes going back on itself
     */
    public static void newSolvers(Maze_piece[][] maze, int size, Maze_solver currentSolver){
        int[] currentPos = currentSolver.getLocation();
        int currentStepsLeft = currentSolver.getStepsLeft();
        // Checks to make sure the current solver hasn't run out of steps
        if (currentStepsLeft > 0) {
            // Checks if new possible position is out of the maze, empty (Maze_corridor) or the end of the maze
            if ((currentPos[0] > 0)  &&  (((maze[currentPos[0]-1][currentPos[1]]) instanceof Maze_corridor)
                    || (maze[currentPos[0]-1][currentPos[1]]) instanceof Maze_end)){ // North
                int[] temp = {currentPos[0]-1,currentPos[1]};
                maze[currentPos[0]-1][currentPos[1]] = new Maze_solver(temp, currentStepsLeft-1);
            }
            if ((currentPos[1] < (size-1))  &&  (((maze[currentPos[0]][currentPos[1]+1]) instanceof Maze_corridor)
                    || (maze[currentPos[0]][currentPos[1]+1]) instanceof Maze_end)){ // East
                int[] temp = {currentPos[0],currentPos[1]+1};
                maze[currentPos[0]][currentPos[1]+1] = new Maze_solver(temp, currentStepsLeft-1);
            }
            if ((currentPos[0] < (size-1))  &&  (((maze[currentPos[0]+1][currentPos[1]]) instanceof Maze_corridor)
                    || (maze[currentPos[0]+1][currentPos[1]]) instanceof Maze_end)){ // South
                int[] temp = {currentPos[0]+1,currentPos[1]};
                maze[currentPos[0]+1][currentPos[1]] = new Maze_solver(temp, currentStepsLeft-1);
            }
            if ((currentPos[1] > 0)  &&  (((maze[currentPos[0]][currentPos[1]-1]) instanceof Maze_corridor)
                    || (maze[currentPos[0]][currentPos[1]-1]) instanceof Maze_end)){ // West
                int[] temp = {currentPos[0],currentPos[1]-1};
                maze[currentPos[0]][currentPos[1]-1] = new Maze_solver(temp,currentStepsLeft-1);
            }
        }
    }
    
    /**
     * Pause for a given time in milliseconds
     */
    public static void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}