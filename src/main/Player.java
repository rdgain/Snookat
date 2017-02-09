package main;

import java.util.ArrayList;

/**
 * Created by Raluca on 20-Feb-16.
 *
 */

public class Player {
    ArrayList<Ball> potted;
    ArrayList<Ball> newPotted;
    private int score;
    public int id;

    public Player() {
        potted = new ArrayList<Ball>();
        newPotted = new ArrayList<Ball>();
        score = 0;
    }

    /**
     * Method to pot a ball for this player. Registers the ball as a newly potted ball this turn.
     * @param b - ball potted.
     */
    public void pot(Ball b) {
        newPotted.add(b);
        score += b.points;
    }

    /**
     * Called at the end of a turn, all newly potted balls added to the rest.
     */
    public void transferBalls() {
        potted.addAll(newPotted);
        newPotted.clear();
    }

    /**
     * Get this player's score.
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add points to the player's score.
     * @param s - points to be added.
     */
    public void addScore(int s) {score += s;}

    public String toString() {
        String balls = "";
        for (Ball b: potted) balls += b.points + ";";
        return "score = " + score + "; balls: " + balls;
    }
}
