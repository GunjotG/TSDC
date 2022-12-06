package boggle;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;


/**
 * The Main class for the first Assignment in CSC207, Fall 2022
 */
public class BoggleMain extends Application{
    /**
    * Main method. 
    * @param args command line arguments.
    **/

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        BoggleModel model = new BoggleModel();
        BoggleTimer timer = new BoggleTimer();
        BoggleMusic music = new BoggleMusic();
        BoggleView view = new BoggleView(model, primaryStage, timer, music);
        }

}
