package boggle;

import javafx.application.Application;
import javafx.stage.Stage;

import java.net.MalformedURLException;


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
    public void start(Stage primaryStage) throws MalformedURLException {
        BoggleModel model = new BoggleModel();
        BoggleView view = new BoggleView(model, primaryStage);
        model.runGame();
    }

}
