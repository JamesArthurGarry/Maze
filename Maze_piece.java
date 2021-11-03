/**
 * Maze_piece which knows about its location
 * The parent class for the pieces which makes up the maze
 */
public class Maze_piece
{   
    // Its location
    private int[] location;
    
    /**
     * Constructor for objects of class Maze_pieces
     * Sets the intial values for the maze piece
     */
    public Maze_piece(int[] position) {
        location = new int[] {position[0], position[1]};
    }
    
    /**
     * An accessor method for
     */
    public int[] getLocation() {
        return location;
    }
}
