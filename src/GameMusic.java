import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class GameMusic {
    private Clip clip; // clip to hold the audio data

    public void loadMusic(String filePath) {
        try {
            // load the audio file from the given file path
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip(); // get a sound clip resource
            clip.open(audioInputStream); // open the clip with the loaded audio stream
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // handle any errors with loading the audio
        }
    }

    public void playMusic() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // loop the music continuously
            clip.start(); // start playing the music
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // stop the music if it is running
        }
    }
}