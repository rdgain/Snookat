package main;

import org.jbox2d.common.Vec2;
import utilities.Constants;
import utilities.MenuKeyListener;
import utilities.SnookerMouseListener;
import utilities.SoundManager;

import static utilities.Constants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Raluca on 20-Feb-16.
 * Display class. Handling view functionality.
 */

public class View extends JComponent {
    SnookerMouseListener mouseListener;
    Model model;
    Random r = new Random();
    int k = -1, pur = 0, counter = FOUL_FRAMES;

    public View (Model model) {
        update(model);
    }

    public void update (Model model) {
        this.model = model;
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;

        /**
         * Draw Snooker table.
         */
        //table
        g.setColor(SNOOKER_TABLE_COLOR);
        int w = (int)(SNOOKER_TABLE_WIDTH * WORLD_SCREEN_RATIO);
        int h = (int) (SNOOKER_TABLE_HEIGHT * WORLD_SCREEN_RATIO);
        g.fillRect(0, 0, w, h);
        //mark lines
        g.setColor(Color.white);
        g.drawLine(0, h * 3/ 4, w, h * 3/ 4);
        g.drawArc(w / 2 - 6 * SCREEN_RADIUS, h * 11 / 16 - 8, 12 * SCREEN_RADIUS, h / 7, 0, -180);
        //pockets
        g.setColor(Color.black);
        g.fillOval(0, 0, 2 * POCKET_SCREEN, 2 * POCKET_SCREEN);
        g.fillOval(0, h - 2*POCKET_SCREEN, 2 * POCKET_SCREEN, 2 * POCKET_SCREEN);
        g.fillOval(w - 2 * POCKET_SCREEN, 0, 2 * POCKET_SCREEN, 2 * POCKET_SCREEN);
        g.fillOval(w - 2 * POCKET_SCREEN, h - 2*POCKET_SCREEN, 2 * POCKET_SCREEN, 2 * POCKET_SCREEN);
        g.fillArc(w - POCKET_SCREEN, h / 2 - POCKET_SCREEN, 2 * POCKET_SCREEN, 2 * POCKET_SCREEN, 270, -180);
        g.fillArc(-POCKET_SCREEN, h/2 - POCKET_SCREEN, 2*POCKET_SCREEN, 2*POCKET_SCREEN, 90, -180);

        /**
         * Paint main ball.
         */
        try { // in case mainball disappears, catch exception
            drawBallImage(g, model.mainBall);
        } catch (NullPointerException e) {
            System.out.println("Lost main ball. Look for it under the table.");
        }

        /**
         * Draw other active balls.
         */
        ArrayList<Ball> dead = new ArrayList<Ball>();
        for (Ball b : model.balls) {
            if (b != null) {
                if (b.body != null)
                    drawBallImage(g, b);
                else {
                    System.out.println("Lost body of a ball but ball still here? Oh dear...");
                    dead.add(b);
                }
            }
        }
        for (Ball b: dead)
            model.balls.remove(b);

        /**
         * Draw cushions.
         */
        for (Cushion c : model.cushions) {
            drawCushion(g, c);
        }

        /**
         * Draw results on the side.
         */
        for (Player p: model.players) {
            drawPlayer(g, p, model.players.indexOf(p));
        }

        /**
         * Draw game information and instructions.
         */
        //first panel: next ball

        int x = (int)(SNOOKER_TABLE_WIDTH * WORLD_SCREEN_RATIO);
        int y = (int)(SNOOKER_TABLE_HEIGHT * WORLD_SCREEN_RATIO) - PANEL_SMALL.getHeight(null);
        g.drawImage(PANEL_SMALL, x, y, null);

        //next ball
        String ball;
        if (model.endOfGame) {
            if (model.lastBall != null) {
                ball = Ball.getString(model.lastBall.points + 1);
            } else {
                ball = Ball.getString(2); //yellow
            }
        } else {
            if (model.lastBall == null) {
                ball = "red";
            } else {
                if (model.lastBall.points == 1) {
                    ball = "coloured";
                } else {
                    ball = "red";
                }
            }
        }
        g.setColor(Color.white);
        g.drawString("NEXT BALL:", x + 30, y + 45);
        g.drawString(ball, x + 30, y + 60);

        //second panel: instructions
        g.drawImage(PANEL_SMALL, x + PANEL_SMALL.getWidth(null), y, null);
        g.setColor(Color.white);
        g.drawString("Instructions:", x + PANEL_SMALL.getWidth(null) + 30, y + 45);
        g.drawString("N key", x + PANEL_SMALL.getWidth(null) + 50, y + 60);

        /**
         * Draw cue.
         */
        if (mouseListener.draw) {
            mouseListener.drawCue(g); //draw cue
            mouseListener.draw(g); //draw direction line
        }

        /**
         * Foul notification.
         */
        if (model.FOUL && counter > 0) {
            counter--;
            g.setColor(Color.red);
            g.setFont(new Font("default", Font.BOLD, 100));
            g.drawString("FOUL", SCREEN_WIDTH / 2 - 120, SCREEN_HEIGHT / 2);
        } else {
            counter = FOUL_FRAMES;
            model.FOUL = false;
        }

        /**
         * Draw instructions panel.
         */
        if (MenuKeyListener.instructions) {
            g.drawImage(INSTR, SCREEN_WIDTH / 2 - INSTR.getWidth(null) / 2, SCREEN_HEIGHT / 2 - INSTR.getHeight(null) / 2, null);
            mouseListener.instr = true;
        } else mouseListener.instr = false;

        /**
         * Draw game over panel.
         */
        if (model.isEnded()) {
            g.drawImage(GAME_OVER, SCREEN_WIDTH / 2 - INSTR.getWidth(null) / 2, SCREEN_HEIGHT / 2 - INSTR.getHeight(null) / 2, null);
            //draw winner string
            g.setColor(Color.white);
            g.drawString("WINNER: Player" + (model.getWinner().id + 1) + " ! Total points: " + model.getWinner().getScore(),
                    SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT / 2);
            mouseListener.endGame = true;
            if (MenuKeyListener.restart) model.restart();
            if (MenuKeyListener.quit) model.quit = true;
        } else mouseListener.endGame = false;
    }

    /**
     * Draw active balls (Java graphics).
     * @param g - graphics object
     * @param b - ball
     */
    public void drawBall(Graphics2D g, Ball b) {
        int x = convertWorldXtoScreenX(b.body.getPosition().x);
        int y = convertWorldYtoScreenY(b.body.getPosition().y);
        g.setColor(BALL_COLORS[b.points]);
        g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
    }

    /**
     * Draw active balls (Sprites).
     * @param g - graphics object
     * @param b - ball
     */
    public void drawBallImage (Graphics2D g, Ball b) {
        Image image = BALL_IMG[b.points];
        float imW = image.getWidth(null);
        float imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(b.body.getAngle());
        t.scale(2*SCREEN_RADIUS/imW,2*SCREEN_RADIUS/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();

        int x = convertWorldXtoScreenX(b.body.getPosition().x);
        int y = convertWorldYtoScreenY(b.body.getPosition().y);

        g.translate(x, y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
    }

    /**
     * Draw player results.
     * @param g - graphics object
     * @param p - player
     * @param id - player ID
     */
    public void drawPlayer(Graphics2D g, Player p, int id) {

        /**
         * Draw background panel.
         */
        g.drawImage(PANEL, (int)(SNOOKER_TABLE_WIDTH * WORLD_SCREEN_RATIO) + id * PANEL.getWidth(null), 0, null);

        /**
         * Draw player stats.
         */

        int x = (int)(SNOOKER_TABLE_WIDTH * WORLD_SCREEN_RATIO) + 62 + 130*id;
        int y = 30;
        /*Image image = PLAYER[id];
        float imW = image.getWidth(null);
        float imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(x, y);
        g.drawImage(image, t, null);
        g.setTransform(t0);*/

        g.setColor(Color.white);
        g.setFont(new Font("default", Font.BOLD, 24));
        g.drawString("Player " + (id + 1), x - 45, y + 5);
        g.setFont(new Font("default", Font.BOLD, 12));
        g.drawString("SCORE: " + p.getScore(), x - 25, y + 20);

        /**
         * If not current player, fade out.
         */
        if (id != model.currentPlayer) {
            g.setColor(new Color(0, 0, 0, 150));
            int w = PANEL_SMALL.getWidth(null);
            int h = PANEL_SMALL.getHeight(null);
            g.fillRect(x - w / 2, y - h / 2, w, h - 13);
        }

        /**
         * Draw balls.
         */

        //sometimes play purr sound
        if (!p.potted.isEmpty()) {
            int max = 1000;
            if (r.nextInt(max) > max - 2) {
                SoundManager.purr();
                r = new Random(); //get new random seed
            }
            //randomly choose cat to purr
            if (SoundManager.isPurring()) {
                if (k == -1) {
                    k = r.nextInt(p.potted.size());
                    pur = model.currentPlayer;
                }
            }
            else k = -1;
        }

        for (int i = 0; i < p.potted.size(); i++) {
            Ball b = p.potted.get(i);
            boolean po = i == k && id == pur; //if cat should purr
            drawPottedBallImage(g, b, x, 70 + SCREEN_RADIUS + i * SCREEN_RADIUS * 2, po);
        }
    }

    /**
     * Draw potted ball (Java graphics).
     * @param g - graphics object
     * @param b - ball
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public void drawPottedBall(Graphics2D g, Ball b, int x, int y) {
        g.setColor(BALL_COLORS[b.points]);
        g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
    }

    /**
     * Draw potted ball (Sprites).
     * @param g - graphics object
     * @param b - ball
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public void drawPottedBallImage (Graphics2D g, Ball b, int x, int y, boolean purr) {
        if (purr) {
            g.setColor(Color.black);
            g.drawString("zZzZ", x + SCREEN_RADIUS, y - SCREEN_RADIUS);
        }

        Image image = POTTED_BALL[b.points-1];
        float imW = image.getWidth(null);
        float imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.scale(2*SCREEN_RADIUS/imW,2*SCREEN_RADIUS/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();

        g.translate(x, y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
    }

    /**
     * Draw cushion object according to its JBox2D shape path.
     * @param g - graphics object
     * @param c - cushion
     */
    public void drawCushion (Graphics2D g, Cushion c) {
        g.setColor(CUSHION_COLOR);
        Vec2 position = c.body.getPosition();
        float angle = c.body.getAngle();
        AffineTransform af = new AffineTransform();
        af.translate(Constants.convertWorldXtoScreenX(position.x), Constants.convertWorldYtoScreenY(position.y));
        af.scale(WORLD_SCREEN_RATIO, -WORLD_SCREEN_RATIO);
        af.rotate(angle);
        Path2D.Float path = new Path2D.Float (c.shapePath,af);
        g.fill(path);
    }

    @Override
    public Dimension getPreferredSize() {
        return FRAME_SIZE;
    }
}
