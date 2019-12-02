import java.awt.Color;

public class Ball1 implements GameObject {
    private double x = -15;
    private double y = -15;
    private double width = 10;
    private double height = 10;
    
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
    	return Color.RED;
    }
}