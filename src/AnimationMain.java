import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class AnimationMain {

    public static void main(String[] args) {
        Ball1 ball1 = new Ball1();
        int xPos = -40;
        int yPos = -20;
        Block[][] blocks = new Block[6][20];
        for (int i = 0; i < 6; ++i) {
        	for (int j = 0; j < 20; ++j) {
                Block block = new Block();
                block.setCoordinates(xPos, yPos);
                xPos += 40;
                blocks[i][j] = block;
                //Rectangle r1 = new Rectangle(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        	}
        	yPos += 20;
        	xPos = 0;
        }
        
        AnimationPanel animationPanel = new AnimationPanel(new BorderLayout(), ball1, blocks);
        
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
        JFrame frame = new JFrame("Simple animation");
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        frame.setContentPane(animationPanel);
                        frame.setSize(800, 600);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                }
        );
    }
    
    private static boolean overlaps(Rectangle r1, Rectangle r2) {
        if ((r2.x < r1.x + r1.width) && (r2.x + r2.width > r1.x) && 
        		(r2.y < r1.y + r1.height) && (r2.y + r2.height > r1.y)) {
        	return true;
        }
        else {
        	return false;
        }
    }
}