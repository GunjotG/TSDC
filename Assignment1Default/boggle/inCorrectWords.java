package boggle;

public class inCorrectWords {
    int numWordsNotFound = 0;
    private static inCorrectWords firstInstance = null;

    private inCorrectWords(){
    }

    public static inCorrectWords getFirstInstance(){
        if (firstInstance == null){
            firstInstance = new inCorrectWords();
        }
        return firstInstance;
    }
    //for when they find a correct word
    public void incrementNumWordsNotFound(){
        firstInstance.numWordsNotFound++;
    }

    // for new round
    public void resetNumWordsNotFounds(){
        firstInstance.numWordsNotFound=0;
    }
}
