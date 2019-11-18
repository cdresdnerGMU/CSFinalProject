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

public class Game implements Runnable {
	
	private static final int SCREEN_WIDTH = 840; //The width of the screen
	private static final int SCREEN_HEIGHT = 700; //The height of the screen
	
	private static final Color[] ROW_COLORS = new Color[] {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, //The colors of every two rows of blocks
			Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW};
	private static final int[] ROW_POINTS = new int[] {7, 7, 5, 5, 3, 3, 1, 1}; //Sets the point values for each block row

	private Paddle paddle;
	private Ball1 ball1;
	private AnimationPanel animationPanel;
	private Block[][] blocks;
	
	private Thread t = new Thread(this);
	private Boolean gameIsRunning = false;
	
	public Game() {
		
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
    	
    	
    	paddle = new Paddle(); //Creates the paddle
    	int paddleX = (SCREEN_WIDTH/2) - (paddle.getWidth()/2); //Sets the initial x coordinate for the paddle (places the paddle in the center of the screen)
    	int paddleY = SCREEN_HEIGHT - 120; //Sets the initial y coordinate for the paddle (120 pixels from the bottom of the screen)
    	
    	paddle.setCoordinates(paddleX, paddleY); //Sets paddleX and paddleY into the paddle
    	
        ball1 = new Ball1(); //Creates the ball
        
        int xPos = 0; //Sets the x coordinate for the first block
        int yPos = 60; //Sets the y coordinate for the first block
        blocks = new Block[8][14]; //Sets an initial empty array to hold blocks (8 rows by 14 columns)
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
        animationPanel = 
        		new AnimationPanel(new BorderLayout(), SCREEN_WIDTH, SCREEN_HEIGHT, ball1, blocks, paddle, this);
       
        //Build the user interface framework
        buildGUI(animationPanel);
		
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
    
    public boolean gameIsRunning() {
    	return gameIsRunning;
    }
    
    public void startGame() {
    	
    	gameIsRunning = true;
    	t.start();
        
    }

	@Override
	public void run() {
		
		int lineY = paddle.getY() + paddle.getHeight() + 20;
		
		int ballYPos = paddle.getY() - 20;
		int ballXPos = paddle.getX() - 20;
		// i made two different dirs for X and Y b/c if it hits a wall, the Xdir will change but Ydir shouldn't..
		// but if it hits top, both dir should change? maybe theres a better way to do this.
        int ballDirX = 1;
        int ballDirY = 1;
        int maxX = SCREEN_WIDTH;
        //Initial loop to generate ball movement (Temporary; this just moves the ball in a straight line)
        //for (int i = ballStartPos; i > -10000; i -= 3) { 
        
        while (gameIsRunning) {
			ballYPos -= (3 * ballDirY); // since the top is 0, we have to decrement (It's weird)
			ballXPos += (3 * ballDirX);// moving the ball in
			ball1.setCoordinates(395, ballYPos);
			ball1.setCoordinates(ballXPos, ballYPos);// always start the ball at an angle.
			if (ball1.getX() >= maxX) { //if it hits the right most wall, the dirX should change
				ballDirX *= -1;
			}
			if(ball1.getX() <= 0) {//most left wall
				ballDirX *= -1;
			}
            animationPanel.repaint();
            Rectangle r2 = new Rectangle(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
            
            if (ball1.getY() >= lineY) {
            	gameIsRunning = false;
            	break;
            }
            
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
            		ballDirX *= -1;
            		ballDirY *= -1;
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
	
}