package balloons;

import gdi.game.sprite.AbstractSpriteWorld;

import java.awt.*;
import java.util.Random;

public class ToughBalloon extends Balloon {

    /**
     * Create a tough balloon at a specific location with a diameter
     * Tough balloons can handle 1 extra shot
     * @param x Center x-coordinate of the balloon
     * @param y Center y-coordinate of the balloon
     * @param diameter Diameter of the balloon
     * @param w AbstractSpriteWorld
     */
    public ToughBalloon(double x, double y, int diameter, AbstractSpriteWorld w) {
        super(x, y, diameter, w);

        //override default values
        lives = 2;
        Type = "ToughBalloon";
    }

    /**
     * We override the Hit function to adjust the color
     */
    @Override
    public void Hit() {
        lives--;

        //no new color if the balloon is popped (for particles this is important)
        if(lives == 0) return;
        //set a new color
        Random rand = new Random();
        Color newColor = balloonColors[rand.nextInt(balloonColors.length)];

        //we don't like to have the same color again
        while(balloonColor.equals(newColor)) {
            newColor = balloonColors[rand.nextInt(balloonColors.length)];
        }
        balloonColor = newColor;
    }
}
