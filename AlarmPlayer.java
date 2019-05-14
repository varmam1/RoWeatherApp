import javax.sound.sampled.*;
import java.applet.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AlarmPlayer {
    public AlarmPlayer() {}
    private static Clip clip;
    public static void playAlarm() {
        Line.Info linfo = new Line.Info(Clip.class);
        Line line = null;
        try {
            line = AudioSystem.getLine(linfo);
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./alarm.wav"));
            clip = (Clip) line;
            clip.open(ais);
            clip.start();
            //stops after 0 seconds
            Thread.sleep(10000);
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
        stopAlarm();
    }
}
