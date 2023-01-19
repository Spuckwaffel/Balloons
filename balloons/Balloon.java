package balloons;
import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Balloon extends Sprite{

    /** Array of available colors for the balloon **/
    protected static final Color[] balloonColors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.WHITE, Color.ORANGE, Color.RED};

    /** Every balloon has a type name, so we can easily figure out what balloon it really is **/
    protected String Type = "Balloon";

    /** Lives of the balloon **/
    protected int lives = 1;

    /** Color **/
    protected Color balloonColor;

    /** Shape of the balloon **/
    private final Shape balloonShape;

    /** Current direction of the balloon **/
    public boolean moveUp = true;

    /** Diameter of the balloon **/
    public final int Diameter;

    /**
     * Create a balloon at a specific location with a diameter
     * @param x Center x-coordinate of the balloon
     * @param y Center y-coordinate of the balloon
     * @param diameter Diameter of the balloon
     * @param w AbstractSpriteWorld
     */
    public Balloon(double x, double y, int diameter, AbstractSpriteWorld w) {
        super(x, y, w);

        //create a balloon and set the color
        Diameter = diameter;
        balloonShape = new Ellipse2D.Double(-((double)diameter / 2),-((double)diameter / 2),diameter, diameter);

        Random rand = new Random();
        balloonColor = balloonColors[rand.nextInt(balloonColors.length)];
    }


    @Override
    protected final void renderLocal(Graphics2D g){

        //is the balloon popped? then don't render it
        if(isPopped()) {
            return;
        }

        g.setColor(balloonColor);

        g.fill(balloonShape);

        //a tip like a pro
        Shape BalloonTip = new Polygon(
                new int[] {-10, 0, 10},
                new int[] {(Diameter / 2) + 10, (Diameter / 2) - 5 /* draw a bit into the balloon */, (Diameter / 2) + 10},
                3);

        g.fill(BalloonTip);
    }

    /**
     * Indicate a hit
     */
    public void Hit() {
        lives--;
    }


    /**
     * Is the balloon popped?
     * @return true if balloon is popped
     */
    public boolean isPopped() {
        return lives == 0;
    }


    /**
     * Get the balloon type
     * @return Balloon type
     */
    public String getType() {
        return Type;
    }

    /**
     * Get the diameter of the balloon
     * @return Diameter of the balloon
     */
    public int getDiameter() {
        return Diameter;
    }
}
