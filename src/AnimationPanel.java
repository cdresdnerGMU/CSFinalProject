import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class AnimationPanel extends JPanel {
	
    private Ball1 ball1;
    //private Ball2 ball2;
    
    public AnimationPanel(LayoutManager layout, Ball1 ball1) { //, Ball2 ball2) {
        super(layout);
        this.ball1 = ball1;
        //this.ball2 = ball2;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(ball1.getColor());
        g.fillOval(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
        
        //g.setColor(ball2.getColor());
        //g.fillRect(ball2.getX(), ball2.getY(), ball2.getWidth(), ball2.getHeight());
        
    }
}