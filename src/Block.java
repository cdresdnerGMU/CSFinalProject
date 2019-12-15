import java.awt.Color;
import javax.sound.sampled.Clip;

/**
 * This class initializes the blocks - the game pieces that will be struck and destroyed by the ball in order to
 * accumulate points.
 */
public class Block implements GameObject {
    private double x = 5;
    private double y = 5;
    private double width = 60;
    private double height = 15;
    private boolean blockHit;
    Clip glass;
    Color fillColor;
    int points;
    
    /**
     * Initializes the sound clip played by a block when it is destroyed
     * @param glass the sound clip
     */
    public Block(Clip glass) {
    	this.glass = glass;
    }
    
    void setFillColor(Color fillColor) {
    	this.fillColor = fillColor;
    }
    
    void setPoints(int points) {
    	this.points = points;
    }
    
    public int getPoints() {
    	return points;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getWidth(){
        return width;
    }
    
    public double getHeight(){
        return height;
    }
    
    // SETTERS
    void setCoordinates(double newX, double newY){
        x = newX;
        y = newY;
    }
    
    Color getFillColor() {
    	return fillColor;
    }
    
    Color getBorderColor() {
    	return Color.black;
    }
    
    public void blockHit() {
    	points -= 1;
    	if (points == 4) {
    		fillColor = Color.RED;
    	}
    	if (points == 3) {
    		fillColor = Color.ORANGE;
    	}
    	if (points == 2) {
    		fillColor = Color.GREEN;
    	}
    	if (points == 1) {
    		fillColor = Color.YELLOW;
    	}
    	if (points < 1) {
        	blockHit = true;
        	
        	playGlassBreak();
    	}
    	
    }
    
    public boolean blockWasHit() {
    	return blockHit;
    }
    
    public void clearBlockHit() {
    	blockHit = false;
    }
    
    public void playGlassBreak() {
        glass.stop();
        glass.setFramePosition(0);
        glass.start(); 
    }
}