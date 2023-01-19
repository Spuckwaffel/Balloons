package balloons;

import gdi.game.sprite.AbstractSpriteWorld;

public class TrojanBalloon extends Balloon{

    /**
     * Create a trojan balloon at a specific location with a diameter
     * Trojan balloons split up to 2 balloons when shot
     * Configure the new balloons in the Game itself
     * @param x Center x-coordinate of the balloon
     * @param y Center y-coordinate of the balloon
     * @param diameter Diameter of the balloon
     * @param w AbstractSpriteWorld
     */
    public TrojanBalloon(double x, double y, int diameter, AbstractSpriteWorld w) {
        super(x, y, diameter, w);

        //override the type
        Type = "TrojanBalloon";
    }
}
