package main;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import static utilities.Constants.*;

/**
 * Created by Raluca on 20-Feb-16.
 *
 */
public class Ball {
    int points;
    boolean canReset;
    public Body body;
    public Vec2 oldPos; //last registered position of this ball
    public int distMoved; //distance moved during a turn
    Vec2 initialPos; //initial position of this ball
    float rollingFriction;
    World world;
    boolean out; //flag - true if ball is off the table (potted), false otherwise.

    public Ball(World world, Vec2 pos, int points, boolean canReset) {
        this.points = points;
        this.canReset = canReset;
        initialPos = new Vec2(pos);
        oldPos = new Vec2(pos);
        distMoved = 0;
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(pos);
        bodyDef.linearVelocity.set(0, 0);
        bodyDef.angularDamping = 0.1f;
        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = BALL_RADIUS;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = (float) (BALL_MASS / (Math.PI * BALL_RADIUS * BALL_RADIUS));
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 1.0f;
        fixtureDef.setUserData(this);
        body.createFixture(fixtureDef);

        rollingFriction = 0.2f;
        out = false;
    }

    /**
     * Reset the ball object to its initial state.
     */
    public void reset() {
        body.setLinearVelocity(new Vec2());
        body.setAngularVelocity(0);
        body.setTransform(initialPos, body.getAngle());
        oldPos = new Vec2(initialPos);
        distMoved = 0;
    }

    /**
     * Checks if this ball moved during a turn.
     * @return - true if moved, false otherwise
     */
    public boolean moved() {
        //System.out.println(getDistance(oldPos, body.getPosition()));
        return distMoved > 0;
    }

    /**
     * Updates the ball. Applies rolling friction, calculates if the ball moved or if it was potted
     */
    public void update() {
        if (body.getLinearVelocity().length() > 0.001) {
            distMoved++;
            //System.out.println(distMoved);
        }
        if (rollingFriction>0) {
            Vec2 force = new Vec2(body.getLinearVelocity());
            force = force.mul(-rollingFriction * body.getMass());
            body.applyForceToCenter(force);
        }

        //check if ball out of the table (potted)
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        float w = SNOOKER_TABLE_WIDTH;
        float h = SNOOKER_TABLE_HEIGHT;
        float p = POCKET_SIZE;

        out = (x > (w - 2 * p)) && (y > (h - 2 * p) || y < 2 * p) || // top and bot right
                x < 2 * p && (y > (h - 2 * p) || y < 2 * p) || // top and bot left
                y < (h - 2 * p) && y > 2 * p && (x > (w - p) || x < p); // middle left and right

        /*out = body.getPosition().x > SNOOKER_TABLE_WIDTH - POCKET_SIZE ||
                body.getPosition().x < POCKET_SIZE ||
                body.getPosition().y > SNOOKER_TABLE_HEIGHT - POCKET_SIZE ||
                body.getPosition().y < POCKET_SIZE;
                */
    }

    public static String getString(int points) {
        switch (points) {
            case 1: return "red";
            case 2: return "yellow";
            case 3: return "green";
            case 4: return "brown";
            case 5: return "blue";
            case 6: return "pink";
            case 7: return "black";
            default: return "white";
        }
    }
}
