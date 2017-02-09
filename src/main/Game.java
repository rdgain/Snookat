package main;

import utilities.JEasyFrame;
import utilities.MenuKeyListener;
import utilities.SnookerMouseListener;

import static utilities.Constants.*;

/**
 * Created by Raluca on 20-Feb-16.
 * Main game class, handling controller functionality.
 */
public class Game {
    private Model model;

    public Game() {
        model = new Model();
    }

    public static void main(String[] args) {
        Game game = new Game();
        View view = new View(game.model);
        JEasyFrame frame = new JEasyFrame(view, "Snookat");
        SnookerMouseListener mouseListener = new SnookerMouseListener(game.model);
        view.addMouseMotionListener(mouseListener);
        view.addMouseListener(mouseListener);
        frame.addKeyListener(new MenuKeyListener());
        view.mouseListener = mouseListener;

        /**
         * Game loop.
         */
        while (!game.model.quit) {
            for (int i=0;i< EULER_UPDATES_N;i++) {
                try {
                    if (game.model.isTurnGoing()) {
                        mouseListener.draw = false;
                        mouseListener.finishedTurn = false;
                    } else {
                        mouseListener.finishedTurn = true;
                        mouseListener.draw = true;
                    }
                    if (mouseListener.finishedTurn && game.model.mainBall.moved()) { //make sure main ball moved
                        game.model.finishedTurn();
                        game.model.mainBall.distMoved = 0;
                    }
                    game.update(mouseListener.finishedTurn);
                } catch (Exception ignored) {}
            }
            view.repaint();

            /*String p = "Player " + game.model.currentPlayer + " - Score: " + game.model.players.get(game.model.currentPlayer).getScore();
            frame.setTitle(p);*/

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(boolean finishedTurn) {
        model.update(finishedTurn);
    }
}
