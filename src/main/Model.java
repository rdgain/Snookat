package main;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import utilities.SoundManager;

import static utilities.Constants.*;

import java.util.ArrayList;

/**
 * Created by Raluca on 20-Feb-16.
 * Snooker game logic. Handling model functionality.
 */
public class Model {
    public World world;
    public Ball mainBall;
    public Ball lastBall;
    ArrayList<Ball> balls;
    ArrayList<Ball> hits; // list of all balls hit by the white ball in a turn
    ArrayList<Cushion> cushions;
    ArrayList<Player> players; // contains a list of balls potted by each player
    public int currentPlayer;
    int turns;
    boolean finishedTurn, validFirstHit;
    boolean FOUL, quit, endOfGame;

    /**
     * Constructor, create the world and initialise the game.
     */
    public Model() {
        restart();
    }

    /**
     * Restart method.
     */
    public void restart() {
        //initiate world
        world = new World(new Vec2(0, -GRAVITY));
        world.setContinuousPhysics(true);
        hits = new ArrayList<Ball>();
        new BallCollider();
        Settings.velocityThreshold = 0.0001f;

        // create players
        players = new ArrayList<Player>(NO_PLAYERS);
        lastBall = null;
        for (int i = 0; i < NO_PLAYERS; i++) {
            players.add(new Player());
            players.get(i).id = i;
        }
        currentPlayer = 0;

        // create main ball
        mainBall = new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT / 8), 0, true);

        // create balls and add them to the list
        balls = new ArrayList<Ball>();
        float pos = 11.7f;
        float pos2 = 16f;

        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - 6 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT / 4), 2, true)); //yellow
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + 6 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT / 4), 3, true)); //green
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT / 4), 4, true)); //brown
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT / 2), 5, true)); //blue
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT * pos / pos2 - 6 * BALL_RADIUS), 6, true)); //pink
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT * 7 / 8), 7, true)); //black

        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT * pos / pos2 - 4 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 - 2 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 - 2 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - 2 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT * pos / pos2), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + 2 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - 3 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 2 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 2 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 2 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + 3 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 2 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - 4 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 4 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 - 2 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 4 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT * pos / pos2 + 4 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + 2 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 4 * BALL_RADIUS), 1, false)); //red
        balls.add(new Ball(world, new Vec2(SNOOKER_TABLE_WIDTH / 2 + 4 * BALL_RADIUS, SNOOKER_TABLE_HEIGHT * pos / pos2 + 4 * BALL_RADIUS), 1, false)); //red

        // create cushions and add them to the list
        cushions = new ArrayList<Cushion>(NO_CUSHIONS);
        cushions.add(new Cushion(SNOOKER_TABLE_WIDTH - CUSHION_DEPTH, SNOOKER_TABLE_HEIGHT - POCKET_SIZE * 2 - CUSHION_LENGTH, MathUtils.PI/2, CUSHION_LENGTH, CUSHION_DEPTH, world));
        cushions.add(new Cushion(SNOOKER_TABLE_WIDTH - CUSHION_DEPTH, CUSHION_LENGTH + POCKET_SIZE * 2, MathUtils.PI/2, CUSHION_LENGTH, CUSHION_DEPTH, world));
        cushions.add(new Cushion(SNOOKER_TABLE_WIDTH / 2, SNOOKER_TABLE_HEIGHT - CUSHION_DEPTH, MathUtils.PI, CUSHION_LENGTH, CUSHION_DEPTH, world));
        cushions.add(new Cushion(CUSHION_DEPTH, SNOOKER_TABLE_HEIGHT - POCKET_SIZE * 2 - CUSHION_LENGTH, MathUtils.PI * 3 / 2, CUSHION_LENGTH, CUSHION_DEPTH, world));
        cushions.add(new Cushion(CUSHION_DEPTH, CUSHION_LENGTH + POCKET_SIZE * 2, MathUtils.PI * 3 / 2, CUSHION_LENGTH, CUSHION_DEPTH, world));
        cushions.add(new Cushion(SNOOKER_TABLE_WIDTH / 2, CUSHION_DEPTH, 0, CUSHION_LENGTH, CUSHION_DEPTH, world));

        turns = 1;
        finishedTurn = false;
        validFirstHit = true;
        FOUL = false;
        quit = false;
        endOfGame = false;
    }

    /**
     * Perform all actions needed when a turn is finished.
     * Change current player as necessary.
     */
    public void finishedTurn() {
        turns--;
        validFirstHit = !hits.isEmpty() && checkValidPot(hits.get(0));
        //System.out.println("Balls not touched: " + hits.isEmpty());

        if (!validFirstHit || hits.isEmpty()) {
            foul();
        } else if (players.get(currentPlayer).newPotted.isEmpty()) {
            if (turns == 0) {
                //change player
                turns = 1;
                currentPlayer = (currentPlayer + 1) % NO_PLAYERS;
            }
        } else { // if balls were potted, same player gets to go again
            turns++;
        }

        //print results for debugging
        for(Player p: players) {p.transferBalls(); System.out.println("Player " + players.indexOf(p) + ": " + p);}
        System.out.println("-------------");

        hits = new ArrayList<Ball>(); // reinitialise balls hit
    }

    /**
     * Checks if a ball potted is valid.
     * @param b - ball potted.
     * @return true if no games were potted by current player and the ball just potted was red;
     *          true if ball "on" was potted; false otherwise and foul.
     *          true if last stage of game was reached and next ball in order was potted
     *          (order: yellow, brown, green, blue, pink, black).
     */
    public boolean checkValidPot(Ball b) {
        if (!endOfGame) {
            return (lastBall == null && b.points == 1) ||
                    (b.points == 1 && lastBall.points > 1 ||
                            b.points > 1 && (lastBall != null ? lastBall.points : 0) == 1);
        }
        else {
            return b.points == 2 || b.points == lastBall.points + 1;
        }
    }

    /**
     * Awards foul to the opponent (2 turns).
     * Changes current player.
     */
    public void foul() {
        FOUL = true;
        System.out.println("FOUL");
        turns = 2;
        //change player
        currentPlayer = (currentPlayer + 1) % NO_PLAYERS;
        players.get(currentPlayer).addScore(FOUL_PENALTY);
    }

    /**
     * Method to pot a ball. Checks if it is valid, resets or removes balls from the table.
     * @param b - ball to pot.
     */
    public void pot(Ball b) {
        b.out = false; //avoid crazy stuff
        SoundManager.fall();
        isEndOfGame();

        if (b.canReset) {
            if (b.points > 1) { //if coloured ball
                if (endOfGame) { //if last stage reached, stop resetting
                    if (checkValidPot(b)) {
                        balls.remove(b);
                        world.destroyBody(b.body);
                    } else {
                        foul();
                        b.reset();
                    }
                } else {
                    b.reset();
                }
            } else { // white ball, always reset
                b.reset();
            }
        } else {
            balls.remove(b);
            world.destroyBody(b.body);
        }

        if (b.equals(mainBall)) {
            foul();
        } else {
            if (checkValidPot(b)) {
                players.get(currentPlayer).pot(b);
                lastBall = b;
            } else {
                foul();
            }
        }
    }

    /**
     * Updates game state. All balls are updated and rolling friction applied.
     * @param finishedTurn - boolean if turn was finished
     */
    public void update(boolean finishedTurn) {
        this.finishedTurn = finishedTurn;

        //update all balls (add rolling friction forces) and check if they were potted
        synchronized (this) {
            mainBall.update();
            if (mainBall.out) pot(mainBall);
            for (Ball b : balls) {
                b.update();
                if (b.out) pot(b);
            }
        }

        // update world
        int VELOCITY_ITERATIONS = EULER_UPDATES_N;
        int POSITION_ITERATIONS = EULER_UPDATES_N;
        world.step(DELTA_T, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    /**
     * Check end of game (when all balls have been potted)
     * @return true if game is ended, false otherwise
     */
    public boolean isEnded() {
        /**
         * Debug end of game screen. Uncomment this and comment the other one.
         */
        //return true;

        return balls.isEmpty();
    }

    /**
     * Get the winner of the game if the game has ended.
     * @return - Player object (winner of game, most points).
     */
    public Player getWinner() {
        int max = 0;
        Player winner = null;
        if (isEnded()) {
            for (Player p: players) {
                if (p.getScore() > max) {
                    max = p.getScore();
                    winner = p;
                }
            }
        }
        /**
         * Uncomment this to debug end of game screen.
         */
        //winner = new Player();
        //winner.pot(new Ball(world, new Vec2(), 0, false));
        return winner;
    }

    /**
     * Check if the last stage of the game has been reached,
     * when only coloured balls are left.
     * @return true if stage reached, false otherwise.
     */
    public boolean isEndOfGame() {
        for (Ball b: balls)
            if (b.points == 1) return false;
        endOfGame = true;
        return true;
    }

    /**
     * Checks if any active balls are still moving.
     * @return true if any ball is still moving (turn still going), false otherwise.
     */
    public boolean isTurnGoing() {
        ArrayList<Ball> collision = new ArrayList<Ball>();
        collision.addAll(balls);
        collision.add(mainBall);
        for (Ball b: collision)
            if (b.body.getLinearVelocity().length() > 0) return true;
        return false;
    }

    /**
     * Used to check which ball was hit first in a turn.
     * Registers ball - ball collisions.
     */
    class BallCollider implements ContactListener {

        public BallCollider() {
         // Set the contact listener for the world to this
            world.setContactListener(this);
        }

        @Override
        public void beginContact(Contact contact) {
            Object object1 = contact.getFixtureA().getUserData();
            Object object2 = contact.getFixtureB().getUserData();

            if (object1 instanceof Ball && object2 instanceof Ball) {
                if (!isEnded() && mainBall.moved())
                    SoundManager.hit();
                Ball b1 = (Ball) object1;
                Ball b2 = (Ball) object2;
                if (b1.equals(mainBall) || b2.equals(mainBall)) {
                    if (!finishedTurn) { //add ball that was hit by white ball
                        if (b1.equals(mainBall)) hits.add(b2);
                        else hits.add(b1);
                    }
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {}

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {}

    }

}
