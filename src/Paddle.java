import java.awt.Color;

public class Paddle implements GameObject {
    private double x = 5;
    private double y = 5;
    private double width = 60;
    private double height = 20;
    
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
    
    void setCoordinates(double newX, double newY){
        x = newX;
        y = newY;
    }
    
    Color getColor() {
    	return Color.BLUE;
    }
    
    void setX(double x) {
    	this.x = x;
    }
}