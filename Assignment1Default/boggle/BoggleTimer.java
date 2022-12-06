package boggle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class BoggleTimer{
    //set countdown time
    private final Integer startTime = 60;
    private Integer seconds = startTime;

    public void startTimer(BoggleModel model) {

        Timeline time = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){
            //update timer every second
            @Override
            public void handle(ActionEvent event) {
                seconds--;
                BoggleView.timerLabel.setText("Time: "+seconds.toString());
                if(seconds<=0){
                    terminateGame(model);
                    time.stop();
                }
            }
        });

        time.setCycleCount(Timeline.INDEFINITE);
        time.getKeyFrames().add(frame);
        if(time!=null){
            time.stop();
        }
        time.play();
    }
    
    public void addTimer(){
        seconds += 5;
    }
    
    private void terminateGame(BoggleModel model){
        model.endGame();
    }
}
