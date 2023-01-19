package balloons;

/**
 *
 * Please read the detailed pdf for additional information!
 *
 */

public class Main {
    public static void main(String[] args) {

        //create a new game with width, height and balloon count
        BalloonGame game = new BalloonGame(800, 600, 3);

        game.run();
    }
}
