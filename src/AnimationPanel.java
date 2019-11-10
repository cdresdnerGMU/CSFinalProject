import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class AnimationPanel extends JPanel {
	
    private Ball1 ball1;
    private Block[][] blocks;
    private Paddle paddle;
    private int screenWidth;
    private int screenHeight;
    
    public AnimationPanel(LayoutManager layout, int screenWidth, int screenHeight,
    		Ball1 ball1, Block[][] blocks, Paddle paddle) {
    	
        super(layout);
        this.ball1 = ball1;
        this.blocks = blocks;
        this.paddle = paddle;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(paddle.getColor());
        g.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        
        g.setColor(ball1.getColor());
        g.fillOval(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
        
        for (int i = 0; i < blocks.length; i++) {
	     
        	for (int j = 0; j < blocks[i].length; j++) {
	        		
		        g.setColor(blocks[i][j].getFillColor());
		        g.fillRect(blocks[i][j].getX(), blocks[i][j].getY(), blocks[i][j].getWidth(), blocks[i][j].getHeight());
		        
		        g.setColor(blocks[i][j].getBorderColor());
		        g.drawRect(blocks[i][j].getX(), blocks[i][j].getY(), blocks[i][j].getWidth(), blocks[i][j].getHeight());
        	}
        }
        
        g.setColor(Color.BLACK);
        int  lineY = paddle.getY() + paddle.getHeight() + 20;
        g.drawLine(0, lineY, screenWidth, lineY);
    }
}