import java.awt.Color;

public class PowerUpBall extends Ball1 {
	@Override
    Color getColor() {
    	return Color.GRAY;
    }
	void powerDown() {
		width = 0; //makes the ball "invisible"
		height = 0;
	}
	void powerOn() {
		width = 10; //restores the ball's visibility + hitbox
		height = 10;
	}

}
