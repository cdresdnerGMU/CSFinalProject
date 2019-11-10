import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class AnimationMain {

	private static final int SCREEN_WIDTH = 840;
	private static final int SCREEN_HEIGHT = 700;
	
	private static final Color[] ROW_COLORS = new Color[] {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE,
			Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW};
	private static final int[] ROW_POINTS = new int[] {7, 7, 5, 5, 3, 3, 1, 1};
	
    public static void main(String[] args) {
    	Paddle paddle = new Paddle();
    	int paddleX = (SCREEN_WIDTH/2) - (paddle.getWidth()/2);
    	int paddleY = SCREEN_HEIGHT - 120;
    	
    	paddle.setCoordinates(paddleX, paddleY);
    	
        Ball1 ball1 = new Ball1();
        int xPos = 0;
        int yPos = 60;
        Block[][] blocks = new Block[8][14];
        for (int i = 0; i < 8; ++i) {
        	for (int j = 0; j < 14; ++j) {
                Block block = new Block();
                block.setCoordinates(xPos, yPos);
                block.setPoints(ROW_POINTS[i]);
                block.setFillColor(ROW_COLORS[i]);
                xPos += 60;
                blocks[i][j] = block;
                //Rectangle r1 = new Rectangle(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        	}
        	yPos += 15;
        	xPos = 0;
        }
        
        AnimationPanel animationPanel = 
        		new AnimationPanel(new BorderLayout(), SCREEN_WIDTH, SCREEN_HEIGHT, ball1, blocks, paddle);
        
        buildGUI(animationPanel);
        
        for (int i = 500; i > 0; i -= 3) {
        	ball1.setCoordinates(395, i);
            animationPanel.repaint();
            Rectangle r2 = new Rectangle(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
//            if (overlaps(r1, r2)) {
//            	block.blockHit();
//            }
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }
    
    public static void buildGUI(AnimationPanel animationPanel) {
        JFrame frame = new JFrame("Breakout");
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        frame.setContentPane(animationPanel);
                        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                }
        );
    }
    
    private static boolean touches(Rectangle r1, Rectangle r2) {
        if ((r2.x <= r1.x + r1.width) && (r2.x + r2.width >= r1.x) && 
        		(r2.y < r1.y + r1.height) && (r2.y + r2.height >= r1.y)) {
        	return true;
        }
        else {
        	return false;
        }
    }
}