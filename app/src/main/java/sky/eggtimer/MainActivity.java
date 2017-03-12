package sky.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView text;
    Boolean countdownIsActive = false;
    Button btn;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.timeText);
        btn = (Button)findViewById(R.id.btn);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(600);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void startTimer(View view){
        if(!countdownIsActive) {
            countdownIsActive = true;
            seekBar.setEnabled(false);
            btn.setText("Stop");
            // Adding delay of 100 ms to because of conversion
            // to ensure onTick works properly
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long milliSecondsUntilFinished) {
                    updateTimer((int) milliSecondsUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    resetTimer();
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.rooster);
                    mp.start();
                }
            }.start();
        }else{
            resetTimer();
        }
    }

    public void updateTimer(int timer){
        int minutes = (int)timer / 60;
        // total number of seconds
        // left over * 60
        int seconds = timer - minutes * 60;

        String firstString = Integer.toString(minutes);
        String secondString = Integer.toString(seconds);
        // Checking if seconds and minutes are less than 9
        // Adding a 0 for aesthetics
        if(seconds <= 9){
            secondString = "0" + secondString;
        }
        if(minutes <= 9){
            firstString = "0" + firstString;
        }
        text.setText(firstString + ":" + secondString);
    }

    public void resetTimer(){
        text.setText("00:00");
        seekBar.setProgress(0);
        countDownTimer.cancel();
        btn.setText("Start");
        seekBar.setEnabled(true);
        countdownIsActive = false;
    }

    @Override
    protected void onPause() {
        resetTimer();
        super.onPause();
    }
}
