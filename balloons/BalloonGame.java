package balloons;
import gdi.game.events.KeyEvent;
import gdi.game.sprite.SpriteWorld;

import java.awt.*;
import java.util.Random;

public class BalloonGame extends SpriteWorld{

    /** Balloons array **/
    private Balloon[] balloons;

    /** remainingBalloons counter**/
    private int remainingBalloons = 0;

    /** Is the game finished / has the player won **/
    private boolean isFinished = false;

    /** Dart object **/
    private final Dart dart;

    /**
     * BalloonGame Constructor
     * Fixes the window size on a Windows computer (remove if it causes problems)
     * @param width width of the game window
     * @param height height of the game window
     * @param balloonCount number of balloons in the game
     */
    public BalloonGame(int width, int height, int balloonCount) {

        //windows has some issues showing the window in the correct size (for me this fixed it)
        super(width + 32, height + 78);

        //create a new dart object
        dart = new Dart(this);

        //create empty balloon set
        balloons = new Balloon[balloonCount];

        remainingBalloons = balloonCount;

        Random rand = new Random();

        //loop through every balloon
        for(int i = 0; i < balloonCount; i++) {

            //set a random x and y position for the balloon (first x/3 excluded and +50 because of balloon radius)
            double x = rand.nextDouble((double)width / 3.0 + 50, width);
            double y = rand.nextDouble(0, height);

            //25% chance getting a trojan balloon or tough balloon
            int num = rand.nextInt(0, 100);
            if(num < 25) {
                balloons[i] = new TrojanBalloon(x, y, 100,this);
            }
            else if(num < 50) {
                balloons[i] = new ToughBalloon(x, y, 100,this);
            }
            else {
                balloons[i] = new Balloon(x, y, 100,this);
            }
        }
    }

    @Override
    protected void setupWorld() {
        //set title name
        this.setTitle("Balloon Game");
    }

    @Override
    protected void renderForeground(Graphics2D g) {
        //we just render the winning screen in the foreground
        if(isFinished){
            //create a box with a fixed size and text size
            g.setColor(Color.GRAY);
            g.fillRect(this.getWidth() / 2 - 150, this.getHeight() / 2 - 60, 300, 120);
            g.setColor(Color.black);
            g.setFont(new Font(g.getFont().getName(), Font.BOLD, 40));
            g.drawString("You win!", this.getWidth() / 2 - 90, this.getHeight() / 2 + 15);
            g.drawRect(this.getWidth() / 2 - 150, this.getHeight() / 2 - 60, 300, 120);
        }
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        g.setColor(Color.CYAN);
        //in my last homework I got punished that sizes might change, so I will not hardcode with and height and get them instead
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void update(double deltaTime, double time) {

        super.update(deltaTime,time);

        //nothing to update if the game is finished
        if (isFinished) return;

        //only move the dart if the dart is "flying"
        if(dart.isFlying){
            dart.moveDart(deltaTime);
        }

        //loop through every balloon
        for(int i = 0; i < balloons.length; i++){


            //if the balloon is popped skip it
            if(balloons[i].isPopped()) {
                continue;
            }



            //check if dart tip popped a balloon
            if(Math.pow(dart.getDartTip()[0] - balloons[i].getX(), 2) + Math.pow(dart.getDartTip()[1] - balloons[i].getY(), 2) <= Math.pow((double)balloons[i].getDiameter() / 2, 2)){

                //call the hit function
                balloons[i].Hit();

                //the dart should reset after every pop (was shown in the demo?)
                //comment this out if you ont want this behaviour
                dart.resetDart();

                //if the balloon is a Trojan balloon we have to do extra stuff
                //we can't do this in the TrojanBalloon class as we have to modify the balloons array and only BalloonGame.java owns this array
                if(balloons[i].getType().equals("TrojanBalloon")) {
                    //create a new array that has space for a new balloon
                    Balloon[] newBalloons = new Balloon[balloons.length + 1];

                    //copy array except the trojan balloon
                    for(int j = 0, k = 0; j < balloons.length; j++) {

                        //if it's not the trojan balloon, copy it
                        if(j != i) {
                            newBalloons[k]=balloons[j];
                            k++;
                        }
                    }

                    //create 2 new small balloons at a random position in range of the old balloon
                    Random newPos = new Random();
                    newBalloons[balloons.length -1] = new Balloon(
                            balloons[i].getX() - newPos.nextInt(10, 50),
                            balloons[i].getY() - newPos.nextInt(10, 50), 50, this);

                    newBalloons[balloons.length] = new Balloon(
                            balloons[i].getX() + newPos.nextInt(10, 50),
                            balloons[i].getY() - newPos.nextInt(10, 50), 50, this);

                    //copy array back
                    balloons = newBalloons;

                    //we kill one balloon and 2 new spawn -> 1 more balloon in the game
                    remainingBalloons++;

                    //array changed! return immediately!
                    //current balloon got popped, so code below won't be valid!
                    continue;
                }

                //is the balloon popped?
                if(balloons[i].isPopped()){

                    //1 balloon less
                    remainingBalloons--;
                    if(remainingBalloons == 0){
                        //game is won if all balloons are popped
                        isFinished = true;
                    }
                    //current balloon got popped, so code below wont be valid!
                    continue;
                }
            }

            //if the current balloon didn't pop, move the balloon up or down
            //calculate the pixels that the balloons have to move (100px/s)
            double newBalloonPixelOffset = deltaTime * 100;

            //gets if the current balloon direction is up or down
            if(balloons[i].moveUp){
                // what if the Y coordinate is at 7, and we have to move 10 px a frame? exactly, new coordinate is at 3 and not - 3
                if (balloons[i].getY() - newBalloonPixelOffset < 0){

                    balloons[i].setY(newBalloonPixelOffset - balloons[i].getY());
                    //we bounced off the ceiling new direction is down
                    balloons[i].moveUp = false;
                }
                else {
                    balloons[i].setY(balloons[i].getY() - newBalloonPixelOffset);
                }
            }
            else {
                //same as above, handle edge cases
                if (balloons[i].getY() + newBalloonPixelOffset > this.getHeight()){

                    balloons[i].setY((double)this.getHeight() - (newBalloonPixelOffset - ((double)this.getHeight() - balloons[i].getY())));

                    //we bounced off the floor new direction in up
                    balloons[i].moveUp = true;
                }
                else {
                    balloons[i].setY(balloons[i].getY() + newBalloonPixelOffset);
                }
            }
        }
    }

    @Override
    protected void keyDown(KeyEvent ke) {

        //don't handle any key events if the game is finished
        if (isFinished) return;

        //move dart up or down
        if(ke.getKeyCode() == 38 || ke.getKeyCode() == 40){
            //2 degree change for precision
            dart.rotate(ke.getKeyCode() == 38 ? 2 : -2);
        }

        //launch dart
        else if(ke.getKeyCode() == 32) {
            dart.Launch();
        }
    }
}
