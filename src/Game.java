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
	private static final int EXTRA_LIFE = 100;
	
	private static final Color[] ROW_COLORS = new Color[] {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, //The colors of every two rows of blocks
			Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW};
	private static final int[] ROW_POINTS = new int[] {4, 4, 3, 3, 2, 2, 1, 1}; //Sets the point values for each block row

	private Paddle paddle;
	private Ball1 ball1;
	private AnimationPanel animationPanel;
	private Block[][] blocks;
	private int blockCount;
	private PowerUpBall pBall1;
	private int ptempScore = 0;
	
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
	
	double pBallDirY1 = 0;
	double pBallDirX1 = 0;
	double pBallAngleX1 = 0;
	double pBallAngleY1 = 0;

	double pBallYPos1;
	double pBallXPos1;

	
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
        pBall1 = new PowerUpBall();
        pBall1.powerDown();
        
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
        blocks[3][4].setFillColor(Color.MAGENTA);
        blocks[3][4].setPoints(1);
        blocks[2][9].setFillColor(Color.CYAN);
        blocks[2][9].setPoints(1);
        blocks[7][10].setFillColor(Color.MAGENTA);
        blocks[7][3].setFillColor(Color.CYAN);
        blocks[5][5].setFillColor(Color.CYAN);
        blocks[5][5].setPoints(1);
        
        //Creates the animation panel
        animationPanel = 
        		new AnimationPanel(new BorderLayout(), SCREEN_WIDTH, SCREEN_HEIGHT, ball1, pBall1, blocks, paddle, this);
       
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
    	int rHeight = (int) (rBlock.getHeight());
    	Rectangle bottomHalf = new Rectangle((int)rBlock.getX(), (int)rBlock.getY() + rHeight, (int)rBlock.getWidth(), 1); //generates a rectangle of height 1
    	//at the bottom of the rectangle, so the hitbot ONLY calculates for the bottom
        if ((rBall.x <= bottomHalf.x + bottomHalf.width) && (rBall.x + rBall.width >= bottomHalf.x) && 
        		(rBall.y <= bottomHalf.y + bottomHalf.height) && (rBall.y + rBall.height >= bottomHalf.y)) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
    private boolean touchesLeft(Rectangle rBlock, Rectangle rBall) {
    	int rLength = (int) (rBlock.getWidth());
    	Rectangle left = new Rectangle((int)rBlock.getX(), (int)rBlock.getY(), 1, (int)rBlock.getHeight());
    	//generates two rectangles at the left + right most ends to caculate the hitboxes
    	if ((rBall.x <= left.x + left.width) && (rBall.x + rBall.width >= left.x) && 
        		(rBall.y <= left.y + left.height) && (rBall.y + rBall.height >= left.y)) {
        	return true;
        }
    	else {
    		return false;
    	}
    }
    
    public boolean touchesRight(Rectangle rBlock, Rectangle rBall) {
    	int rLength = (int) (rBlock.getWidth());
    	Rectangle right = new Rectangle((int)rBlock.getX() + rLength - 1, (int)rBlock.getY(), 1, (int)rBlock.getHeight());
    	//generates two rectangles at the left + right most ends to caculate the hitboxes
    	if ((rBall.x <= right.x + right.width) && (rBall.x + rBall.width >= right.x) && 
        		(rBall.y <= right.y + right.height) && (rBall.y + rBall.height >= right.y)) {
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

	/**
	 * run the game
	 */
	@Override

	public void run() {
		
		double lineY = paddle.getY() + paddle.getHeight() + 20;
		

		// i made two different dirs for X and Y b/c if it hits a wall, the Xdir will change but Ydir shouldn't..
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
                if (ballYPos <= 0) { //Ball reverses direction when it hits the ceiling
                    ballDirY = -1;
                }
				pBallYPos1 -= (pBallAngleY1 * pBallDirY1); // since the top is 0, we have to decrement (It's weird)
				pBallXPos1 += (pBallAngleX1 * pBallDirX1);// moving the ball in
				pBall1.setCoordinates(pBallXPos1, pBallYPos1);// always start the ball at an angle.
				if (pBall1.getX() >= maxX) { //if it hits the right most wall, the dirX should change
					pBallDirX1 *= -1;
				}
				if(pBall1.getX() <= 0) {//most left wall
					pBallDirX1 *= -1;
				}

	            
	            Rectangle r2 = new Rectangle((int)ball1.getX(), (int)ball1.getY(), (int)ball1.getWidth(), (int)ball1.getHeight()); //ball hitbox
	            Rectangle paddleHitbox = new Rectangle((int)paddle.getX(), (int)paddle.getY(), (int)paddle.getWidth(), (int)paddle.getHeight());
	            if (touches(paddleHitbox, r2)) { //Ball reverses direction when it hits the paddle
	            	changeBallAngle(ball1, paddle);
	            	ballDirY = 1;
	            }

	            if (ball1.getY() >= lineY) { //Ball will reset to its default position and lives will be decremented when it hits the bottom of the screen
	            	lives -= 1;
	            	ptempScore = 0;
	            	this.inPlay = false;
	            	ball1.setCoordinates(-15, -15);
	            	pBall1.powerDown();//hides the power up ball
	            	pBallDirY1 = 0;
	            	pBallDirX1 = 0;
	            	pBallAngleX1 = 0;
	            	pBallAngleY1 = 0;
	            	paddle.powerDown();
	            	
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
	            
				
	            Rectangle p1 = new Rectangle((int)pBall1.getX(), (int)pBall1.getY(), (int)pBall1.getWidth(), (int)pBall1.getHeight()); //powerup ball hitbox
	            if (touches(paddleHitbox, p1)) { //Ball reverses direction when it hits the paddle
	            	changeBallAngle(pBall1, paddle);
	            	pBallDirY1 = 1;
	            }

	            if (pBallYPos1 <= 0) { //Ball reverses direction when it hits the ceiling
	            	pBallDirY1 = -1;
	            }
	            if (pBall1.getY() >= lineY || ball1.getY() >= lineY) { //Powerup goes away when it hits the bottom or when the original ball hits the bottom
	            	pBall1.setCoordinates(-1000, 1000);
	            	pBallDirY1 = 0;
	            	pBallDirX1 = 0;
	            	pBallAngleX1 = 0;
	            	pBallAngleY1 = 0;
	            }
	            
			
	            
	            for (int j = 0; j < blocks.length; j++) { //Loop through each row of blocks
	            	for (int k = 0; k < blocks[j].length; k++) { //Loop through each column of blocks
	            		Block block = blocks[j][k];
	            		if (!block.blockWasHit()) {
		            		Rectangle r1 = new Rectangle((int)block.getX(), (int)block.getY(), (int)block.getWidth(), (int)block.getHeight());
                            if (touches(r1, r2)) {
                                if (score - ptempScore >= 5 && ptempScore != 0) { //the paddle powerup is viable for 5 brick breaks
                                    paddle.powerDown();
                                }
                                changeBallAngle(ball1, block);
                                if (touchesRight(r1, r2)) { //checks collision with the right side of the block
                                	if (touchesBottom(r1, r2)) { //checks for collision with the bottom of the block (corners)
                                		ballDirY *= -1;
                                	}
                                	if (ballDirX < 0) {
                                		ballDirX *= -1;
                                	}
                                }
                                else if (touchesLeft(r1, r2)) {//checks collision with the left side of the block
                                	if (touchesBottom(r1, r2)) {//checks for collision for the left corner
                                		ballDirY *= -1;
                                	}
                                	if (ballDirX > 0) {
                                		ballDirX *= -1;
                                	}
                                }
                                else if (touchesBottom(r1, r2)) { //bounces off the bottom
                                    ballDirY *= -1;
                                }
                                else {
                                    ballDirY = 1;
                                }

                                score += 1;
                                temp_score += block.getPoints();

                                if (temp_score >= EXTRA_LIFE){
                                    lives += 1; //every time the temp_score reaches 100, the lives is incremented by 1
                                    temp_score = temp_score - EXTRA_LIFE; //set back to 0

                                }
                                if (block.getFillColor() == Color.MAGENTA) {
                                	pBall1.powerOn();
                                    pBallYPos1 = ballYPos; //powerup balls originate from the original ball
                                    pBallXPos1 = ballXPos;
                                    pBallDirY1 = -ballDirY; //has to be negative because the normal ball's direction hasn't changed yet. (the bonus ball comes out first)
                                    pBallDirX1 = -ballDirX; //same as above but in x direction
                                    pBallAngleX1 = ballAngleX*-.9; //slows down
                                    pBallAngleY1 = ballAngleY*-.5;
                                }

                                if (block.getFillColor() == Color.CYAN) {
                                    paddle.powerUp(); //increases paddle size
                                    ptempScore = score;
                                }

                                block.blockHit();
                                blockCount -= 1;
                                if (blockCount <= 0) {
                                    nextLevel();
                                }
                            }
		                    if (touches(r1, p1)){ //checks for collision with the powerup ball
		                    	changeBallAngle(pBall1, block);
                                if (touchesRight(r1, p1)) {
                                	if (touchesBottom(r1, p1)) {
                                		System.out.println("Right corner");
                                		pBallDirY1 *= -1;
                                	}
                                	if (pBallDirX1 < 0) {
                                		pBallDirX1 *= -1;
                                	}
                                	System.out.println("Touches right");
                                }
                                else if (touchesLeft(r1, p1)) {
                                	if (touchesBottom(r1, p1)) {
                                		pBallDirY1 *= -1;
                                		System.out.println("Left corner");
                                	}
                                	if (pBallDirX1 > 0) {
                                		pBallDirX1 *= -1;
                                	}
                                	System.out.println("Touches left");
                                }
                                else if (touchesBottom(r1, p1)) {
                                    pBallDirY1 *= -1;
                                    System.out.println("Touches bottom");
                                }
                                else {
                                    pBallDirY1 = 1;
                                    System.out.println("Touches top");
                                }
		    	            	
		                		score += 1;
		                		temp_score += block.getPoints();


		    	            	if (block.getFillColor() == Color.MAGENTA) {
		    	            		pBall1.powerOn(); //turns on the power up visibitily
		    	            		pBallYPos1 = ballYPos; //powerup balls originate from the original ball
		    	            		pBallXPos1 = ballXPos;
		    	            		pBallDirY1 = -ballDirY;
		    		            	pBallDirX1 = -ballDirX;
		    		            	pBallAngleX1 = ballAngleX*-.9; //multiplied so that the powerup follows a different path and isn't identical to ball1
		    		            	pBallAngleY1 = ballAngleY-.5;
		    	            	}
		    	            	if (block.getFillColor() == Color.CYAN) {
		    	            		paddle.powerUp(); //increases paddle size
		    	            		ptempScore = score;
		    	            	}
		                		block.blockHit();
		                		blockCount -= 1;
		                		if (blockCount <= 0) { //if all the blocks have been hit, proceed to the next level
		                			nextLevel();

		                		}
                                if (temp_score >= EXTRA_LIFE) {
                                    lives += 1;
                                    temp_score = temp_score - EXTRA_LIFE;
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

	}

	/**
	 * getter for live
	 * @return return live count
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * getter for score
	 * @return score count
	 */
	public int getScore() {
		return score;
	}

	/**
	 * changing of the Angle of the ball (both power ball and normal ball) when it hits a block or paddle to provide a little more control for the user
	 * @param ball1 is the ball that hit another object (either block or paddle)
	 * @param gameObj is the object that the ball has hit
	 */
	private void changeBallAngle(Ball1 ball1, GameObject gameObj) { //if a ball hits an object, call this method to change the ball angle depends where it hits on the object

        double ballCenter = ball1.getX() + (ball1.getWidth() / 2);
        double paddleCenter = gameObj.getX() + (gameObj.getWidth() / 2);
        double paddleWidth = gameObj.getWidth();
        double paddleHalf = paddleWidth / 2.0;
        double paddleQuarter = paddleWidth / 4.0;
        double paddleFiveFourths = paddleQuarter * 5;
        double ballOnPaddlePos = ballCenter - gameObj.getX();

        if (ballOnPaddlePos < 0) { //Account for both sides of the panel
            ballOnPaddlePos = 0;
        }
        if (ballOnPaddlePos > paddleWidth) { //Ensure that ballOnPaddlePos does not exceed paddleWidth
            ballOnPaddlePos = paddleWidth;
        }

        if (ball1 instanceof PowerUpBall){ //check if its a powerball or normal ball
            pBallAngleX1 = Math.abs((ballOnPaddlePos - paddleHalf) * 0.2); //The ball's main change in angle after bouncing

            pBallAngleY1 = Math.abs((ballOnPaddlePos + paddleHalf) / paddleQuarter); //Allows the ball's speed to be controlled after changing angle

            if (ballCenter <= paddleCenter) {
                if (!(gameObj instanceof Block)) {
                    pBallDirX1 = -1;
                }
                pBallAngleY1 = Math.abs((paddleQuarter + ballOnPaddlePos) / paddleQuarter);
            }
            else {
                if (!(gameObj instanceof Block)) {
                    pBallDirX1 = 1;
                }
                pBallAngleY1 = Math.abs((paddleFiveFourths - ballOnPaddlePos) / paddleQuarter);
            }
        }
        else { //regular ball
            ballAngleX = Math.abs((ballOnPaddlePos - paddleHalf) * 0.2); //The ball's main change in angle after bouncing

            ballAngleY = Math.abs((ballOnPaddlePos + paddleHalf) / paddleQuarter); //Allows the ball's speed to be controlled after changing angle

            if (ballCenter <= paddleCenter) {
                if (!(gameObj instanceof Block)) {
                    ballDirX = -1;
                }
                ballAngleY = Math.abs((paddleQuarter + ballOnPaddlePos) / paddleQuarter);
            } else {
                if (!(gameObj instanceof Block)) {
                    ballDirX = 1;
                }
                ballAngleY = Math.abs((paddleFiveFourths - ballOnPaddlePos) / paddleQuarter);
            }
        }
    }

	/**
	 * once the blocks are cleared, it resets the board.
	 */
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
        blockCount = 280;
	}

	/**
	 * If the player wants to play a new game, it resets everything
	 */
	private void newGame() {
		level = 0; //Reset level
		score = 0; //Reset score
		lives = 3; //increment life by one
		temp_score = 0; //Reset score towards extra lives
		nextLevel(); //Initate nextLevel to reset the game
	}
	
}
