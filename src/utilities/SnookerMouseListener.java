package utilities;

import main.Model;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import static utilities.Constants.*;

/**
 * Created by Raluca on 20-Feb-16.
 * Mouse listener class for extensive mouse functionality.
 */
public class SnookerMouseListener extends MouseInputAdapter {
    Model model;
    private static int mouseX, mouseY;
    public boolean draw, instr, endGame; //restrictions
    public boolean finishedTurn;
    public float force;
    public Vec2 start, end, vel;
    boolean mousePressed;

    public SnookerMouseListener (Model model) {
        super();
        this.model = model;
        draw = true;
        force = 0;
        finishedTurn = false;
        mouseX = 0;
        mouseY = 0;
        start = new Vec2(mouseX, mouseY);
        mousePressed = false;
        instr = false;
        endGame = false;
    }

    /**
     * Gets mouse coordinates when mouse moves.
     * @param e - mouse event
     */
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        //System.out.println("Move event: "+mouseX+","+mouseY);
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited (MouseEvent e) {}

    /**
     * Pressing a mouse button sets the direction in which the main ball will be shot.
     * Registers the current position of the ball.
     * @param e - mouse event
     */
    public void mousePressed (MouseEvent e) {
        if (draw && !instr && !endGame) {
            mousePressed = true;
            model.mainBall.oldPos = new Vec2(model.mainBall.body.getPosition());
            mouseX = e.getX();
            mouseY = e.getY();
            start = new Vec2(mouseX, mouseY);
            //System.out.println("start: " + mouseX + ' ' + mouseY);
            vel = Constants.minus(getWorldCoordinatesOfMousePointer(), model.mainBall.body.getPosition());
            vel.normalize();
        }
    }

    /**
     * Releasing the mouse button shoots the main ball.
     * Registers the current position of the ball again
     * (to avoid weird things going on between
     * pressing and releasing a mouse button).
     * Sets the magnitude of the force applied to the ball,
     * depending on the distance from the first click to the
     * mouse cursor.
     * @param e - mouse event
     */
    public void mouseReleased (MouseEvent e) {
        if (draw && !instr && !endGame) {
            model.mainBall.oldPos = new Vec2(model.mainBall.body.getPosition());

            mouseX = e.getX();
            mouseY = e.getY();
            end = new Vec2(mouseX, mouseY);
            float dist = Constants.getDistance(start, end);
            force = dist > MAX_FORCE ? MAX_FORCE : dist;
            //System.out.println(force);
            vel.x *= 8 * force / Constants.DELAY;
            vel.y *= 8 * force / Constants.DELAY;
            model.mainBall.body.applyForceToCenter(vel);

            finishedTurn = false;
            force = 0;
            draw = false;
            mousePressed = false;
        }
    }

    /**
     * Updates the draw methods.
     * @param e - mouse event.
     */
    public void mouseDragged(MouseEvent e) {
        if (draw & !instr && !endGame) {
            mouseX = e.getX();
            mouseY = e.getY();
            end = new Vec2(mouseX, mouseY);
            force = Constants.getDistance(start, end) / 2;
            draw = true;
            mousePressed = true;
        }
    }

    /**
     * Original method by Michael Faribank, CE812 module.
     * No credit taken.
     * @return - coordinates of mouse pointer translated to world coordinates
     */
    public static Vec2 getWorldCoordinatesOfMousePointer() {
        return new Vec2(Constants.convertScreenXtoWorldX(mouseX), Constants.convertScreenYtoWorldY(mouseY));
    }

    /**
     * Method to draw a mark line starting from the main ball,
     * in the direction of the cursor,
     * to offer visual indication of direction of ball.
     * @param g - graphics object
     */
    public void draw(Graphics2D g) {
        int x = Constants.convertWorldXtoScreenX(model.mainBall.body.getPosition().x);
        int y = Constants.convertWorldYtoScreenY(model.mainBall.body.getPosition().y);
        g.setColor(Color.WHITE);
        Vec2 v;
        if (!mousePressed) {
            v = Constants.minus(new Vec2(mouseX, mouseY), new Vec2(x, y));
        }
        else v = Constants.minus(start, new Vec2(x,y));
        v.normalize();
        float mult = DISPLAY_LINE;
        g.drawLine(x, y, (int) (x + v.x * mult), (int) (y + v.y * mult));
    }

    /**
     * Method to draw cue.
     * @param g - graphics object
     */
    public void drawCue (Graphics2D g) {
        int x = Constants.convertWorldXtoScreenX(model.mainBall.body.getPosition().x);
        int y = Constants.convertWorldYtoScreenY(model.mainBall.body.getPosition().y);
        Image img = CUE[model.currentPlayer];
        float imW = img.getWidth(null);
        float imH = img.getHeight(null);
        AffineTransform t = new AffineTransform();
        if (mousePressed)
            t.rotate(getAngle(start, new Vec2(x, y)));
        else
            t.rotate(getAngle(new Vec2(mouseX,mouseY), new Vec2(x,y)));
        t.translate(-imW / 2.0, -imH - SCREEN_RADIUS - force);
        AffineTransform t0 = g.getTransform();
        g.translate(x, y);
        g.drawImage(img, t, null);
        g.setTransform(t0);
    }

}
