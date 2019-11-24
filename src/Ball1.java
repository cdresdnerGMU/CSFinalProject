import java.awt.Color;

public class Ball1 {
    private int x = -15;
    private int y = -15;
    private int width = 10;
    private int height = 10;
    
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
    
    void setCoordinates(int newX, int newY){
        x = newX;
        y = newY;
    }
    
    Color getColor() {
    	return Color.RED;
    }
}