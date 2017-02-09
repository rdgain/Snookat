package utilities;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Raluca on 20-Feb-16.
 * Class holding all constants
 */
public abstract class Constants {
    /**
     * Display settings.
     */
    public static final int SCREEN_HEIGHT = 920;
    public static final int SCREEN_WIDTH = 740;
    public static final Dimension FRAME_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    public static final float WORLD_WIDTH = 10; //metres
    public static final float WORLD_HEIGHT = SCREEN_HEIGHT * (WORLD_WIDTH / SCREEN_WIDTH);
    public static final float WORLD_SCREEN_RATIO = 1 / WORLD_WIDTH * SCREEN_WIDTH;
    public static final float GRAVITY = 0f;
    public static final int DELAY = 20; // sleep time between two drawn frames in milliseconds
    public static final int EULER_UPDATES_N = 10;
    public static final float DELTA_T = DELAY / 1000.0f; // estimate for time between two frames in seconds
    public static final int FOUL_FRAMES = 60; //number of frames to display FOUL notification
    public static final int DISPLAY_LINE = 50; //length of line to display direction of shot

    /**
     * Snooker table settings.
     */
    public static final float SNOOKER_TABLE_HEIGHT = WORLD_HEIGHT;
    public static final float BALL_RADIUS = 0.2f;
    public static final int SCREEN_RADIUS = (int) Math.max(convertWorldXtoScreenX(BALL_RADIUS),1);
    public static final float BALL_MASS = 1;
    public static final float POCKET_SIZE = 0.3f; //radius
    public static final int POCKET_SCREEN = Math.max(convertWorldXtoScreenX(POCKET_SIZE),1) + 4;
    public static final float CUSHION_DEPTH = 0.3f / 2; // half depth
    public static final float CUSHION_LENGTH = (SNOOKER_TABLE_HEIGHT - POCKET_SIZE * 6) / 4; //half length
    public static final float CUSHION_CURVE = 0.1f;
    public static final int NO_CUSHIONS = 6;
    public static final float SNOOKER_TABLE_WIDTH = CUSHION_LENGTH * 2 + CUSHION_DEPTH * 4 + POCKET_SIZE * 2;
    public static final float MAX_FORCE = 500;
    public static final int FOUL_PENALTY = 4;

    /**
     * Player constants.
     */
    public static final int NO_PLAYERS = 2;

    /**
     * Drawing constants.
     */
    //public static final Color BG_COLOR = Color.BLACK;
    public static final Color CUSHION_COLOR = new Color(12, 41, 5);
    public static final Color SNOOKER_TABLE_COLOR = new Color(30, 84, 24);
    public static final Color[] BALL_COLORS = {Color.WHITE, new Color(209, 20, 23), new Color(225, 213, 20), new Color(47, 183, 52),
            new Color(124, 59, 39), new Color(42, 134, 183), new Color(225, 105, 118), Color.BLACK};

    /**
     * Images
     */
    public static Image CAT_WHITE, CAT_RED, CAT_YELLOW, CAT_GREEN, CAT_BROWN, CAT_BLUE, CAT_PINK, CAT_BLACK;
    public static Image CAT_SLEEP_RED, CAT_SLEEP_YELLOW, CAT_SLEEP_GREEN, CAT_SLEEP_BROWN, CAT_SLEEP_BLUE, CAT_SLEEP_PINK, CAT_SLEEP_BLACK;
    public static Image PLAYER1, PLAYER2, PANEL, PANEL_SMALL, CUE1, CUE2, INSTR, GAME_OVER;
    static {
        try {
            CAT_WHITE = ImageManager.loadImage("cat_white");
            CAT_RED = ImageManager.loadImage("cat_red");
            CAT_YELLOW = ImageManager.loadImage("cat_yellow");
            CAT_GREEN = ImageManager.loadImage("cat_green");
            CAT_BROWN = ImageManager.loadImage("cat_brown");
            CAT_BLUE = ImageManager.loadImage("cat_blue");
            CAT_PINK = ImageManager.loadImage("cat_pink");
            CAT_BLACK = ImageManager.loadImage("cat_black");
            CAT_SLEEP_RED = ImageManager.loadImage("catsleep_red");
            CAT_SLEEP_YELLOW = ImageManager.loadImage("catsleep_yellow");
            CAT_SLEEP_GREEN = ImageManager.loadImage("catsleep_green");
            CAT_SLEEP_BROWN = ImageManager.loadImage("catsleep_brown");
            CAT_SLEEP_BLUE = ImageManager.loadImage("catsleep_blue");
            CAT_SLEEP_PINK = ImageManager.loadImage("catsleep_pink");
            CAT_SLEEP_BLACK = ImageManager.loadImage("catsleep_black");
            PLAYER1 = ImageManager.loadImage("pl1");
            PLAYER2 = ImageManager.loadImage("pl2");
            PANEL = ImageManager.loadImage("panel2");
            CUE1 = ImageManager.loadImage("cue");
            CUE2 = ImageManager.loadImage("cue2");
            PANEL_SMALL = ImageManager.loadImage("glass_panel2");
            INSTR = ImageManager.loadImage("instructions2");
            GAME_OVER = ImageManager.loadImage("game_over2");
        } catch (IOException e) { System.exit(1); }
    }
    public static final Image[] BALL_IMG = {CAT_WHITE, CAT_RED, CAT_YELLOW, CAT_GREEN, CAT_BROWN, CAT_BLUE, CAT_PINK, CAT_BLACK};
    public static final Image[] POTTED_BALL = {CAT_SLEEP_RED, CAT_SLEEP_YELLOW, CAT_SLEEP_GREEN, CAT_SLEEP_BROWN, CAT_SLEEP_BLUE, CAT_SLEEP_PINK, CAT_SLEEP_BLACK};
    //public static final Image[] PLAYER = {PLAYER1, PLAYER2};
    public static final Image[] CUE = {CUE1, CUE2};

    /**
     * Auxiliary methods for switching between world and screen coordinates
     */
    public static int convertWorldXtoScreenX(float worldX) {
        return (int) (worldX/WORLD_WIDTH*SCREEN_WIDTH);
    }
    public static int convertWorldYtoScreenY(float worldY) {
        // minus sign in here is because screen coordinates are upside down.
        return (int) (SCREEN_HEIGHT-(worldY/WORLD_HEIGHT*SCREEN_HEIGHT));
    }
    public static float convertScreenXtoWorldX(int screenX) {
        return screenX*WORLD_WIDTH/SCREEN_WIDTH;
    }
    public static float convertScreenYtoWorldY(int screenY) {
        return (SCREEN_HEIGHT-screenY)*WORLD_HEIGHT/SCREEN_HEIGHT;
    }

    /**
     * Auxiliary Vec2 methods.
     */
    public static float getDistance(Vec2 start, Vec2 end) { return MathUtils.distance(start,end);}
    public static Vec2 minus(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x - v2.x, v1.y - v2.y);
    }
    public static float getAngle(Vec2 from, Vec2 to) {
        return MathUtils.PI / 2 + (float) Math.atan2(to.y - from.y, to.x - from.x);
    }

}
