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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
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
    BoggleMusic music;
    Stage stage;

    Button instructions, endGame, muteMusic, startTimerButton; //buttons for functions
    Label scoreLabel = new Label("");
    Label hintLabel = new Label("");
    Label gridTypelabel = new Label("");
    Label difTypeLabel = new Label("");

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

    public BoggleView(BoggleModel model, Stage stage, BoggleTimer timer, BoggleMusic music) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.model = model;
        this.stage = stage;
        this.timer = timer;
        this.music = music;
        this.buttonList = new ArrayList<>();
        this.wordsGuessed = "";
        this.position_wordGuessed = new ArrayList<>();
        initUI();
    }

    /**
     * Initialize interface
     */
    private void initUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
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
        hintLabel.setId("Hint");

        gridTypelabel.setText("GridType: 4x4");
        gridTypelabel.setMinWidth(50);
        gridTypelabel.setFont(new Font(20));

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

        hintLabel.setText("Hint: " + model.getHint());
        hintLabel.setFont(new Font(20));

        //add buttons
        instructions = new Button("Instructions");
        instructions.setId("instructions");
        instructions.setPrefSize(150, 50);
        instructions.setFont(new Font(12));

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

        computerWords.setText("Computer words found: " + model.getGame().computerMove(model.allWords));
        computerWords.setMinWidth(50);
        computerWords.setFont(new Font(20));

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

        HBox controls = new HBox(20, instructions, endGame, muteMusic, timerLabel, startTimerButton);
        controls.setPadding(new Insets(20, 20, 20, 20));
        controls.setAlignment(Pos.CENTER);

        VBox scoreBox = new VBox(20, scoreLabel, hintLabel, gridTypelabel, gridType, gridType2, difTypeLabel, diffType1, diffType2, diffType3, computerWords);

        scoreBox.setPadding(new Insets(20, 20, 20, 20));
        scoreBox.setAlignment(Pos.TOP_CENTER);

        HBox timerBox = new HBox(5);
        timerBox.setPadding(new Insets(20, 20, 20, 20));
//        timerBox.getChildren().addAll(timerLabel, startTimerButton);
        timerBox.setAlignment(Pos.BASELINE_CENTER);

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> swapGridType(newVal));
        toggleGroup2.selectedToggleProperty().addListener((observable, oldVal, newVal) -> setDifficult(newVal));

        music.start("music.wav");


        //Although this is the same as endGame, all the appropriate scores are reset but are do not need to be dispayed to the user.
        instructions.setOnAction(e -> {
           //Accessibility feature: read out instructions when game starts
            Voice voice = new Voice();
            String[] instructions = new String[]{
                    "The Boggle board contains a grid of letters that are randomly placed.",
                    "We're both going to try to find words in this grid by joining the letters.",
                    "You can form a word by connecting adjoining letters on the grid.",
                    "Two letters adjoin if they are next to each other horizontally, ",
                    "vertically, ",
                    "or diagonally.",
                    "The words you find must be at least 4 letters long, ",
                    "and you can't use a letter twice in any single word.",
                    "press the same button twice to submit a guess word",
                    "Your points will be based on word length:",
                    "a 4-letter word is worth 1 point,",
                    "5-letter words earn 2 points,",
                    "and so on.",
                    "After you find as many words as you can,",
                    "I will find all the remaining words.",
                    "Hit return when you're ready..."
            };
            voice.saySentences(instructions);
            borderPane.requestFocus();
        });

        //Once this button is clicked the model should be called to end game. The scores are announced on the screen while the game summary
        //is printed in the terminal
        endGame.setOnAction(e -> {
            wordToVoice(endGame);
            System.out.println("end game!");
            this.wordsGuessed = "";
            this.position_wordGuessed = new ArrayList<>();
            model.endGame();
            model.runGame();
            buttonArrayList();
            GridPane g = addButtonsToCanvas();
            g.setAlignment(Pos.CENTER);
            borderPane.setCenter(g);
            updateHint("reset");
            updateScore();
            updateCompWords();

            borderPane.requestFocus();
        });

        //Configures this such that it mutes the music playing in the game during launch.
        muteMusic.setOnAction(e -> {
            //mute music code
            wordToVoice(muteMusic);
            if(muteMusic.getText() == "Mute Music"){
                muteMusic.setText("Unmute Music");
                music.pause();
                System.out.println("Unmute Music");
            }
            else{
                muteMusic.setText("Mute Music");
                music.play();
                System.out.println("Mute Music");
            }

            borderPane.requestFocus();
        });

        //starts a timed game. Timer will be visible once button is clicked and starts to countdown
        startTimerButton.setOnAction(e -> {
            wordToVoice(startTimerButton);
            initializeTimer();
            Button source = (Button) e.getSource();
            source.setVisible(false);
            timerLabel.setVisible(true);
            borderPane.requestFocus();
        });



        //Accessibility feature: read out the word of the button clicked
        for(Button button: buttonList){
            button.setOnAction(e ->{
                wordToVoice(button);
            });
        }

        buttonArrayList();
        GridPane gridPane = addButtonsToCanvas();
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        borderPane.setTop(controls);
        borderPane.setRight(scoreBox);
        borderPane.setBottom(timerBox);

//        gc.setStroke(Color.BLANCHEDALMOND);
//        gc.setFill(Color.CORAL);
//        gc.fillRect(150, 20, this.width, this.height);



        var scene = new Scene(borderPane, 800, 600);
        this.stage.setScene(scene);
        this.stage.show();


    }

    /**
     * Based on user selection, switch from the current grid type to
     * the type selected (4x4, 5x5).
     *
     * @param val
     */
    private void swapGridType(Toggle val) {
        RadioButton state = (RadioButton) val.getToggleGroup().getSelectedToggle();
        String stateText = state.getText();
        if (stateText.equals("4x4")) {
            gridTypelabel.setText("GridType: 4x4");
            buttonList.clear();
            this.model.size = 4;
            this.model.endGame();
            this.model.changeGridSize(this.model.size);
            updateHint("reset");
            updateScore();
            updateCompWords();
            //change grid type from the model
        } else if (stateText.equals("5x5")) {
            gridTypelabel.setText("GridType: 5x5");
            buttonList.clear();
            this.model.size = 5;
            this.model.endGame();
            this.model.changeGridSize(this.model.size);


            updateHint("reset");
            updateScore();
            updateCompWords();
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
        String dif = "";
        switch (stateText) {
            case "easy" -> {
                difTypeLabel.setText("Difficulty: EASY");
                dif = "easy";
            }
            //change grid type from the model
            case "medium" -> {
                difTypeLabel.setText("Difficulty: MEDIUM");
                dif = "medium";
            }
            //change grid type from the model (also end the game before doing so)
            case "hard" -> {
                difTypeLabel.setText("Difficulty: HARD");
                dif = "hard";
            }
        }
        this.model.changeGridSize(this.model.size);
        updateScore();
        updateHint("reset");
        buttonArrayList();
        GridPane g = addButtonsToCanvas();
        g.setAlignment(Pos.CENTER);
        this.model.setDiffuclty(dif);
        updateCompWords();
        borderPane.setCenter(g);
    }


    /**
     * This function edits the existing button arraylist with a new set of gridSize x gridSize buttons to be displayed.
     */
    private void buttonArrayList() {
        int size = this.model.getGrid().numCols();
        this.buttonList.clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button button = new Button(Character.toString(this.model.getGrid().getCharAt(i, j)));
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> {
                    System.out.println(button.getText() + " " + finalI + finalJ);
                    //Highlight button when pressed, unhighlight after guessing a word
                    wordToVoice(button);

                    Integer o = Integer.valueOf(String.valueOf(finalI) + String.valueOf(finalJ));
                    if (this.wordsGuessed.contains(button.getText()) && this.position_wordGuessed.contains(o)) {
                        System.out.println("guessing word");
                        model.checkWord(this.wordsGuessed);
                        this.wordsGuessed = "";
                        this.position_wordGuessed.clear();
                        for (Button val : buttonList) {
                            val.setStyle(null);
                        }
                        updateHint("no reset");
                        updateScore();
                        updateTimer();
                    } else {
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
     * Update score on UI
     */
    private void updateScore() {
        scoreLabel.setText("Score is: " + model.getScore());
    }

    private void updateHint(String reset){
        if (reset.equals("reset")){
            inCorrectWords obj = inCorrectWords.getFirstInstance();
            obj.resetNumWordsNotFounds();
        }
        hintLabel.setText("Hint: " + model.getHint());
    }

    private void updateTimer(){
        if(timerLabel.isVisible())
            this.timer.addTimer();
    }
    /**
     * Using the class attribute for button arraylist, add all buttons into the canvas in a manner
     * appropriate to its grid size. The function should return a gridpane which is to be displayed on the screen.
     *
     * @return GridPane gPane
     */
    private GridPane addButtonsToCanvas() {
                int count = 0;
                GridPane gPane = new GridPane();
                gPane.setPrefHeight(500);
                gPane.setMaxHeight(500);
                gPane.setPrefWidth(500);
                gPane.setMaxWidth(500);
                gPane.setPrefSize(500, 500);

                if (buttonList.size() == 16) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            buttonList.get(count).setPrefWidth(Integer.MAX_VALUE);
                            buttonList.get(count).setPrefHeight(Integer.MAX_VALUE);
                            buttonList.get(count).maxWidth(Integer.MAX_VALUE);
                            buttonList.get(count).maxHeight(Integer.MAX_VALUE);
                            gPane.add(buttonList.get(count), i, j);
                            count++;
                        }
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            buttonList.get(count).setPrefWidth(Integer.MAX_VALUE);
                            buttonList.get(count).setPrefHeight(Integer.MAX_VALUE);
                            buttonList.get(count).maxWidth(Integer.MAX_VALUE);
                            buttonList.get(count).maxHeight(Integer.MAX_VALUE);
                            gPane.add(buttonList.get(count), i, j);
                            count++;
                        }
                    }
                }
        return gPane;
    }
    public void updateCompWords() {
        computerWords.setText("Computer words found: " + model.getGame().computerMove(model.allWords));
        System.out.println();
        computerWords.setMinWidth(50);
        computerWords.setFont(new Font(20));
    }



    //initialize Timer
    private void initializeTimer() {
        this.timer.startTimer(this.model);
    }
    //Accessibility feature: read out the word of the button clicked
    private void wordToVoice(Button buttonClicked){
        Voice voice = new Voice();
        String character = buttonClicked.getText();
        voice.sayWords(character);
    }
}

