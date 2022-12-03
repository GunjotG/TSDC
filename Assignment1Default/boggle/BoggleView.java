package boggle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;


/**
 * Boggle View
 *
 * Manages the GUI Component of the group project and is the view in the mock Model-View-Controller model.
 */
public class BoggleView {

    BoggleModel model; //reference to model
    Stage stage;

    Button newGame, endGame, muteMusic; //buttons for functions
    Button enterWord; //This button is used when a user is ready to guess a word.
    Label scoreLabel = new Label("");
    Label gridTypelabel = new Label("");

    RadioButton gridType; //4x4 boggle grid radio button
    RadioButton gridType2; //5x5 boggle grid radio button

    ArrayList<Button> buttonList; // list of grid buttons (interacted with to engage with boggle)
    BorderPane borderPane;
    Canvas canvas;
    GraphicsContext gc; //the graphics context will be linked to the canvas

    private String wordsGuessed; //Collection of characters populated from the buttons that the user pressed.
    private List<Integer> position_wordGuessed;

    HBox controls; //Segmented gui components used to organize the BorderPane.

    VBox scoreBox;

    private Stack<Object> undoStack; //undo stack, to facilitate drawing/erasing of graphics objects

    private double width; //height and width of canvas
    private double height;

    /**
     * Constructor
     *
     * @param model reference to tetris model
     * @param stage application stage
     */

    public BoggleView(BoggleModel model, Stage stage) throws MalformedURLException {
        this.model = model;
        this.stage = stage;
        this.buttonList = new ArrayList<>();
        this.wordsGuessed = "";
        this.position_wordGuessed = new ArrayList<>();
        initUI();
    }

    /**
     * Initialize interface
     */
    private void initUI() throws MalformedURLException {
        this.stage.setTitle("TSDC Boggle");
        this.width = 600;
        this.height = 500;

        borderPane = new BorderPane();

        //add canvas
        canvas = new Canvas(this.width, this.height);
        canvas.setId("Canvas");
        gc = canvas.getGraphicsContext2D();

        //labels
        gridTypelabel.setId("GameModeLabel");
        scoreLabel.setId("ScoreLabel");

        gridTypelabel.setText("GridType: 4x4");
        gridTypelabel.setMinWidth(50);
        gridTypelabel.setFont(new Font(20));


        final ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton gridType = new RadioButton("4x4");
        gridType.setToggleGroup(toggleGroup);
        gridType.setSelected(true);
        gridType.setUserData(Color.SALMON);
        gridType.setFont(new Font(16));

        RadioButton gridType2 = new RadioButton("5x5");
        gridType2.setToggleGroup(toggleGroup);
        gridType2.setUserData(Color.SALMON);
        gridType2.setFont(new Font(16));

        scoreLabel.setText("Score is: 0");
        scoreLabel.setFont(new Font(20));

        //add buttons
        newGame = new Button("New Game");
        newGame.setId("newGame");
        newGame.setPrefSize(150, 50);
        newGame.setFont(new Font(12));

        endGame = new Button("End Game");
        endGame.setId("endGame");
        endGame.setPrefSize(150, 50);
        endGame.setFont(new Font(12));

        muteMusic = new Button("Mute Music");
        muteMusic.setId("Save");
        muteMusic.setPrefSize(150, 50);
        muteMusic.setFont(new Font(12));

        HBox controls = new HBox(20, newGame, endGame, muteMusic);
        controls.setPadding(new Insets(20, 20, 20, 20));
        controls.setAlignment(Pos.CENTER);



        VBox scoreBox = new VBox(20, scoreLabel, gridTypelabel, gridType, gridType2);
        scoreBox.setPadding(new Insets(20, 20, 20, 20));
        scoreBox.setAlignment(Pos.TOP_CENTER);

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> swapGridType(newVal));


        //Although this is the same as endGame, all the appropriate scores are reset but are do not need to be dispayed to the user.
        newGame.setOnAction(e -> {
            System.out.println("new game!");
            borderPane.requestFocus();
        });

        //Once this button is clicked the model should be called to end game. The scores are announced on the screen while the game summary
        //is printed in the terminal
        endGame.setOnAction(e -> {
            System.out.println("end game!");
            this.wordsGuessed = "";
            this.position_wordGuessed = new ArrayList<>();
            model.runGame();
            model.endGame();
            buttonArrayList();
            GridPane g = addButtonsToCanvas();
            g.setAlignment(Pos.CENTER);
            borderPane.setCenter(g);
            updateScore();
            borderPane.requestFocus();
        });

        //Configures this such that it mutes the music playing in the game during launch.
        muteMusic.setOnAction(e -> {
            //mute music code
            System.out.println("mute music!");
            borderPane.requestFocus();
        });
        buttonArrayList();
        addButtonsToCanvas();
        GridPane gridPane = addButtonsToCanvas();
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        borderPane.setTop(controls);
        borderPane.setRight(scoreBox);

//        gc.setStroke(Color.BLANCHEDALMOND);
//        gc.setFill(Color.CORAL);
//        gc.fillRect(150, 20, this.width, this.height);
//
//        String path = "/Users/sfarah/TSDC/music.mp3";
//        Media buzzer = new Media(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(buzzer);
//        mediaPlayer.play();

        var scene = new Scene(borderPane, 800, 600);
        this.stage.setScene(scene);
        this.stage.show();


    }

    /**
     * Based on user selection, switch from the current grid type to
     * the type selected (4x4, 5x5).
     * @param val
     */
    private void swapGridType(Toggle val){
        RadioButton state = (RadioButton)val.getToggleGroup().getSelectedToggle();
        String stateText = state.getText();
        if(stateText.equals("4x4")){
            gridTypelabel.setText("GridType: 4x4");
            buttonList.clear();
            this.model.size = 4;
            this.model.endGame();
            updateScore();
            //change grid type from the model
        }else if(stateText.equals("5x5")){
            gridTypelabel.setText("GridType: 5x5");
            buttonList.clear();
            this.model.size = 5;
            this.model.endGame();
            updateScore();
            //change grid type from the model (also end the game before doing so)
        }
        buttonArrayList();
        GridPane g = addButtonsToCanvas();
        g.setAlignment(Pos.CENTER);
        borderPane.setCenter(g);
    }


    /**
     * This function edits the existing button arraylist with a new set of gridSize x gridSize buttons to be displayed.
     */
    private void buttonArrayList(){
        int size = this.model.getGrid().numCols();
        this.buttonList.clear();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Button button = new Button(Character.toString(this.model.getGrid().getCharAt(i,j)));
                button.prefHeight(150);
                button.maxHeight(150);
                button.prefWidth(150);
                button.maxHeight(150);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> {
                   System.out.println(button.getText() + " " + finalI + finalJ);
                    //Highlight button when pressed, unhighlight after guessing a word

                    Integer o = Integer.valueOf(String.valueOf(finalI) + String.valueOf(finalJ));
                    if (this.wordsGuessed.contains(button.getText()) && this.position_wordGuessed.contains(o)){
                       System.out.println("guessing word");
                        model.checkWord(this.wordsGuessed);
                        this.wordsGuessed = "";
                        for(Button val: buttonList){
                            val.setStyle(null);
                        }
                        updateScore();
                   }else{
                        button.setStyle("-fx-background-color: red;" + "-fx-text-fill: white");//turn a button red after the user has pressed it.
                       this.wordsGuessed = this.wordsGuessed + button.getText();
                       this.position_wordGuessed.add(o);
                   }
                });
                this.buttonList.add(button);

            }
        }
    }

    /**
     * Create a line between the most recent button pressed and the last.
     * @param x, y
     **/

    private void buttonLineSelection(int x, int y) {
        //to be created in later sprint
    }

    /**
     * Update score on UI
     */
    private void updateScore() {
        scoreLabel.setText("Score is: " + model.getScore());
    }

    /**
     * Using the class attribute for button arraylist, add all buttons into the canvas in a manner
     * appropriate to its grid size. The function should return a gridpane which is to be displayed on the screen.
     * @return GridPane gPane
     */
    private GridPane addButtonsToCanvas(){
        int count = 0;
        GridPane gPane = new GridPane();
        gPane.setPrefHeight(500);
        gPane.setMaxHeight(500);
        gPane.setPrefWidth(500);
        gPane.setMaxWidth(500);
        if (buttonList.size() == 16){
            for(int i = 0; i<4; i++){
                for(int j = 0; j < 4; j++){
                    gPane.add(buttonList.get(count), i,j);
                    count++;
                }
            }
        }else{
            for(int i = 0; i<5; i++){
                for(int j = 0; j < 5; j++){
                    gPane.add(buttonList.get(count), i,j);
                    count++;
                }
            }
        }
        return gPane;
    }
}