import java.awt.Color;

public class Ball1 {
    private int x = 5;
    private int y = 5;
    private int width = 20;
    private int height = 20;
    
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
    
    Color getColor() {
    	return Color.ORANGE;
    }
}