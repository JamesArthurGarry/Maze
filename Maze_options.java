import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Creates the option buttons for the maze generation and solving types
 * Start off the maze methods
 */
public class Maze_options extends JFrame
{  
    /**
     * Creates the maze option buttons
     */
    public void mazeOptions()
    {
        // Get content pane
        JFrame frame = new JFrame("Maze options");
        //setTitle("Maze options");
        //setLocation(100, 50);
        Container contents = frame.getContentPane();
        
        // Intialise option panel
        Container optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(1,3));
		
	// Create a dummy maze object to use maze methods
        Maze mazeObject = new Maze();
        
        // Create and add buttons for the panel
        // Button for generating and solving a maze from a text file
        JButton textFileButton = new JButton("Solve text file maze");
            textFileButton.addActionListener(ev -> runMazeGenTextFile());
            optionPanel.add(textFileButton);
        // Button for generating and attempting to solve a random maze until it is solvealbe
        JButton randomMazeButton = new JButton("Solve random maze");
            randomMazeButton.addActionListener(ev -> mazeObject.mazeGenRandom(true,10));
            optionPanel.add(randomMazeButton);
        // Button for doing the above and then let the user solve the maze
        JButton userMazeButton = new JButton("User solve random maze");
            userMazeButton.addActionListener(ev -> mazeObject.mazeGenRandomToSolve(10));
            optionPanel.add(userMazeButton);
        
        // Adding option panel to contents and then packing and displaying it
        contents.add(optionPanel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void runMazeGenTextFile(){
        // Create a dummy maze object to use maze methods
        Maze mazeObject = new Maze();
        mazeObject.mazeGenTextFile();
    }
}