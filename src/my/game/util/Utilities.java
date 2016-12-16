package my.game.util;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

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
		// from http://stackoverflow.com/questions/16044136/no-sound-after-export-to-jar
		if(Debug.DEBUG) System.out.println(input);
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Utilities.class.getResource("/sound/" + input));
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(dbVol+(int)globalVolume); 
			clip.start();
			clip.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent e) {
					if(e.getType() == Type.STOP && e.getSource() instanceof Clip) {
						if(action != null) action.action();
						((Clip)e.getSource()).close();
					}
				}
			});
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
