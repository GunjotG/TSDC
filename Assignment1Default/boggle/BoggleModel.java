package boggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoggleModel {
    private BoggleStats stats;
    private BoggleGrid baseGrid;
    private Dictionary bogDict;
    private BoggleGame boggleGame;
    public int size; //boggle grid size, is equivalent to the num rows.
    public Map<String, ArrayList<Position>> allWords;

    private String humanGuess;
    public BoggleModel(){
        this.size = 4;
        this.baseGrid = new BoggleGrid(this.size); //default grid size is 4x4
        this.boggleGame = new BoggleGame();
        this.stats = boggleGame.gameStats;
        this.baseGrid.initalizeBoard(this.boggleGame.randomizeLetters(this.size));
        this.bogDict = new Dictionary("/Users/sfarah/TSDC/Assignment1Default/wordlist.txt"); //change path references where appropriate
        this.humanGuess = "";
        this.allWords = new HashMap<>();
        this.boggleGame.findAllWords(allWords, bogDict, baseGrid);
    }

    /**
     * A new game begins. All game related variables are set to the respective default values.
     */
    public void runGame(){
        Map<String, ArrayList<Position>> allWords = new HashMap<>();
        changeGridSize(this.size);
    }

    /**
     * Concludes a game, all appropriate scores are tallied. All appropriate game variables are reset.
     */
    public void endGame(){
        this.stats.summarizeGame();
        this.boggleGame = new BoggleGame();
        this.stats = boggleGame.gameStats;
    }

    /**
     * Creates a new boggle board with a new random selection of letters according to the changed size.
     * @param value
     */
    public void changeGridSize(int value){
        this.baseGrid = new BoggleGrid(value);
        this.size = value;
        this.baseGrid.initalizeBoard(this.boggleGame.randomizeLetters(value));
        endGame();
        this.boggleGame.findAllWords(allWords,bogDict,baseGrid);
        System.out.println(this.baseGrid);
    }
    
    public void setDiffuclty(String diffuclty){
        this.boggleGame.setDif(diffuclty);
    }
    
     /**Increment player scores if they get a guess. A guess is defined by consecutive adjacent selections of buttons.
     *
     * @param word
     */
    public void checkWord(String word){
        this.boggleGame.humanMove(getGrid(),this.allWords, word);
    }

    public int getScore() {
        return getStats().getScore();
    }

    public BoggleStats getStats(){
        return this.boggleGame.gameStats;
    }

    public BoggleGrid getGrid(){
        return this.baseGrid;
    }

    public BoggleGame getGame() { return this.boggleGame;}
}
