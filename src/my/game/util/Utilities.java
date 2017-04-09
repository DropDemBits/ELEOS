package my.game.util;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.Mixer;

public class Utilities {

	private Utilities() {}
	
	public static boolean muted = false;
	public static float globalVolume = 0.5f;
    
	public static synchronized void playSound(String input) {
		playSound(input, globalVolume);
	}
	
	public static synchronized void playSound(String input, float vol) {
		playSound(input, vol, null);
	}
	
	public static synchronized void playSound(String input, float volume, IClipEndAction action) {
		if(globalVolume > 1.0f) globalVolume = 1.0f;
        if(volume > 1.0f) volume = 1.0f;
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
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            //float range = control.getMaximum() - control.getMinimum();
            control.setValue(Math.max(control.getMaximum() * Math.min(volume, globalVolume), control.getMinimum()));
            System.out.println(control.getValue());
            clip.start();
            
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP && event.getFramePosition() == ((Clip)event.getSource()).getFrameLength()) {
                        try {
                            if(action != null) action.action();
                            ((Clip)event.getSource()).drain();
                            ((Clip)event.getSource()).stop();
                            ((Clip)event.getSource()).close();
                            ais.close();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
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
