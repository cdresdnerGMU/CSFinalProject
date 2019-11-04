import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class AnimationMain {

    public static void main(String[] args) {
        Ball1 ball1 = new Ball1();
        //Ball2 ball2 = new Ball2();
        AnimationPanel animationPanel = new AnimationPanel(new BorderLayout(), ball1); //, ball2);
        
        buildGUI(animationPanel);
        
        for (int i = 0; i <= 400; i += 1) {
        	ball1.setCoordinates(i, 50);
        	//ball2.setCoordinates(i, 100 + i);
            animationPanel.repaint();
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
                        frame.setSize(400, 500);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                }
        );
    }
}