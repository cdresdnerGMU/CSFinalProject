import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * This class is a swing panel that is used to paint various game components
 * It paints the ball, paddle, and blocks
 * It uses a mouse motion listener to detect mouse movement to make the paddle move
 * @author cdresdner
 *
 */
public class AnimationPanel extends JPanel implements MouseMotionListener {
	
    private Ball1 ball1; //The primary ball (more balls may be added for power-ups)
    private Block[][] blocks; //2D array of blocks
    private Paddle paddle;  //The paddle which is moved using the mouse
    private int screenWidth; //The width of the screen
    private int screenHeight; //The height of the screen
    private int score = 0; //The player's accumulated score
    private int lives = 3; //The player's remaining lives
    private int level = 1; //The current level
    
    /**
     * Constructs a new animation panel for the game
     * @param layout - Border layout manager
     * @param screenWidth - The width of the screen 
     * @param screenHeight - The height of the screen
     * @param ball1 - The primary ball
     * @param blocks - 2D array of blocks
     * @param paddle - The paddle which is moved using the mouse
     */
    public AnimationPanel(LayoutManager layout, int screenWidth, int screenHeight,
    		Ball1 ball1, Block[][] blocks, Paddle paddle) {
    	
        super(layout);
        this.ball1 = ball1;
        this.blocks = blocks;
        this.paddle = paddle;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        super.addMouseMotionListener(this);
    }
    
    /**
     * This method paints all components of the game
     * @param g - The graphics class passed in by the super class
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(paddle.getColor()); //Set the color for the paddle
        g.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight()); //Fill a rectangle to create the paddle using the color set above
        
        g.setColor(ball1.getColor()); //Set the color for the ball
        g.fillOval(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight()); //Fill an oval (circle) to create the ball using the color set above
        
        for (int i = 0; i < blocks.length; i++) { //Loop through each row of blocks
	     
        	for (int j = 0; j < blocks[i].length; j++) { //Loop through each column of blocks
	        		
		        g.setColor(blocks[i][j].getFillColor());  //Set the fill color for each block
		        g.fillRect(blocks[i][j].getX(), blocks[i][j].getY(), blocks[i][j].getWidth(), blocks[i][j].getHeight()); //Fill a rectangle to create each block using the color set above
		        
		        g.setColor(blocks[i][j].getBorderColor()); //Set the border color for each block
		        g.drawRect(blocks[i][j].getX(), blocks[i][j].getY(), blocks[i][j].getWidth(), blocks[i][j].getHeight()); //Draw a rectangular border to create a border for each block using the color set above
        	}
        }
        
        //Draw a horizontal line just below the paddle
        g.setColor(Color.BLACK); //Set the line's color to black
        int  lineY = paddle.getY() + paddle.getHeight() + 20; //Draw the line 20 pixels below the  paddle
        g.drawLine(0, lineY, screenWidth, lineY);
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24)); //Set the font for the score
        g.drawString("Score: ", 650, lineY + 33); //Draw the word "Score: "
        g.drawString(Integer.toString(score), 715, lineY + 34); //Draw the score value
        g.setFont(new Font("TimesRoman", Font.PLAIN, 16)); //Set the font for the lives remaining
        g.drawString("Lives: ", 10, lineY + 33); //Draw the word "Lives: "
        g.drawString(Integer.toString(lives), 60, lineY + 33); //Draw the amount of lives remaining
        g.drawString("Level: ", 120, lineY + 33); //Draw the word "Level: "
        g.drawString(Integer.toString(level), 170, lineY + 33); //Draw the current level
        
    }

    /**
     * This method is not used. No need to detect mouse drag
     */
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * This method detects when the mouse is moved and allows the paddle to follow its movements
	 * The paddle is only moved horizontally; y will not change
	 * The center of the paddle follows the mouse's x coordinate
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		
		Point point = e.getPoint(); //The mouse's position
        
        double pos = point.getX() - (paddle.getWidth()/2); //Positions the center of the paddle to the mouse's x coordinate
       
        if (pos < 0) { //Prevents the paddle from going past the left edge of the screen
        	pos = 0;
        }
        
        if (pos > (screenWidth - paddle.getWidth())) { //Prevents the paddle from going past the right edge of the screen
        	pos = screenWidth - paddle.getWidth();
        }
        
        paddle.setX((int) pos); //Sets the left edge of the paddle

	}
}