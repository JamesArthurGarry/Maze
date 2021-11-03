/**
 * A player piece which knows about its location (From its parent class)
 */
public class Maze_player extends Maze_piece
{
    /**
     * Constructor for objects of class Maze_player
     * Sets the intial location for the player
     */
    public Maze_player(int[] location) {
        // Calls the parent class (Maze_piece) constructor method with its location
        super(location);
    }
}
