package utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * No credits taken for this class.
 * Original source: CE218 Lab material, Dr Norbert Voelker.
 */

public class SoundManager {

	/**
	 * File path.
	 */
	final static String path = "sounds/";

	/**
	 * note: having too many clips open may cause
	 * "LineUnavailableException: No Free Voices"
 	 */

	public final static Clip purr = getClip("Purr");
	public final static Clip[] hit = new Clip[22];
	static {
		for (int i = 0; i < hit.length; i++) {
			hit[i] = getClip("Hit");
		}
	}
	public final static Clip fall = getClip("Fall");

	/**
	 * methods which do not modify any fields
 	 */
	public static void play(Clip clip) {
		clip.setFramePosition(0);
		clip.start();
	}

	private static Clip getClip(String filename) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path
					+ filename + ".wav"));
			clip.open(sample);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}

	/**
	 * Methods playing particular sounds.
	 */
	public static void purr() {
		if (!purr.isRunning()) play(purr);
	}

	/**
	 * Checks if purr sound is playing.
	 * @return true if sound playing, false otherwise.
	 */
	public static boolean isPurring() {
		return purr.isRunning();
	}

    public static void hit() {
		for (Clip c: hit) {
			if (!c.isRunning()) {
				play(c);
				return;
			}
		}
        //play(hit[ playing % hit.length ]);
    }

	public static void fall() {
		play(fall);
	}
}
