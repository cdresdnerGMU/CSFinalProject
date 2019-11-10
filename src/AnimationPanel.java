import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class AnimationPanel extends JPanel {
	
    private Ball1 ball1;
    private Block[][] blocks;
    
    public AnimationPanel(LayoutManager layout, Ball1 ball1, Block[][] blocks) {
        super(layout);
        this.ball1 = ball1;
        this.blocks = blocks;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
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
    }
}