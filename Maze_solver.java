/**
 * A maze solver which knows about its location  (From its parent class)
 * and how many steps it has left
 */
public class Maze_solver extends Maze_piece
{
    // How many steps left before the solver would reach the all possible locations in the maze
    // If this is reached then the maze is unsolveable
    private int stepsLeft;
    
    /**
     * Constructor for objects of class Maze_solver
     * Sets the intial values of the solver
     */
    public Maze_solver(int[] position, int stepsRemaining) {
        // Calls the super class (Maze_piece) constructor method with its location
        super(position);
        stepsLeft = stepsRemaining;
    }
    
    /**
     * An accessor method for steps left
     */
    public int getStepsLeft() {
        return stepsLeft;
    }
}
