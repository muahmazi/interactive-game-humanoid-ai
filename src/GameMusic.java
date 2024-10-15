import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameMusic {
    private Clip clip;

    public void loadMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playMusic() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            clip.start();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
