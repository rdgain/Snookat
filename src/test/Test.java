package test;

import utilities.JEasyFrame;

import static utilities.Constants.*;

/**
 * Created by Raluca on 24-Mar-16.
 */
public class Test {
    TestModel model;
    int count;

    Test() {
        model = new TestModel();
        count = 0;
    }

    public static void main(String[] args) {
        Test game = new Test();
        TestView view = new TestView(game.model);
        JEasyFrame frame = new JEasyFrame(view, "Basic Physics Engine");
        while (true) {
            game.update();
            view.repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        count++; model.update(count);
    }
}
