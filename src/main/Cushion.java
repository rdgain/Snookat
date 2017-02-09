package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import static utilities.Constants.*;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

/**
 * Created by Raluca on 20-Feb-16.
 *
 */
public class Cushion {
    //Color color = CUSHION_COLOR;
    Body body;
    public Path2D.Float shapePath;

    /**
     * Constructor to initialise cushion object. Creates body and adds it to the world.
     * @param x - position x
     * @param y - position y
     * @param angle - angle of object
     * @param width - half width
     * @param height - half height
     * @param world - JBox2D World object
     */
    public Cushion (float x, float y, float angle, float width, float height, World world) {
        shapePath = makeShape(width, height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(x, y);
        bodyDef.linearVelocity.set(0, 0);
        bodyDef.setAngle(angle);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vec2[] vertices = verticesOfPath2D(shapePath);
        shape.set(vertices, vertices.length);
        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.restitution = 1;
        body.createFixture(fix);
    }

    /**
     * Gets Vec2 list of vertices from Path2D object
     * @param p - Path2D object
     * @return - list of vertices
     */
    public static Vec2[] verticesOfPath2D(Path2D.Float p) {
        ArrayList<Vec2> res = new ArrayList<Vec2>();
        float[] values = new float[6];
        PathIterator pi = p.getPathIterator(null, 0.2);
        while (!pi.isDone()) {
            int type = pi.currentSegment(values);
            if (type == PathIterator.SEG_LINETO) {
                res.add(new Vec2(values[0], values[1]));
            }
            pi.next();
        }
        Vec2[] result = new Vec2[res.size()];
        for (int i=0; i<res.size(); i++) {
            result[i] = res.get(i);
        }
        return result;
    }

    /**
     * Makes a cushion shape of half width x1 and half height y1
     * @param x1 - half width
     * @param y1 - half height
     * @return - Path2D object describing cushion shape
     */
    public static Path2D.Float makeShape(float x1, float y1) {
        float curve = CUSHION_CURVE;
        Path2D.Float p = new Path2D.Float();
        p.moveTo(-x1, -y1);
        p.lineTo(x1, -y1);
        p.curveTo(x1, 0, x1 - curve, y1/2, x1 - curve*2, y1);
        p.lineTo(-x1 + curve*2, y1);
        p.curveTo(-x1 + curve, y1/2, -x1, 0, -x1, -y1);
        p.closePath();
        return p;
    }
}
