import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class AnimationPanel extends JPanel implements MouseMotionListener {
	
    private Ball1 ball1;
    private Block[][] blocks;
    private Paddle paddle;
    private int screenWidth;
    private int screenHeight;
    private int score = 0;
    private int lives = 3;
    private int level = 1;
    
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
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        g.drawString("Score: ", 650, lineY + 33);
        g.drawString(Integer.toString(score), 715, lineY + 34);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        g.drawString("Lives: ", 10, lineY + 33);
        g.drawString(Integer.toString(lives), 60, lineY + 33);
        g.drawString("Level: ", 120, lineY + 33);
        g.drawString(Integer.toString(level), 170, lineY + 33);
        
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		Point point = e.getPoint();
        //double pos= Math.max( Math.min (point.getX()-(paddle.getWidth()/2), screenWidth - paddle.getWidth()), 0);
        
        double pos = point.getX() - (paddle.getWidth()/2);
       
        if (pos < 0) {
        	pos = 0;
        }
        if (pos > (screenWidth - paddle.getWidth())) {
        	pos = screenWidth - paddle.getWidth();
        }
        
        paddle.setX((int) pos);

	}
}