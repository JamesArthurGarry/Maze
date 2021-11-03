/**
 * A static wall which knows about its location (From its parent class)
 */
public class Maze_wall extends Maze_piece
{
    /**
     * Constructor for objects of class Maze_wall
     * Sets the intial values for the wall
     */
    public Maze_wall(int[] location) {
        // Calls the parent class (Maze_piece) constructor method with its location
        super(location);
    }
}
