import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Game implements Runnable {
	
	private static final int SCREEN_WIDTH = 840; //The width of the screen
	private static final int SCREEN_HEIGHT = 700; //The height of the screen
	private static final int EXTRA_LIFE = 500;
	
	private static final Color[] ROW_COLORS = new Color[] {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, //The colors of every two rows of blocks
			Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW};
	private static final int[] ROW_POINTS = new int[] {4, 4, 3, 3, 2, 2, 1, 1}; //Sets the point values for each block row

	private Paddle paddle;
	private Ball1 ball1;
	private AnimationPanel animationPanel;
	private Block[][] blocks;
	private int blockCount;
	private PowerUpBall tempBall1;
	private PowerUpBall tempBall2;
	
	private int lives = 3;
	private int score = 0;
	private int level = 0;
	
	private int temp_score = 0;
	
	double ballDirY = 1.0;
	double ballDirX = 1.0;
	double ballYPos;
	double ballXPos;
	double ballAngleX = 3;
	double ballAngleY = 3;
	
	
	private Thread t = new Thread(this);
	
	private boolean gameIsRunning;
	private boolean inPlay;
	
	private JFrame frame;
	
	public Game() {
		
    	Clip glass = null;
    	
        String glassBreakPath = this.getClass().getResource("GlassBreak.wav").getPath();
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        if (osName.toLowerCase().indexOf("win") > -1) {
            if (glassBreakPath.startsWith("/")) {
                glassBreakPath = glassBreakPath.substring(1);
            }
            glassBreakPath = glassBreakPath.replace("/", "\\");
        }
    	
    	try {
	    	AudioInputStream sample;
	    	File soundFile = new File(glassBreakPath);
	        sample = AudioSystem.getAudioInputStream(soundFile);
	        glass = AudioSystem.getClip();
	        glass.open(sample);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	paddle = new Paddle(); //Creates the paddle
    	double paddleX = (SCREEN_WIDTH/2) - (paddle.getWidth()/2); //Sets the initial x coordinate for the paddle (places the paddle in the center of the screen)
    	double paddleY = SCREEN_HEIGHT - 120; //Sets the initial y coordinate for the paddle (120 pixels from the bottom of the screen)
    	
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
        		new AnimationPanel(new BorderLayout(), SCREEN_WIDTH, SCREEN_HEIGHT, ball1, tempBall1, tempBall2, blocks, paddle, this);
       
        //Build the user interface framework
        buildGUI(animationPanel);
        
        nextLevel();
		
	}
	
	/**
     * Embeds the animation panel inside of a swing frame
     * @param animationPanel
     */
    public void buildGUI(AnimationPanel animationPanel) {
        frame = new JFrame("Breakout"); //Sets the title for the frame
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
    private boolean touches(Rectangle rBlock, Rectangle rBall) {
        if ((rBall.x <= rBlock.x + rBlock.width) && (rBall.x + rBall.width >= rBlock.x) && 
        		(rBall.y < rBlock.y + rBlock.height) && (rBall.y + rBall.height >= rBlock.y)) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
     /**
     * This method determines whether the ball has made contact with the bottom of a block
     * @param rBlock - rectangle for the block
     * @param rBall - rectangle for the ball (hitbox)
     * @return
     */
    private boolean touchesBottom(Rectangle rBlock, Rectangle rBall) {
    	int rHeight = (int) (rBlock.getHeight() / 2);
    	Rectangle bottomHalf = new Rectangle((int)rBlock.getX(), (int)rBlock.getY() + rHeight, (int)rBlock.getWidth(), rHeight);
        if ((rBall.x <= bottomHalf.x + bottomHalf.width) && (rBall.x + rBall.width >= bottomHalf.x) && 
        		(rBall.y <= bottomHalf.y + bottomHalf.height) && (rBall.y + rBall.height >= bottomHalf.y)) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
    public boolean gameIsRunning() {
    	return gameIsRunning;
    }
	
    /** Method that sets up the game and starts it
    **/
    public void startGame() {
    	
    	ballAngleX = 3;
    	ballAngleY = 3;
    	
		ballYPos = paddle.getY() - 20;
		ballXPos = paddle.getX() + (paddle.getWidth() / 2);
		
		ballDirY = 1;
    	
    	if (!gameIsRunning) {
    		t.start();
    	}
    	gameIsRunning = true;
    	this.inPlay = true;
    	
    }

    public boolean inPlay() {
    	return inPlay;
    }
    
	@Override
	public void run() {
		
		double lineY = paddle.getY() + paddle.getHeight() + 20;
		

		// i made two different dirs for X and Y b/c if it hits a wall, the Xdir will change but Ydir shouldn't..
		// but if it hits top, both dir should change? maybe theres a better way to do this.
        ballDirX = 1.0;
        int maxX = SCREEN_WIDTH;
        //Initial loop to generate ball movement (Temporary; this just moves the ball in a straight line)
        //for (int i = ballStartPos; i > -10000; i -= 3) { 
        
        while (gameIsRunning) {
        	
        	if (this.inPlay) {
	        		
				ballYPos -= (ballAngleY * ballDirY); // since the top is 0, we have to decrement (It's weird)
				ballXPos += (ballAngleX * ballDirX);// moving the ball in
				ball1.setCoordinates(ballXPos, ballYPos);// always start the ball at an angle.
				if (ball1.getX() >= maxX) { //if it hits the right most wall, the dirX should change
					ballDirX *= -1;
				}
				if(ball1.getX() <= 0) {//most left wall
					ballDirX *= -1;
				}
	
	            
	            Rectangle r2 = new Rectangle((int)ball1.getX(), (int)ball1.getY(), (int)ball1.getWidth(), (int)ball1.getHeight()); //ball hitbox
	            Rectangle paddleHitbox = new Rectangle((int)paddle.getX(), (int)paddle.getY(), (int)paddle.getWidth(), (int)paddle.getHeight());
	            if (touches(paddleHitbox, r2)) { //Ball reverses direction when it hits the paddle
	            	changeBallAngle(ball1, paddle);
	            	ballDirY = 1;
	            }
	            
	            if (ballYPos <= 0) { //Ball reverses direction when it hits the ceiling
	            	ballDirY = -1;
	            }
	            
	            if (ball1.getY() >= lineY) { //Ball will reset to its default position and lives will be decremented when it hits the bottom of the screen
	            	lives -= 1;
	            	this.inPlay = false;
	            	ball1.setCoordinates(-15, -15);
	            	
	            	if (lives == 0) {
	            	      //default icon, custom title
	                    int n = JOptionPane.showConfirmDialog(
	                        frame,
	                        "Would you like to play again?",
	                        "Game Over",
	                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	                    if (n == JOptionPane.YES_OPTION) {
	                    	newGame();
	                    }
	                    else {
	                    	System.exit(0);
	                    }
	            	}
	            }
	            
			    if (score == EXTRA_LIFE) {
			    	lives++;
			    }
	            
	            for (int j = 0; j < blocks.length; j++) { //Loop through each row of blocks
	            	for (int k = 0; k < blocks[j].length; k++) { //Loop through each column of blocks
	            		Block block = blocks[j][k];
	            		if (!block.blockWasHit()) {
		            		Rectangle r1 = new Rectangle((int)block.getX(), (int)block.getY(), (int)block.getWidth(), (int)block.getHeight());
		                    if (touches(r1, r2)) {
		    	            	changeBallAngle(ball1, block);
		    	            	if (touchesBottom(r1, r2)) {
		    	            		ballDirY = -1; 
		    	            	}
		    	            	else {
		    	            		ballDirY = 1;
		    	            	}
		    	            	
		                		score += block.getPoints();
		                		temp_score += block.getPoints();

		    	            	if (temp_score >= EXTRA_LIFE){
		    	            		lives += 1; //every time the temp_score reaches 500, the lives is incremented by 1
		    	            		temp_score = temp_score - EXTRA_LIFE; //set back to 0

								}

		                		block.blockHit();
		                		blockCount -= 1;
		                		if (blockCount <= 0) {
		                			nextLevel();
		                		}
		                    }
	            		}
	            	}
	            	
	            }
        	}
        	
        	animationPanel.repaint();

            try {
                Thread.sleep(10); //Repaints the panel every 10 milliseconds
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace(); //Throws an interrupted exception (this should not happen)
            }
        }
      //default icon, custom title
        int n = JOptionPane.showConfirmDialog(
            frame,
            "Would you like to play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (n == JOptionPane.YES_OPTION) {
        	
        }
        else {
        	System.exit(0);
        }
	}
	
	public int getLives() {
		return lives;
	}

	public int getScore() {
		return score;
	}
	
	private void changeBallAngle(Ball1 ball1, GameObject gameObj) {
        double ballCenter = ball1.getX() + (ball1.getWidth() / 2);
        double paddleCenter = gameObj.getX() + (gameObj.getWidth() / 2);
        double paddleWidth = gameObj.getWidth();
        double paddleHalf = paddleWidth / 2.0;
        double paddleQuarter = paddleWidth / 4.0;
        double paddleFiveFourths = paddleQuarter * 5;
        double ballOnPaddlePos = ballCenter - gameObj.getX();
       
        if (ballOnPaddlePos < 0) {
            ballOnPaddlePos = 0;
        }
        if (ballOnPaddlePos > paddleWidth) {
            ballOnPaddlePos = paddleWidth;
        }
       
        ballAngleX = Math.abs((ballOnPaddlePos - paddleHalf) * 0.2);
       
        ballAngleY = Math.abs((ballOnPaddlePos + paddleHalf) / paddleQuarter);
       
        if (ballCenter <= paddleCenter) {
            if (!(gameObj instanceof Block)) {
                ballDirX = -1;
            }
            ballAngleY = Math.abs((paddleQuarter + ballOnPaddlePos) / paddleQuarter);
        }
        else {
            if (!(gameObj instanceof Block)) {
                ballDirX = 1;
            }
            ballAngleY = Math.abs((paddleFiveFourths - ballOnPaddlePos) / paddleQuarter);
        }
    }
	
	private void nextLevel() {
		this.inPlay = false;
    	ball1.setCoordinates(-15, -15);
		level += 1;
		animationPanel.setLevel(level);
        for (int j = 0; j < blocks.length; j++) { //Loop through each row of blocks
        	for (int k = 0; k < blocks[j].length; k++) { //Loop through each column of blocks
        		Block block = blocks[j][k];
        		block.clearBlockHit();
        	}
        }
        blockCount = 112;
	}
	
	private void newGame() {
		level = 0; //Reset level
		score = 0; //Reset score
		lives = 3; //Reset lives
		temp_score = 0; //Reset score towards extra lives
		nextLevel(); //Initate nextLevel to reset the game
	}
}
