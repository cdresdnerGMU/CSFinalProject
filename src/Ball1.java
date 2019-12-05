import java.awt.Color;

/**
 * getters and setters for the ball characteristics
 */
public class Ball1 implements GameObject {
    private double x = -15;
    private double y = -15;
    protected double width = 10;
    protected double height = 10;

    /**
     * gets the x coordinate of the ball
     * @return the x coordinate
     */
    public double getX(){
        return x;
    }

    /**
     * gets the y coordinate of the ball
     * @return the y coordinate
     */
    public double getY(){
        return y;
    }

    /**
     * gets the width of the ball for the hitbox
     * @return the width of the ball
     */
    public double getWidth(){
        return width;
    }

    /**
     * gets the height of the ball for the hit box
     * @return the height of the bal
     */
    public double getHeight(){
        return height;
    }

    /**
     * sets the ball somewhere
     * @param newX x coordinate
     * @param newY y coordinate
     */
    void setCoordinates(double newX, double newY){
        x = newX;
        y = newY;
    }

    /**
     * color of the ball
     * @return the color
     */
    Color getColor() {
    	return Color.RED;
    }
}
