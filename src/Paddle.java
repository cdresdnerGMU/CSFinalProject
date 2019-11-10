import java.awt.Color;

public class Paddle {
    private int x = 5;
    private int y = 5;
    private int width = 60;
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
    
    void setCoordinates(int newX, int newY){
        x = newX;
        y = newY;
    }
    
    Color getColor() {
    	return Color.BLUE;
    }
}