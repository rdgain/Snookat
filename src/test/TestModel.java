package test;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import static utilities.Constants.*;
import static utilities.Constants.BALL_MASS;
import static utilities.Constants.BALL_RADIUS;

/**
 * Created by Raluca on 24-Mar-16.
 */
public class TestModel {
    Body body;

    public TestModel() {
        World world = new World(new Vec2(0, -GRAVITY));
        world.setContinuousPhysics(true);
        Settings.velocityThreshold = 0.0001f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2);
        bodyDef.linearVelocity.set(0, 0);
        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = BALL_RADIUS;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = (float) (BALL_MASS / (Math.PI * BALL_RADIUS * BALL_RADIUS));
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1.0f;
        body.createFixture(fixtureDef);
    }

    public void update(int count) {
        body.applyForceToCenter(new Vec2(1,0));
        if (count > 50) {
            body.setLinearVelocity(new Vec2());
            body.setTransform(new Vec2(WORLD_WIDTH/4, WORLD_HEIGHT/2), 0);
        }

    }
}
