import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This is the main class that sets the positions of the blocks and the initial position of the paddle and ball
 * This class creates a loop that calculates the xy coordinates for the ball (still being worked on)
 * Repaints the panel every 10 milliseconds so that movement of the ball visually changes
 * @author cdresdner
 *
 */
public class AnimationMain {

	private static final int SCREEN_WIDTH = 840; //The width of the screen
	private static final int SCREEN_HEIGHT = 700; //The height of the screen
	
	private static final Color[] ROW_COLORS = new Color[] {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, //The colors of every two rows of blocks
			Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW};
	private static final int[] ROW_POINTS = new int[] {7, 7, 5, 5, 3, 3, 1, 1}; //Sets the point values for each block row
	
	/**
	 * The main method which is the entry point for the game (no args are needed)
	 * @param args
	 */
    public static void main(String[] args) {
    
    	Clip glass = null;
    	
    	try {
	    	AudioInputStream sample;
	    	File soundFile = new File("/Users/cdresdner/git/CSFinalProject/GlassBreak.wav");
	        //URL defaultSound = getClass().getResource("/Users/cdresdner/git/CSFinalProject/bin/GlassBreak.mp3");
	        sample = AudioSystem.getAudioInputStream(soundFile);
	        glass = AudioSystem.getClip();
	        glass.open(sample);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	Paddle paddle = new Paddle(); //Creates the paddle
    	int paddleX = (SCREEN_WIDTH/2) - (paddle.getWidth()/2); //Sets the initial x coordinate for the paddle (places the paddle in the center of the screen)
    	int paddleY = SCREEN_HEIGHT - 120; //Sets the initial y coordinate for the paddle (120 pixels from the bottom of the screen)
    	
    	paddle.setCoordinates(paddleX, paddleY); //Sets paddleX and paddleY into the paddle
    	
        Ball1 ball1 = new Ball1(); //Creates the ball
        
        int xPos = 0; //Sets the x coordinate for the first block
        int yPos = 60; //Sets the y coordinate for the first block
        Block[][] blocks = new Block[8][14]; //Sets an initial empty array to hold blocks (8 rows by 14 columns)
        for (int i = 0; i < 8; ++i) { //Loops through each row of blocks
        	for (int j = 0; j < 14; ++j) { //Loops through each column of blocks
                Block block = new Block(glass); //Creates a new block
                block.setCoordinates(xPos, yPos); //Sets xPos and yPos into the current block
                block.setPoints(ROW_POINTS[i]); //Set the amount of points for the current block
                block.setFillColor(ROW_COLORS[i]); //Sets the color for the current block
                xPos += 60; //Increments xPos for the next block to be drawn
                blocks[i][j] = block; //Assigns the block to the array
        	}
        	yPos += 15; //Increments yPos for a new row
        	xPos = 0; //Sets xPos back to 0 for a new row
        }
        
        //Creates the animation panel
        AnimationPanel animationPanel = 
        		new AnimationPanel(new BorderLayout(), SCREEN_WIDTH, SCREEN_HEIGHT, ball1, blocks, paddle);
       
        //Build the user interface framework
        buildGUI(animationPanel);
        
        for (int i = 500; i > -10000; i -= 3) { //Initial loop to generate ball movement (Temporary; this just moves the ball in a straight line)
        	//Still need to 
        	ball1.setCoordinates(395, i);
            animationPanel.repaint();
            Rectangle r2 = new Rectangle(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
            
            boolean blockHit = false;
            
            for (int j = 0; j < blocks.length; j++) { //Loop through each row of blocks
            	for (int k = 0; k < blocks[j].length; k++) { //Loop through each column of blocks
            		Block block = blocks[j][k];
            		Rectangle r1 = new Rectangle(block.getX(), block.getY(), block.getWidth(), block.getHeight());
                    if (touches(r1, r2)) {
                    	block.blockHit();
                    	blockHit = true;
                    	break;
                    }
            	}
            	
            	if (blockHit) {
            		break;
            	}
            	
            }
            
            

            
            try {
                Thread.sleep(10); //Repaints the panel every 10 milliseconds
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace(); //Throws an interrupted exception (this should not happen)
            }
        }
    }
    
    /**
     * Embeds the animation panel inside of a swing frame
     * @param animationPanel
     */
    public static void buildGUI(AnimationPanel animationPanel) {
        JFrame frame = new JFrame("Breakout"); //Sets the title for the frame
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() { //Sets the data for the frame
                        frame.setContentPane(animationPanel);
                        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                }
        );
    }
    
    /**
     * This method determines whether the ball has made contact with a block
     * @param rBlock - rectangle for the block
     * @param rBall - rectangle for the ball (hitbox)
     * @return
     */
    private static boolean touches(Rectangle rBlock, Rectangle rBall) {
        if ((rBall.x <= rBlock.x + rBlock.width) && (rBall.x + rBall.width >= rBlock.x) && 
        		(rBall.y < rBlock.y + rBlock.height) && (rBall.y + rBall.height >= rBlock.y)) {
        	return true;
        }
        else {
        	return false;
        }
    }
}