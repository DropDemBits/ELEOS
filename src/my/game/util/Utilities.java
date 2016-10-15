package my.game.util;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.*;

public class Utilities {

	private Utilities() {}
	
	public static boolean muted = false;
	public static double globalVolume = 0d;
	
	public static synchronized void playSound(String input) {
		/*if (muted) return;
		// from http://stackoverflow.com/questions/16044136/no-sound-after-export-to-jar
		if(Debug.DEBUG) System.out.println(input);
		try {
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Utilities.class.getResource("/sound/" + input));
			
			AudioFormat format = inputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			
			clip.open(inputStream);
			clip.start();
			clip.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent e) {
					if(e.getType() == Type.STOP && e.getSource() instanceof Clip) {
						((Clip)e.getSource()).close();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		playSound(input, 0);
	}
	
	public static synchronized void playSound(String input, int dbVol) {
		if(globalVolume > 6.0206) globalVolume = 6.0206;
		if (muted) return;
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
						((Clip)e.getSource()).close();
					}
				}
			});
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
}
