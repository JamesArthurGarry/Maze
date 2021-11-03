/**
 * A static solver (The path the solvers take through the maze)
 * which knows about its location (From its parent class)
 */
public class Maze_staticSolver extends Maze_piece
{
    /**
     * Constructor for objects of class Maze_dormantSolver
     * Sets the intial values for the static solver
     */
    public Maze_staticSolver(int[] location)
    {
        // Calls the super class (Maze_piece) constructor method with its location
        super(location);
    }
}
