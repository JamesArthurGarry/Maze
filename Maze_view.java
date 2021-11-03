import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creates a visulisation of the maze using java.awt classes
 */
public class Maze_view extends JFrame
{
    // Color used for objects that have no defined color (Just a precaution)
    private static final Color UNKNOWN_COLOR = Color.gray;
    
    // A map for storing colours for the Maze_piece
    private Map<Class, Color> colours;
    
    // A global mazeView object
    private MazeView mazeViewPanel;
    
    // A global JPanel for the current solving state output
    private JLabel solvingStateLabel;
    
    
    // A global JPanel for the current step output
    private JLabel stepCountLabel;
    
    /**
     * Constructor to create/intialise the maze view panel
     */
    public Maze_view(int height, int width)
    {
        // Intialise colours hashmap
        colours = new LinkedHashMap<>();
        
        // Intialise maze view
        setTitle("Maze flood solving algorithm");
        setLocation(100, 50);
        Container contents = getContentPane();
        
        // Create and add mazeViewPanel to the content pane in CENTER location
        mazeViewPanel = new MazeView(height, width);
        contents.add(mazeViewPanel, BorderLayout.CENTER);
        
        // Create and add infoPanel to the content pane in SOUTH location
        contents.add(createInfoPanel(), BorderLayout.SOUTH);
        
        // Adds the keyboard listener to the display (So it can listen to keyboard events)
        addKeyListener(new keyboardinput());
        
        // Packs the container and its contents so it's ready to be displayed to the user
        pack();
        // Displays to the user
        setVisible(true);
    }
    
    /**
     * Creates and returns the info panel to the constructor
     */
    public Container createInfoPanel()
    {
        // Intialise label for solving state
        solvingStateLabel = new JLabel("Solving...");
        // Set text to be white and background to be black and not transparent
        solvingStateLabel.setOpaque(true);
        solvingStateLabel.setBackground(Color.BLACK);
        solvingStateLabel.setForeground(Color.WHITE);
        
        // Add a dummy label to seperate the lables
        JLabel seperateLabel = new JLabel(" ");
        seperateLabel.setOpaque(true);
        seperateLabel.setBackground(Color.BLACK);
        
        // Intialise label for step count
        stepCountLabel = new JLabel("Current step: " + 0);
        // Set text to be white and background to be black and not transparent
        stepCountLabel.setOpaque(true);
        stepCountLabel.setBackground(Color.BLACK);
        stepCountLabel.setForeground(Color.WHITE);
        
        // Intilaise and place the two above lables into their container
        // An internal panel is required to allow the two JLabels to be centred
        Container infoPanel = new JPanel();
        Container innerPanel = new JPanel();
        // Add the labels to the innerPanel
        innerPanel.setLayout(new GridLayout(1,3));
        innerPanel.add(solvingStateLabel);
        innerPanel.add(seperateLabel);
        innerPanel.add(stepCountLabel);
        // Add the innerPanel to the infoPanel
        infoPanel.add(innerPanel);
        // Set background to be black
        infoPanel.setBackground(Color.BLACK);
        
        return infoPanel;
    }
    
    /**
     * Updates maze view with new maze, solving state and current step count
     */
    public void updateMazeView(Maze_piece[][] maze, String solvingState, int currentStep, boolean updateMaze)
    {
        // Makes sure the maze view is visibel
        if (!isVisible()){
            setVisible(true);
        }
        
        // Makes sure the maze vew panel is the correct size for the given maze
        int size = maze.length;
        mazeViewPanel.preparePaint(size);
        
        // If the maze is to be updated
        if (updateMaze){
            // 'Paints' the maze onto the maze panel, row by col
            // Using the maze pieces respective colour 
            for (int row = 0; row<size; row++){
                for (int col = 0; col<size; col++){
                    mazeViewPanel.drawPiece(row, col, getColour(maze[row][col].getClass()));
                }
            }
        }
        
        // Updates solving sate so it alternates between 'Solving...' and 'Solving..'
        // solvingState is set to be 'Solving...' by Maze_solve while solving so on even steps its replaced with 'Solving..'
        solvingStateLabel.setForeground(Color.WHITE);
        if (solvingState.equals("Solving...") && (currentStep%2==0)){
            solvingState = "Solving..";
        }
        // If the maze is not solveable change text colour to red
        else if (solvingState.equals("Maze not solveable")) {
            solvingStateLabel.setForeground(Color.RED);
        }
        // If the maze has been solved change text colour to greem
        else if (solvingState.equals("Maze solved!")){
            solvingStateLabel.setForeground(Color.GREEN);
        }
        
        // Update solvingStateLabel and currentStepLabel
        solvingStateLabel.setText(solvingState);
        stepCountLabel.setText("Current step: " + currentStep);
        
        // Paint over old representation of maze with new updated one
        mazeViewPanel.repaint();
    }
    
    /**
     * Define a color to be used for a given class
     */
    public void setColour(Class mazePieceClass, Color colour)
    {
        colours.put(mazePieceClass, colour);
    }

    /**
     * Returns the colour for the given Maze_piece
     */
    private Color getColour(Class mazePieceClass)
    {
        Color colour = colours.get(mazePieceClass);
        if (colour == null){
            // If no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return colour;
        }
    }
    
    /**
     * Provide a graphical view of a sqaure maze. This is 
     * a nested class which defines a custom component for the user interface. This
     * component displays the maze
     */
    private class MazeView extends JPanel
    {
        // Intial global variables for use in MazeView
        private final int GRID_VIEW_SCALING_FACTOR = 6;
        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics graphic;
        private Image mazeImage;

        /**
         * Create a new MazeView component
         */
        public MazeView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again
         */
        public void preparePaint(int Size)
        {
            // If the size has changed
            if (! size.equals(getSize())){
                // Create new image and graphic to display the maze upon
                size = getSize();
                mazeImage = mazeViewPanel.createImage(size.width, size.height);
                graphic = mazeImage.getGraphics();
                
                // Sets the scaling for x and y based of current size of the image
                // If less than 1 set to be the preset of grid view scaling factor
                xScale = (size.width / gridWidth) * Math.round(100/Size);
                if (xScale < 1){
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = (size.height / gridHeight) * Math.round(100/Size);
                if (yScale < 1){
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on the panel in a given color
         */
        public void drawPiece(int y, int x, Color color)
        {
            // Sets the colour of the graphic
            // Then create a rectangle of that colout in the given position with set size
            graphic.setColor(color);
            graphic.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The maze view component needs to be redisplayed. Copy the
         * internal image to screen
         * In essence update the screen
         */
        public void paintComponent(Graphics graphic)
        {
            // If there is an image to 'paint' from 
            if (mazeImage != null){
                Dimension currentSize = getSize();
                // If the image doesn't need to be resized
                if (size.equals(currentSize)){ 
                    // Paint the image onto the graphic to display
                    graphic.drawImage(mazeImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image
                    // Then paint the image onto the graphic to display
                    graphic.drawImage(mazeImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }    
    
    /**
     * Listens for keyboard events
     */
    private class keyboardinput extends KeyAdapter {
        // When a key is pressed runs keyPressed in Maze and passes the event
        @Override
        public void keyPressed(KeyEvent e) {
            // Uses dummy object to call Maze method
            Maze mazeObject = new Maze();
            mazeObject.keyPressed(e);
        }
        
        // When a key is released runs keyPressed in Maze and passes the event
        @Override
        public void keyReleased(KeyEvent e) {
            // Uses dummy object to call Maze method
            Maze mazeObject = new Maze();
            mazeObject.keyReleased(e);
        }
    }
}    