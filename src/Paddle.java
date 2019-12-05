import java.awt.Color;

/**
 * getters and setters for the paddle characteristics
 */
public class Paddle implements GameObject {
    private double x = 5;
    private double y = 5;
    private double width = 60;
    private double height = 20;

    /**
     * gets the x coord of the paddle
     * @return the x coord
     */
    public double getX(){
        return x;
    }

    /**
     * gets the y coord of the paddle
     * @return the y coord
     */
    public double getY(){
        return y;
    }

    /**
     * returns the width of the paddle
     * @returns the width for the hit box
     */
    public double getWidth(){
        return width;
    }

    /**
     * returns the height of the paddle
     * @return the height for the hit box
     */
    public double getHeight(){
        return height;
    }

    /**
     * sets the paddle somewhere
     * @param newX x coordinates
     * @param newY y coordinates
     */
    void setCoordinates(double newX, double newY){
        x = newX;
        y = newY;
    }

    /**
     * when the powerup block is broken, it changes the width of the paddle
     */
    public void powerUp() {
    	this.width = 100;
    }

    /**
     * after five hits, the power up expires and changes the width of the paddle back to its original width
     */
    public void powerDown() {
    	this.width = 60;
    }

    /**
     * color of the paddle
     * @return color
     */
    Color getColor() {
    	return Color.BLUE;
    }
    
    void setX(double x) {
    	this.x = x;
    }
}
