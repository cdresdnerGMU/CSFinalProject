import java.awt.Color;
import javax.sound.sampled.Clip;

public class Block {
    private int x = 5;
    private int y = 5;
    private int width = 60;
    private int height = 15;
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
    
    int getX(){
        return x;
    }
    
    int getY(){
        return y;
    }
    
    int getWidth(){
        return width;
    }
    
    int getHeight(){
        return height;
    }
    
    // SETTERS
    void setCoordinates(int newX, int newY){
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
    	this.width = 0;
    	this.height = 0;
    	this.x = 0;
    	this.y = 0;
    	playGlassBreak();
    }
    
    public void playGlassBreak() {
        glass.stop();
        glass.setFramePosition(0);
        glass.start(); 
    }
}