package balloons;

import gdi.game.sprite.AbstractSpriteWorld;
import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;

public class Dart extends Sprite {

    /** Save the angle of the dart **/
    private double vecAngle = 0;

    /** Create a dart vector **/
    private Vec2D dartVec = new Vec2D(60, 0.0);

    /** Set the start position opf a dart (see pdf why startDart[1] is 0) **/
    private final double[] startDart = {0,0};

    /** Save total air time (used for gravity) **/
    private double timeInAir = 0;

    /** Save if dart is flying **/
    public boolean isFlying = false;

    /** The gravity of the dart in px/s^2 **/
    private static final double DART_GRAVITY = 490.5;

    /** The speed of the dart in px/s **/
    private static final double DART_SPEED = 600;

    /**
     * Creates a dart
     * @param w AbstractSpriteWorld
     */
    public Dart(AbstractSpriteWorld w) {
        super(w);
    }

    @Override
    protected final void renderLocal(Graphics2D g){

        g.setColor(Color.black);

        //if the dart is not flying, ALWAYS get the newest height of the window, so it works even if resized.
        //if the dart is flying, we do not care about any window resizing, so the way the dart flies won't change.
        if(!isFlying) {
            g.drawLine(0, (int)(double)this.getSpriteWorld().getHeight() / 2, (int)dartVec.x, (int)((double)this.getSpriteWorld().getHeight() / 2 - dartVec.y));
        }
        else {
            g.drawLine((int)startDart[0], (int)startDart[1], (int)(startDart[0] + dartVec.x), (int)(startDart[1] - dartVec.y));
        }
    }

    /**
     * Rotate the dart.
     * Rotation is impossible to set while flying.
     * Rotation has to be between -90 and 90 (included)
     * @param degrees New angle
     */
    public void rotate(double degrees){

        //return if user tries to rotate the dart more than 90 degrees or dart is launched
        if(Math.abs(vecAngle + degrees) > 90 || isFlying) {
            return;
        }

        //set new angle
        vecAngle += degrees;

        //rotate dart
        dartVec = dartVec.rotateAround(new Vec2D(0,0), degrees);
    }

    /**
     * Gets the tip of the Dart
     * @return Array of coordinates of the tip (arr[0] = x, arr[1] = y)
     */
    public double[] getDartTip() {
        return new double[] {(startDart[0] + dartVec.x), (startDart[1] - dartVec.y)};
    }

    /**
     * Resets the Dart to its normal position
     */
    public void resetDart() {
        //set the x coordinate of the start of the dart to 0
        startDart[0] = 0;

        //set the x coordinate of the start of the dart to the current screen height / 2
        startDart[1] = (double)this.getSpriteWorld().getHeight() / 2;

        //reset the rotation to its last rotation
        dartVec = new Vec2D(60, 0.0);
        dartVec = dartVec.rotateAround(new Vec2D(0,0), vecAngle);
        isFlying = false;
        timeInAir = 0;
    }

    /**
     * Moves the dart if the dart is flying and adjusts rotation/gravity
     * @param timeElapsedSLF time elapsed since the last frame
     */
    public void moveDart(double timeElapsedSLF){
        //confirm that it should only set new pos if the dart is flying

        if (isFlying){

            //update the total time in air
            timeInAir += timeElapsedSLF;

            //get the new x and y coordinate of the dart depending on the total time in air
            //speed in x and y is obviously different depending on the angle, so we have to calculate this too
            final double x = Math.cos(Math.toRadians(vecAngle)) * DART_SPEED * timeInAir;
            final double y = -Math.sin(Math.toRadians(vecAngle)) * DART_SPEED * timeInAir + (0.5 * DART_GRAVITY * (timeInAir * timeInAir));

            //get always the correct angle of the dart depending on the total time in air
            //vx does not have to be calculated each frame, but a lonely vx declared in the class does not look nice
            final double vx = Math.cos(Math.toRadians(vecAngle)) * DART_SPEED;

            //negative, because y coordinate  in programming is the opposite way than in maths
            final double vy = -(-Math.sin(Math.toRadians(vecAngle)) * DART_SPEED + DART_GRAVITY * timeInAir);

            //get the angle and set it
            dartVec = new Vec2D(60, 0).rotateAround(new Vec2D(0,0), Math.toDegrees(Math.atan(vy / vx)));

            //set the new position of the dart
            startDart[0]  = x;
            startDart[1]  = y + ((double)this.getSpriteWorld().getHeight() / 2);


            //if the dart is out of bounds, remove it. Out of bounds is below the screen and further than the width of the game
            //the dart is allowed to go above the screen and come back
            if(startDart[0]> (double)this.getSpriteWorld().getWidth() || startDart[1] > (double)this.getSpriteWorld().getHeight()) {
                //reset
                resetDart();
            }
        }
    }

    /**
     * Launch the dart. Launching will only work if the dart is not flying
     */
    public void Launch() {
        if(!isFlying) {
            isFlying = true;
            //update the screen size once again (pdf for more infos)
            startDart[1] = (double)this.getSpriteWorld().getHeight() / 2;
            timeInAir = 0;
        }
    }
}
