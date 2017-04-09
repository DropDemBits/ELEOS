package my.game.util;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Utilities {

	private Utilities() {}
	
	public static boolean muted = false;
	public static double globalVolume = 0d;
    
	public static synchronized void playSound(String input) {
		playSound(input, 0);
	}
	
	public static synchronized void playSound(String input, int dbVol) {
		playSound(input, dbVol, null);
	}
	
	public static synchronized void playSound(String input, int dbVol, IClipEndAction action) {
		if(globalVolume > 6.0206) globalVolume = 6.0206;
		if (muted) return;
		if(input == null) {
			muted = true;
			return;
		}
		
		if(Debug.DEBUG) System.out.println(input);
		try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Utilities.class.getResource("/sound/" + input));
            DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
            clip.loop(0);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static String[] getPaths(String path) {
		try {
			InputStream stream = Utilities.class.getResourceAsStream(path);
			byte[] buffer = new byte[stream.available()];
			String[] paths = new String(buffer).split("\n");
			return paths;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public interface IClipEndAction {
		public void action();
	}
	
}
