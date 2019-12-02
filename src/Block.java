import java.awt.Color;
import javax.sound.sampled.Clip;

public class Block implements GameObject {
    private double x = 5;
    private double y = 5;
    private double width = 60;
    private double height = 15;
    private boolean blockHit;
    Clip glass;
    Color fillColor;
    int points;
    
    public Block(Clip glass) {
    	this.glass = glass;
    }
    
    void setFillColor(Color fillColor) {
    	this.fillColor = fillColor;
    }
    
    void setPoints(int points) {
    	this.points = points;
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
//    	this.width = 0;
//    	this.height = 0;
//    	this.x = 0;
//    	this.y = 0;
    	blockHit = true;
    	playGlassBreak();
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