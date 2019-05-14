package RoWeatherApp;
import javax.sound.sampled.*;
import java.applet.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SoundPlayer {
    public SoundPlayer() {}
    private static Clip clip;
    public static void playAlarm() {
        Line.Info linfo = new Line.Info(Clip.class);
        Line line = null;
        try {
            line = AudioSystem.getLine(linfo);
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./src/RoWeatherApp/alarm.wav"));
            clip = (Clip) line;
            clip.open(ais);
            clip.start();
            //stops after 10 seconds
            Thread.sleep(0);
            //clip.stop();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stopAlarm() {
        clip.stop();
    }

    public static void main(String[] args) {
        playAlarm();
    }
}
