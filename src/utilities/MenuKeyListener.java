package utilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MenuKeyListener extends KeyAdapter {
	public static boolean instructions = false, restart = false, quit = false;

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_N: instructions = !instructions; break;
			case KeyEvent.VK_ENTER: restart = true; break;
			case KeyEvent.VK_ESCAPE: quit = true; break;
	    }
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_ENTER: restart = false; break;
			case KeyEvent.VK_ESCAPE: quit = false; break;
		}
	}
}
