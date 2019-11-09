import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class AnimationPanel extends JPanel {
	
    private Ball1 ball1;
    private Block block;
    
    public AnimationPanel(LayoutManager layout, Ball1 ball1, Block block) {
        super(layout);
        this.ball1 = ball1;
        this.block = block;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
        g.setColor(ball1.getColor());
        g.fillOval(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
        
        g.setColor(block.getColor());
        g.fillRect(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        
    }
}