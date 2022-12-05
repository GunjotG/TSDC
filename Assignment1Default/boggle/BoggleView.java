package boggle;

import boggle.inCorrectWords;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;


/**
 * Boggle View
 *
 * Manages the GUI Component of the group project and is the view in the mock Model-View-Controller model.
 */
public class BoggleView {

    BoggleModel model; //reference to model
    BoggleTimer timer;
    Stage stage;

    Button newGame, endGame, muteMusic, startTimerButton; //buttons for functions
    Label scoreLabel = new Label("");
    Label incorrectWordsLabel = new Label("");
    Label gridTypelabel = new Label("");
    Label difTypeLabel = new Label("");

    Label HintLabel = new Label("");

    Label computerWords = new Label("");
    static Label timerLabel;

    RadioButton gridType; //4x4 boggle grid radio button
    RadioButton gridType2; //5x5 boggle grid radio button

    RadioButton diffType1; //easy
    RadioButton diffType2; //medium
    RadioButton diffType3; //hard

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

    public BoggleView(BoggleModel model, Stage stage, BoggleTimer timer) {
        this.model = model;
        this.stage = stage;
        this.timer = timer;
        this.buttonList = new ArrayList<>();
        this.wordsGuessed = "";
        this.position_wordGuessed = new ArrayList<>();
        initUI();
    }

    /**
     * Initialize interface
     */
    private void initUI() {
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
        incorrectWordsLabel.setId("IncorrectWordsLabel");

        gridTypelabel.setText("GridType: 4x4");
        gridTypelabel.setMinWidth(50);
        gridTypelabel.setFont(new Font(20));

        computerWords.setText("Computer Words found: " + model.getStats().getComputerWords().size());
        computerWords.setMinWidth(50);
        computerWords.setFont(new Font(20));

        //create label for timer
        timerLabel = new Label();
        timerLabel.setText("Time: 60");
        timerLabel.setFont(new Font(20));
        timerLabel.setMinWidth(50);
        timerLabel.setVisible(false);

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

        int numWordsNotFound = inCorrectWords.getFirstInstance().numWordsNotFound;
        incorrectWordsLabel.setText("IncorrectWords: " + numWordsNotFound);
        incorrectWordsLabel.setFont(new Font(20));

        HintLabel.setText("Hint: ");
        HintLabel.setFont(new Font(20));

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
        
        difTypeLabel.setText("Difficulty:");
        difTypeLabel.setMinWidth(50);
        difTypeLabel.setFont(new Font(20));

        final ToggleGroup toggleGroup2 = new ToggleGroup();

        RadioButton diffType1 = new RadioButton("easy");
        diffType1.setToggleGroup(toggleGroup2);
        diffType1.setSelected(true);
        diffType1.setUserData(Color.SALMON);
        diffType1.setFont(new Font(16));

        RadioButton diffType2 = new RadioButton("medium");
        diffType2.setToggleGroup(toggleGroup2);
        diffType2.setUserData(Color.SALMON);
        diffType2.setFont(new Font(16));

        RadioButton diffType3 = new RadioButton("hard");
        diffType3.setToggleGroup(toggleGroup2);
        diffType3.setUserData(Color.SALMON);
        diffType3.setFont(new Font(16));
        
        startTimerButton = new Button("Start a timed Game");
        startTimerButton.setId("startTimer");
        startTimerButton.setPrefSize(150, 50);
        startTimerButton.setFont(new Font(12));

        HBox controls = new HBox(20, newGame, endGame, muteMusic, timerLabel, startTimerButton);
        controls.setPadding(new Insets(20, 20, 20, 20));
        controls.setAlignment(Pos.CENTER);
        
        VBox scoreBox = new VBox(20, scoreLabel, incorrectWordsLabel, gridTypelabel, gridType, gridType2, difTypeLabel, diffType1, diffType2, diffType3, computerWords);
        
        scoreBox.setPadding(new Insets(20, 20, 20, 20));
        scoreBox.setAlignment(Pos.TOP_CENTER);
//
//        HBox timerBox = new HBox(5);
//        timerBox.setPadding(new Insets(20, 20, 20, 20));
//        timerBox.getChildren().addAll(timerLabel, startTimerButton);
//        timerBox.setAlignment(Pos.BASELINE_CENTER);

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> swapGridType(newVal));
        toggleGroup2.selectedToggleProperty().addListener((observable, oldVal, newVal) -> setDifficult(newVal));


        //Although this is the same as endGame, all the appropriate scores are reset but are do not need to be dispayed to the user.
        newGame.setOnAction(e -> {
            System.out.println("new game!");
            updateIncorrectWords("reset");
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
            updateIncorrectWords("reset");
            updateScore();
            borderPane.requestFocus();
        });

        //Configures this such that it mutes the music playing in the game during launch.
        muteMusic.setOnAction(e -> {
            //mute music code
            System.out.println("mute music!");
            borderPane.requestFocus();
        });

        //starts a timed game. Timer will be visible once button is clicked and starts to countdown
        startTimerButton.setOnAction(e ->{
            initializeTimer();
            Button source = (Button)e.getSource();
            source.setVisible(false);
            timerLabel.setVisible(true);
            borderPane.requestFocus();
        });

        buttonArrayList();
        addButtonsToCanvas();
        GridPane gridPane = addButtonsToCanvas();
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        borderPane.setTop(controls);
        borderPane.setRight(scoreBox);
//        borderPane.setBottom(timerBox);

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
            this.model.changeGridSize(this.model.size);
            updateIncorrectWords("reset");
            updateScore();
            //change grid type from the model
        }else if(stateText.equals("5x5")){
            gridTypelabel.setText("GridType: 5x5");
            buttonList.clear();
            this.model.size = 5;
            this.model.endGame();
            this.model.changeGridSize(this.model.size);
            updateIncorrectWords("reset");
            updateScore();
            //change grid type from the model (also end the game before doing so)
        }
        buttonArrayList();
        GridPane g = addButtonsToCanvas();
        g.setAlignment(Pos.CENTER);
        borderPane.setCenter(g);
    }

    private void setDifficult(Toggle val){
        RadioButton state = (RadioButton)val.getToggleGroup().getSelectedToggle();
        String stateText = state.getText();

        switch (stateText) {
            case "easy" -> {
                difTypeLabel.setText("Difficulty: EASY");
                this.model.setDiffuclty("easy");
                buttonList.clear();
            }
            //change grid type from the model
            case "medium" -> {
                difTypeLabel.setText("Difficulty: MEDIUM");
                this.model.setDiffuclty("medium");
                buttonList.clear();
            }
            //change grid type from the model (also end the game before doing so)
            case "hard" -> {
                difTypeLabel.setText("Difficulty: HARD");
                this.model.setDiffuclty("hard");
                buttonList.clear();
            }
        }
        buttonArrayList();
        GridPane g = addButtonsToCanvas();
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
                        updateIncorrectWords("no");
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
     * Update numWordsNotFound on gui
     */
    private void updateIncorrectWords(String reset){
        inCorrectWords a = inCorrectWords.getFirstInstance();
        if (reset.equals("reset")){
            a.resetNumWordsNotFounds();
        }
        incorrectWordsLabel.setText("IncorrectWords: " + a.numWordsNotFound);
        incorrectWordsLabel.setFont(new Font(20));
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
        gPane.setPrefSize(500,500);

        if (buttonList.size() == 16){
            for(int i = 0; i<4; i++){
                for(int j = 0; j < 4; j++){
                    buttonList.get(count).setPrefWidth(Integer.MAX_VALUE);
                    buttonList.get(count).setPrefHeight(Integer.MAX_VALUE);
                    buttonList.get(count).maxWidth(Integer.MAX_VALUE);
                    buttonList.get(count).maxHeight(Integer.MAX_VALUE);
                    gPane.add(buttonList.get(count), i,j);
                    count++;
                }
            }
        }else{
            for(int i = 0; i<5; i++){
                for(int j = 0; j < 5; j++){
                    buttonList.get(count).setPrefWidth(Integer.MAX_VALUE);
                    buttonList.get(count).setPrefHeight(Integer.MAX_VALUE);
                    buttonList.get(count).maxWidth(Integer.MAX_VALUE);
                    buttonList.get(count).maxHeight(Integer.MAX_VALUE);
                    gPane.add(buttonList.get(count), i,j);
                    count++;
                }
            }
        }
        return gPane;
    }
    private void initializeTimer(){
        this.timer.startTimer();
    }

}