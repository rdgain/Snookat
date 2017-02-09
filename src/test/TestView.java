package test;

import org.jbox2d.dynamics.Body;

import javax.swing.*;
import java.awt.*;

import static utilities.Constants.*;
import static utilities.Constants.SCREEN_RADIUS;

/**
 * Created by Raluca on 24-Mar-16.
 */
public class TestView extends JComponent {

    TestModel model;

    public TestView (TestModel model) {
        update(model);
    }

    public void update (TestModel model) {
        this.model = model;
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        // paint the background
        g.setColor(SNOOKER_TABLE_COLOR);
        g.fillRect(0, 0, (int)(SNOOKER_TABLE_WIDTH * WORLD_SCREEN_RATIO), (int)(SNOOKER_TABLE_HEIGHT * WORLD_SCREEN_RATIO));
        drawBody(g, model.body);
    }

    public void drawBody(Graphics2D g, Body b) {
        int x = convertWorldXtoScreenX(b.getPosition().x);
        int y = convertWorldYtoScreenY(b.getPosition().y);
        //System.out.println(x + " " + y);
        g.setColor(Color.cyan);
        g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
    }

    @Override
    public Dimension getPreferredSize() {
        return FRAME_SIZE;
    }
}
